package org.peng.utils;

import cn.hutool.core.date.SystemClock;
import com.google.protobuf.ByteString;
import com.ibm.etcd.api.KeyValue;
import com.ibm.etcd.api.LeaseGrantResponse;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.EtcdClient;
import com.ibm.etcd.client.KvStoreClient;
import com.ibm.etcd.client.kv.KvClient;
import com.ibm.etcd.client.lease.LeaseClient;
import com.ibm.etcd.client.lease.PersistentLease;
import com.ibm.etcd.client.lock.LockClient;
import org.peng.config.IConfigCenter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author sp
 * @date 2021-10-08 18:49
 */
public class JdEtcdClient implements IConfigCenter {

    private KvClient kvClient;
    private LeaseClient leaseClient;
    private LockClient lockClient;

    private JdEtcdClient(KvStoreClient kvStoreClient) {
        this.kvClient = kvStoreClient.getKvClient();
        this.leaseClient = kvStoreClient.getLeaseClient();
        this.lockClient = kvStoreClient.getLockClient();
    }

    @Override
    public void put(String key, String value) {
        kvClient.put(ByteString.copyFromUtf8(key), ByteString.copyFromUtf8(value)).sync();
    }

    @Override
    public void put(String key, String value, long leaseId) {
        kvClient.put(ByteString.copyFromUtf8(key), ByteString.copyFromUtf8(value), leaseId).sync();

    }

    @Override
    public void putAndGrant(String key, String value, long ttl) {
        LeaseGrantResponse lease = leaseClient.grant(ttl).sync();
        put(key, value, lease.getID());
    }

    @Override
    public String get(String key) {
        RangeResponse rangeResponse = kvClient.get(ByteString.copyFromUtf8(key)).sync();
        List<KeyValue> keyValues = rangeResponse.getKvsList();
        if (CollectionUtils.isEmpty(keyValues)) {
            return null;
        }
        return keyValues.get(0).getValue().toStringUtf8();
    }

    @Override
    public List<KeyValue> getPrefix(String key) {
        RangeResponse rangeResponse = kvClient.get(ByteString.copyFromUtf8(key)).asPrefix().sync();
        return rangeResponse.getKvsList();
    }

    @Override
    public KvClient.WatchIterator watchPrefix(String key) {
        return kvClient.watch(ByteString.copyFromUtf8(key)).asPrefix().start();
    }

    @Override
    public long keepAlive(String key, String value, int frequencySecs, int minTtl) throws Exception {
        //minTtl秒租期，每frequencySecs秒续约一下
        PersistentLease lease = leaseClient.maintain().leaseId(SystemClock.now()).keepAliveFreq(frequencySecs).minTtl(minTtl).start();
        long newId = lease.get(3L, SECONDS);
        put(key, value, newId);
        return newId;

    }

    @Override
    public long timeToLive(long leaseId) {
        try {
            return leaseClient.ttl(leaseId).get().getTTL();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0L;
        }

    }

    /**
     * @param endPoints 如https://127.0.0.1:2379 有多个时逗号分隔
     */
    public static JdEtcdClient build(String endPoints) {
        return new JdEtcdClient(EtcdClient.forEndpoints(endPoints).withPlainText().build());
    }
}

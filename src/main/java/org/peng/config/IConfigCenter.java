package org.peng.config;

import com.ibm.etcd.api.KeyValue;
import com.ibm.etcd.client.kv.KvClient;

import java.util.List;

/**
 * @author sp
 * @date 2021-10-08 17:50
 */
public interface IConfigCenter {

    /*存入key，value*/
    void put (String key,String value);

    /*
    * 存入key，value，和租约id
    * */
    void put(String key,String value,long leaseId);

    /*
    * 存入key，value，和过期时间，单位秒
    * */
    void putAndGrant(String key,String value,long ttl);

    /*
    * 根据key，获取value
    * */
    String get(String key);

    /*
    * 获取指定前缀的所有key-value
    * */
    List<KeyValue> getPrefix(String key);

    /*
    * 监听前缀为key的
    * */
    KvClient.WatchIterator watchPrefix(String key);


    /**
     * 自动续约
     * @param frequencySecs 续约频率，最小是4秒，默认是5秒
     * @param minTtl 最小存活时间，最小是2秒，默认是10秒
     * @return 返回leaseId
     */
    long keepAlive(String key, String value, int frequencySecs, int minTtl) throws Exception;

    /*
    * 判断剩余的过期时间
    * */
    long timeToLive(long leaseId);



}

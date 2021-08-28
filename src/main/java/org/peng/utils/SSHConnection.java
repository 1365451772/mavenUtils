package org.peng.utils;

/**
 * @author sp
 * @date 2021-08-13 17:13
 */

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Properties;

@Component
public class SSHConnection {
    //数据库连接对象
    private Connection ct = null;
    //SSH回话对象
    private Session session;
    //SSH连接配置
    private final static String SSH_REMOTE_SERVER = "101.201.152.129";
    private final static int SSH_REMOTE_PORT = 22;
    private final static String SSH_USER = "root";
    private final static String SSH_PASSWORD = "sshpwd";

    //目标数据库的连接信息
    private final static String MYSQL_REMOTE_SERVER = "cms.mysql.aliyuncs.com";
    private final static int MYSQL_REMOTE_PORT = 3306;

    //本机的数据库的连接信息（测试使用）
    private final static String URL = "jdbc:mysql://127.0.0.1:3307/cms?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull";
    private final static int LOCAl_PORT = 3307;
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    /**
     * 建立SSH连接
     */
    public void init() {
        try {
            //创建SSH回话
            JSch jsch = new JSch();
            session = jsch.getSession(SSH_USER, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);
            session.setPassword(SSH_PASSWORD);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            //打印SSH服务器版本信息
            System.out.println(session.getServerVersion());
            //将本地3307端口的请求转发到目标地址的3306端口
            session.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, MYSQL_REMOTE_PORT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开SSH连接
     */
    public void destroy() {
        this.session.disconnect();
    }

    /**
     * 获取数据库链接（测试使用）
     *
     * @return
     */
    public Connection getConnection() {
        try {
            return ct = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ct;
    }

    //测试使用
    public static void main(String[] args) throws SQLException {
        SSHConnection sshConnection = new SSHConnection();
        sshConnection.init();
        Connection connection = sshConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM goods_spu limit 10");
        ResultSet rs = preparedStatement.executeQuery();
        System.out.println(rs);
        while (rs.next()) {
            //获取id列数据
            String id = rs.getString("id");
            //获取goodsName列数据
            String goodsName = rs.getString("goods_name");
            //输出结果
            System.out.println(id + "\t" + goodsName);
        }
        rs.close();
        connection.close();
        sshConnection.destroy();
    }
}

package org.peng.utils;//package org.peng.utils;
//
///**
// * @author sp
// * @date 2021-08-13 17:16
// */
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
//@Slf4j
//@Component
//@WebListener
//public class DataSourceContextListener implements ServletContextListener {
//
//    public DataSourceContextListener() {
//        super();
//    }
//
//    /**
//     * @see ServletContextListener#contextInitialized(ServletContextEvent)
//     */
//    @Override
//    public void contextInitialized(ServletContextEvent arg0) {
//        try {
//            SSHConnection.init();
//            log.info("SSH连接初始化成功");
//        } catch (Throwable e) {
//            log.error("SSH连接异常：{}", e.getLocalizedMessage());
//        }
//    }
//
//    /**
//     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
//     * 关闭ssh连接
//     */
//    @Override
//    public void contextDestroyed(ServletContextEvent arg0) {
//        SSHConnection.destroy();
//        log.info("SSH连接关闭");
//    }
//}

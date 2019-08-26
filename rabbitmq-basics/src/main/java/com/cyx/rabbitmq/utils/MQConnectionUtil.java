package com.cyx.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * 消息队列连接工具
 */
/* 因为 @Value 要给静态变量注入参数，所以需要创建 Bean 对象。 */
@Component
public class MQConnectionUtil {

    // 队列名称
    public static final String HELLO_QUEUE = "hello";
    public static final String WORK_QUEUE = "work";

    // 交换器名称
    public static final String LOGS_EXCHANGE = "logs";

    // 连接参数
    private static final String HOST = "192.168.106.120";
    private static final String PORT = "5672";
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String VIRTUAL_HOST = "/";


    /**
     * 获取连接对象。
     * Connection 是 Socket 连接的抽象，并且为我们管理协议版本协商（Protocol Version Negotiation）、认证（Authentication ）等等事情。
     * 这样，我们就连接到了虚拟机上的一个消息代理（Broker）。
     */
    public static Connection getConnection() throws Exception {
        // 创建连接工厂类，用来获取 Connection 对象。
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接参数。
        factory.setHost(HOST);
        factory.setPort(Integer.parseInt(PORT));
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);

        // 创建连接对象并返回。
        return factory.newConnection();
    }
}

package com.cyx.rabbitmq._04_routing;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class LogSender {

    public static void main(String[] args) throws Exception {
        // 发送 info 级别的 log
        for (int i = 0; i < 15; i++) {
            sendLog("info_log" + i, "info");
        }

        // 发送 error 级别的 log
        for (int i = 0; i < 10; i++) {
            sendLog("error_log" + i, "error");
        }
    }

    /**
     * 发送日志
     *
     * @param msg 日志信息
     * @param level 日志级别（info、warning 、error）
     */
    public static void sendLog(String msg, String level) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明一个名称为 logs 的直连路由器
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 发布消息，以日志类型作为路由键
        channel.basicPublish(MQConnectionUtil.LOGS_EXCHANGE, level, null, msg.getBytes());

        channel.close();
        connection.close();
    }
}

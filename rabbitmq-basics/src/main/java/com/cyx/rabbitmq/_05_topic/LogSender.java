package com.cyx.rabbitmq._05_topic;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class LogSender {

    public static void main(String[] args) throws Exception {
        // 发送 web 端 info 级别的 log
        for (int i = 0; i < 10; i++) {
            sendLog("web_info_log" + i, "web", "info");
        }
        // 发送 web 端 error 级别的 log
        for (int i = 0; i < 5; i++) {
            sendLog("web_error_log" + i, "web", "error");
        }

        // 发送 app 端 info 级别的 log
        for (int i = 0; i < 10; i++) {
            sendLog("app_info_log" + i, "app", "info");
        }
        // 发送 app 端 error 级别的 log
        for (int i = 0; i < 5; i++) {
            sendLog("app_error_log" + i, "app", "error");
        }
    }

    /**
     * 发送日志
     *
     * @param msg 日志信息
     * @param facility 设备
     * @param level 日志级别（info、warning 、error）
     */
    public static void sendLog(String msg, String facility, String level) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明一个名称为 logs 的主题路由器
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.TOPIC);
        // 发布消息，以“设备.日志级别”作为路由键
        channel.basicPublish(MQConnectionUtil.LOGS_EXCHANGE, facility + "." + level, null, msg.getBytes());

        channel.close();
        connection.close();
    }
}

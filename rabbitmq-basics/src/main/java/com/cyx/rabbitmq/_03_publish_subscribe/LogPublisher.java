package com.cyx.rabbitmq._03_publish_subscribe;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 日志发布者
 */
public class LogPublisher {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 30; i++) {
            publishMsg("log" + i);
        }
    }

    /**
     * 发布信息
     */
    public static void publishMsg(String msg) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        /*
            声明交换机：
            Exchange.DeclareOk exchangeDeclare(
                    String exchange,            交换机名称。
                    BuiltinExchangeType type    交换机类型（direct、fanout、topic、headers）。
            )
        */
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.FANOUT);
        /* 路由键会被 fanout 类型的路由器忽略 */
        channel.basicPublish(MQConnectionUtil.LOGS_EXCHANGE, "logs", null, msg.getBytes());

        // 这里路由器还没有绑定队列，这些发送给路由器的消息将会丢失。但这对我们无所谓，如果还没有消费者监听，我们可以安全地丢弃这些消息。
        channel.close();
        connection.close();
    }
}

package com.cyx.rabbitmq._05_topic;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 获取 web 端的所有日志信息
 */
public class WebLogReceiver {

    public static void main(String[] args) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明路由器
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.TOPIC);
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定路由和队列，接收所有 web 端的日志信息
        channel.queueBind(queueName, MQConnectionUtil.LOGS_EXCHANGE, "web.*");
        // 监听消息
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}

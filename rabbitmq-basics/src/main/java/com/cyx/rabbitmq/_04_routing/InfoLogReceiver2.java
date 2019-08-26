package com.cyx.rabbitmq._04_routing;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class InfoLogReceiver2 {

    public static void main(String[] args) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明路由器
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定路由和队列，以需要接收的日志级别作为绑定键，获取对应路由键的信息
        channel.queueBind(queueName, MQConnectionUtil.LOGS_EXCHANGE, "info");
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

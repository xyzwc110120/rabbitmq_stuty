package com.cyx.rabbitmq._03_publish_subscribe;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * 日志订阅者
 */
public class LogSubscribe2 {

    public static void main(String[] args) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明路由器及类型
        channel.exchangeDeclare(MQConnectionUtil.LOGS_EXCHANGE, BuiltinExchangeType.FANOUT);
        // 声明一个临时队列（独占、自动删除、非持久），并获取队列名称
        String queueName = channel.queueDeclare().getQueue();
        /*
            绑定交换器和队列：
            Queue.BindOk queueBind(
                    String queue,       队列名称。
                    String exchange,    交换器名称。
                    String routingKey   用于绑定的路由键。
            )
        */
        channel.queueBind(queueName, MQConnectionUtil.LOGS_EXCHANGE, "");
        // 开始监听消息
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}

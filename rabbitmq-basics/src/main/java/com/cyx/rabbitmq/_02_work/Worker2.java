package com.cyx.rabbitmq._02_work;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 任务消费者
 */
public class Worker2 {

    public static void main(String[] args) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(MQConnectionUtil.WORK_QUEUE, false, false, false, null);
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String task = new String(body);
                // 处理该任务需要使用多少时间
                int index = task.indexOf(".");
                if (index != -1) {
                    String time = task.substring(index);
                    try {
                        Thread.sleep(time.length() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 手动确认消息
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println(task + "处理完成");
            }
        };
        channel.basicConsume(MQConnectionUtil.WORK_QUEUE, false, consumer);
    }
}

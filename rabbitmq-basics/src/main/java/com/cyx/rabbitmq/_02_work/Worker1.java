package com.cyx.rabbitmq._02_work;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 任务消费者
 */
public class Worker1 {

    public static void main(String[] args) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(MQConnectionUtil.WORK_QUEUE, false, false, false, null);
        /*
            消费者接收的最大消息数：
            void basicQos(
                    int prefetchSize   服务器将发送的最大消息数，若为 0，则表示无限制。
            )
        */
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

                /*
                    手动确认消息：
                    void basicAck(
                            long deliveryTag,   消息的 index。当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel。
                            boolean multiple    是否批量确认。若为 true，将一次性确认所有小于等于 deliveryTag 的消息。
                    )
                */
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println(task + "处理完成");
            }
        };
        channel.basicConsume(MQConnectionUtil.WORK_QUEUE, false, consumer);
    }
}

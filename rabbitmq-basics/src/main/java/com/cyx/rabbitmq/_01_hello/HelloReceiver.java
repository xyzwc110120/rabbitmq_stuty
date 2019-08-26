package com.cyx.rabbitmq._01_hello;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息消费者
 */
@Component
public class HelloReceiver {

    /**
     * 获取消息
     */
    public static void main(String[] args) throws Exception {
        // 创建连接对象。
        Connection connection = MQConnectionUtil.getConnection();
        // 创建通道对象。
        Channel channel = connection.createChannel();
        // 我们在这里也声明了队列，因为我们可能在启动发送者之前启动接收者，因此需要保证在接收消息之前，队列已经存在。
        channel.queueDeclare(MQConnectionUtil.HELLO_QUEUE, false, false, false, null);
        /*
            创建消费者（自 4.0+ 版本后无法再使用 QueueingConsumer，而官方推荐使用 DefaultConsumer）。
            DefaultConsumer 是 Consumer 接口的实现类，我们使用它来缓冲从服务器 push 来的消息。
            告诉服务器（RabbitMQ）把队列中的消息发过来。因为这个过程是异步的，可以通过 DefaultConsumer 来进行回调。
            因为推送消息是异步的，我们需要以对象的形式提供一个回调，它会缓存消息，直到我们准备好使用它。
        */
        Consumer consumer = new DefaultConsumer(channel) {

            // 处理消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 打印获取到的消息
                System.out.println(new String(body));
            }
        };
        /*
            通过 Channel 把消费者和消息队列进行关联：
            String basicConsume(
                    String queue,       队列名称。
                    boolean autoAck,    是否自动确认（应答）。若为 true，从队列中获取的消息，无论消费者获取到消息后是否成功消息，都认为是消息已经成功消费。
                                        若为 false，为手动提交，消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，如果消费者一直没有反馈，那么该消息将一直处于不可用状态。
                                        如果选用自动确认，在消费者拿走消息执行过程中出现宕机时，消息可能就会丢失。
                    Consumer callback   消费者对象。
            )

            使用 channel.basicAck(envelope.getDeliveryTag(), false); 进行消息确认。
        */
        channel.basicConsume(MQConnectionUtil.HELLO_QUEUE, true, consumer);
    }
}

package com.cyx.rabbitmq._01_hello;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.stereotype.Component;

/**
 * 消息生产者
 */
@Component
public class HelloSender {

    /**
     * 发送消息
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            sendMsg("Hello World" + i);
        }
    }

    public static void sendMsg(String msg) throws Exception {
        // 获取连接对象
        Connection connection = MQConnectionUtil.getConnection();
        // 创建了一个通道（Channel），大部分的 API 操作均在这里完成。
        Channel channel = connection.createChannel();
        /*
            声明队列。如果队列存在，则不做任何操作，如果不存在则创建队列：
            Queue.DeclareOk queueDeclare(
                    String queue,          队列名称。
                    boolean durable,       是否持久化队列。队列默认是在内存中的，如果 RabbitMQ 重启则会丢失。如果设置为 true，则会保存到 Erlang 自带的数据路中，RabbitMQ 重启后会重新读取。
                    boolean exclusive,     是否排外，是否私有当前队列。如果私有了，其他通道不可以访问访问当前队列（一般用于一个队列只用于一个消费者的时候）。
                    boolean autoDelete,    是否自动删除。若为 true，服务器将在不再使用时删除它。
                    Map<String, Object> arguments   队列的其他属性（构造参数）。
            )
        */
        channel.queueDeclare(MQConnectionUtil.HELLO_QUEUE, false, false, false, null);
        /*
            发布一条消息：
            void basicPublish(
                    String exchange,          要将消息发布至的交换机，"" 表示默认交换机，用队列名称当做路由键。
                    String routingKey,        路由键（用于绑定哪个队列）。
                    BasicProperties props,    指定消息的基本属性，如路由头等。
                    byte[] body               消息文本。
            )
        */
        // 发送消息，必须指明消息要发到哪个队列。消息的内容是一个字节数组，所以你可以随意编码（Encode）。
        channel.basicPublish("", MQConnectionUtil.HELLO_QUEUE, null, msg.getBytes());
        // 最后，必须将通道和连接关闭，释放资源。
        channel.close();
        connection.close();
    }
}

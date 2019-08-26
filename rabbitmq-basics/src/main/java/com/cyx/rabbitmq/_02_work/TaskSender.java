package com.cyx.rabbitmq._02_work;

import com.cyx.rabbitmq.utils.MQConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Random;

/**
 * 任务发布者
 */
public class TaskSender {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 30; i++) {
            // 发送任务，点号“.”来表示任务的复杂性，一个点就表示需要耗时 1 秒。
            int point = new Random().nextInt(4);
            StringBuilder task = new StringBuilder("Task" + i);
            for (int a = 0; a < point; a++) {
                task.append(".");
            }
            // 发布任务
            sendTask(task.toString());
        }
    }

    /**
     * 发布任务
     */
    public static void sendTask(String task) throws Exception {
        Connection connection = MQConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(MQConnectionUtil.WORK_QUEUE, false, false, false, null);
        channel.basicPublish("", MQConnectionUtil.WORK_QUEUE, null, task.getBytes());
        channel.close();
        connection.close();
    }
}

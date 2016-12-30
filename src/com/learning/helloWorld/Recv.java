package com.learning.helloWorld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by John_zero on 2016/12/30.
 */
public class Recv
{
    // 队列名
    private final static String QUEUE_NAME = "HelloWorld";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException, java.lang.InterruptedException
    {
        ConnectionFactory factory = new ConnectionFactory();
        // 主机
        factory.setHost("192.168.3.109");
        // 端口
        factory.setPort(15672);
        // 账号和密码
        factory.setUsername("admin");
        factory.setPassword("admin_pwd");
        // 连接
        Connection connection = factory.newConnection();
        // 频道
        Channel channel = connection.createChannel();
        // 队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 消费队列
        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        // 消息确认
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}

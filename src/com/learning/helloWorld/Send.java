package com.learning.helloWorld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

/**
 * Created by John_zero on 2016/12/30.
 */
public class Send
{
    // 队列名
    private final static String QUEUE_NAME = "HelloWorld";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        // 主机
        factory.setHost("192.168.3.105");
        // 端口
        factory.setPort(5672);
        // 账号和密码
        factory.setUsername("admin");
        factory.setPassword("admin_pwd");
        // 连接
        Connection connection = factory.newConnection();
        // 频道
        Channel channel = connection.createChannel();
        // 队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息
        String message = "Hello World!";
        // 往队列中发出一条消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        // 关闭频道
        channel.close();
        // 关闭连接
        connection.close();
    }

}

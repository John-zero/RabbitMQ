package com.learning.workQueues;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 生产者
 * Created by John_zero on 2016/12/30.
 */
public class NewTask
{

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        // 主机
        factory.setHost("192.168.3.105");
        // 端口
        factory.setPort(5672);
        // 账号和密码
        factory.setUsername("admin");
        factory.setPassword("admin_pwd");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 设置为持久化
         */
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        String message = getMessage(argv);

        /**
         * MessageProperties.PERSISTENT_TEXT_PLAIN 标识消息为持久化
         */
        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings)
    {
        if (strings.length < 1)
            return "Hello World!";

        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter)
    {
        int length = strings.length;
        if (length == 0)
            return "";

        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++)
            words.append(delimiter).append(strings[i]);

        return words.toString();
    }

}

package com.learning.publishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 * 转发器 fanout
 * Created by John_zero on 2017/1/3.
 */
public class EmitLog
{
    private static final String EXCHANGE_NAME = "logs";

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
         * 转发器 (名称, 类型)
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = getMessage(argv);

        /**
         * 匿名转发器 (不指定转发器名称["" 默认转发器], 指定队列名称)
         *  channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
         * 命名转发器 (指定转发器名称, 不指定队列名称)
         *  channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
         */

        /**
         * EXCHANGE_NAME 转发器名称
         * "" 队列名称
         * null
         * 消息内容
         */
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings)
    {
        if (strings.length < 1)
            return "info: Hello World!";

        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter)
    {
        if (strings.length == 0)
            return "";

        StringBuilder words = new StringBuilder(strings[0]);
        for (int k = 1; k < strings.length; k++)
            words.append(delimiter).append(strings[k]);

        return words.toString();
    }

}

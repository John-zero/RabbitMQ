package com.learning.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 * 转发器 topic
 * Created by John_zero on 2017/1/3.
 */
public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv)
    {
        Connection connection = null;
        try
        {
            ConnectionFactory factory = new ConnectionFactory();

            // 主机
            factory.setHost("192.168.3.105");
            // 端口
            factory.setPort(5672);
            // 账号和密码
            factory.setUsername("admin");
            factory.setPassword("admin_pwd");

            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            /**
             * 转发器 (名称, 类型)
             */
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String routingKey = getRouting(argv);
            String message = getMessage(argv);

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "' : '" + message + "'");
        }
        catch  (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (Exception ignore)
                {
                    ignore.printStackTrace();
                }
            }
        }
    }

    private static String getRouting(String[] strings)
    {
        if (strings.length < 1)
            return "anonymous.info";

        return strings[0];
    }

    private static String getMessage(String[] strings)
    {
        if (strings.length < 2)
            return "Hello World!";

        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex)
    {
        int length = strings.length;
        if (length == 0 )
            return "";
        if (length < startIndex )
            return "";

        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

}

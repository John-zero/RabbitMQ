package com.learning.topics;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 * 转发器 topic
 * Created by John_zero on 2017/1/3.
 */
public class ReceiveLogsTopic
{

    private static final String EXCHANGE_NAME = "topic_logs";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        /**
         * 临时队列
         */
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1)
        {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        /**
         * 将队列绑定到转发器
         */
        for (String bindingKey : argv)
        {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        /**
         * autoAck = true; 自动应答
         */
        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, consumer);
    }

}

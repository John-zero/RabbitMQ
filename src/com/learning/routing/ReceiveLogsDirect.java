package com.learning.routing;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 * 转发器 direct
 * Created by John_zero on 2017/1/3.
 */
public class ReceiveLogsDirect
{

    private static final String EXCHANGE_NAME = "direct_logs";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        /**
         * 临时队列
         */
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1)
        {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        /**
         * 将队列绑定到转发器
         */
        for(String severity : argv)
        {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
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

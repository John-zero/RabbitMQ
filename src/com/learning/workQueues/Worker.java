package com.learning.workQueues;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 * Created by John_zero on 2016/12/30.
 */
public class Worker
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

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        /**
         * 设置为持久化
         */
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * 公平转发
         */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        /**
         * autoAck = true; 消费者被杀死, 消息会丢失
         * autoAck = false; (默认) 消息应答, 保证消息不会丢失(除非 RabbitMQ 挂掉, 这点的处理机制就是消息持久化)
         */
        boolean autoAck = false;
        /**
         * TASK_QUEUE_NAME 队列名称
         * autoAck 应答机制
         * Consumer 消费处理机制
         */
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try
                {
                    doWork(message);
                }
                catch (Exception e)
                {
                    System.out.println(" [x] Error");
                    e.printStackTrace();
                }
                finally
                {
                    System.out.println(" [x] Done");
                    /**
                     * 主动应答
                     */
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }

    private static void doWork(String task) throws InterruptedException
    {
        for (char ch : task.toCharArray())
        {
            if (ch == '.')
                Thread.sleep(1000);
        }
    }

}

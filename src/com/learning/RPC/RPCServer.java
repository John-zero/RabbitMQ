package com.learning.RPC;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * Created by John_zero on 2017/1/3.
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n)
    {
        if (n ==0)
            return 0;
        if (n == 1)
            return 1;
        return fib(n-1) + fib(n-2);
    }

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
             * 设置为非持久化
             */
            boolean durable = false;
            channel.queueDeclare(RPC_QUEUE_NAME, durable, false, false, null);

            /**
             * 公平转发
             */
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true)
            {
                String response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try
                {
                    String message = new String(delivery.getBody(),"UTF-8");
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + message + ")");
                    response = "" + fib(n);
                }
                catch (Exception e)
                {
                    System.out.println(" [.] " + e.toString());
                    response = "";
                }
                finally
                {
                    channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
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

}
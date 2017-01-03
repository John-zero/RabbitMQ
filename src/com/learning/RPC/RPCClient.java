package com.learning.RPC;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.util.UUID;

/**
 * Created by John_zero on 2017/1/3.
 */
public class RPCClient
{
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private QueueingConsumer consumer;

    public RPCClient() throws Exception
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
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);

        /**
         * autoAck = true; 消费者被杀死, 消息会丢失
         */
        boolean autoAck = true;
        channel.basicConsume(replyQueueName, autoAck, consumer);
    }

    public String call(String message) throws Exception
    {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        while (true)
        {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId))
            {
                response = new String(delivery.getBody(),"UTF-8");
                break;
            }
        }

        return response;
    }

    public void close() throws Exception
    {
        connection.close();
    }

    public static void main(String[] argv)
    {
        RPCClient fibonacciRpc = null;
        String response = null;
        try
        {
            fibonacciRpc = new RPCClient();

            System.out.println(" [x] Requesting fib(30)");
            response = fibonacciRpc.call("30");
            System.out.println(" [.] Got '" + response + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fibonacciRpc!= null)
            {
                try
                {
                    fibonacciRpc.close();
                }
                catch (Exception ignore)
                {
                    ignore.printStackTrace();
                }
            }
        }
    }

}

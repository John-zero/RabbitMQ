package com.learning.publishSubscribe;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消费者
 * 转发器 fanout
 * 模拟日志文件输出
 * Created by John_zero on 2017/1/3.
 */
public class ReceiveLogsToFile
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
        /**
         * 临时队列 (非持久的, 唯一的, 自动删除的队列, 队列名称由服务器随机产生)
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 将临时队列绑定到转发器
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");

                FileOutputStream fileOutputStream = null;
                try
                {
//                    String dir = ReceiveLogsToFile.class.getClassLoader().getResource("").getPath();
                    String dir = "W:\\logs";
                    String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    fileOutputStream = new FileOutputStream(new File(dir, logFileName + ".txt"), true);
                    fileOutputStream.write(("DEBUG: " + message + "\r\n").getBytes());
                    fileOutputStream.flush();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(fileOutputStream != null)
                        fileOutputStream.close();
                }
            }
        };

        /**
         * autoAck = true; 自动应答
         */
        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, consumer);
    }

}

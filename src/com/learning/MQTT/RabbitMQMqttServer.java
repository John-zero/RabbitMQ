package com.learning.MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_zero on 2017/1/5.
 * 注意:
 *  qos: 只能是 0, 1, 2
 */
public class RabbitMQMqttServer
{

    public static void main (String [] args)
    {
        RabbitMQMqttServer rabbitMQMqttServer = new RabbitMQMqttServer();

        String HOST = "tcp://192.168.3.105:1883";
        String clientId = "server_105";
        MqttCallback callback = new RabbitMQMqttServerCallback();
        String userName = "admin";
        String password = "admin_pwd";
        MqttClient mqttClient = rabbitMQMqttServer.connect(HOST, clientId, callback, userName, password);

        if(mqttClient == null)
        {
            System.out.println("服务端 连接 RabbitMQ 失败");
            return;
        }

        System.out.println("服务端 连接 RabbitMQ 成功");

        String message = "192.168.3.105, RabbitMQ MQTT, Hello World!";

        // P2P机制 --------------------------------------------------------------------------
        String topIc = "RabbitMQ_MQTT_105";
        MqttTopic mqttTopic = mqttClient.getTopic(topIc);

        rabbitMQMqttServer.publish(mqttTopic, message);

        // 广播机制 --------------------------------------------------------------------------
        List<String> topIcs = new ArrayList<>();
        topIcs.add("RabbitMQ_MQTT_105");

        rabbitMQMqttServer.broadcast(mqttClient, topIcs, message);
    }

    protected MqttClient connect (String HOST, String clientId, MqttCallback callback, String userName, String password)
    {
        try
        {
            MqttClient mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());

            // 设置回调
            mqttClient.setCallback(callback);

            // 设置连接
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            mqttConnectOptions.setConnectionTimeout(10); // 单位: 秒
            mqttConnectOptions.setKeepAliveInterval(20); // 单位: 秒
            mqttConnectOptions.setMaxInflight(10); // 默认值: 10

            mqttClient.connect(mqttConnectOptions);

            return mqttClient;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * P2P 点对点方式
     * @param mqttTopic
     * @param message
     */
    protected void publish (MqttTopic mqttTopic, String message)
    {
        try
        {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(1);
            mqttMessage.setRetained(false);
            mqttMessage.setPayload(message.getBytes());

            MqttDeliveryToken mqttDeliveryToken = mqttTopic.publish(mqttMessage);

            mqttDeliveryToken.setActionCallback(new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken iMqttToken)
                {
                    System.out.println(String.format("服务端 publish message : %s, result: 成功", message));
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable)
                {
                    System.out.println(String.format("服务端 publish message : %s, result: 失败, 异常: %s", message, throwable));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 广播方式
     * @param mqttClient
     * @param topIcs
     * @param message
     */
    protected void broadcast (MqttClient mqttClient, List<String> topIcs, String message)
    {
        if(!mqttClient.isConnected())
        {
            System.out.println("服务端 连接已经断开");

            // 触发断线重连机制

            return;
        }

        for(String topic : topIcs)
        {
            try
            {
                MqttTopic mqttTopic = mqttClient.getTopic(topic);

                this.publish(mqttTopic, message);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}

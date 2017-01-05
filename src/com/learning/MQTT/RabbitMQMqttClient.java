package com.learning.MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by John_zero on 2017/1/5.
 */
public class RabbitMQMqttClient
{

    public static void main (String [] args)
    {
        RabbitMQMqttClient rabbitMQMqttClient = new RabbitMQMqttClient();

        String HOST = "tcp://192.168.3.105:61613";
        String clientId = "client_105";
        MqttCallback callback = new RabbitMQMqttClientCallback();
        String topIc = "RabbitMQ_MQTT_105";
        String userName = "";
        String password = "";
        MqttClient mqttClient = rabbitMQMqttClient.connect(HOST, clientId, callback, topIc, userName, password);

        if(mqttClient != null)
            System.out.println("客户端 连接 RabbitMQ 成功");
        else
            System.out.println("客户端 连接 RabbitMQ 失败");

        rabbitMQMqttClient.subscribe(mqttClient, topIc, 1);

//        rabbitMQMqttClient.unsubscribe(mqttClient, topIc);
    }


    protected MqttClient connect (String HOST, String clientId, MqttCallback callback, String topIc, String userName, String password)
    {
        try
        {
            MqttClient mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());

            // 设置回调
            mqttClient.setCallback(callback);

            // 设置订阅
            MqttTopic mqttTopic = mqttClient.getTopic(topIc);

            // 设置连接
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            mqttConnectOptions.setConnectionTimeout(10); // 单位: 秒
            mqttConnectOptions.setKeepAliveInterval(20); // 单位: 秒
            mqttConnectOptions.setMaxInflight(10); // 默认值: 10
            mqttConnectOptions.setWill(mqttTopic, "close".getBytes(), 2, true);

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
     * 订阅主题
     * @param mqttClient
     * @param qos
     */
    protected void subscribe (MqttClient mqttClient, String topIc, int qos)
    {
        try
        {
            mqttClient.subscribe(topIc, qos);

            System.out.println(String.format("订阅主题, topIc: %s, qos: $s", topIc, qos));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅
     * @param mqttClient
     */
    protected void unsubscribe (MqttClient mqttClient, String topIc)
    {
        try
        {
            mqttClient.unsubscribe(topIc);

            System.out.println(String.format("取消订阅, topIc: %s", topIc));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

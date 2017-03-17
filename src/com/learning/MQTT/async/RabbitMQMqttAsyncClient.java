package com.learning.MQTT.async;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * RabbitMQ MQTT Client
 * Created by John_zero on 2017/3/17.
 */
public final class RabbitMQMqttAsyncClient
{

    /**
     * 连接 MQTT Server
     * @param HOST
     * @param clientId
     * @param callback
     * @param userName
     * @param password
     * @return
     */
    protected static MqttAsyncClient connect (String HOST, String clientId, MqttCallback callback, String userName, String password)
    {
        try
        {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            mqttConnectOptions.setConnectionTimeout(10); // 单位: 秒
            mqttConnectOptions.setKeepAliveInterval(20); // 单位: 秒
            mqttConnectOptions.setMaxInflight(10); // 默认值: 10

            MqttAsyncClient mqttAsyncClient = new MqttAsyncClient(HOST, clientId, new MemoryPersistence());
            // 设置回调
            mqttAsyncClient.setCallback(callback);
            // 设置连接
            mqttAsyncClient.connect(mqttConnectOptions).waitForCompletion(60 * 1000);

            return mqttAsyncClient;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 订阅主题
     * @param mqttAsyncClient
     * @param topIc
     * @param qos
     */
    protected static void subscribe (MqttAsyncClient mqttAsyncClient, String topIc, int qos)
    {
        try
        {
            mqttAsyncClient.subscribe(topIc, qos);

            System.out.println(String.format("订阅主题, topIc: %s, qos: %s", topIc, qos));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅
     * @param mqttAsyncClient
     * @param topIc
     */
    protected static void unsubscribe (MqttAsyncClient mqttAsyncClient, String topIc)
    {
        try
        {
            mqttAsyncClient.unsubscribe(topIc);

            System.out.println(String.format("取消订阅, topIc: %s", topIc));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

package com.learning.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by John_zero on 2017/1/5.
 */
public class RabbitMQMqttClientCallback implements MqttCallback
{

    /**
     * 连接丢失 (重连)
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable)
    {
        System.out.println("触发了 connectionLost(): " + throwable);
    }

    /**
     * 订阅消息处理
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        System.out.println(String.format("触发了 messageArrived(): %s, %s", s, mqttMessage));

        System.out.println("主题 : " + s);
        System.out.println("消息Qos : " + mqttMessage.getQos());
        System.out.println("消息内容 : " + new String(mqttMessage.getPayload()));
    }

    /**
     * 令牌
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
    {
        System.out.println("触发了 deliveryComplete(): " + iMqttDeliveryToken.isComplete());
    }

}

package com.learning.MQTT.async;

import com.learning.MQTT.RabbitMQMqttClientCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 消费方
 * Created by John_zero on 2017/3/17.
 */
public class RabbitMQMqttAsyncConsumer
{

    public static void main (String [] args)
    {
        String HOST = "tcp://192.168.3.105:1883";
        String clientId = "client_105";
        MqttCallback callback = new RabbitMQMqttClientCallback();
        String topIc = "RabbitMQ_MQTT_105";
        String userName = "admin";
        String password = "admin_pwd";
        MqttAsyncClient mqttAsyncClient = RabbitMQMqttAsyncClient.connect(HOST, clientId, callback, userName, password);

        if(mqttAsyncClient == null)
        {
            System.out.println("消费方 连接 RabbitMQ Server 失败");
            return;
        }

        System.out.println("消费方 连接 RabbitMQ Server 成功");

        RabbitMQMqttAsyncClient.subscribe(mqttAsyncClient, topIc, 1);

        mqttAsyncClient.setCallback(new MqttCallback() {

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

        });

//        RabbitMQMqttAsyncClient.unsubscribe(mqttClient, topIc);
    }

}

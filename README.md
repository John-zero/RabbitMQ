# RabbitMQ

RabbitMQ 初步拟定学习方向<br> 
　　1. RabbitMQ 服务器搭建<br>
　　2. HelloWorld 入门 (测试搭建是否OK)<br>	
　　3. 玩法 http://www.rabbitmq.com/getstarted.html<br>
　　　　"Hello World!"<br> 
　　　　Work queues<br> 
　　　　Publish/Subscribe<br> 
　　　　Routing<br> 
　　　　Topics<br> 
　　　　RPC<br> 
　　4. 协议 http://www.rabbitmq.com/protocols.html<br> 
　　　　AMQP (默认协议)<br> 
　　　　MQTT (插件集成) (http://www.rabbitmq.com/mqtt.html)<br> 
　　　　　Java 版的其中一个实现版是  Eclipse Paho<br> 
　　　　　扩展: 像 HTTP 协议, MQTT 协议, XMPP 协议, QQ 协议 等都属于 OSI 参考模型的应用层协议<br> 
　　　　　　而像 TCP 或者 UDP 协议则是属于 OSI 参考模型的传输层协议<br> 
　　　　　　在 OSI 参考模型中, 数据的通信是一个封装和解封装的过程<br> 
　　　　　　平常所说的 Socket 并不是一种协议, 而是一个编程接口, 是对 TCP 和 UDP 协议的封装<br> 
　　　　　　通过 TCP 和 UDP 直接传输的数据是无意义的, 必须经过应用层的协议<br> 
　　　　　　像 MQTT 协议, HTTP 协议 等都是开源的协议, 而 QQ 协议是腾讯内部自定义的协议, 是不对外公开的<br> 
　　　　HTTP<br> 
　　5. 消息消费过程, 消息持久化, 消息生命周期, ACK<br> 
　　6. RabbitMQ Management 插件与 WEB 界面<br> 
　　7. 监控, 集群, 调优<br> 
　　8. 经验<br> 

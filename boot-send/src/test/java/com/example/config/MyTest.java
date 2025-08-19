package com.example.config;



import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MyTest {
    /**
     * rabbitmq提供的操作模版类
     * 除了能操作高级消息队列协议中规定的方法,还能操作rabbitmq自身提供的一些特殊方法
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * amqp提供的操作模版类
     * 只能操作高级消息队列协议中规定的方法
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    void send() {
        String message = "boot消息001";
        amqpTemplate.convertAndSend("bootExchange","bootKey", message);
    }

}

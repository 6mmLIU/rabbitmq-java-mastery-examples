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
    void receive() {
        /**
         * receiveAndConvert:主动获取队列中的消息
         * 参数:队列名字
         * 返回值:返回队列中最前面的一条消息,如果队列中没有消息,则返回空
         */
        String message = (String) amqpTemplate.receiveAndConvert("bootQueue");
        System.out.println(message);
    }

}

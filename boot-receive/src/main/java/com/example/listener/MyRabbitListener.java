package com.example.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyRabbitListener {
    @RabbitListener(queues = {"bootQueue"})
    public void onMessage(String message) {
        System.out.println(message);
    }
}

package com.hahaha.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitConfig {
    @Bean
    public Queue myQueue() {
        return new Queue("bootQueue", true, false, false, null);
    }

    @Bean
    public DirectExchange myBootExchange() {
        return new DirectExchange("bootExchange", true, false, null);
    }

    @Bean
    public Binding myBootBinding() {
        return new Binding("bootQueue", Binding.DestinationType.QUEUE, "bootExchange",
                "bootKey", null);
    }
}

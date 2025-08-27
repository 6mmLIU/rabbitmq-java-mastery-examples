package com.hahaha;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class FanoutSend {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.171.130");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            //创建物理连接
            connection = factory.newConnection();
            //在物理链接中创建虚拟信道
            channel = connection.createChannel();

            String queueName = "fanoutQueue";
            /**
             * 定义队列,如果队列不存在创建,存在就直接调用
             * 参数1:队列名
             * 参数2: 是否持久化,true表示持久化
             * 参数3:是否排他
             * 参数4:无消费者监听该队列,是否自动删除
             * 参数5:属性,一般填null即可
             */
            channel.queueDeclare(queueName, true, false, false, null);

            String exchangeName = "fanoutExchange";
            /**
             * 交换机
             * 参数1:交换机名字
             * 参数2:交换机类型(direct topic fanout)
             * 参数3:是否持久化
             */
            channel.exchangeDeclare(exchangeName, "fanout", true);

            /**
             * 绑定队列
             * 参数1: 队列名
             * 参数2: 交换机名
             * 参数3: 路由键
             */
            channel.queueBind(queueName, exchangeName, "fanout");

            String message = "hello fanout exchange 消息002";
            /**
             * 发送消息
             * 参数1: 交换机名字
             * 参数2: 路由键
             * 参数3: 属性,null即可
             * 参数4: 消息内容
             */
            channel.basicPublish(exchangeName, "fanoutKey", null,
                    message.getBytes(StandardCharsets.UTF_8));

            System.out.println("消息已发送：" + message);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 先关闭信道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ignore) {}
            }
            // 再关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception ignore) {}
            }
        }
    }
}

package com.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DirectReceive {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.171.130");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        //创建物理连接
        connection = factory.newConnection();
        //在物理链接中创建虚拟信道
        channel = connection.createChannel();
        String queueName = "directQueue";
        /**
         * 定义队列,如果队列不存在创建,存在就直接调用
         * 参数1:队列名
         * 参数2: 是否持久化,true表示持久化
         * 参数3:是否排他
         * 参数4:无消费者监听该队列,是否自动删除
         * 参数5:属性,一般填null即可
         */
        channel.queueDeclare(queueName, true, false, false, null);
        String exchangeName = "directExchange";
        /**
         * 交换机
         * 参数1:交换机名字
         * 参数2:交换机类型(direct topic fanout)
         * 参数3:是否持久化
         */
        channel.exchangeDeclare(exchangeName, "direct", true);
        /**
         * 绑定队列
         * 参数1: 队列名
         * 参数2: 交换机名
         * 参数3: 路由键
         */
        channel.queueBind(queueName, exchangeName, "directKey");

        /**
         * 监听消息
         * 参数1: 监听的队列名
         * 参数2: 是否自动确认消息,如果为true,表示自动确认,当我们通过handledelivery得到消息后,即可以认为消息接收成功,不考虑后续的业务执行
         * false表示手动确认,需要我们手动调用方法确认消息已经消费成功
         * 参数3:用来回调的对象
         */
        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            /**
             * 处理消息的方法
             * 参数1: 消费者编号
             * 参数2:一些属性,有消息编号,路由键,交换机等
             * 参数3:属性
             * 参数4:消息内容
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.err.println(message);
                try {//模拟业务代码,所有业务代码执行成功后,调用.basicAck确认消息消费成功
                    if (message.equals("error")){
                        int a = 10 / 0;
                    }
                    this.getChannel().basicAck(envelope.getDeliveryTag(), true);
                } catch (Exception e) {
                    //把消息放到队列中重新排队
                    this.getChannel().basicRecover();

                }
            }
        });
    }

}

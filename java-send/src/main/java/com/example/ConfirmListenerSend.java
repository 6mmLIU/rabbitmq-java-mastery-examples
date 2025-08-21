package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConfirmListenerSend {
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
//开启发送者确认模式
            channel.confirmSelect();
            /**
             * 添加异步的发送者确认监听器
             */
            channel.addConfirmListener(new ConfirmListener() {
                /**
                 * 监听到消息发送者确认监听器
                 * 参数1:消息编号
                 * 参数2:是否是批量确认
                 */
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送成功确认,消息编号:" + deliveryTag + "是否是批量确认" + multiple);
                }


                /**
                 * 监听到消息发送失败会调用这个方法
                 * @param l
                 * @param b
                 * @throws IOException
                 */
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送失败确认,消息编号:" + deliveryTag + "是否是批量确认" + multiple);
                    System.out.println("需要补发消息,补发批量确认的所有消息,有可能带来重复消息,需要消费者做幂等处理");
                }
            });
            for (int i = 1; i <= 100; i++) {
                String message = "消息" + i;
                channel.basicPublish(
                        exchangeName,
                        "directKey",
                        null,
                        message.getBytes(StandardCharsets.UTF_8)
                );
            }

            String message = "hello direct exchange 消息001";
            /**
             * 发送消息
             * 参数1: 交换机名字
             * 参数2: 路由键
             * 参数3: 属性,null即可
             * 参数4: 消息内容
             */
            channel.basicPublish(exchangeName, "directKey", null,
                    message.getBytes(StandardCharsets.UTF_8));
            try {
                boolean b = channel.waitForConfirms(1000l);
                if (!b) {
                    System.out.println("补发消息");
                }
            } catch (InterruptedException e) {
                System.out.println("不能确定消息是否发送成功,需要补发消息,但是可能造成重复消息,需要在消费者代码中实现操作的幂等性");
            } catch (TimeoutException e) {
                System.out.println("不能确定消息是否发送成功,需要补发消息,但是可能造成重复消息,需要在消费者代码中实现操作的幂等性");
            }
            System.out.println("消息已发送：" + message);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}

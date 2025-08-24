# RabbitMQ Java Mastery Examples

这是一个演示如何使用 RabbitMQ 的示例集合，展示了纯 Java 与 Spring Boot 两种方式的消息发送与接收。

## 模块说明
- `java-send`：使用原生 Java 客户端发送消息的示例。
- `java-receive`：使用原生 Java 客户端接收消息的示例。
- `boot-send`：基于 Spring Boot 的消息发送示例。
- `boot-receive`：基于 Spring Boot 的消息接收示例。

## 代码示例
下面展示一个最简单的“直连交换机”发送示例，代码中附有中文注释，帮助理解各个步骤：

```java
ConnectionFactory factory = new ConnectionFactory(); // 创建连接工厂
factory.setHost("192.168.171.130"); // RabbitMQ 服务地址
factory.setPort(5672);              // 端口
factory.setUsername("root");        // 用户名
factory.setPassword("root");        // 密码
factory.setVirtualHost("/");        // 虚拟主机

Connection connection = factory.newConnection(); // 创建物理连接
Channel channel = connection.createChannel();    // 创建信道

// 声明持久化队列，若不存在则创建
channel.queueDeclare("directQueue", true, false, false, null);
// 声明直连交换机
channel.exchangeDeclare("directExchange", "direct", true);
// 队列与交换机通过路由键绑定
channel.queueBind("directQueue", "directExchange", "directKey");

// 发送消息
channel.basicPublish("directExchange", "directKey", null,
    "hello direct exchange 消息002".getBytes(StandardCharsets.UTF_8));

// 关闭资源
channel.close();
connection.close();
```

## 运行示例
1. 确保本地或远程有可用的 RabbitMQ 服务，并在代码中修改对应的连接信息。
2. 在某个模块目录下执行 `mvn package` 或直接运行带有 `main` 方法的类即可体验。


# RabbitMQ Java Mastery Examples

本仓库收集了一组使用 Java 与 Spring Boot 访问 RabbitMQ 的示例, 每个子目录都是一个独立的 Maven 项目。

## 模块说明

- **java-send**: 使用原生 Java 客户端发送消息, 示例涵盖直连、主题、扇出等交换机类型, 并展示事务与发布确认模式。
- **java-receive**: 与 `java-send` 对应的接收端, 演示如何消费不同交换机类型的消息。
- **boot-send**: 基于 Spring Boot 的发送端示例, 通过配置类和 `AmqpTemplate` 发送消息。
- **boot-receive**: 基于 Spring Boot 的接收端示例, 使用监听器注解接收并处理消息。

## 构建与运行示例

以下命令展示如何构建并运行 `java-send` 模块中的 `DirectSend` 示例:

```bash
cd java-send
mvn -q package
java -cp target/java-send-1.0-SNAPSHOT.jar com.example.DirectSend
```

运行 Spring Boot 示例可使用:

```bash
cd boot-send
mvn -q spring-boot:run
```

## 运行测试

每个模块可通过 Maven 执行测试:

```bash
mvn -q test
```


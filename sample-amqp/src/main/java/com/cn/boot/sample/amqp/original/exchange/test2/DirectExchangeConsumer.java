package com.cn.boot.sample.amqp.original.exchange.test2;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * DirectExchange-消费者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class DirectExchangeConsumer {

    static {
        try {
            init();
        } catch (IOException | TimeoutException e) {
            log.error("error:", e);
        }
    }

    private static void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test02_direct_exchange";
        String queueName = "test02_direct_queue";
        String routingKey = "test02";

        // 声明交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                log.info("收到消息：message = " + message);
                log.info("routingKey = " + routingKey);
                log.info("contentType = " + contentType);
                log.info("deliveryTag = " + deliveryTag);
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}

package com.cn.boot.sample.amqp.original.exchange.test4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * FanoutExchange-生产者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class FanoutExchangeProduct {
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

        log.info("开始发送");
        String exchangeName = "test04_fanout_exchange";
        String routingKey = ""; // 不设置路由key或随便设置
        String content = "Hello RabbitMQ! Fanout Exchange...";
        channel.basicPublish(exchangeName, routingKey, null, content.getBytes());
        log.info("发送成功");

//        channel.close();
//        connection.close();
    }
}

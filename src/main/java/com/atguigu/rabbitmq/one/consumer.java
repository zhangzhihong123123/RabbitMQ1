package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class consumer {
    //队列信息
    public static  final  String QUEUE_NAME="hello";
    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.231.131");
        factory.setUsername("admin");
        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消   消息的回调
        CancelCallback cancelCallback=consumerTag -> {
            System.out.println("消息消费呗中断");
        };
        /**
         * 消费者信息
         * 1.消费哪个队列
         * 2.消费成功以后是否要自动应答，true自动应答，false手动挡
         * 3.消费者未成功消费的回调内容1
         * 4.消费者取消的回调
         *
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}

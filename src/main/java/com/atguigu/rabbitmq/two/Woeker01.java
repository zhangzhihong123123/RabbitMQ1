package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.untils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个工作线程相当于消费者
 */
public class Woeker01 {
    public static  final  String QUEUE_NAME="hello1";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = untils.getChannel();
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("接收到的消息"+new String(message.getBody()));
        };
        //取消   消息的回调
        CancelCallback cancelCallback= consumerTag -> {
            System.out.println(consumerTag+"消息消费者中断");
        };
        /**
         * 消费者信息
         * 1.消费哪个队列
         * 2.消费成功以后是否要自动应答，true自动应答，false手动挡
         * 3.消费者未成功消费的回调内容1
         * 4.消费者取消的回调
         *
         */
        System.out.println("C1--->");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}

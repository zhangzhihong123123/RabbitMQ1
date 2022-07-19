package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.untils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class work03 {
    public   final static String QUEUE_NAME="hello6";
    public static void main(String[] args) throws Exception {

        System.out.println("c1应答长....");
        Channel channel = untils.getChannel();
        /**
         * 消费者信息
         * 1.消费哪个队列
         * 2.消费成功以后是否要自动应答，true自动应答，false手动挡
         * 3.消费者未成功消费的回调内容1
         * 4.消费者取消的回调
         *
         */
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{

            System.out.println("开始休眠10s...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //1.消息标记
            // 2.false 代表只应答接收到的哪个传递的信息，true为应答所有的消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            System.out.println("接收到的消息"+new String(delivery.getBody()));
        };
        //取消   消息的回调
        CancelCallback cancelCallback= consumerTag -> {
            System.out.println(consumerTag+"消息消费者中断");
        };
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);


    }
}

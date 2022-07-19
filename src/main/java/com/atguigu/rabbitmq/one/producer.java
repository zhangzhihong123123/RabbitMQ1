package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class producer {

    public  static  final  String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip
        factory.setHost("192.168.231.131");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        //生成队列
        //1.队列名字
        //2.队列是不是持久化
        //3.是不是一个消费者消费，false一个消费者
        //4.是否自动删除，最后一个消费者断开连以后，该队列是否自动删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message="hello wold";
        /**
         * 发送一个消费
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个，本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送完毕");

    }
}

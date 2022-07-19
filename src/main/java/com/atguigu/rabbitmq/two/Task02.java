package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.untils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 持久化
 */
public class Task02 {
    public static  final  String QUEUE_NAME="hello1";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = untils.getChannel();
        boolean durable=true;
        //队列持久化
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
        //从控制太中接受消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext())
        {
            String message=scanner.next();
            //MessageProperties.PERSISTENT_TEXT_PLAIN  消息持久化参数
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));;
            System.out.println("发送消息完成"+message);
        }
    }
}

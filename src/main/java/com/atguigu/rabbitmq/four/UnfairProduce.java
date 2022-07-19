package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.untils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 这个和轮循的代码是一样的
 */
public class UnfairProduce {
    public static  final  String QUEUE_NAME="hello3";
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = untils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制太中接受消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext())
        {
            String message=scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));;
            System.out.println("发送消息完成"+message);
        }

    }
}

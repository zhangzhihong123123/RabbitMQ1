package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.untils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class confirmTest {
    public static void main(String[] args) throws Exception {
            publishMessageIndivdually();
            publishMessageBatch();
            publishMessaheAsync();
    }
    public static  void publishMessageIndivdually()throws Exception{
        String quequeName= UUID.randomUUID().toString();
        Channel channel = untils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        long begin=System.currentTimeMillis();
        for(int i=0;i<1000;i++)
        {
            String message=i+"";
            channel.basicPublish("",quequeName,null,message.getBytes(StandardCharsets.UTF_8));
            //服务端返回false或者超时时间内未返回，生产者可以消息重发
            boolean flag=channel.waitForConfirms();
//            if(flag)
//            {
//                System.out.println("消息发送成功");
//            }
        }
        long end=System.currentTimeMillis();
        System.out.println("发布"+1000+"个单独确认的消息，耗时"+(end-begin)+"ms");
    }
    public static  void publishMessageBatch()throws  Exception{
        String quequeName= UUID.randomUUID().toString();
        Channel channel = untils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //批量确认消息大小
        int batchSize = 100;
        // 未确认消息个数
        int outstandingMessageCount = 0;
        long begin=System.currentTimeMillis();
        for(int i=0;i<1000;i++)
        {
            String message=i+"";
            channel.basicPublish("",quequeName,null,message.getBytes(StandardCharsets.UTF_8));
            outstandingMessageCount++;
            if(outstandingMessageCount==batchSize)
            {
                channel.waitForConfirms();
                outstandingMessageCount=0;
            }
        }
        long end=System.currentTimeMillis();
        System.out.println("发布"+"1000个批量确认的消息，耗时"+(end-begin)+"ms");
    }
    public static void publishMessaheAsync()throws  Exception{
        String quequeName= UUID.randomUUID().toString();
        Channel channel = untils.getChannel();
        channel.queueDeclare(quequeName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表，适合高并发的情况
         *1.轻松的将序号与消息进行关联
         * 2.轻松的批量的删除条目，只要给到序列号
         * 3.支持并发的访问
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms=new ConcurrentSkipListMap<>();
        /**
         * 确认到消息的一个回调
         * 1.消息的序列号
         * 2.true可以确认小于等于当前序列号的消息
         * false确认当前序列号的消息
         */
        ConfirmCallback ackCallback=(sequenceNumber,multiple)->{
            if(multiple)
            {
                //返回的是小于等于当前序列号未确认的消息，是一个map
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                //消除该部分未确认的消息
                confirmed.clear();
            }
            else
            {
                //只清楚当前序列号的消息
                outstandingConfirms.remove(sequenceNumber);
            }
        };
        ConfirmCallback nackCallback=(sequenceNumber,multiple)->{
            String message=outstandingConfirms.get(sequenceNumber);
            System.out.println("发布消息"+message+"未被确认，序列号"+sequenceNumber);
        };
        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback,nackCallback);
        long begin=System.currentTimeMillis();
        for(int i=0;i<1000;i++)
        {
            String message="消息"+i;
            /**
             * channel.getNextPublishSeqNo()获取下一个消息的序列号
             * 全部都是未确认的消息体
             */
             outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
             channel.basicPublish("",quequeName,null,message.getBytes(StandardCharsets.UTF_8));
        }
        long end=System.currentTimeMillis();
        System.out.println("发布1000个异步确认的消息，耗时"+(end-begin)+"ms");
    }

}

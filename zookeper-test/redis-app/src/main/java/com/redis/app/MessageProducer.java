package com.redis.app;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


@Slf4j
public class MessageProducer extends Thread{

    public static final String MESSAGE_KEY="message::queue";

    private volatile int count;

    public  void putMessage(String message){
        Jedis jedis = JedisPoolUtils.getJedis();
        long size = jedis.lpush(MESSAGE_KEY, message);
        log.info("putMessage====>"+Thread.currentThread().getName() + " put message,size=" + size + ",count=" + count);
        ++count;
    }


    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            putMessage("message"+count);
        }
    }

    public static void main(String[] args) {
        MessageProducer messageProducer = new MessageProducer();
        Thread thred1 = new Thread(messageProducer, "thred1");
        Thread thred2 =new Thread(messageProducer,"thred2");
        Thread thred3 =new Thread(messageProducer,"thred3");
        Thread thred4 =new Thread(messageProducer,"thred4");
        Thread thred5=new Thread(messageProducer,"thred5");
        Thread thred6 = new Thread(messageProducer,"thred6");
        thred1.start();
        thred2.start();
        thred3.start();
        thred4.start();
        thred5.start();
        thred6.start();
    }


}

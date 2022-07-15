package com.redis.app;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


@Slf4j
public class MessageConsumer implements Runnable{
    public static final String MESSAGE_KEY="message::queue";

    private volatile int count;
    private static   Jedis jedis = JedisPoolUtils.getJedis();;

    /**
     *
     * @author wujiang
     * @date 2022/7/15 17:41
         brpop命令可以接收多个键，其完整的命令格式为 BRPOP key [key ...] timeout,
    如:brpop key1 0。意义是同时检测多个键，如果所有键都没有元素则阻塞，
    如果其中一个有元素则从该键中弹出该元素(会按照key的顺序进行读取，可以实现具有优先级的队列)

     */

    public void consumer(){

        //List<String> message = jedis.brpop(0, MESSAGE_KEY);
        String message = jedis.rpop(MESSAGE_KEY);

        log.info("consumer==>"+Thread.currentThread().getName() + " consumer message,message=" + JSON.toJSONString(message) + ",count=" + count);
        count++;
    }

    @Override
    public void run() {
        while (true){
            consumer();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        new Thread(messageConsumer,"thread7").start();
        new Thread(messageConsumer,"thread8").start();
    }
}

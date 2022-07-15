package com.redis.app;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisPoolUtils {

    private static JedisPool pool =null;


    static {

        //加载配置文件
        InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("redis.properties");
        Properties pro = new Properties();
        try {
            pro.load(in);
        }catch (IOException e) {
            e.printStackTrace();
        }

        //获得池子对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.parseInt(pro.get("jedispool.max-idle").toString()));//最大闲置个数
        poolConfig.setMaxWaitMillis(Integer.parseInt(pro.get("jedispool.max-active").toString()));//最大闲置个数
        poolConfig.setMinIdle(Integer.parseInt(pro.get("jedispool.min-idle").toString()));//最小闲置个数
        poolConfig.setMaxTotal(Integer.parseInt(pro.get("jedispool.max-idle").toString()));//最大连接数
        pool = new JedisPool(poolConfig, pro.getProperty("redis.host"), Integer.parseInt(pro.get("redis.port").toString()));
    }



    /** 
     * 获得jedis资源
     * @author wujiang
     * @date 2022/7/15 16:08
     * @return Jedis
     */
    
    public static Jedis getJedis(){
        Jedis resource = pool.getResource();
        return resource;
    }


    public static void main(String[] args) {
        Jedis jedis = getJedis();
        System.out.println( jedis.get("aaa"));
    }
}

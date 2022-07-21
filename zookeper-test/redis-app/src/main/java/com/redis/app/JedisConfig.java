//package com.redis.app;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//
//@Slf4j
//public class JedisConfig extends CachingConfigurerSupport {
//
//    @Value("${spring.redis.host}")
//    private  String host;
//
//    @Value("${spring.redis.port}")
//    private  int port;
//
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private  int max_active;
//
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private  int max_wait;
//
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private  int max_idle;
//
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private  int min_idle;
//
//    @Value("${spring.redis.jedis.pool.timeout}")
//    private int timeout;
//
//
//
//
//    /**
//     * 获得jedis资源
//     *
//     * @return Jedis
//     * @author wujiang
//     * @date 2022/7/15 16:08
//     */
//
////    @Bean
////    public JedisPool redisPoolFactory(){
////        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
////        jedisPoolConfig.setMaxIdle(max_idle);
////        jedisPoolConfig.setMaxWaitMillis(max_wait);
////        jedisPoolConfig.setMaxTotal(max_active);
////        jedisPoolConfig.setMinIdle(min_idle);
////        JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,null);
////        log.info("============>JedisPool注入成功！");
////        log.info("redis地址：" + host + ":" + port);
////        return  jedisPool;
////    }
//
//}

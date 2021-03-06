package com.app.controller;

import com.service.RedisService;
import com.service.ZookeeperService;
import com.wuj.entity.TcConfig;
import com.wuj.entity.ZookeeperEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;

@RestController
@Api("zk学习样例")
@RequestMapping("/zk")
public class ZookeeperController {
    @Autowired
    private ZookeeperService zookeeperService;

    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;

    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Autowired
    private JedisPool pool;


    @ApiOperation("获取zk配置")
    @GetMapping("/get/{id}")
    public ZookeeperEntity getConfig(@PathVariable("id") Long id){
        ZookeeperEntity zookeeperEntity = zookeeperService.qryZookeeperInfo(id);
        return  zookeeperEntity;
    }


    @GetMapping("/generateAuthCode/{telephone}")
    public Object generateAuthCode(@PathVariable("telephone") String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return redisService.get(REDIS_KEY_PREFIX_AUTH_CODE+telephone);
    }


    @ApiOperation("校验验证码")
    @GetMapping("/verifyAuthCode/{telephone}/{authCode}")
    public Object verifyAuthCode(@PathVariable("telephone")String telephone, @PathVariable("authCode")String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return "请输入验证码";
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return "验证码校验成功==>"+ true;
        } else {
            return "验证码不正确==>"+ false;
        }
    }

    @ApiOperation("新增配置")
    @PostMapping("/add")
    public String addConfig(@RequestBody TcConfig vo){
        zookeeperService.addConfig(vo);
        ZookeeperEntity voe = new ZookeeperEntity();
        BeanUtils.copyProperties(vo,voe);
        zookeeperService.addConfig1(voe);
        return "操作成功！！";
    }

    @ApiOperation("")
    @GetMapping("/testJedis")
    public String testTask(){
        Jedis jedis = pool.getResource();
        long hahah = jedis.lpush("app::list", "hahah");
            return jedis.rpop("app::list").toString();
    }

}

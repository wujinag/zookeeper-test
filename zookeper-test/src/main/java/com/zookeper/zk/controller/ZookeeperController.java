package com.zookeper.zk.controller;

import com.service.RedisService;
import com.service.ZookeeperService;
import com.wuj.entity.TcConfig;
import com.wuj.entiy.ZookeeperEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return sb.toString();
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
        return "操作成功！！";
    }

}

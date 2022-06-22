package com.zookeper.zk.controller;

import com.service.ZookeeperService;
import com.wuj.entiy.ZookeeperEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aaa")
public class ZookeeperController {
    @Autowired
    private ZookeeperService zookeeperService;

    @GetMapping("/bbb")
    public ZookeeperEntity getConfig(){
        ZookeeperEntity zookeeperEntity = zookeeperService.qryZookeeperInfo(1L);
        return  zookeeperEntity;
    }
}

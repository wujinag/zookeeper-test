package com.service.impl;

import com.wuj.entiy.ZookeeperEntity;
import com.wuj.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZookeeperService implements com.service.ZookeeperService {

    @Resource
    private ConfigMapper zkMapper;

    @Override
    public ZookeeperEntity qryZookeeperInfo(Long id) {
        return zkMapper.getZKconfig(id);
    }
}

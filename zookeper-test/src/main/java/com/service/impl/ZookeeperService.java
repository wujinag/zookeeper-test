package com.service.impl;

import com.wuj.entity.TcConfig;
import com.wuj.entiy.ZookeeperEntity;
import com.wuj.mapper.ConfigMapper;
import com.wuj.mapper.TcConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZookeeperService implements com.service.ZookeeperService {

    @Resource
    private ConfigMapper zkMapper;

    @Resource
    private TcConfigMapper tcConfigMapper;

    @Override
    public ZookeeperEntity qryZookeeperInfo(Long id) {
        return zkMapper.getZKconfig(id);
    }

    @Override
    public void addConfig(TcConfig entity) {
        tcConfigMapper.insert(entity);
    }


}

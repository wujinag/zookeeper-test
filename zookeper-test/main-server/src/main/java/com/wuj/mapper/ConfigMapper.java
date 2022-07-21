package com.wuj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuj.entity.ZookeeperEntity;

public interface ConfigMapper extends BaseMapper<ZookeeperEntity> {
    ZookeeperEntity getZKconfig(Long id);
}

package com.wuj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuj.entiy.ZookeeperEntity;
import org.apache.ibatis.annotations.Mapper;

public interface ConfigMapper extends BaseMapper<ZookeeperEntity> {
    ZookeeperEntity getZKconfig(Long id);
}

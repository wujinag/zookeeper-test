package com.service;

import com.wuj.entity.TcConfig;
import com.wuj.entiy.ZookeeperEntity;

public interface ZookeeperService {

  ZookeeperEntity  qryZookeeperInfo(Long id);

  void addConfig(TcConfig entity);

  void addConfig1(ZookeeperEntity entity);

}

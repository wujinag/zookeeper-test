package com.wuj.mapper;

import com.wuj.entity.TcConfig;
import com.wuj.entity.TcConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TcConfigMapper {
    long countByExample(TcConfigExample example);

    int deleteByExample(TcConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TcConfig record);

    int insertSelective(TcConfig record);

    List<TcConfig> selectByExample(TcConfigExample example);

    TcConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TcConfig record, @Param("example") TcConfigExample example);

    int updateByExample(@Param("record") TcConfig record, @Param("example") TcConfigExample example);

    int updateByPrimaryKeySelective(TcConfig record);

    int updateByPrimaryKey(TcConfig record);
}
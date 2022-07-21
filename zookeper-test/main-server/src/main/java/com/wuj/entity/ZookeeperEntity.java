package com.wuj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tc_config")
public class ZookeeperEntity implements Serializable {

    private static final long serialVersionUID = -5025929053752096471L;

    @TableId
    private Long id;

    private String configName;

    private String configIp;
}

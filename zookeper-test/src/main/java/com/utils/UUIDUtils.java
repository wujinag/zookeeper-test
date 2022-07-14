package com.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.UUID;

/**
 * UUID 生成工具
 * @author wujiang
 * @date 2022/7/11 11:06

 */

public class UUIDUtils {

    /**
     * JDK 自带UUID生成
     * @author wujiang
     * @date 2022/7/11 11:09
     * @return String
     */

    public static  String getJDKUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /** 
     *  传入网卡信息，基于时间制作生成器
     * @author wujiang
     * @date 2022/7/11 11:12
     * @return String
     */
    public static String getGeneratorUUID(){
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        return timeBasedGenerator.generate().toString().replaceAll("-", "");
    }}

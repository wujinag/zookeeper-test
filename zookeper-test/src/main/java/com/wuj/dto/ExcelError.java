package com.wuj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class ExcelError implements Serializable {
    private static final long serialVersionUID = 2228127639802583746L;
    /** 第几行 从1开始计数 */
    private int row;
    /** 第几列  从1开始计数 */
    private int column;
    /** 错误消息 */
    private String errorMsg;



}

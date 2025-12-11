package com.dddd.qa.zybh.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author zhangsc
 * @date 2024年07月17日 14:30:45
 * @packageName com.dddd.qa.zybh.utils
 * @className OrderData
 * @describe 好像没用了。。。
 */
public class OrderData {
    private long number;
    private long sellerOrderNumber;
    private String thirdOrderNumber;
    private String buyerComment;
    private String sellerComment;
    private int source;

    @JsonProperty("number")
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    // 省略其他 getter 和 setter 方法...
}
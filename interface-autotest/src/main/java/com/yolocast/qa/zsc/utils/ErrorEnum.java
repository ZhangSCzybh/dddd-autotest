package com.yolocast.qa.zsc.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zsc
 * @ClassName ErrorEnum
 * @Description
 * @Date 2022-04-25
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {
    ISCODE("code", "接口返回码非200"),
    ISFAILED("failed", "接口调用失败"),
    ISNOSUCCESS("noSuccess", "接口返回报错"),

    ISEMPTY("keyEmpty", "关键字段为空"),
    ISTYPEWRONG("TypeWrong", "字段类型错误"),
    ISLACK("keyLack", "关键字段缺失"),
    ISWRONG("keyWrong", "关键字段数值错误"),


    ISDATEWRONG("DateWrong", "返回结果日期错误"),
    ISSORTWRONG("keySortWrong", "返回结果排序错误"),
    ISSIZEWRONG("SizeWrong", "返回结果数量有误"),
    ISMissingQUANTITY("Missingquantity","返回结果数量缺少"),

    ;


    public void setDec(String dec) {
        this.dec = dec;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String dec;
    private String msg;

    public static ErrorEnum getMsgByDec(String dec) {
        if (null != dec && !"".equals(dec)) {
            for (ErrorEnum errorEnum : values()) {
                if (errorEnum.getDec().equals(dec)) {
                    return errorEnum;
                }
            }
        }
        return null;
    }
}

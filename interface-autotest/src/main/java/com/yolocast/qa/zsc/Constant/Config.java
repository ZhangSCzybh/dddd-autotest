package com.yolocast.qa.zsc.Constant;


import com.yolocast.qa.zsc.utils.DateUtil;

import java.time.LocalDate;

public class Config {

    /**********************************warningMessage***********************************/
    public static String availableInfo = "【接口可用性报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s\n接口：%s \n入参信息：%s\n返回结果：%s ";
    public static String result_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s\n返回结果：%s ";
    public static String check_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s ";
    public static String inter_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s\n详细信息：%s ";

    //用于CommonUtil中，字段存在，为空判断报警信息
    public static String common_message = "【信息】 \n运行环境：" + Common.profiesEnv + " \n接口：%s \n入参：%s \n详细信息: %s";
    //用于检查推流分发情况报警信息

    /**********************************业务**********************************************/
    public static String Pro = "yolocast-business";



    public static String zsc = "17858803001";





    /********************************日期************************************************/
    public static String format = "MM月dd";//x月x日
    public static String formatYear = DateUtil.DATE_TIME_FORMAT_YYYY年MM月DD日;//x年x月x日
    public static String formatDay = DateUtil.DATE_FORMAT_YYYY_MM_DD;//yyyy-mm-dd
    public static LocalDate nowDay = DateUtil.getPreviousDate(0);//当天
    public static String year = DateUtil.getPreviousDate(0).getYear() + "年";//该年
    public static String date = DateUtil.LocalDateFormatStr(DateUtil.getPreviousDate(0), format);//当天
    public static String NowYmd = DateUtil.getYMD(0);//当天/yyyymmdd
    public static long getTimestampAfterTenMinutes = DateUtil.getTodayCurrent()+600000;//获取十分钟后的时间戳
    public static long getTimestampAfterFortyMinutes = DateUtil.getTodayCurrent()+2400000;//获取十分钟后的时间戳

    public static String getSysdateStr = DateUtil.getSysdateStr();//时间格式：2022-04-24 21:29:20

    public static String PreviousDateNow = DateUtil.LocalDateFormatStr(DateUtil.getPreviousDate(0), formatDay);//






}

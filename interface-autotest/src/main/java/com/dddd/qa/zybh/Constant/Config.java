package com.dddd.qa.zybh.Constant;


import com.dddd.qa.zybh.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Config {

    /**********************************warningMessage***********************************/
    public static String availableInfo = "【接口可用性报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s\n接口：%s \n入参信息：%s\n返回结果：%s ";
    public static String result_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s\n返回结果：%s ";
    public static String check_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s ";
    public static String inter_message = "【接口报警】 \n运行环境：" + Common.profiesEnv + "\n业务：%s \n场景：%s \n报错类型：%s \n接口：%s \n入参信息：%s\n详细信息：%s ";

    //用于CommonUtil中，字段存在，为空判断报警信息
    public static String common_message = "【信息】 \n运行环境：" + Common.profiesEnv + " \n接口：%s \n入参：%s \n详细信息: %s";


    /**********************************业务**********************************************/
    public static String Pro = "叮叮当当";
    public static String DDingDDangYunYingPro = "智采运营平台";
    public static String DDingDDangPro = "智采企业平台";
    public static String FuliYunYingPro = "福粒运营平台";
    public static String FuliPro = "福粒企业平台";
    public static String MallPro = "商城端";



    public static String zsc = "17858803001";




    /********************************日期************************************************/
    public static String format = "MM月dd";//x月x日
    public static LocalDate nowDay = DateUtil.getPreviousDate(0);//当天
    public static String NowYmd = DateUtil.getYMD(0);//当天/yyyymmdd
    public static String formatYear = DateUtil.DATE_TIME_FORMAT_YYYY年MM月DD日;//x年x月x日
    public static String formatDay = DateUtil.DATE_FORMAT_YYYY_MM_DD;//yyyy-mm-dd
    public static String formatDay02 = DateUtil.DATE_FORMAT_YYYYMMDD; //20250325
    public static String formatYYYYMMDD = DateUtil.DateFormatYYYYMMDD(); //时间格式20250325
    public static String year = DateUtil.getPreviousDate(0).getYear() + "年";//该年
    public static String date = DateUtil.LocalDateFormatStr(DateUtil.getPreviousDate(0), format);//当天


    public static long getTimestampAfterTenMinutes = DateUtil.getTodayCurrent()+600000;//获取十分钟后的时间戳
    public static long getTimestampAfterFortyMinutes = DateUtil.getTodayCurrent()+2400000;//获取十分钟后的时间戳
    public static String getSysdateStr = DateUtil.getSysdateStr();//时间格式：2022-04-24 21:29:20
    public static long getTimestamp = DateUtil.getTodayCurrent();//获取当前的时间戳




    //Token
    public static String employeeCache = "M2tOMn2d0G4e8H4V7t1NbW359w4ZaG7h2V4N9zEh250adU2Ndo9U7kDt0M2Q8W4p0Q3eaTI5QVpBalNYM3Q0WmJNdWU5QkJINVVnYWZuVjFWQXZOVUZwMnVYOEtXNGxveU5LM3lzZzdkQia9NTzE4a3V3Z3ZwZzRhMWRwT1FsejlvWGFIOEZpK3Y3c0dXdUh5Syt6cUZVNkVYOEhQK2lLWVdJZ0lpSGMrM0RjSTF1WjJOUWZLNGgybzJlZ3dSK0lhVzViaZHgwejdxVk1uUFVoRFpqUkZ2M1VWdWlVa1NkRGNNTHZsd3ZjSVlwOUErejlsUGFFSmlKT1hSbnhhMUxSUVJOOW5RaHNUNVFhKzI5Yk4xYk8rNlIwZmhha2lIQjBHOEM3WnU4Qmp5cE94QWc9PQ";






}

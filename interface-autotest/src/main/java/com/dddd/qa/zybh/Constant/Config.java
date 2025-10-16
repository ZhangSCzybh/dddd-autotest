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
    public static String YGPCPro = "员工pc";
    public static String SelfSupplierPro = "自建供应商平台";

    public static String zsc = "17858803001";

    //存储一些常量
    public static String approvalNo;
    public static String supplierName;
    public static String itemOrderNumber;//待发货退款使用的订单号字段
    public static String itemOrderNumber1;//退货退款
    public static String itemOrderNumber2;//漏发退款
    public static String itemOrderNumber3;//换货



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
    public static String getSysdateStrAfterTenMinutes = DateUtil.getSysdateStrAfterTenMinutes();//当前时间10分钟后，时间格式：2022-04-24 21:29:20
    public static String getSysdateStrAfterThirtyMinutes = DateUtil.getSysdateStrAfterThirtyMinutes();//当前时间半小时后，时间格式：2022-04-24 21:29:20
    public static long getTimestamp = DateUtil.getTodayCurrent();//获取当前的时间戳





}

package com.dddd.qa.zybh.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class Common {
    private static final Properties STATIC_PROPERTIES = new Properties();

    static {
        try {
            InputStream in = Common.class.getClassLoader().getResourceAsStream("test.properties");
            //字节流是无法读取中文的，否则会乱码。所以采取reader把inputStream转换成reader用字符流来读取中文
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            if (null != bf) {
                STATIC_PROPERTIES.load(bf);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    //不同环境切换**** 修改15行的配置文件
    public static String profiesEnv = STATIC_PROPERTIES.getProperty("profiles.active");

    //叮叮当当
    public static String DDingDDdangUrl = STATIC_PROPERTIES.getProperty("ddingddang.url");
    public static String loginDDingDDangUri = STATIC_PROPERTIES.getProperty("login.ddingddang.uri");
    public static String loginDDingDDangInfo = STATIC_PROPERTIES.getProperty("login.DDingDDang.info");


    //福粒
    public static String FuliUrl = STATIC_PROPERTIES.getProperty("fuli.url");
    public static String SupplierUrl = STATIC_PROPERTIES.getProperty("supplier.url");


    public static String addCartUri = STATIC_PROPERTIES.getProperty("addcart.uri");
    public static String submitOrderUri = STATIC_PROPERTIES.getProperty("submit.order.uri");
    public static String comfirmOrderUri = STATIC_PROPERTIES.getProperty("confirm.order.uri");
    public static String supplierOrderUri = STATIC_PROPERTIES.getProperty("supplier.order.uri");
    public static String supplierOrderShipUri = STATIC_PROPERTIES.getProperty("supplier.order.ship.uri");




    //jenkins绝对路径
    public static String jenkinsUrl = STATIC_PROPERTIES.getProperty("jenkins.url");
    //dingding
    public static String privateDingtalkUrl = STATIC_PROPERTIES.getProperty("private.dingtalk.alert.url");
    public static String dingTalkUrlSecret = STATIC_PROPERTIES.getProperty("dingding,url.secret");

    //用来存储cookies的变量
    public static String Cookies;
    public static String DDingDDangToken;

    //下单--sku列表
    public static String[] array1 = {"9147280", "9147301", "9147304", "9147239"};
    public static String[] array2 = {"9147167", "9147172", "9147194", "9147221","9147420","9147423","9147173","9147205","9147224","9147232","9147251","9147254","9147211","9147346","9147354","9147268","9147283","9147300"};
    public static String[] array3 = {"9147309", "9147239", "9147343", "9147346","9147268","9147274","9147280","9147301","9147304"};
    public static String[] array4 = {"9147325", "9147417", "9147418", "9147422","9147424","9147253","9147254","9147255","9147257","9147262","9147357","9147292","9147295","9147298","9147302"};
    public static String[] array5 = {"9147305", "9147309", "9147419", "9147215","9147240","9147248","9147347","9147357","9147269","9147277","9147297"};
    //下单--员工账号
    public static String staffFuliToken01 = "fe84dd3442044fb4bc691d6c839f8722";
    public static String staffFuliToken02 = "a06a29079c1541989b5db3ebe8b38d6c";
    public static String staffFuliToken03 = "0f621cc6b3de4f0ebd5b9735020e8aa6";
    public static String staffFuliToken04 = "2b1c7c89596844e4901a8d4d5bb0fe11";
    public static String staffFuliToken05 = "53d8c099e12c460a812e6f3b940cdb52";

    //发货--供应商账号
    public static String supplierToken01 = "Y2pmZDBdbi2adN0T7h7ZcaDD3R0jeMCm6t2adMAG3pFX9Y7WbU9zbR3n0BA42W8FfQF48aVdBMUdRMVRnQU5KNmtOTFU4QTFKL2g5VjNkVGhweE5jeUN6TXo3WmtFUTZlWno5czNTMzdaaVc0Uy9TL0xDdUEzUjdReW9YdXpyRUVxY21CNVlWdjIrVHozVy8yOEp1dXhVY3dtWHJoNjl1aXBwOHpjT0pKVmJlMDdhR3hVa1lzeE1XME5ubUpJb2VMWVBVPQ";
    public static String supplierToken02 = "cUxGbW194l7U0EDh1VCT4DCI236d4T7lfZER41DB0j9Ra17pf39M3l4Yc0CSfj1h72AY7lRFSkpmSE5RT3lwRDdQWW5YMG5xbEl3M0pzZThiaUEFla3NTeUpkcUVLeFliabFBSNDdSZTZYdkQ1aDN3UHZPcGtCMXUrdDhsNzQ0STc3c1RXQWFDQldoL1lQV0x6WGN4VklFaVlFcmFiaQ0lENW9ycEQ2d09renp5ZzF3ZENZa2h0RzlaVEQ2Q0gzdGpmSFlFPQ";
    public static String supplierToken03 = "L1lPZUEJ2BDNcmBF9iFa8a1G1d82cZFHeh614QFW1ZFu0d2E0xEY8VBS59E6ec7DbU82aOEsycWhzaHlOclg2YTRWSXc1Vm4vTEZFbGxtWjNVeHJsVW5KNFNLQ0I0Wia9jRmlYVFdFdEpKemt6T284blYxdW1rc3d1SUc0WnBiaWnZxT0RIc20xK2RjRCs3aEYrbW5RVnZuWDNoVTZ4aEpQMmdRS3llNUZ6d2tiaMzJRPT0";
    public static String supplierToken04 = "ZStXRT5l2R3cfHFNemFY0nEAf35R5275dyFW0n0hcjAZ3TAF3HBU6W6V5nEUdm0J6a1T8kttdCsrdFJycERLREhXY21vQlZ0UVpTWk51bkFRU21PNFBxMmpLRmprWnd0bEExanNUc3pZMmVGS0phNjArUU9uOHhaL2JUWHpsOEw4U0oxUjZKbDdsQ0FuaFcrNjRIM3Rhc1JFYjdURkFZUWVYemdjbTkveStSbURGVWdSUDRlRXU0dy9TOERjclRpUmNRPQ";


















    //yolocast
    public static String yolocastUrl = STATIC_PROPERTIES.getProperty("yolocast.url");
    public static String loginYolocastEmail = STATIC_PROPERTIES.getProperty("login.Yolocast.email");
    public static String loginYolocastPassword = STATIC_PROPERTIES.getProperty("login.Yolocast.password");
    public static String loginUri = STATIC_PROPERTIES.getProperty("login.uri");
    public static String scheduleEventsListuri = STATIC_PROPERTIES.getProperty("schedule.events.List.uri");
    public static String sourceListuri = STATIC_PROPERTIES.getProperty("source.uri");
    public static String sourceResetKeyuri = STATIC_PROPERTIES.getProperty("source.reset.key.uri");
    public static String activityCreateuri = STATIC_PROPERTIES.getProperty("create.activity.uri");
    //test环境数据库
    public static String driver = STATIC_PROPERTIES.getProperty("driver");
    public static String username = STATIC_PROPERTIES.getProperty("username");
    public static String password = STATIC_PROPERTIES.getProperty("password");
    public static String yololivUrl = STATIC_PROPERTIES.getProperty("url");
    public static String activityUrl = STATIC_PROPERTIES.getProperty("activity.url");
    public static String streamUrl = STATIC_PROPERTIES.getProperty("stream.url");
    public static String usercenterUrl = STATIC_PROPERTIES.getProperty("usercenter.url");
    //yolocast-c端平台
    //TODO c端 apitest
    //yolocast-dashboard
    //TODO dashboard apitest


}

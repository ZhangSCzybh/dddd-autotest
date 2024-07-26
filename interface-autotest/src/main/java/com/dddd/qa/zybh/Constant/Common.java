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

    //钉钉机器人
    public static String privateDingtalkUrl = STATIC_PROPERTIES.getProperty("private.dingtalk.alert.url");

    //jenkins绝对路径
    public static String jenkinsUrl = STATIC_PROPERTIES.getProperty("jenkins.url");

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

    //用来存储cookies的变量
    public static String Cookies;
    public static String DDingDDangToken;

    //下单--sku列表
    //public static String[] array1 = {"9147280", "9147301", "9147304", "9147239"};
    public static String[] array1 = {"9147169", "9147305", "9147417", "9147178","9147190","9147226","9147249","9147260","9147176","9147339","9147175","9147276","9147285","9147289","9147293"};
    public static String[] array2 = {"9147167", "9147172", "9147194", "9147221","9147420","9147423","9147173","9147205","9147224","9147232","9147251","9147254","9147211","9147346","9147354","9147268","9147283","9147300"};
    public static String[] array3 = {"9147309", "9147239", "9147343", "9147346","9147268","9147274","9147280","9147301","9147304"};
    public static String[] array4 = {"9147325", "9147417", "9147418", "9147422","9147424","9147253","9147254","9147255","9147257","9147262","9147357","9147292","9147295","9147298","9147302"};
    public static String[] array5 = {"9147305", "9147309", "9147419", "9147215","9147240","9147248","9147347","9147357","9147269","9147277","9147297"};








    /**********************************************************作废***********************************************************/

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

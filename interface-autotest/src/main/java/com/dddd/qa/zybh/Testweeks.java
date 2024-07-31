package com.dddd.qa.zybh;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author zhangsc
 * @date 2024年07月17日 11:41:00
 * @packageName com.dddd.qa.zybh
 * @className Testweeks
 * @describe TODO
 */
public class Testweeks {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    //ceshi 测试
    //karen 吴女士
    //蒋美娣 13858653282
    //赵秀、WXY13666605555
    //仙齐、18767176714
    //信达办公用品经营部
    //万客隆商贸有限公司
    //宇轩图文设计中心
    //智惠恒数码有限公司




    public static void main(String[] args) throws Exception {
        //输入快递单号查询快递
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入快递单号：");
        if (scan.hasNext()) {
            String number = scan.next();
            expressInquiry(number);
        }
        scan.close();

    }

    //数据驱动
    @DataProvider(name = "testData")
    public Object[][] testData() {
        return new Object[][] {
                {2} // 数据集1
        };
    }

    //快递查询
    private static String expressInquiry(String num){
        String host = "https://wdexpress.market.alicloudapi.com"; // 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/gxali"; // 【2】后缀
        String appcode = "5d72c5063a934c3fae588ea0e8cec2ce"; // 【3】开通服务后 买家中心-查看AppCode
        String n = "320063036617889"; // 【4】请求参数，详见文档描述
        String t = "";//  【4】请求参数，不知道可不填 95%能自动识别
        String urlSend = host + path + "?n=" + num + "&t="+ t;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                //System.out.println("获取返回的json:" + json);
                JSONObject jsonObject = new JSONObject(json);


                System.out.println("快递单号:" +jsonObject.get("LogisticCode").toString());
                System.out.println("快递公司:" +jsonObject.get("Name").toString());
                System.out.println("更新时间:" +jsonObject.get("updateTime").toString());
                System.out.println("快递历时:" +jsonObject.get("takeTime").toString());
                System.out.println("配送电话:" +jsonObject.get("CourierPhone").toString());
                System.out.println("*********************************************************");


                int length = new JSONArray(jsonObject.get("Traces")).toArray().length;
                for (int i = 0; i < length; i++) {
                    String dizhi = new JSONObject(new JSONArray(jsonObject.get("Traces")).get(i)).get("AcceptStation").toString();
                    String time = new JSONObject(new JSONArray(jsonObject.get("Traces")).get(i)).get("AcceptTime").toString();
                    System.out.println("接收站点:" + dizhi + "\n"
                    +"接收时间:" + time );
                }

            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else if (httpCode == 403 && error.equals("Api Market Subscription quota exhausted")) {
                    System.out.println("套餐包次数用完，请续购套餐");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                }
            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
        }

        return null;
    }

    //快递查询
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    //获取csv文件数据
    public static List<String> readCSV(String path) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }



}
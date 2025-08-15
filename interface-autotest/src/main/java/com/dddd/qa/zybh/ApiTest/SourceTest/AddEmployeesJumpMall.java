package com.dddd.qa.zybh.ApiTest.SourceTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import com.jayway.jsonpath.internal.function.text.Concatenate;
import com.sun.xml.internal.bind.v2.TODO;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangshichao
 * @date 2025年08月15日 17:56:51
 * @packageName com.dddd.qa.zybh.ApiTest.SourceTest
 * @className AddEmployeesJumpMall
 * @describe 不同环境Common.loginFuliHrInfo只需要替换这个
 * 本地路径 GetCaseUtil.getAllCases(addressInfo); jenkins路径GetCaseUtil.getAllCases1(addressInfo)
 */
public class AddEmployeesJumpMall {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();

    private static final String addressInfo = "dddd/addressInfo";
    private static String loginMallToken;
    private static String employeeLoginName;
    private static final String password= "addzaiyebuhui";
    private static String addressId;

    //TODO
    //1、福粒企业hr平台，新增员工
    //2、登录福粒商城、存储token
    //3、新增地址、获取地址id、存储id


    @BeforeClass
    public static void setUp() {
        Common.FuliHrToekn = LoginUtil.loginFuliHrToken("https://back.lixiangshop.com/enterpriseadmin/account/login" , Common.loginFuliHrInfo);
        logger.info("执行登录获取福粒HR平台的token：" + Common.FuliHrToekn);
    }


    @Test(description = "福粒Hr平台新增员工")
    public void addEmployees() {
        //存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("email", password + DateUtil.RandomSixDigit()+ "@163.com");
        param.put("employeeName", password+ DateUtil.RandomSixDigit());
        param.put("employeeNo", password+DateUtil.RandomSixDigit());
        param.put("loginName", password+DateUtil.RandomSixDigit());
        param.put("mobile", Config.getTimestamp);
        param.put("password", password);
        param.put("resPassword", password);
        param.put("deptId", 809);
        String body = param.toString();
        String createUrl ="https://back.lixiangshop.com/enterpriseadmin/employees";
        headers.put("enterprise-cache", Common.FuliHrToekn);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        logger.info("新增员工：" + result);
    }

    @Test(dependsOnMethods = "addEmployees" ,description = "获取第一个员工的loginName")
    public void employeesList(){
        JSONObject param = JSONUtil.createObj();
        param.put("page", 1);
        param.put("pageSize", 100);
        String body = param.toString();
        String createUrl ="https://back.lixiangshop.com/enterpriseadmin/employees";
        headers.put("enterprise-cache", Common.FuliHrToekn);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String employeeName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("employeeName").toString();
        employeeLoginName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("loginName").toString();
        logger.info("新增员工: employeeName={},loginName={}", employeeName,employeeLoginName);

    }


    @Test(dependsOnMethods = "employeesList",description = "员工账号登录商城，获取登录token，新增地址")
    public void addAddress(){
        loginMallToken=  loginOperationPlatformToken("https://server.lixiangshop.com/enterprise/account/login");
        //存放参数
        com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases(addressInfo);
        String body2 = param2.toString();
        String createUrl2 = "https://server.lixiangshop.com/enterprise/address/addAddress";
        headers.put("employee-cache", loginMallToken);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(body2).execute().body();
        logger.info("新增地址：" + result2);
    }

    @Test(dependsOnMethods = "addAddress",description = "获取地址Id")
    public void addressLst(){
        JSONObject param = JSONUtil.createObj();
        String body = param.toString();
        String createUrl ="https://server.lixiangshop.com/enterprise/address/getAddressList";
        headers.put("employee-cache", loginMallToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        JSONArray data = jsonresult.getJSONArray("result");
        JSONObject firstOrder = data.getJSONObject(0);
        addressId =  firstOrder.get("id").toString();
        logger.info("新增地址Id：" + addressId);

        caveat("登录账号: " + employeeLoginName + "\n"
                +"登录密码：" + password + "\n"
                +"employee-cache：" + loginMallToken + "\n"
                + "地址Id："+ addressId);

    }

    public static String loginOperationPlatformToken(String loginUrl) {
        try {
            // 1. 创建 URL 对象
            URL url = new URL(loginUrl);
            // 2. 打开 HttpURLConnection 连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 3. 设置请求方法为 POST
            connection.setRequestMethod("POST");
            // 4. 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");
            // 5. 允许写出和输入
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 6. 写出请求体
            try (OutputStream os = connection.getOutputStream()) {
                // 将请求体数据写入输出流
                //byte[] input = userinfo.getBytes("UTF-8");
                //byte[] input = ("{ \"selfOperatedPlatform\": \"1\" ,\"password\": \"zaiyebuhui01\",\"loginName\": \"zaiyebuhui01\",\"agreePrivacyPolicy\": \"1\"}").getBytes("utf-8");
                JSONObject json = new JSONObject();
                json.put("selfOperatedPlatform", "1");
                json.put("password",password );
                json.put("loginName", employeeLoginName);
                json.put("agreePrivacyPolicy", "1");
                byte[] input = json.toString().getBytes("UTF-8");

                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("employee-cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

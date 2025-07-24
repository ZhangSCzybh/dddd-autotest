package com.dddd.qa.zybh.ApiTest.AiTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dddd.qa.zybh.utils.LoginResponse;
import com.alibaba.fastjson.JSON;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2025年03月17日 15:48:43
 * @packageName com.dddd.qa.zybh.ApiTest.AiTest
 * @className CursorTest
 * @describe 系统用户登录测试
 */
public class CursorTest {
    
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final String LOGIN_URL = "https://backpre.lixiangshop.com/admin/account/login";
    private static final String SUPPLIER_LIST_URL = "https://backpre.lixiangshop.com/admin/supplier/getSupplierInfoList";
    private static String scene = "福粒运用平台";


    /**
     * 读取响应内容
     */
    private String getResponseContent(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    /**
     * 解析响应内容为LoginResponse对象
     */
    private LoginResponse parseResponse(String responseContent) {
        return JSON.parseObject(responseContent, LoginResponse.class);
    }

    @Test(description = "系统用户登录测试")
    public void testAdminLogin() throws IOException {
        // 1. 创建URL对象
        URL url = new URL(LOGIN_URL);
        
        // 2. 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // 3. 设置请求方法为POST
        connection.setRequestMethod("POST");
        
        // 4. 设置请求头
        connection.setRequestProperty("Content-Type", "application/json");
        
        // 5. 允许输入输出
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 6. 构建请求体
        JSONObject param = JSONUtil.createObj();
        param.put("loginName", "admintest");
        param.put("password", "fortest");
        String requestBody = param.toString();

        // 7. 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // 读取响应内容并解析
        String responseContent = getResponseContent(connection);
        LoginResponse response = parseResponse(responseContent);
        
        logger.info("登录响应结果: code={}, message={}", response.getCode(), response.getMsg());

        // 获取session-token
        String sessionToken = connection.getHeaderField("fuli-cache");
        logger.info("获取到的fuli-cache: {}", sessionToken);

        // 断言验证
        Assert.assertEquals(response.getCode(), 1001, "登录应该成功");
        Assert.assertNotNull(sessionToken, "fuli-cache不应为空");
        
        // 如果登录成功，验证用户信息
        if (response.getCode() == 1001) {
            Assert.assertNotNull(response.getResult().getLoginName(), "登录成功时result不应为空");
        }

        connection.disconnect();
    }

    @Test(description = "错误密码登录测试")
    public void testWrongPassword() throws IOException {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        JSONObject param = JSONUtil.createObj();
        param.put("loginName", "king");
        param.put("password", "wrong_password");
        String requestBody = param.toString();

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        String responseContent = getResponseContent(connection);
        LoginResponse response = parseResponse(responseContent);
        
        logger.info("错误密码登录响应: code={}, message={}", response.getCode(), response.getMsg());

        Assert.assertNotEquals(response.getCode(), 1001, "使用错误密码应该无法登录成功");
        // 可以添加对错误信息的验证
        Assert.assertNotNull(response.getMsg(), "错误信息不应为空");

        connection.disconnect();
    }

    @Test(description = "空参数登录测试")
    public void testEmptyParams() throws IOException {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 测试空用户名
        JSONObject param1 = JSONUtil.createObj();
        param1.put("loginName", "");
        param1.put("password", "123456");
        String requestBody1 = param1.toString();

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody1.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        String responseContent = getResponseContent(connection);
        LoginResponse response = parseResponse(responseContent);
        
        logger.info("空参数登录响应: code={}, message={}", response.getCode(), response.getMsg());

        Assert.assertNotEquals(response.getCode(), 1001, "空用户名应该无法登录成功");
        Assert.assertNotNull(response.getMsg(), "错误信息不应为空");

        connection.disconnect();
    }

    @Test(description = "特殊字符参数登录测试")
    public void testSpecialCharParams() throws IOException {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        JSONObject param = JSONUtil.createObj();
        param.put("loginName", "<script>alert(1)</script>");
        param.put("password", "' OR '1'='1");
        String requestBody = param.toString();

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        String responseContent = getResponseContent(connection);
        LoginResponse response = parseResponse(responseContent);
        
        logger.info("特殊字符登录响应: code={}, message={}", response.getCode(), response.getMsg());

        Assert.assertNotEquals(response.getCode(), 1001, "特殊字符参数应该无法登录成功");
        Assert.assertNotNull(response.getMsg(), "错误信息不应为空");

        connection.disconnect();
    }

    @Test(description = "缺少必要参数登录测试")
    public void testMissingParams() throws IOException {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 只传用户名，不传密码
        JSONObject param = JSONUtil.createObj();
        param.put("loginName", "king");
        String requestBody = param.toString();

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        String responseContent = getResponseContent(connection);
        LoginResponse response = parseResponse(responseContent);
        
        logger.info("缺少参数登录响应: code={}, message={}", response.getCode(), response.getMsg());

        Assert.assertNotEquals(response.getCode(), 1001, "缺少必要参数应该无法登录成功");
        Assert.assertNotNull(response.getMsg(), "错误信息不应为空");

        connection.disconnect();
    }

    @Test(description = "获取供应商列表测试", dependsOnMethods = "testAdminLogin")
    public void testGetSupplierList() throws IOException {
        // 1. 先获取登录token
        System.out.println(getLoginToken());

        // 2. 调用供应商列表接口
        URL url = new URL(SUPPLIER_LIST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("fuli-cache", getLoginToken());

        // 读取响应内容
        String responseContent = getResponseContent(connection);
        logger.info("获取供应商列表响应: {}", responseContent);

        // 解析响应内容
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        CommonUtil.assertAvailable(responseJson, null, SUPPLIER_LIST_URL,Config.FuliYunYingPro, scene);

        // 断言验证
        Assert.assertNotNull(responseJson.get("result"), "供应商列表不应为空");
        // 获取result中的list数组
        //cn.hutool.json.JSONArray supplierList = responseJson.getJSONArray("result");
        String firstSupplierId = (new JSONObject((new JSONArray((new JSONObject(responseJson.get("result"))).get("list"))).get(0))).get("id").toString();
        String firstSupplierName = (new JSONObject((new JSONArray((new JSONObject(responseJson.get("result"))).get("list"))).get(0))).get("name").toString();
        logger.info("第一个供应商: {}", firstSupplierName + ",id:" + firstSupplierId);
        //Assert.assertEquals(firstSupplierId,"889584");
        connection.disconnect();
    }

    /**
     * 获取登录token的工具方法
     */
    private String getLoginToken() throws IOException {
        URL loginUrl = new URL(LOGIN_URL);
        HttpURLConnection loginConnection = (HttpURLConnection) loginUrl.openConnection();
        loginConnection.setRequestMethod("POST");
        loginConnection.setRequestProperty("Content-Type", "application/json");
        loginConnection.setDoOutput(true);
        loginConnection.setDoInput(true);

        JSONObject loginParam = JSONUtil.createObj();
        loginParam.put("loginName", "admintest");
        loginParam.put("password", "fortest");
        String loginRequestBody = loginParam.toString();

        try (OutputStream os = loginConnection.getOutputStream()) {
            byte[] input = loginRequestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        String fuliCache = loginConnection.getHeaderField("fuli-cache");
        loginConnection.disconnect();
        return fuliCache;
    }
}

package com.dddd.qa.zybh.ApiTest.AiTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
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

/**
 * @author zhangsc
 * @date 2025年03月17日 15:48:43
 * @packageName com.dddd.qa.zybh.ApiTest.AiTest
 * @className CursorTest
 * @describe 系统用户登录测试
 */
public class CursorTest {
    
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final String LOGIN_URL = "https://admintest.ddingddang.com/api/admin/sysuser/login";

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
        param.put("loginName", "king");
        param.put("password", "123456");
        String requestBody = param.toString();

        // 7. 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // 读取响应内容
        String responseContent = getResponseContent(connection);
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        
        // 从响应体中获取code
        int responseCode = responseJson.getInt("code");
        String message = responseJson.getStr("msg");
        logger.info("登录响应结果: code=" + responseCode + ", message=" + message);

        // 获取session-token
        String sessionToken = connection.getHeaderField("session-token");
        logger.info("获取到的session-token: " + sessionToken);

        // 断言验证
        Assert.assertEquals(responseCode, 1001, "登录应该成功");
        Assert.assertNotNull(sessionToken, "session-token不应为空");

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
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        
        int responseCode = responseJson.getInt("code");
        String message = responseJson.getStr("msg");
        logger.info("错误密码登录响应: code=" + responseCode + ", message=" + message);

        Assert.assertNotEquals(responseCode, 200, "使用错误密码应该无法登录成功");

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
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        
        int responseCode = responseJson.getInt("code");
        String message = responseJson.getStr("msg");
        logger.info("空参数登录响应: code=" + responseCode + ", message=" + message);

        Assert.assertNotEquals(responseCode, 200, "空用户名应该无法登录成功");

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
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        
        int responseCode = responseJson.getInt("code");
        String message = responseJson.getStr("msg");
        logger.info("特殊字符登录响应: code=" + responseCode + ", message=" + message);

        Assert.assertNotEquals(responseCode, 200, "特殊字符参数应该无法登录成功");

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
        JSONObject responseJson = JSONUtil.parseObj(responseContent);
        
        int responseCode = responseJson.getInt("code");
        String message = responseJson.getStr("msg");
        logger.info("缺少参数登录响应: code=" + responseCode + ", message=" + message);

        Assert.assertNotEquals(responseCode, 200, "缺少必要参数应该无法登录成功");

        connection.disconnect();
    }
}

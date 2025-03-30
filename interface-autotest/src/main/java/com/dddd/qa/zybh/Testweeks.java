package com.dddd.qa.zybh;


import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.reflect.generics.tree.VoidDescriptor;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.io.*;



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
    //仙齐、许林彪18767176714
    //信达办公用品经营部
    //万客隆商贸有限公司
    //宇轩图文设计中心
    //智惠恒数码有限公司

    //数据驱动
    @DataProvider(name = "testData")
    public Object[][] testData() {
        return new Object[][] {
                {2} // 数据集1
        };
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



    public static void main(String[] args) throws IOException {
        URL url = new URL("https://hr.ddingddang.com/api/enterprise/account/login");
        //URL url = new URL("https://cx.ddingddang.com/api/enterprise/employee/account/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 3. 设置请求方法为 POST
        connection.setRequestMethod("POST");
        // 4. 设置请求头
        connection.setRequestProperty("Content-Type", "application/json");
        // 5. 允许写出和输入
        connection.setDoOutput(true);
        connection.setDoInput(true);
        try (OutputStream os = connection.getOutputStream()) {
            // 将请求体数据写入输出流
            //byte[] input= userinfo.getBytes("UTF-8");
            //byte[] input = ("{ \"loginAccount\": \"15757807860\" , \"loginType\": \"1\" , \"password\": \"888888\" , \"enterpriseId\": \"1169\"}").getBytes("utf-8");
            byte[] input = ("{ \"loginName\": \"ddyanshi\" ,\"password\": \"Aa123456.\" ,\"identityType\": \"3\"}").getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        System.out.println(connection.getHeaderField("session-token"));
        //System.out.println(LoginUtil.loginToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri,Common.loginDDingDDangInfo));



    }



    @Test
    public void testLoginAndGetToken() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost("https://hr.ddingddang.com/api/enterprise/account/login");
            //HttpPost httpPost = new HttpPost("https://hr.ddingddang.com/api/enterprise/account/login");
            StringEntity entity = new StringEntity("{ \"identityType\": \"3\", \"username\": \"ddyanshi\", \"password\": \"Aa123456.\" }");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                Assert.assertEquals(statusCode, 200, "Expected response code 200");
                String sessionToken = response.getFirstHeader("session-token").getValue();
                System.out.println("Session Token: " + sessionToken);
                Assert.assertNotNull(sessionToken, "Session Token should not be null");
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }

    @Test
    public void testLoginAndGetSessionToken() throws IOException {
        // Base URL of the API
        String baseUrl = "https://hr.ddingddang.com/api/enterprise/account/login";

        // Request payload (JSON format)
        String requestBody = "{ \"identityType\": \"3\", \"username\": \"ddyanshi\", \"password\": \"Aa123456.\" }";

        // Create HttpClient instance
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Create HttpPost request
            HttpPost post = new HttpPost(baseUrl );

            // Set headers
            post.setHeader("Content-Type", "application/json");

            // Set request body
            post.setEntity(new StringEntity(requestBody));

            // Send the request and capture the response
            HttpResponse response = client.execute(post);

            // Log the response status code for debugging
            System.out.println("Response Status Code: " + response.getStatusLine().getStatusCode());

            // Assert that the response status code is 200 (OK)
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Expected status code 200");

            // Extract headers from the response
            String sessionToken = response.getFirstHeader("session-token") != null
                    ? response.getFirstHeader("session-token").getValue()
                    : null;

            // Log the session token
            System.out.println("Session Token: " + sessionToken);

            // Assert that the session token is not null or empty
            Assert.assertNotNull(sessionToken, "Session token should not be null");
            Assert.assertNotEquals(sessionToken.trim(), "", "Session token should not be empty");

            // Optionally, log the response body
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response Body: " + responseBody);
        }
    }

    @Test
    public void loginToken(){
        try {
            // 1. 创建 URL 对象
            URL url = new URL("https://hr.ddingddang.com/api/enterprise/account/login");
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
                //byte[] input= userinfo.getBytes("UTF-8");
                byte[] input = ("{ \"identityType\": \"3\" ,\"password\": \"zybh123456\",\"username\": \"17858805009\"}").getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            System.out.println(connection.getHeaderField("session-token"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
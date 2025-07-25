package com.dddd.qa.zybh.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dddd.qa.zybh.Constant.Common;
import okhttp3.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2022-05-01 上午11:38
 */
public class LoginUtil {

    //获取账号中心登录cookie
    public static String loginCookie1(String url, String eamil, String password) {
        // 全局请求设置
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        // 创建HttpClient上下文
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        // 创建一个HttpClient
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore).build();
        CloseableHttpResponse res = null;
        // 创建本地的HTTP内容
        try {
            try {
                String bodys = String.format("{\"eamil\":\"%s\",\"password\":\"%s\"}", eamil, password);
                System.out.println(bodys);
                // 创建一个post请求
                HttpPost post = new HttpPost(url);
                // 注入post数据
                post.setEntity(new StringEntity(bodys, "utf-8"));
                post.addHeader("Content-Type", "application/json;charset=utf-8");
                res = httpClient.execute(post, context);
                JSONObject response = JSONObject.parseObject(EntityUtils.toString(res.getEntity()));
                res.close();

                System.out.println(response);
                return response.getJSONObject("data").getString("token");
            } finally {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取账号中心登录cookie-----废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃废弃
    public static String loginCookie(String url, String eamil, String password) {
        //String param = "{\"email\":\"zhangsc@yunxi.tv\",\"password\":\"zsc123456\"}";
        //String body = String.format("{\"eamil\":\"%s\",\"password\":\"%s\"}", eamil, password);
        try {
            cn.hutool.json.JSONObject param = JSONUtil.createObj();
            param.put("email", eamil);
            param.put("password", password);
            String body = param.toString();
            String result = HttpUtil.post(url, body);
            //System.out.println(result);
            cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
            return (String) (new cn.hutool.json.JSONObject(jsonresult.get("data"))).get("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //自建供应商平台token ：enterprise-cache
    public static String loginSelfsupplierToken1(String loginUrl, String userinfo) {
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
                // 使用变量拼接 JSON 字符串
                // 将字符串转换为字节数组
                byte[] input = userinfo.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("enterprise-cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //*****************************从这开始生效**************************

    //叮叮当当员工pc-获取token接口
    public static String loginYGPCToken(String loginUrl, String userinfo) {
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
                byte[] input = userinfo.getBytes("UTF-8");
                //byte[] input = ("{ \"identityType\": \"3\" ,\"password\": \"zybh123456\",\"username\": \"17858805009\"}").getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("session-token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //福粒/慧卡运营平台token ：fuli-cache
    public static String loginOperationPlatformToken(String loginUrl, String userinfo) {
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
                byte[] input = userinfo.getBytes("UTF-8");
                //byte[] input = ("{ \"identityType\": \"3\" ,\"password\": \"zybh123456\",\"username\": \"17858805009\"}").getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("fuli-cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //智采企业平台token：session-token
    public static String loginDingdangZCToken(String loginUrl, String userinfo) {
        OkHttpClient client = new OkHttpClient();
        // 构造请求体
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), userinfo);

        // 构造请求
        Request request = new Request.Builder()
                .url(loginUrl)
                .post(body)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 获取响应头中的 session-token
                String sessionToken = response.header("session-token");
                return sessionToken;
            } else {
                System.out.println("HTTP 请求失败，响应码: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //自建供应商平台Token：enterprise-cache
    public static String loginSelfSupplierToken(String loginUrl) {
        String createUrl = Common.zhicaiHrUrl + Common.enterpriseSelfsupplierCodeuri;
        Map<String, String> headers = new HashMap<>();
        headers.put("session-token", Common.DDingDDangToken);
        // 发起 GET 请求获取 entCode
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        System.out.println(result);
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        String enterpriseSelfsupplierCode = (new cn.hutool.json.JSONObject(jsonresult.get("result")).get("entCode").toString());//获取entCode
        System.out.println("跳转自建供应商平台entCode:" + enterpriseSelfsupplierCode);

        // 拼接 JSON 参数
        String jsonUserInfo = "{ \"entCode\": \"" + enterpriseSelfsupplierCode + "\"}";
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
                // 使用变量拼接 JSON 字符串
                // 将字符串转换为字节数组
                byte[] input = jsonUserInfo.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("enterprise-cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;




    }

    //员工PC跳转登录商城
    public static String loginJumpMallToken(String loginUrl,String ygpcToken){
        String createUrl = Common.zhicaiYgUrl + Common.openMallCodeUri + "?payEnterpriseId=8&source=1";
        Map<String, String> headers = new HashMap<>();
        headers.put("session-token", ygpcToken);
        // 发起 GET 请求获取 entCode
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        System.out.println(result);
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        String empCode =(new cn.hutool.json.JSONObject(jsonresult.get("result")).get("empCode").toString());
        System.out.println("跳转商城的empCode:" + empCode);
        // 拼接 JSON 参数
        //String jsonUserInfo = "{ \"entCode\": \"" + empCode + "\" , \"todd\": \"mall\"}";
        try {
            cn.hutool.json.JSONObject param = JSONUtil.createObj();
            param.put("code", empCode);
            param.put("todd", "mall");
            param.put("token", "");
            String body1 = param.toString();
            String result1 = HttpUtil.post(loginUrl, body1);
            //System.out.println(result);
            cn.hutool.json.JSONObject jsonresult1 = new cn.hutool.json.JSONObject(result1);
            String MallToken = (new cn.hutool.json.JSONObject((new cn.hutool.json.JSONObject(jsonresult1.get("result"))).get("data"))).get("token").toString();
            return MallToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



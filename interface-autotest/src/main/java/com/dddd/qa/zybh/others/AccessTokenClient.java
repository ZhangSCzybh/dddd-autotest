package com.dddd.qa.zybh.others;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dddd.qa.zybh.utils.DateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author zhangsc
 * @date 2025年10月21日 17:22:04
 * @packageName com.dddd.qa.zybh.others
 * @className AccessTokenClient
 * @describe 线下收银三方平台获取accessToken--CLIENT_ID--CLIENT_SECRET
 */


public class AccessTokenClient {


    private static final String API_URL = "http://openapi-dev.lixiangshop.com/openapi/oauth2/accessToken";
    private static final String CLIENT_ID = "ci15d8d5e792904300a66bae5c84cbb3ad";
    private static final String CLIENT_SECRET = "cs162bfe37ae844c56878f25dfb362a120498f07e5d55d4eb7a37cf8a446884a0c";
    private static final HashMap<String, String> headers = new HashMap<>();

    public static void main(String[] args) {
        try {
            String accessToken = fetchAccessToken();
            System.out.println("获取到的accessToken: " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fetchAccessToken() throws Exception {
        // 1. 生成当前时间戳 (格式: yyyy-MM-dd HH:mm:ss)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        System.out.println("时间戳" + timestamp);
        // 2. 生成签名
        String sign = DigestUtils.md5Hex(
                String.format("%s%s%s%s%s",
                        CLIENT_SECRET, timestamp,  CLIENT_ID,"access_token", CLIENT_SECRET)
        );
        System.out.println("签名:" + sign);

        // 3. 构造请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("clientId", CLIENT_ID);
        requestBody.put("clientSecret", CLIENT_SECRET);
        requestBody.put("grantType", "1");
        requestBody.put("timestamp", timestamp);
        requestBody.put("sign", sign);


        String body = requestBody.toString();
        headers.put("Content-Type", "application/json");
        String result = HttpUtil.createPost(API_URL).addHeaders(headers).body(body).execute().body();
        System.out.println("Response body: " + result);
        cn.hutool.json.JSONObject jsonResult = new cn.hutool.json.JSONObject(result);
        String accessToken =  (new cn.hutool.json.JSONObject(jsonResult.get("result")).get("accessToken").toString());

        return accessToken;
    }

    private static String sendHttpPost(String url, String jsonBody) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体
            StringEntity entity = new StringEntity(jsonBody, "UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity1 = response.getEntity();
                if (entity1 != null) {
                    return EntityUtils.toString(entity1, "UTF-8");
                } else {
                    throw new Exception("响应体为空");
                }
            }
        }
    }
}
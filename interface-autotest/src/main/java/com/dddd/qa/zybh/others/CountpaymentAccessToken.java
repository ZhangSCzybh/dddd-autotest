package com.dddd.qa.zybh.others;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dddd.qa.zybh.Constant.Common;
import io.qameta.allure.model.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author zhangsc
 * @date 2025年10月21日 17:22:04
 * @packageName com.dddd.qa.zybh.others
 * @className AccessTokenClient
 * @describe
 */


public class CountpaymentAccessToken {

    private static final HashMap<String, String> headers = new HashMap<>();

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            // 1. 从用户输入获取三个必要参数
            System.out.print("请输入API URL (例如: http://openapi-dev.lixiangshop.com/openapi/oauth2/accessToken): ");
            String counterpaymentApiUrl = scanner.nextLine();
            System.out.print("请输入Client ID: ");
            String counterpaymentClientId = scanner.nextLine();
            System.out.print("请输入Client Secret: ");
            String counterpaymentClientSecret = scanner.nextLine();
            scanner.close();
            // 1. 生成当前时间戳 (格式: yyyy-MM-dd HH:mm:ss)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            System.out.println("时间戳" + timestamp);
            // 2. 生成签名
            String sign = DigestUtils.md5Hex(
                    String.format("%s%s%s%s%s",
                            counterpaymentClientSecret, timestamp,  counterpaymentClientId,"access_token", counterpaymentClientSecret)
            );
            System.out.println("签名:" + sign);

            // 3. 构造请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("clientId", counterpaymentClientId);
            requestBody.put("clientSecret", counterpaymentClientSecret);
            requestBody.put("grantType", "1");
            requestBody.put("timestamp", timestamp);
            requestBody.put("sign", sign);


            String body = requestBody.toString();
            headers.put("Content-Type", "application/json");
            String result = HttpUtil.createPost(counterpaymentApiUrl).addHeaders(headers).body(body).execute().body();
            System.out.println("Response body: " + result);
            cn.hutool.json.JSONObject jsonResult = new cn.hutool.json.JSONObject(result);
            String accessToken =  (new cn.hutool.json.JSONObject(jsonResult.get("result")).get("accessToken").toString());
            System.out.println("获取到的accessToken: " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
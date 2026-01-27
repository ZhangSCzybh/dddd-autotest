package com.dddd.qa.zybh.ApiTest.FuliOpTest;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

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
            System.out.println("时间戳:" + timestamp);
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
            cn.hutool.json.JSONObject jsonResult = new cn.hutool.json.JSONObject(result);
            String accessToken =  (new cn.hutool.json.JSONObject(jsonResult.get("result")).get("accessToken").toString());
            System.out.println("accessToken: " + accessToken);
            System.out.println("Authorization: " +  "Bearer " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
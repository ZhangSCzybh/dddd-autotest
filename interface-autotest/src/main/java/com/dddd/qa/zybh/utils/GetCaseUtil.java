package com.dddd.qa.zybh.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.dddd.qa.zybh.Constant.Common;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsc
 * @date 2022-05-03 下午5:37
 */
public class GetCaseUtil {
    //读取json格式案例
    public static JSONObject getCase(String caseName, String fileName) {
        try {
            File file = new File("");
            String filePath = file.getAbsolutePath() + "/src/main/resources/" + fileName + ".json";

            InputStream inputStream = new FileInputStream(filePath);
            String caseTags = IOUtils.toString(inputStream, "utf8");
            JSONObject config = JSONObject.parseObject(caseTags, Feature.OrderedField);
            return config.getJSONObject(caseName);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public static JSONObject getAllCases(String fileName) {
        JSONObject result = new JSONObject();
        try {
            File file = new File("");
            String filePath = file.getAbsolutePath() + "/src/main/resources/" + fileName + ".json";

            InputStream inputStream = new FileInputStream(filePath);
            String caseTags = IOUtils.toString(inputStream, "utf8");
            result = JSONObject.parseObject(caseTags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JSONObject getAllCases1(String fileName) {
        JSONObject result = new JSONObject();
        try {
            File file = new File("");
            String filePath = Common.jenkinsUrl+"/src/main/resources/" + fileName + ".json";
            InputStream inputStream = new FileInputStream(filePath);
            String caseTags = IOUtils.toString(inputStream, "utf8");
            result = JSONObject.parseObject(caseTags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //自建供应商发货接口
    public static void sendPostRequest(String requestUrl,String token) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl);

        // 创建JSON数组和对象
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logisticsChannel", "百福东方");
        jsonObject.put("logisticsNumber", DateUtil.getTodayCurrent());
        jsonObject.put("remark", "");
        jsonArray.put(jsonObject);

        // 将JSON数组转换为字符串，并设置为请求体
        StringEntity entity = new StringEntity(jsonArray.toString(), "UTF-8");
        httpPost.setEntity(entity);

        // 设置请求头Content-Type为application/json
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Supplier-Cache", token );

        // 发送请求并获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            // 获取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            // 读取响应体
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response Body: " + responseBody);
        } finally {
            // 关闭响应和HttpClient
            response.close();
            httpClient.close();
        }
    }


}


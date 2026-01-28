package com.dddd.qa.zybh.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2022-05-03 下午5:37
 */
public class GetCaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static HashMap<String, String> headers =new HashMap<>();
    private static final String employeePointsParameters = "dddd/employeePointsParameters";
    private static final String employeePointsParametersYGPC = "dddd/employeePointsParametersYGPC";

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


    //读取json格式案例
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
    //读取jenkins路径下的json格式案例
    public static JSONObject getAllCases1(String fileName) {
        JSONObject result = new JSONObject();
        try {
            File file = new File("");
            String filePath = Common.jenkinsUrl+ fileName + ".json";
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
        jsonObject.put("remark", "发货成功:"+DateUtil.getTodayCurrent());
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

    //添加库存
    public static void updateSkuStock(String skuCode){
        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("goodsStorge", 1);
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = Common.SupplierUrl+Common.fuliOpUpdateSkuStateUri+skuCode+"/updatestock";
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "sku:" + skuCode +";库存状态:" + jsonresult.get("msg").toString());

    }


    //智采员工pc平台--发放员工积分
    public static void giveEmployeePointsPC(Integer[] list, String amount){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(Common.ygpcEmployeePointsParameters);
        caveat("检查json文件路径:" + Common.ygpcEmployeePointsParameters);
        param.put("list", list);
        param.put("amount", amount);
        String body = param.toString();
        String createUrl = Common.zhicaiYgUrl+Common.sendEmployeePointsUri;
        headers.put("Session-Token", Common.DDingDDangPCToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        String data = jsonresult.get("result").toString();
        logger.info( data + ":员工积分发放成功！");
        caveat( "===========智采员工积分补发===========" + "\n"+ "员工编号:" + Arrays.toString(list) + "\n"+ "发放额度:" + amount + "积分" + "\n" + "发放结果:" + data);
    }


    //**********************************************不用***************************************************
    //福粒运营平台--上架spu 不用
    public static void updateSpuState(String spuCode){
        cn.hutool.json.JSONObject param = JSONUtil.createObj();//存放参数
        param.put("goodsState", 1);
        param.put("spuCode", spuCode);
        //headers.put("fuli-cache", "SDROdk4F2o9V6k4w8w5Rf2AteJCL92Edbp8d13BB7q3YaWAx8q6ZbUCl7uBYcX9l3X4edUFaWU9MbTNTWThXdWdEVWk0czNIempzWjNiaWExpbExxN045S1dxaiatKRFBNZ2JHS2VwU1dTV3ovZThaWElJNFNlcXhnYlAwZWNNbG9maDA9");
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = "https://backdev.lixiangshop.com"+Common.fuliOpUpdateSpuStateUri;
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "spu:" + spuCode +";上架状态:" + jsonresult.get("msg").toString());

    }

    //福粒运营平台--开启销售sku 不用
    public static void updateSkuState(String skuCode ){
        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("status", 1);
        //headers.put("fuli-cache", "SDROdk4F2o9V6k4w8w5Rf2AteJCL92Edbp8d13BB7q3YaWAx8q6ZbUCl7uBYcX9l3X4edUFaWU9MbTNTWThXdWdEVWk0czNIempzWjNiaWExpbExxN045S1dxaiatKRFBNZ2JHS2VwU1dTV3ovZThaWElJNFNlcXhnYlAwZWNNbG9maDA9");
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = "https://backdev.lixiangshop.com"+Common.fuliOpUpdateSkuStateUri+skuCode+"/updateState";
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "sku:" + skuCode +";销售状态:" + jsonresult.get("msg").toString());
    }

}


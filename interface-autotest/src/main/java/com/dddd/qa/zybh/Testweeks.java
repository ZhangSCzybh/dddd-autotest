package com.dddd.qa.zybh;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author zhangsc
 * @date 2024年07月17日 11:41:00
 * @packageName com.dddd.qa.zybh
 * @className Testweeks
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
        int randomNumber = ThreadLocalRandom.current().nextInt(10, 10000);
    }



    @Test
    public void test(){
        String aa = "GYS20250331009631";
        JSONObject param = JSONUtil.createObj();
        param.put("status", 2);
        param.put("page", 1);
        param.put("pageSize", 100);
        String createUrl = "https://pctest.ddingddang.com/api/enterprise/approval/pending";
        headers.put("session-token", "NTIraFEA1x0Na1DAc13c9l4N9BBUa05M9yAUb2BZen5N6DEc0w5a7nFl2Z0bf28F8a3TeGYvS2twZVR1aDBnYzY1bnRlejJ2YUQ2NEt3Z1hZTXVKSk1nNia9lNFp0dVk3ZEM1dmI3MjFrRVhYYno5blpHTlV6ZThtU2FHTFhwbE96ckNHYTBBYXd4MDRJclRKVGp3TWZaVVJKOEFXZ2tmVXAwODB1UHBLNjNNSWwyd3JmL01jUTFXcW9pWmNISVY1Nm5CMEoyLzB3SU5BOUpKQlQxNVFYVXJIZia9KOStyV1JURTlGZXplR3FTcCt2MFRKcUhqS04yNVA0VTd1N24rQTJIdFI0UnRXYmszdEFiaOVhWVmJJWTVuaUU4Q3ZGYm5ISU9HMllwOXE1TldndUdmMEFQcmpXL0ZJMjMxSnFublNnZjBKM2NUVVNlZnBjQmc5eFNaOWNkVGRBdmJZWUZ1czZSSHFtVmt5REs2NjBnPT0");
        //String result = HttpUtil.createGet(createUrl).addHeaders(headers).body(body).execute().body();
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        //获取第一个提货券
        JSONObject jsonresult = new JSONObject(result);
        //获取数组长度
        String data = jsonresult.get("result").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("list"));
        int length = jsonArray.toArray().length;
        System.out.println(length);

        for(int i = 0; i < length; i++) {
            String approvalNo = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("approvalNo").toString();
            if (approvalNo != null && aa.equals(approvalNo)) {
                // 执行逻辑
                System.out.println(approvalNo);
                String id = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
                System.out.println(id);
            }else {
                System.out.println("无此approvalNo");
            }

        }


    }
}
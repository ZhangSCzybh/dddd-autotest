package com.dddd.qa.zybh.ApiTest.SourceTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangsc
 * @date 2024年12月20日 16:46:49
 * @packageName com.dddd.qa.zybh.ApiTest
 * @className EmployeeIntegral
 * @describe 批量回收积分
 */
public class EmployeeIntegral {
    private static final HashMap<String, String> headers =new HashMap<>();


    //智采员工积分回收
    @DataProvider(name = "recycleIntegral")
    public Object[][] ygIdFromCSV() {
        List<Object[]> data = new ArrayList<>();
        //Common.jenkinsUrl+"/src/main/resources/dddd/supplierToken.csv"
        try (BufferedReader br = new BufferedReader(new FileReader(new File("").getAbsolutePath()+"/src/main/resources/source/recycleIntegralEmployeeId.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("num")) { // 跳过标题行
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }


    @Test(dataProvider = "recycleIntegral")
    public void recycleIntegral(String id ){
        JSONObject param = JSONUtil.createObj();
        param.put("id", id);
        param.put("remark", "");
        String body = param.toString();
        String createUrl = "https://hrtest.ddingddang.com/api/enterprise/employee/recycleRecord/recycleIntegral";
        headers.put("session-token", "");
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);

    }



}

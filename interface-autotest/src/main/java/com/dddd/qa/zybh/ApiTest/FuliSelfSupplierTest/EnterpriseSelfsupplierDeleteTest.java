package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangsc
 * @date 2025年04月29日 16:21:37
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className EnterpriseSelfsupplierDeleteTest
 */
public class EnterpriseSelfsupplierDeleteTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene2 = "邀请供应商模块";

    @Test(dependsOnMethods = {"com.dddd.qa.zybh.ApiTest.YGPCTest.ApprovalTest.processHandle"},description = "删除新建的供应商")
    public void selfSupplierDelete(){
        JSONObject param = JSONUtil.createObj();
        param.put("page",1);
        param.put("pageSize",100);
        List<Integer> enterpriseIds = new ArrayList<>();
        enterpriseIds.add(Integer.valueOf(Common.enterprId));
        param.put("employeeIds", enterpriseIds);
        String body = param.toString();
        String createUrl = Common.SelfsupplierUrl+Common.supplierlistUri;
        headers.put("enterprise-cache", Common.SelfsupplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.SelfSupplierPro, scene2);

        //根据名字获取供应商id，删除对应供应商
        String data = jsonresult.get("result").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String Name = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("name").toString();
            if(Config.supplierName.equals(Name)){
                String Id = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
                logger.info("需要删除的供应商: Id={}, Name={}", Id, Name);
                String createDelUrl = Common.SelfsupplierUrl+Common.supplierdelSupplierInfoUri + "/"+ Id ;
                headers.put("enterprise-cache", Common.SelfsupplierToken);
                String delResult =HttpUtil.createGet(createDelUrl).addHeaders(headers).execute().body();
                JSONObject delJsonresult = new JSONObject(delResult);
                //接口可行性
                CommonUtil.assertAvailable(delJsonresult, body, createUrl, Config.SelfSupplierPro, scene2);
                logger.info("供应商删除成功: Id={}, Name={}", Id, Name);
                break;
            }
        }

    }
}

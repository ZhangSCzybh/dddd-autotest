package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2025年03月30日 10:50:49
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className EnterpriseSelfsupplierTest
 */
public class EnterpriseSelfsupplierTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene2 = "邀请供应商模块";
    private static String applyId;


    @BeforeClass
    public static void setUp() {
        Common.DDingDDangPCToken = LoginUtil.loginYGPCToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri , Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc平台的token：" + Common.DDingDDangPCToken);
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        Common.SelfsupplierToken = LoginUtil.loginSelfSupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri);
        logger.info("执行登录获取自建供应商平台的token：" + Common.SelfsupplierToken);

    }

    @Test(description = "生成邀请供应商")
    public void selfSupplierApply(){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases1(Common.applySelfSupplierInfo);//getAllCases需要换成生产环境的参数
        param.put("name", "自动邀请供应商"+ DateUtil.RandomSixDigit());
        param.put("alias", DateUtil.RandomSixDigit());
        param.put("loginName", "zdyqgys"+DateUtil.RandomSixDigit());
        param.put("password", "zdyqgys"+DateUtil.RandomSixDigit());
        param.put("enterprId", Common.enterprId);
        String body = param.toString();
        String createUrl = Common.SelfsupplierUrl+ Common.applySelfSupplierUri;
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        applyId = (new JSONObject(jsonresult.get("result"))).get("id").toString();
        Config.supplierName = (new JSONObject(jsonresult.get("result"))).get("supplierName").toString();
        logger.info("填写邀请供应商信息时返回的applyId和供应商名称: applyId={}, supplierName={}", applyId, Config.supplierName);
    }

    @Test(dependsOnMethods ={"selfSupplierApply"},description = "邀请的供应商提交审批")
    public void submitApproval() throws InterruptedException {
        JSONObject param = JSONUtil.createObj();//需要换成生产环境的参数
        param.put("appendUrl","/" + applyId + "/2");
        String body = param.toString();
        //String createUrl = Common.SelfsupplierUrl+ Common.supplierRegisterApplyUri;
        String createUrl = Common.SelfsupplierUrl + Common.supplierRegisterApplyUri +"/" + applyId + "/2";
        headers.put("enterprise-cache", Common.SelfsupplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.SelfSupplierPro, scene2);
        Thread.sleep(2000);
    }

    @Test(dependsOnMethods ={"submitApproval"},description = "获取审批中的列表数据")
    public void approvalList(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 1);
        param.put("timeType", 0);
        param.put("page", 1);
        param.put("pageSize", 100);
        String createUrl = Common.SelfsupplierUrl + Common.supplierRegisterSelectlistUri;
        headers.put("enterprise-cache", Common.SelfsupplierToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.SelfSupplierPro, scene2);

        String data = jsonresult.get("result").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String applyIdZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("applyId").toString();
            if(applyId.equals(applyIdZuiXin)){
                Config.approvalNo= (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("approvalNo").toString();
                logger.info("邀请供应商的编号和审批单据编号: applyId={}, approvalNo={}", applyIdZuiXin, Config.approvalNo);
                break;
            }
        }
    }

}

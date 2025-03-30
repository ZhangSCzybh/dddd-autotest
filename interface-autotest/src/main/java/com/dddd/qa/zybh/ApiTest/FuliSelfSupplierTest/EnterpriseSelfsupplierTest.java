package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2025年03月30日 10:50:49
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className EnterpriseSelfsupplierTest
 * @describe TODO
 */
public class EnterpriseSelfsupplierTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangPCToken = LoginUtil.loginToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri , Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc的token：" + Common.DDingDDangPCToken);

        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);

    }


    @Test(description = "获取自建供应商平台跳转code登录自建供应商平台")
    public void EnterpriseSelfsupplierLoginTest(){
        String createUrl = Common.zhicaiHrUrl+Common.enterpriseSelfsupplierCodeuri + "?payEnterpriseId=8";//不同企业的payEnterpriseId需要修改 福粒10 慧卡8
        headers.put("session-token", Common.DDingDDangToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        String enterpriseSelfsupplierCode = (new JSONObject(jsonresult.get("result")).get("entCode").toString());//获取entCode
        System.out.println("跳转自建供应商平台entCode:" + enterpriseSelfsupplierCode);
        // 使用变量拼接 JSON 字符串
        String jsonUserInfo = "{ \"entCode\": \"" + enterpriseSelfsupplierCode + "\"}";
        //登录自建供应商平台
        Common.SelfsupplierToken = LoginUtil.loginSelfsupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri, jsonUserInfo);
        System.out.println("自建供应商平台token:" + Common.SelfsupplierToken);
    }


}

package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangsc
 * @date 2025年04月29日 10:44:05
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className NoTradingVolumeTest
 */
public class NoTradingVolumeTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene = "无交易量商品模块";

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        Common.SelfsupplierToken = LoginUtil.loginSelfSupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri);
        logger.info("执行登录获取自建供应商平台的token：" + Common.SelfsupplierToken);
    }


    @Test(description = "查询信达办公用品经营部供应商无交易量商品")
    public void queryNoOrderSku(){
        JSONObject param = JSONUtil.createObj();
        param.put("page",1);
        param.put("pageSize",100);
        param.put("firstUpStatusTimeType",0);
        param.put("supplierId",Common.supplierId);
        List<Integer> enterpriseIds = new ArrayList<>();
        enterpriseIds.add(Integer.valueOf(Common.enterprId));
        param.put("enterpriseIds", enterpriseIds);
        List<Integer> supplierIds = new ArrayList<>();
        supplierIds.add(Common.supplierId);
        param.put("supplierIds", supplierIds);
        String body = param.toString();
        String createUrl = Common.SelfsupplierUrl + Common.queryNoOrderSkuPagesUri;
        headers.put("enterprise-cache", Common.SelfsupplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.SelfSupplierPro, scene);
        Assert.assertTrue(StrUtil.isNotEmpty(jsonresult.get("result").toString()), String.format(Config.result_message, Config.SelfSupplierPro, scene, ErrorEnum.ISEMPTY.getMsg(), createUrl, body, jsonresult));


    }

}

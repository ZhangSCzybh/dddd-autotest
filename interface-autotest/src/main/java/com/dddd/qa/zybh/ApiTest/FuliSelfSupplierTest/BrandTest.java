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
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2025年11月20日 15:27:58
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className BrandTest
 */
public class BrandTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene2 = "品牌管理";


    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        // 登录获取token
        Common.supplierToken = LoginUtil.loginSupplierToken(Common.SupplierUrl+Common.supplierLoginUri,Common.loginSupplierInfo);
        logger.info("执行登录获取供应商平台的token：" + Common.supplierToken);
    }


    @Test(description = "测试新增品牌，提交审批")
    public void addBrandTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("name","新增品牌"+ DateUtil.RandomSixDigit());
        param.put("nameEn","testAdd"+ DateUtil.RandomSixDigit());
        String body = param.toString();
        String createUrl = Common.SupplierUrl + Common.supplierBrandAddUri;
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.PlatformSupplierPro, scene2);
        logger.info("提交新增品牌:{}", result);

    }


    @Test(dependsOnMethods = "addBrandTest", description = "获取审核中的第一个品牌")
    public void brandlist(){
        JSONObject param = JSONUtil.createObj();
        param.put("type","1");
        param.put("status","1");
        param.put("page","1");
        param.put("pageSize","10");
        String createUrl = Common.SupplierUrl + Common.SupplierBrandreviewListUri;
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        Config.brandId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("id").toString();
        Config.brandName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("name").toString();
        Assert.assertNotNull(Config.brandId, "审核页面中第一个品牌id不为空");
        CommonUtil.assertAvailable(jsonresult, null, createUrl,Config.PlatformSupplierPro, scene2);
        logger.info("第一个审核中的品牌: id={},name={}", Config.brandId,Config.brandName);
        //
    }


    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliOpTest.BrandReviewTest.brandRejectedTest", description = "测试修改品牌提交审核")
    public void updateBrandTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("id",Config.brandId);
        param.put("name","修改新增品牌"+ DateUtil.RandomSixDigit());
        String body = param.toString();
        String createUrl = Common.SupplierUrl + Common.SupplierBrandreviewUpdateUri;
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.PlatformSupplierPro, scene2);

        JSONObject param1 = JSONUtil.createObj();
        param1.put("type","1");
        param1.put("status","1");
        param1.put("page","1");
        param1.put("pageSize","10");
        String createUrl1 = Common.SupplierUrl + Common.SupplierBrandreviewListUri;
        headers.put("enterprise-cache", Common.supplierToken);
        String result1 = HttpUtil.createGet(createUrl1).addHeaders(headers).form(param1).execute().body();
        System.out.println(result1);
        JSONObject jsonresult1 = new JSONObject(result1);
        Config.brandId = (new JSONObject((new JSONArray((new JSONObject(jsonresult1.get("result"))).get("list"))).get(0))).get("id").toString();
        Config.brandName = (new JSONObject((new JSONArray((new JSONObject(jsonresult1.get("result"))).get("list"))).get(0))).get("name").toString();
        Assert.assertNotNull(Config.brandId, "审核页面中第一个品牌id不为空");
        CommonUtil.assertAvailable(jsonresult, null, createUrl1,Config.FuliYunYingPro, scene2);

        logger.info("修改后的品牌: id={},name={}", Config.brandId,Config.brandName);



    }




}

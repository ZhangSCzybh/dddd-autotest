package com.dddd.qa.zybh.ApiTest.FuliMallTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @author zhangsc
 * @date 2025年03月26日 17:45:17
 * @packageName com.dddd.qa.zybh.ApiTest.FuliMallTest
 * @className EmployeeVoucherTest
 * @describe
 */
public class EmployeeVoucherTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene = "提货券兑换";
    private static final String exchangeVoucherCard = "test-dddd/exchangeVoucherCardInfo";
    private static String verifyNumberFristId;


    @Test(description = "获取未兑换提货券列表，第一个卡密")
    public void voucherCardList(){
        String createUrl = Common.MallUrl+Common.vouchersCardListUri + "?type=0";
        headers.put("employee-cache", Common.mallToken);
        String result =HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();

        JSONObject jsonresult = new JSONObject(result);
        verifyNumberFristId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("voucherCardList"))).get(0))).get("verifyNumber").toString();

        CommonUtil.assertAvailable(jsonresult, null, createUrl,Config.MallPro, scene);
        Assert.assertNotNull(verifyNumberFristId, "员工端提货券未兑换页面的verifyNumber不为空");
        logger.info("未兑换提货券第一个verifyNumber: id={}",verifyNumberFristId);


    }

    @Test(dependsOnMethods = {"voucherCardList"},description = "兑换提货券")
    public void Test(){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(exchangeVoucherCard);//getAllCases需要换成生产环境的参数
        param.put("verifyNumber", verifyNumberFristId);
        String body = param.toString();
        String createUrl = Common.MallUrl+Common.vouchersOrdersubmitUri;
        headers.put("employee-cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();

        //校验接口可行性
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("提货券兑换结果: {}", result);
    }
}

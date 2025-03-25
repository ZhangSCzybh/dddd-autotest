package com.dddd.qa.zybh.ApiTest.FuliOpTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @author zhangsc
 * @date 2025年03月23日 15:47:38
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className VoucherTest
 * @describe TODO
 */
public class VoucherTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String createVoucherInfo = "dddd/createVoucherInfo";


    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }


    @Test()
    public void addVoucher(){

        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(createVoucherInfo);
        param.put("name", "再也不会提货券" + Config.getSysdateStr);
        String body = param.toString();
        String createUrl = Common.OpUrl+Common.addVoucherUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        String status = jsonresult.getStr("msg");
        System.out.println(status);
    }
}

package com.dddd.qa.zybh.ApiTest.FuliMallTest;

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
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2026年01月04日 14:40:07
 * @packageName com.dddd.qa.zybh.ApiTest.FuliMallTest
 * @className CounterPaymentDevTest
 * @describe createOrderByCodeTest、createOrdByNoTest、createOrdInsufficientTest：不同环境和不同银行档口信息需要更换
 */
public class CounterPaymentDevTest {
    private static final String counterpaymentApiUrl= "http://openapi-dev.lixiangshop.com/openapi/oauth2/accessToken";
    private static final String counterpaymentClientId= "ci15d8d5e792904300a66bae5c84cbb3ad";
    private static final String counterpaymentClientSecret= "cs162bfe37ae844c56878f25dfb362a120498f07e5d55d4eb7a37cf8a446884a0c";
    private static final String mallUrl= "https://serverdev.lixiangshop.com";
    private static final String openapiUrl="http://openapi-dev.lixiangshop.com";

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "线下收银";
    private static String AccessToken = "";//Authorization
    private static String Code;//付款码

    private static String beforeCredit;//下单前的余额
    private static String number;//子订单号
    private static String payOrderNumber;//支付级订单号





    @BeforeClass
    public static void setUp() throws Exception {
        //获取accessToken
        try {
            AccessToken = fetchDevAccessToken();
            logger.info("获取到的叮叮当当企业的accessToken:{}", AccessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取员工employee-cache
        Common.mallToken = LoginUtil.loginFuliMallToken(mallUrl+"/enterprise/account/login",Common.loginMallInfo1);
        logger.info("获取虞信品员工商城的token：{}", Common.mallToken);



    }

    @Test(description = "获取电子钱包余额",priority = 0)
    public void getEmployeeWallet(){

        String url = "https://serverdev.lixiangshop.com/enterprise/employeeWallet";
        headers.put("employee-cache",Common.mallToken);
        String result = HttpUtil.createGet(url)
                .addHeaders(headers)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);
        System.out.println(jsonResult);
        beforeCredit = (new JSONObject((new JSONArray((new JSONObject(jsonResult.get("result"))).get("employeeCreditDtoList"))).get(0))).get("credit").toString();
        System.out.println(beforeCredit);

    }

    @Test(dependsOnMethods = "getEmployeeWallet",description = "获取付款码")
    public void getCredit(){
        String url = mallUrl + Common.mallGetCreditUri + "?id=-1";//id=0是普通积分
        headers.put("employee-cache",Common.mallToken);
        String result = HttpUtil.createGet(url)
                .addHeaders(headers)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);

        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);
        // 验证返回结果不为空
        Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                String.format(Config.result_message, Config.MallPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, null, jsonResult));
        // 验证更新是否成功
        Assert.assertEquals(jsonResult.getStr("code"), "1001", "生成付款码失败");
        //获取付款码code
        Code = new JSONObject(jsonResult.get("result")).get("code").toString();
        System.out.println("付款码:" + Code);
    }

    @Test(dependsOnMethods = "getCredit",description = "校验付款码状态")
    public void getCodeInfoTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("code", Code);
        String body = param.toString();
        String url = openapiUrl + Common.getCodeInfoUri;
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        int status = (int) new JSONObject(jsonResult.get("result")).get("status");
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);
        // 验证返回结果不为空
        //Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                //String.format(Config.result_message, Config.MallPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, body, jsonResult));
        //校验付款码status是否为1	状态（1正常；2过期,无效付款码；3取消付款；4需要现金支付，等待现金付款；5支付成功）
        if (status!=1){
            caveat(String.format(Config.result_message, Config.MallPro, scene, ErrorEnum.ISWRONG.getMsg(),url,param,jsonResult)
                    +"\n 付款码状态不正常！！！");
        }




    }

    @Test(dependsOnMethods = "getCodeInfoTest",description = "付款码下单")
    public void createOrderByCodeTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 2);
        param.put("code", Code);
        param.put("amount", "0.01");
        param.put("goodName", "线下收银商品-付款码自动下单");
        param.put("canteenId", "01-01");
        param.put("canteenName", "测试食堂");
        param.put("stallId", "01-01-001");
        param.put("stallName", "测试档口");
        param.put("thirdOrderNumber", "111111111");

        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/createOrder";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);
        number = new JSONObject(jsonResult.get("result")).get("number").toString();
        Number balance =  (Number) new JSONObject(jsonResult.get("result")).get("balance");
        System.out.println("子订单号:" + number + ";" + "付款码下单后的电子钱包余额:" + balance);


    }

    @Test(dependsOnMethods = "createOrderByCodeTest", description = "付款码下单的售后退款")
    public void refundByCodeTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("orgCode", "cyj123456");
        param.put("employeeNo", "abc034");
        param.put("number",number);
        param.put("refundReason", "线下收银商品-付款码自动退款");
        param.put("refundAmount", 0.01);
        param.put("thirdRefundNumber", "123123123");
        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/refund";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);

    }

    @Test(dependsOnMethods ="refundByCodeTest" , description = "校验付款码下单退款后余额一致性")
    public void checkCreditByCodeTest(){
        String url = "https://serverdev.lixiangshop.com/enterprise/employeeWallet";
        headers.put("employee-cache",Common.mallToken);
        String result = HttpUtil.createGet(url)
                .addHeaders(headers)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);
        System.out.println(jsonResult);
        String afterCredit = (new JSONObject((new JSONArray((new JSONObject(jsonResult.get("result"))).get("employeeCreditDtoList"))).get(0))).get("credit").toString();
        System.out.println("付款码退款后的电子钱包余额:" + afterCredit);
        Assert.assertEquals(afterCredit, beforeCredit, "付款码下单退款后电子钱包余额一致");


    }


    @Test(description = "员工工号下单",priority = 1)
    public void createOrdByNoTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 1);
        param.put("orgCode", "cyj123456");
        param.put("employeeNo", "abc034");
        param.put("amount", "0.02");
        param.put("goodName", "线下收银商品-员工工号自动下单");
        param.put("canteenId", "01-01");
        param.put("canteenName", "测试食堂");
        param.put("stallId", "01-01-001");
        param.put("stallName", "测试档口");
        param.put("thirdOrderNumber", "111111111");

        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/createOrder";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);
        number = new JSONObject(jsonResult.get("result")).get("number").toString();
        Number balance =  (Number) new JSONObject(jsonResult.get("result")).get("balance");
        System.out.println("子订单号:" + number + ";" + "员工工号下单后的电子钱包余额:" + balance);


    }

    @Test(dependsOnMethods = "createOrdByNoTest", description = "员工工号下单的售后退款",priority = 1)
    public void refundByNoTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("orgCode", "cyj123456");
        param.put("employeeNo", "abc034");
        param.put("number",number);
        param.put("refundReason", "线下收银商品-员工下单自动退款");
        param.put("refundAmount", 0.02);
        param.put("thirdRefundNumber", "123123123");
        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/refund";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);

    }

    @Test(dependsOnMethods ="refundByNoTest" , description = "校验员工下单退款后余额一致性",priority = 1)
    public void checkCreditByNoTest(){
        String url = "https://serverdev.lixiangshop.com/enterprise/employeeWallet";
        headers.put("employee-cache",Common.mallToken);
        String result = HttpUtil.createGet(url)
                .addHeaders(headers)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);
        System.out.println(jsonResult);
        String afterCredit = (new JSONObject((new JSONArray((new JSONObject(jsonResult.get("result"))).get("employeeCreditDtoList"))).get(0))).get("credit").toString();
        System.out.println("员工工号退款后的电子钱包余额:" + afterCredit);
        Assert.assertEquals(afterCredit, beforeCredit, "员工工号下单退款后电子钱包余额一致");


    }



    @Test(description = "获取付款码",priority = 2)
    public void getCreditTest(){
        String url = mallUrl + Common.mallGetCreditUri + "?id=-1";//id=0是普通积分
        headers.put("employee-cache",Common.mallToken);
        String result = HttpUtil.createGet(url)
                .addHeaders(headers)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);

        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);
        // 验证返回结果不为空
        Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                String.format(Config.result_message, Config.MallPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, null, jsonResult));
        // 验证更新是否成功
        Assert.assertEquals(jsonResult.getStr("code"), "1001", "生成付款码失败");
        //获取付款码code
        Code = new JSONObject(jsonResult.get("result")).get("code").toString();
        System.out.println("付款码:" + Code);
    }

    @Test(dependsOnMethods = "getCreditTest", description = "余额不足付款码下单",priority = 2)
    public void createOrdInsufficientTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 2);
        param.put("code", Code);
        param.put("amount", "10000");
        param.put("goodName", "线下收银商品-付款码余额不足自动下单");
        param.put("canteenId", "01-01");
        param.put("canteenName", "测试食堂");
        param.put("stallId", "01-01-001");
        param.put("stallName", "测试档口");
        param.put("thirdOrderNumber", "111111111");

        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/createOrder";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.MallPro, scene);
        payOrderNumber = new JSONObject(jsonResult.get("result")).get("payOrderNumber").toString();
        Number balance =  (Number) new JSONObject(jsonResult.get("result")).get("balance");
        System.out.println("支付级订单号:" + payOrderNumber + ";" + "付款码下单后的电子钱包余额:" + balance);


    }

    @Test(dependsOnMethods = "createOrdInsufficientTest",description = "根据支付单号取消支付",priority = 2)
    public void cancelTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("orgCode", "cyj123456");
        param.put("employeeNo", "abc034");
        param.put("payOrderNumber",payOrderNumber);
        String body = param.toString();
        String url = openapiUrl + "/openapi/payment/cancel";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);
        CommonUtil.assertAvailable(jsonResult, null, url, Config.MallPro, scene);

    }


    //获取Authorization
    public static String fetchDevAccessToken(){
        // 1. 生成当前时间戳 (格式: yyyy-MM-dd HH:mm:ss)
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String timestamp = sdf.format(new Date());
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 服务器时间需要添加8小时
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(calendar.getTime());
        System.out.println("时间戳" + timestamp);
        // 2. 生成签名
        String sign = DigestUtils.md5Hex(
                String.format("%s%s%s%s%s",
                        counterpaymentClientSecret, timestamp,  counterpaymentClientId,"access_token", counterpaymentClientSecret)
        );
        System.out.println("签名:" + sign);

        // 3. 构造请求体
        com.alibaba.fastjson.JSONObject requestBody = new com.alibaba.fastjson.JSONObject();
        requestBody.put("clientId", counterpaymentClientId);
        requestBody.put("clientSecret", counterpaymentClientSecret);
        requestBody.put("grantType", "1");
        requestBody.put("timestamp", timestamp);
        requestBody.put("sign", sign);


        String body = requestBody.toString();
        headers.put("Content-Type", "application/json");
        String result = HttpUtil.createPost(counterpaymentApiUrl).addHeaders(headers).body(body).execute().body();
        System.out.println("Response body: " + result);
        cn.hutool.json.JSONObject jsonResult = new cn.hutool.json.JSONObject(result);
        String accessToken =  (new cn.hutool.json.JSONObject(jsonResult.get("result")).get("accessToken").toString());

        return accessToken;
    }

}

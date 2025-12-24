package com.dddd.qa.zybh.ApiTest.FuliMallTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
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

import java.util.HashMap;

import static com.dddd.qa.zybh.BaseTest.caveat;
import static com.dddd.qa.zybh.ApiTest.FuliMallTest.AccessTokenClient.fetchAccessToken;

/**
 * @author zhangsc
 * @date 2025年10月24日 16:11:07
 * @packageName com.dddd.qa.zybh.ApiTest.FuliMallTest
 * @className CounterPaymentTest
 * @describe
 */
public class CounterPaymentTest {


    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "线下收银";
    private static String AccessToken = "";
    private static String Code;

    @BeforeClass
    public static void setUp() throws Exception {
        //获取accessToken
        try {
            AccessToken = fetchAccessToken();
            logger.info("获取到的叮叮当当企业的accessToken:{}", AccessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取员工yian-cache
        Common.DDingDDangPCToken = LoginUtil.loginYGPCToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri, Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc平台的token：{}", Common.DDingDDangPCToken);
        Common.mallToken = LoginUtil.loginJumpMallToken(Common.MallUrl + Common.jumpMallLoginUri, Common.DDingDDangPCToken);
        logger.info("获取17858803001员工pc跳转商城的token：{}", Common.mallToken);

    }


    @Test(description = "获取付款码")
    public void getCredit(){
        JSONObject param = JSONUtil.createObj();
        String url = Common.MallUrl + Common.mallGetCreditUri + "?id=0";//id=0是普通积分
        headers.put("yian-cache",Common.mallToken);
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
    }

    @Test(dependsOnMethods = "getCredit",description = "校验付款码状态")
    public void getCodeInfoTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("code", Code);
        String body = param.toString();
        String url = Common.OpenapiUrl + Common.getCodeInfoUri;
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


    //@Test(dependsOnMethods = "getCredit",description = "付款码下单")
    public void createOrderTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 1);
        param.put("amount", "0.01");
        param.put("goodName", "zybh三方收银商品");
        param.put("orgCode", "289");
        param.put("employeeNo", "128945");
        String body = param.toString();
        String url = Common.OpenapiUrl + "/openapi/payment/createOrder";
        headers.put("Authorization","Bearer " + AccessToken);
        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();
        JSONObject jsonResult = new JSONObject(result);
        System.out.println(jsonResult);

    }

}

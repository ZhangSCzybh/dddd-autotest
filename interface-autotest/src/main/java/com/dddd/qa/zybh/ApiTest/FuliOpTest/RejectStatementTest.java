package com.dddd.qa.zybh.ApiTest.FuliOpTest;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangsc
 * @date 2026年01月26日 16:54:33
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className StatementSettlementTest
 * @describe TODO 驳回账单
 */
public class RejectStatementTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "对账管理-对账单";

    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri, Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }

    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest.PlatformSupplierStatementTest.updateStatusTest", description = "对账单正确性校验")
    public void checkOPStatementCorrectnessTest(){
        String createUrl = Common.OpUrl + Common.opAdminStatementInfoUri + Config.statementId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);

        //接口可行性
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.FuliYunYingPro, scene);

        //校验结算金额正确性
        //整数用Integer
        //Integer actualityAmount =new JSONObject(jsonresult.get("result")).getInt("actualityAmount");
        //Assert.assertEquals(10, actualityAmount.intValue(), String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), createUrl, null, jsonresult));

        //校验订单数量正确性
        Integer orderCount =new JSONObject(jsonresult.get("result")).getInt("orderCount");
        Assert.assertEquals(4, orderCount.intValue(), String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //如果接口返回可能是小数，或者为了保险，使用 Double
        Double settlementAmount = new JSONObject(jsonresult.get("result")).getDouble("settlementAmount");
        Assert.assertEquals(0.08, settlementAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验售后订单数量正确性
        Integer orderRefundCount = new JSONObject(jsonresult.get("result")).getInt("orderRefundCount");
        Assert.assertEquals(3, orderRefundCount.intValue(), String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验扣除金额正确性
        Double deductAmount = new JSONObject(jsonresult.get("result")).getDouble("deductAmount");
        Assert.assertEquals(-0.06, deductAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验应收金额正确性
        Double receivableAmount = new JSONObject(jsonresult.get("result")).getDouble("receivableAmount");
        Assert.assertEquals(0.02, receivableAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验实收金额正确性
        Double actualityAmount = new JSONObject(jsonresult.get("result")).getDouble("actualityAmount");
        Assert.assertEquals(0.02, actualityAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

    }

    @Test(dependsOnMethods = "checkOPStatementCorrectnessTest", description = "对账单驳回")
    public void rejectStatementTest(){
        // 存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("statementStatus", 5);
        param.put("rejectReason", "驳回测试");

        List<Integer> ids = new ArrayList<>();
        ids.add(Integer.valueOf(Config.statementId));
        param.put("ids", ids);

        String body = param.toString();
        String createUrl =Common.OpUrl+ Common.opAdminStatementUpdateStatusUri;
        // 存放请求头
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("对账单驳回结果: {}", result);
    }


}

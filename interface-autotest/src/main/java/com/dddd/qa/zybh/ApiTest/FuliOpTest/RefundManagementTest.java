package com.dddd.qa.zybh.ApiTest.FuliOpTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.dddd.qa.zybh.BaseTest.caveat;


/**
 * @author zhangshichao
 * @date 2025年08月14日 14:07:44
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className RefundManagementTest
 * @describe TODO
 */
public class RefundManagementTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();

    private static final String scene = "退换货管理";
    private static String refundId;
    private static String refundReason;
    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }

    @Test(description = "退换货管理列表找到退款的子订单号和退款id",dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest.fuliPlatformSkuRefund")
    public void opRefundList(){
        JSONObject param = JSONUtil.createObj();
        param.put("isRefund", true);
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = "https://cardback.ddingddang.com/admin/refund/list?";
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.YGPCPro, scene);
        //获取退换货订单长度
        String data = jsonresult.get("data").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("rows"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String itemOrderNumberZuixin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("itemOrderNumber").toString();
            if (Config.itemOrderNumber.equals(itemOrderNumberZuixin)) {
                refundId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("id").toString();
                refundReason=  (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("reason").toString();
                logger.info("获取到的退款订单号和退款id: itemOrderNumber={}, id={}", itemOrderNumberZuixin, refundId);
                break;
            }else {
                logger.info("没有匹配到的退款订单号和退款id，不进行退款操作：itemOrderNumber={}, id={}", itemOrderNumberZuixin, refundId);
                break;
            }
        }
    }


    @Test(dependsOnMethods = "opRefundList",description = "运营平台售后，同意退款，待发货退款")
    public void auditRefund(){
        JSONObject param = JSONUtil.createObj();
        param.put("auditStatus", 2);
        String createUrl = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String msg = jsonresult.get("msg").toString();
        logger.info("订单退款成功: msg={},itemOrderNumber={}, id={}", msg, Config.itemOrderNumber, refundId);
        caveat("账号: " + Common.mallToken + "\n"
                +"订单编号：" + Config.itemOrderNumber + "\n"
                +"售后原因：" + refundReason + "\n"
                + "退款状态："+ msg);

    }
}


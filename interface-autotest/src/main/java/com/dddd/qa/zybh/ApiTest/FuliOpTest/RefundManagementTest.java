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

    //售后：待发货退款
    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest.fuliPlatformSkuRefund",description = "运营平台退换货管理，同意退款，待发货退款")
    public void auditRefund(){
        //获取退换货管理列表--待发货退款订单的refundid和refundReason
        getRefundId(Config.itemOrderNumber);
        //待发货退款
        JSONObject param = JSONUtil.createObj();
        param.put("auditStatus", 2);
        String createUrl = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String msg = jsonresult.get("msg").toString();
        logger.info("订单退款成功: msg={},itemOrderNumber={}, id={}", msg, Config.itemOrderNumber, refundId);
        caveat("【待发货退款--售后成功通知】" + "\n"
                +"账号: " + Common.mallToken + "\n"
                +"订单编号：" + Config.itemOrderNumber + "\n"
                +"售后原因：" + refundReason + "\n"
                + "退款状态："+ msg);

    }

    //售后：退货退款
    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest.fuliPlatformSkuReturnRefund",description = "运营平台退换货管理，同意退款，退货退款")
    public void auditReturnRefund(){
        //获取退换货管理列表--待发货退款订单的refundid和refundReason
        getRefundId(Config.itemOrderNumber1);
        //邮寄地址提交
        JSONObject param = JSONUtil.createObj();
        param.put("auditStatus", 11);
        param.put("backAddress", "退货地址");
        param.put("backPostNo", "退货邮编");
        param.put("returnName", "退货联系人");
        param.put("returnPhone", "17858803001");
        String createUrl = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String msg = jsonresult.get("msg").toString();

        CommonUtil.assertAvailable(jsonresult, String.valueOf(param), createUrl,Config.FuliYunYingPro, scene);
        logger.info("订单退货邮寄地址提交成功: msg={},itemOrderNumber={}, id={}", msg, Config.itemOrderNumber1, refundId);


        //收件地址填写
        JSONObject param1 = JSONUtil.createObj();
        param1.put("auditStatus", 12);
        param1.put("expressCode", "anjiekuaidi");
        param1.put("expressNumber", "1234211112221");
        param1.put("receiveAddress", "上海 徐汇区 湖南路街道  个月");
        param1.put("receiveName", "test");
        param1.put("receivePhone", "17858800001");
        String createUrl1 = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(param1).execute().body();
        JSONObject jsonresult1 = new JSONObject(result1);
        String msg1 = jsonresult1.get("msg").toString();
        CommonUtil.assertAvailable(jsonresult1, String.valueOf(param1), createUrl1,Config.FuliYunYingPro, scene);
        logger.info("订单退货收件地址提交成功: msg={},itemOrderNumber={}, id={}", msg1, Config.itemOrderNumber1, refundId);

        //退货退款
        JSONObject param2 = JSONUtil.createObj();
        param2.put("auditStatus", 2);
        String createUrl2 = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(param2).execute().body();
        JSONObject jsonresult2 = new JSONObject(result2);
        String msg2 = jsonresult2.get("msg").toString();

        CommonUtil.assertAvailable(jsonresult2, String.valueOf(param2), createUrl2,Config.FuliYunYingPro, scene);
        logger.info("订单退货退款成功: msg={},itemOrderNumber={}, id={}", msg2, Config.itemOrderNumber1, refundId);
        caveat("【退货退款--售后成功通知】" + "\n"
                +"账号: " + Common.mallToken + "\n"
                +"订单编号：" + Config.itemOrderNumber1 + "\n"
                +"售后原因：" + refundReason + "\n"
                + "退款状态："+ msg2);

    }

    //售后：漏发退款
    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest.fuliPlatformSkuMissingShipmentRefund",description = "运营平台退换货管理，同意退款，漏发退款")
    public void auditMissingShipmentRefund(){
        //获取退换货管理列表--待发货退款订单的refundid和refundReason
        getRefundId(Config.itemOrderNumber2);
        //漏发退款
        JSONObject param = JSONUtil.createObj();
        param.put("auditStatus", 2);
        String createUrl = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String msg = jsonresult.get("msg").toString();
        logger.info("订单漏发退款成功: msg={},itemOrderNumber={}, id={}", msg, Config.itemOrderNumber2, refundId);
        caveat("【漏发退款--售后成功通知】" + "\n"
                +"账号: " + Common.mallToken + "\n"
                +"订单编号：" + Config.itemOrderNumber2 + "\n"
                +"售后原因：" + refundReason + "\n"
                + "退款状态："+ msg);

    }

    //售后：换货
    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliMallTest.RefundTest.fuliPlatformSkuExchange",description = "运营平台退换货管理，同意退款，换货不退款")
    public void auditExchangeRefund(){
        //获取退换货管理列表--待发货退款订单的refundid和refundReason
        getRefundId(Config.itemOrderNumber3);

        //换货邮寄地址提交
        JSONObject param = JSONUtil.createObj();
        param.put("auditStatus", 11);
        param.put("backAddress", "换货地址");
        param.put("backPostNo", "换货邮编");
        param.put("returnName", "换货联系人");
        param.put("returnPhone", "17858803001");
        String createUrl = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String msg = jsonresult.get("msg").toString();

        CommonUtil.assertAvailable(jsonresult, String.valueOf(param), createUrl,Config.FuliYunYingPro, scene);
        logger.info("订单换货邮寄地址提交成功: msg={},itemOrderNumber={}, id={}", msg, Config.itemOrderNumber3, refundId);

        //换货收件地址填写
        JSONObject param1 = JSONUtil.createObj();
        param1.put("auditStatus", 12);
        param1.put("expressCode", "anjiekuaidi");
        param1.put("expressNumber", "1234211112221");
        param1.put("receiveAddress", "上海 徐汇区 湖南路街道  个月");
        param1.put("receiveName", "test");
        param1.put("receivePhone", "17858800001");
        String createUrl1 = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(param1).execute().body();
        JSONObject jsonresult1 = new JSONObject(result1);
        String msg1 = jsonresult1.get("msg").toString();
        CommonUtil.assertAvailable(jsonresult1, String.valueOf(param1), createUrl1,Config.FuliYunYingPro, scene);
        logger.info("订单换货收件地址提交成功: msg={},itemOrderNumber={}, id={}", msg1, Config.itemOrderNumber3, refundId);

        //换货不退款
        JSONObject param2 = JSONUtil.createObj();
        param2.put("auditStatus", 2);
        String createUrl2 = Common.OpUrl+Common.opAdminRefundAuditUri +refundId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(param2).execute().body();
        JSONObject jsonresult2 = new JSONObject(result2);
        String msg2 = jsonresult2.get("msg").toString();

        CommonUtil.assertAvailable(jsonresult2, String.valueOf(param2), createUrl2,Config.FuliYunYingPro, scene);
        logger.info("订单换货成功: msg={},itemOrderNumber={}, id={}", msg2, Config.itemOrderNumber3, refundId);
        caveat("【换货不退款--售后成功通知】" + "\n"
                +"账号: " + Common.mallToken + "\n"
                +"订单编号：" + Config.itemOrderNumber3 + "\n"
                +"售后原因：" + refundReason + "\n"
                + "退款状态："+ msg2);

    }

    //获取退换货管理列表--待发货退款订单的refundid和refundReason
    public static void getRefundId(String itemOrderNumber){
        JSONObject param = JSONUtil.createObj();
        param.put("isRefund", true);
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = Common.OpUrl + Common.opAdminRefundListUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.YGPCPro, scene);
        //获取退换货订单长度
        String data = jsonresult.get("data").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("rows"));
        int length = jsonArray.toArray().length;
        System.out.println(length);
        for(int i = 0; i < length; i++) {
            String itemOrderNumberZuixin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("itemOrderNumber").toString();
            System.out.println(itemOrderNumberZuixin);
            if (itemOrderNumber.equals(itemOrderNumberZuixin)) {
                refundId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("id").toString();
                refundReason=  (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("reason").toString();
                logger.info("获取到的退款订单号和退款id: itemOrderNumber={}, id={}", itemOrderNumberZuixin, refundId);
                break;
            }else {
                logger.info("没有匹配到的退款订单号和退款id，不进行退款操作：itemOrderNumber={}, id={}", itemOrderNumberZuixin, refundId);
            }
        }
    }
}


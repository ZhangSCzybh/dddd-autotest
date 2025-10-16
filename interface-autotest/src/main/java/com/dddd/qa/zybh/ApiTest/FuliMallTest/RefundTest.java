package com.dddd.qa.zybh.ApiTest.FuliMallTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangshichao
 * @date 2025年08月14日 14:10:22
 * @packageName com.dddd.qa.zybh.ApiTest.FuliMallTest
 * @className RefundTest
 * @describe 平台供应商商品售后退款测试
 */
public class RefundTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();

    private static final String ORDER_PROD_DETAILS = "dddd/refundOrder";
    private static final String FULI_PLATFORM_SKU = "10097718";
    private static final String USER_RECEIVE_ADDR_ID = "13133";
    private static final String SCENE_ORDER = "商品下单";
    private static final String SCENE_REFUND = "售后退款";
    private static final String SUPPLIER_TOKEN_DATA = "bzc4YU11as8T4k15948T205s11AN2DEJfpFWcUDte1CWbEFZ9O3ObH5lfG4Ke01x4EAcb0JFQ1ByOUJBTlN3a3Z2bCs5UVFSeWpHaTd6WXUrckNKVGpVMWY3RjJadFRYTmFyOFFwTWZXYjJZVlFjc0cwOWV6NW8rM1JiaR3kzOFVkaE5EYkY3dEkvQVFyUWZDcDRJNCt6L2Y4dWs0QWRrc0wrWWErQW1nd2hJWnI4N3ZjeTRYZXk4cTJHaHdCMW5KajZuZGVGUjZLQlc3Y2NXU013UG5zcWRp";

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangPCToken = LoginUtil.loginYGPCToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri, Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc平台的token：{}", Common.DDingDDangPCToken);
        Common.mallToken = LoginUtil.loginJumpMallToken(Common.MallUrl + Common.jumpMallLoginUri, Common.DDingDDangPCToken);
        logger.info("获取17858803001员工pc跳转商城的token：{}", Common.mallToken);
    }

    /**
     * 下单平台供应商，福粒通用积分
     */
    @Test(invocationCount = 4, description = "下单平台供应商，福粒通用积分")
    public void purchaseFuliPlatformSku() {
        // 存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("checked", "0");
        param.put("count", "1");
        param.put("skuId", FULI_PLATFORM_SKU);
        String body = param.toString();
        String createUrl = Common.MallUrl + Common.addCartUri;
        
        // 存放请求头
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        logger.info("加入购物车：{}", result);

        // 对购物车里的商品进行提交订单
        com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases1(ORDER_PROD_DETAILS);
        param2.put("userReceiveAddrId", USER_RECEIVE_ADDR_ID);
        String body2 = param2.toString();
        String createUrl2 = Common.MallUrl + Common.submitOrderUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(body2).execute().body();
        logger.info("创建订单：{}", result2);

        // 获取订单编号
        JSONObject jsonResult = new JSONObject(result2);
        String orderNumber = jsonResult.getStr("data");
        logger.info("订单编号：{}", orderNumber);
        String status = jsonResult.getStr("msg");

        // 确认下单
        String createUrl3 = Common.MallUrl + Common.comfirmOrderUri + "?orderNumber=" + orderNumber;
        headers.put("Yian-Cache", Common.mallToken);
        String result3 = HttpUtil.createPost(createUrl3).addHeaders(headers).execute().body();
        logger.info("确认下单：{}", result3);
        JSONObject jsonResult3 = new JSONObject(result3);
        String status3 = jsonResult3.getStr("msg");
        
        Assert.assertNotNull(orderNumber, String.format(Config.result_message, Config.MallPro, SCENE_ORDER, ErrorEnum.ISEMPTY.getMsg(), Common.comfirmOrderUri, orderNumber, jsonResult3));
        
        caveat("【下单通知】" + "\n"
                + "账号:" + Common.mallToken + "\n"
                + "创建订单:" + status + "\n"
                + "订单编号:" + orderNumber + "\n"
                + "确认下单:" + status3);
    }

    /**
     * 平台供应商发货
     */
    @Test(dependsOnMethods = "purchaseFuliPlatformSku", description = "平台供应商发货")
    public void fuliPlatformSupplierOrderDelivery() throws Exception {
        JSONObject param = JSONUtil.createObj();
        param.put("status", 2);
        param.put("page", 1);
        param.put("pageSize", 4);
        String body = param.toString();
        String createUrl = Common.SupplierUrl + Common.supplierOrderUri;
        headers.put("Supplier-Cache", SUPPLIER_TOKEN_DATA);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        logger.info("供应商订单列表：{}", result);

        // 获取订单编号
        JSONObject jsonResult = new JSONObject(result);

        // 获取数组长度
        String data = jsonResult.get("data").toString();
        JSONObject dataJson = new JSONObject(data);
        JSONArray jsonArray = new JSONArray(dataJson.get("rows"));
        int length = jsonArray.toArray().length;
        
        for (int i = 1; i < length; i++) {
            String number = (new JSONObject((new JSONArray((new JSONObject(jsonResult.get("data"))).get("rows"))).get(i))).get("number").toString();
            GetCaseUtil.sendPostRequest(Common.SupplierUrl + Common.supplierOrderShipUri + number, SUPPLIER_TOKEN_DATA);
            logger.info("商品{}--发货完成：{}", i, number);
            
            try {
                Thread.sleep(100); // 暂停100毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                logger.warn("发货循环被中断");
                break;
            }
        }
    }

    /**
     * 平台供应商单商品售后，提交待发货退款
     */
    @Test(dependsOnMethods = "fuliPlatformSupplierOrderDelivery", description = "平台供应商单商品售后，提交待发货退款")
    public void fuliPlatformSkuRefund() {
        Config.itemOrderNumber = getMallItemOrderNumber(0);
        Assert.assertNotNull(Config.itemOrderNumber, "第一个订单的子订单编号不为空");
        logger.info("第一个订单的子订单编号: id={}", Config.itemOrderNumber);

        JSONObject param = JSONUtil.createObj();
        param.put("itemOrderNumber", Config.itemOrderNumber);
        param.put("refundApplyProblemId", 40);
        param.put("reason", "测试待发货退款test");
        String body = param.toString();
        String createUrl = Common.MallUrl + Common.mallOrderRefundApplyUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonResult = new JSONObject(result);
        String status = jsonResult.getStr("msg");
        
        CommonUtil.assertAvailable(jsonResult, body, createUrl, Config.FuliYunYingPro, SCENE_REFUND);
        logger.info("第一个订单的子订单提交售后: id={}", Config.itemOrderNumber);
        
        caveat("【待发货退款通知】" + "\n"
                + "账号:" + Common.mallToken + "\n"
                + "订单编号:" + Config.itemOrderNumber + "\n"
                + "提交售后:" + status + "\n"
                + "售后类型:待发货退款");
    }

    /**
     * 平台供应商单商品售后，提交退货退款
     */
    @Test(dependsOnMethods = "fuliPlatformSupplierOrderDelivery", description = "平台供应商单商品售后，提交退货退款")
    public void fuliPlatformSkuReturnRefund() {
        Config.itemOrderNumber1 = getMallItemOrderNumber(1);
        Assert.assertNotNull(Config.itemOrderNumber1, "第2个订单的子订单编号不为空");
        logger.info("第2个订单的子订单编号: id={}", Config.itemOrderNumber1);

        JSONObject param = JSONUtil.createObj();
        param.put("itemOrderNumber", Config.itemOrderNumber1);
        param.put("isReceived", 0);
        param.put("refundApplyProblemId", 20);
        param.put("reason", "退货退款，不想要了");
        param.put("returnType", 1);
        param.put("thirdRefundReason", 0);
        param.put("thirdRefundReasonDesc", "不想要了（无理由）");
        
        // 处理 picList（必须用 List<String> 传入）
        List<String> picList = new ArrayList<>();
        picList.add("https://item.qn.ddingddang.com/1760584998390.jpg");
        param.put("picList", picList);

        String body = param.toString();
        String createUrl = Common.MallUrl + Common.mallOrderRefundApplyUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonResult = new JSONObject(result);
        String status = jsonResult.getStr("msg");
        
        CommonUtil.assertAvailable(jsonResult, body, createUrl, Config.MallPro, SCENE_REFUND);
        logger.info("第2个订单的子订单提交售后: id={}", Config.itemOrderNumber1);
        
        caveat("【退货退款通知】" + "\n"
                + "账号:" + Common.mallToken + "\n"
                + "订单编号:" + Config.itemOrderNumber1 + "\n"
                + "提交售后:" + status + "\n"
                + "售后类型:退货退款");
    }

    /**
     * 平台供应商单商品售后，提交漏发退款
     */
    @Test(dependsOnMethods = "fuliPlatformSupplierOrderDelivery", description = "平台供应商单商品售后，提交漏发退款")
    public void fuliPlatformSkuMissingShipmentRefund() {
        Config.itemOrderNumber2 = getMallItemOrderNumber(2);
        Assert.assertNotNull(Config.itemOrderNumber2, "第3个订单的子订单编号不为空");
        logger.info("第3个订单的子订单编号: id={}", Config.itemOrderNumber2);

        JSONObject param = JSONUtil.createObj();
        param.put("itemOrderNumber", Config.itemOrderNumber2);
        param.put("isReceived", 0);
        param.put("refundApplyProblemId", 50);
        param.put("reason", "有个商品漏发了");
        param.put("returnType", 1);
        param.put("thirdRefundReason", "1");
        param.put("thirdRefundReasonDesc", "商品漏发");
        
        // 处理 picList（必须用 List<String> 传入）
        List<String> picList = new ArrayList<>();
        picList.add("https://item.qn.ddingddang.com/1760584998390.jpg");
        param.put("picList", picList);

        String body = param.toString();
        String createUrl = Common.MallUrl + Common.mallOrderRefundApplyUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonResult = new JSONObject(result);
        String status = jsonResult.getStr("msg");
        
        CommonUtil.assertAvailable(jsonResult, body, createUrl, Config.MallPro, SCENE_REFUND);
        logger.info("第3个订单的子订单提交售后: id={}", Config.itemOrderNumber2);
        
        caveat("【漏发退款通知】" + "\n"
                + "账号:" + Common.mallToken + "\n"
                + "订单编号:" + Config.itemOrderNumber2 + "\n"
                + "提交售后:" + status + "\n"
                + "售后类型:漏发退款");
    }

    /**
     * 平台供应商单商品售后，提交换货不退款
     */
    @Test(dependsOnMethods = "fuliPlatformSupplierOrderDelivery", description = "平台供应商单商品售后，提交换货不退款")
    public void fuliPlatformSkuExchange() {
        Config.itemOrderNumber3 = getMallItemOrderNumber(3);
        Assert.assertNotNull(Config.itemOrderNumber3, "第4个订单的子订单编号不为空");
        logger.info("第4个订单的子订单编号: id={}", Config.itemOrderNumber3);

        JSONObject param = JSONUtil.createObj();
        param.put("itemOrderNumber", Config.itemOrderNumber3);
        param.put("isReceived", 1);
        param.put("refundApplyProblemId", 30);
        param.put("reason", "发错了换货");
        param.put("returnType", 1);
        param.put("thirdRefundReason", "3");
        param.put("thirdRefundReasonDesc", "发错款号");
        
        // 处理 picList（必须用 List<String> 传入）
        List<String> picList = new ArrayList<>();
        picList.add("https://item.qn.ddingddang.com/1760584998390.jpg");
        param.put("picList", picList);

        String body = param.toString();
        String createUrl = Common.MallUrl + Common.mallOrderRefundApplyUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonResult = new JSONObject(result);
        String status = jsonResult.getStr("msg");
        
        CommonUtil.assertAvailable(jsonResult, body, createUrl, Config.MallPro, SCENE_REFUND);
        logger.info("第4个订单的子订单提交售后: id={}", Config.itemOrderNumber3);
        
        caveat("【换货不退款通知】" + "\n"
                + "账号:" + Common.mallToken + "\n"
                + "订单编号:" + Config.itemOrderNumber3 + "\n"
                + "提交售后:" + status + "\n"
                + "售后类型:测试换货");
    }

    /**
     * 获取商城订单列表指定位置的子订单号
     * @param index 索引位置
     * @return 子订单号
     */
    public static String getMallItemOrderNumber(Integer index) {
        JSONObject param = JSONUtil.createObj();
        param.put("page", 1);
        param.put("pageSize", 10);
        String body = param.toString();
        String createUrl = Common.MallUrl + Common.mallOrderListUri;
        headers.put("Yian-Cache", Common.mallToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();

        JSONObject jsonResult = new JSONObject(result);
        // 解析 JSON 数据
        JSONObject data = new JSONObject(jsonResult.get("data"));
        JSONArray rows = data.getJSONArray("rows");
        // 获取指定索引的订单
        JSONObject order = rows.getJSONObject(index);
        // 获取订单中的 itemOrderVOList 数组
        JSONArray itemOrderVOList = order.getJSONArray("itemOrderVOList");
        // 获取 itemOrderVOList 中的第一个元素
        JSONObject firstItemOrder = itemOrderVOList.getJSONObject(0);
        // 提取 itemOrderNumber 的值
        String itemOrderNumber = firstItemOrder.get("itemOrderNumber").toString();

        Assert.assertNotNull(itemOrderNumber, "订单的子订单编号不为空");
        CommonUtil.assertAvailable(jsonResult, body, createUrl, Config.MallPro, SCENE_REFUND);

        return itemOrderNumber;
    }
}
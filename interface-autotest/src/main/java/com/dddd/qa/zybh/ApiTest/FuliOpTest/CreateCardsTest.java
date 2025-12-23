package com.dddd.qa.zybh.ApiTest.FuliOpTest;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @author zhangsc
 * @date 2025年12月10日 16:21:50
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className CreateCardsTest
 * @describe getAllCases需要换成生产环境的参数
 */
public class CreateCardsTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "创建会员卡";
    private static final String OpUrl = "https://backpre.lixiangshop.com";
    private static final String createCardInfo = "/test-dddd/createCardInfo";
    private static  String customerId;


    @BeforeClass
    public static void setUp() {
        JSONObject json = new JSONObject();
        json.put("loginName", "zhangshichao");//不同环境修改此处
        json.put("password", "zhang2024");//不同环境修改此处
        String userInfo = json.toString();
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(OpUrl + Common.loginOPUri ,userInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }
    @Test(description = "创建会员卡")
    public void CreateCardTest(){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases1(createCardInfo);//杭州再也不会企业
        String body = param.toString();
        String createUrl = OpUrl+"/admin/cards/sale/orders";
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //校验接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.FuliYunYingPro, scene);
        logger.info("创建会员卡成功");
    }

    @Test(dependsOnMethods = "CreateCardTest",description = "会员卡审批")
    public void CardApprovalTest(){
        //查询需要审批的会员卡
        JSONObject param = JSONUtil.createObj();
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = OpUrl + "/admin/cards/verify/orders";
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        String saleOrderId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("saleOrderId").toString();
        customerId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("customerId").toString();
        logger.info("需要审批的会员卡编号saleOrderId:{}", saleOrderId);

        //审批通过
        JSONObject param1 = JSONUtil.createObj();
        param1.put("isPassed", true);
        String body1 = param1.toString();
        String createUrl1 = OpUrl + "/admin/cards/verify/orders/" + saleOrderId;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
        logger.info("审批通过:{}", result1);
    }

    @Test(dependsOnMethods = "CardApprovalTest", description = "会员卡制卡-入库-发放")
    public void CardDeliverTest(){
        //查询需要制卡的会员卡
        JSONObject param2 = JSONUtil.createObj();
        param2.put("page", 1);
        param2.put("pageSize", 10);
        String createUrl2 = OpUrl + "/admin/cards/batches";
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result2 = HttpUtil.createGet(createUrl2).addHeaders(headers).form(param2).execute().body();
        JSONObject jsonresult2 = new JSONObject(result2);
        String batchId = (new JSONObject((new JSONArray((new JSONObject(jsonresult2.get("result"))).get("list"))).get(0))).get("batchId").toString();
        String batchcustomerId = (new JSONObject((new JSONArray((new JSONObject(jsonresult2.get("result"))).get("list"))).get(0))).get("customerId").toString();
        if (customerId.equals(batchcustomerId)){
            //会员卡制卡
            JSONObject param4 = JSONUtil.createObj();
            String body4 = param4.toString();
            String createUrl4 = OpUrl + "/admin/cards/batches/" + batchId + "/making";
            headers.put("fuli-cache", Common.fuliOperationPlatformToken);
            String result4 = HttpUtil.createGet(createUrl4).addHeaders(headers).form(body4).execute().body();
            System.out.println("制卡结果:" + result4);
            logger.info("制卡成功");

            //会员卡入库
            JSONObject param3 = JSONUtil.createObj();
            String body3 = param3.toString();
            String createUrl3 = OpUrl + "/admin/cards/batches/" + batchId + "/store";
            headers.put("fuli-cache", Common.fuliOperationPlatformToken);
            String result3 = HttpUtil.createGet(createUrl3).addHeaders(headers).form(body3).execute().body();
            logger.info("入库成功:{}", result3);

            //会员卡发放
            JSONObject param5 = JSONUtil.createObj();
            String body5 = param5.toString();
            String createUrl5 = OpUrl + "/admin/cards/batches/" + batchId + "/deliver";
            headers.put("fuli-cache", Common.fuliOperationPlatformToken);
            String result5 = HttpUtil.createGet(createUrl5).addHeaders(headers).form(body5).execute().body();
            logger.info("发放成功:{}", result5);

            //查看会员卡卡号
            JSONObject param6 = JSONUtil.createObj();
            param6.put("page", 1);
            param6.put("pageSize", 10);
            String createUrl6 = OpUrl + "/admin/cards/batches/" + batchId + "/cardlist";
            headers.put("fuli-cache", Common.fuliOperationPlatformToken);
            String result6 = HttpUtil.createGet(createUrl6).addHeaders(headers).form(param6).execute().body();
            System.out.println(result6);
            JSONObject jsonresult6 = new JSONObject(result6);
            //Config.cardNumber = (new JSONObject((new JSONArray((new JSONObject(jsonresult6.get("result"))).get("list"))).get(0))).get("cardNumber").toString();

            // 1. 先把整个响应转成JSONObject（假设jsonresult6是JSON字符串）
            JSONObject response = new JSONObject(jsonresult6);

// 2. 获取"result"字段（它本身是个JSONObject）
            JSONObject result = response.getJSONObject("result");

// 3. 获取"list"数组（它是个JSONArray）
            JSONArray list = result.getJSONArray("list");

// 4. 取第一个元素（数组索引0）
            JSONObject firstItem = list.getJSONObject(0);

// 5. 直接取cardNumber（安全又简单！）
            String cardNumber = firstItem.get("cardNumber").toString();

// 赋值给Config
            Config.cardNumber = cardNumber;

            logger.info("获取到cardNumber:{}", Config.cardNumber);

        }
    }
}

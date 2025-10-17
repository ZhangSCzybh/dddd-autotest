package com.dddd.qa.zybh.ApiTest.FuliOpTest;

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

/**
 * @author zhangsc
 * @date 2025年10月17日 10:02:01
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className SelfSupplierSkuUpdatePriceTest
 * @describe TODO
 */
public class SelfSupplierSkuUpdatePriceTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "慧卡运营平台--商品价格策略";
    private static final String SKU_ID = "10142410";

    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }

    /**
     * 价格策略--正常更新价格测试
     */
    @Test(description = "正常更新商品价格")
    public void testNormalUpdatePrice() {
        JSONObject param = buildBaseParams();
        param.put("gixedGoodsPriceType",2);
        param.put("priceType",6);
        param.put("priceValue","8");
        param.put("appointPrice", "8");
        executeTest(param, "正常更新价格场景");
    }

    /**
     * 价格策略--固定毛利率 testFixedGrossProfitMargin
     */
    @Test(description = "固定毛利率测试")
    public void testFixedGrossProfitMargin() {
        JSONObject param = buildBaseParams();
        param.put("appointPrice",1.5 );
        param.put("gixedGoodsPriceType","");
        param.put("priceType",1);
        param.put("priceValue",30);
        executeTest(param, "固定毛利率测试");
    }

    /**
     * 价格策略--毛利率折扣
     */
    @Test(description = "毛利率折扣测试")
    public void testGrossProfitMarginDiscount() {
        JSONObject param = buildBaseParams();
        param.put("appointPrice",8.5 );
        param.put("gixedGoodsPriceType","");
        param.put("priceType",2);
        param.put("priceValue",98);
        executeTest(param, "毛利率折扣测试");
    }

    /**
     * 价格策略--指导价折扣
     */
    @Test(description = "指导价折扣测试")
    public void testGuidedPriceDiscount() {
        JSONObject param = buildBaseParams();
        param.put("appointPrice",9 );
        param.put("gixedGoodsPriceType","");
        param.put("priceType",3);
        param.put("priceValue",90);
        executeTest(param, "指导价折扣测试");
    }

    /**
     * 价格策略--成本价加价
     */
    @Test(description = "成本价加价测试")
    public void testCostPriceMarkup() {
        JSONObject param = buildBaseParams();
        param.put("appointPrice",11 );
        param.put("gixedGoodsPriceType","");
        param.put("priceType",4);
        param.put("priceValue",10);
        executeTest(param, "成本价加价测试");
    }

    /**
     * 价格策略--指导价减价
     */
    @Test(description = "指导价减价测试")
    public void testGuidedPriceReduction() {
        JSONObject param = buildBaseParams();
        param.put("appointPrice",6.5 );
        param.put("gixedGoodsPriceType","");
        param.put("priceType",5);
        param.put("priceValue","3.5");
        executeTest(param, "指导价减价测试");
    }

    /**
     * 改价有效期测试 - 不是一直生效effectiveType=1
     */
    @Test(description = "改价有效期测试")
    public void testImmediateEffective() {
        JSONObject param = buildBaseParams();
        param.put("effectiveType", 1);
        param.put("appointPrice","8" );
        param.put("gixedGoodsPriceType",2);
        param.put("priceType",6);
        param.put("priceValue","8");
        param.put("startDate", Config.getSysdateStrAfterTenMinutes);
        param.put("endDate", Config.getSysdateStrAfterThirtyMinutes);
        executeTest(param, "改价有效期测试");
    }

    /**
     * 构建基础请求参数
     */
    private JSONObject buildBaseParams() {
        JSONObject param = JSONUtil.createObj();
        param.put("skuIds", SKU_ID);
        param.put("effectiveType", 0);
        return param;
    }

    /**
     * 执行测试并验证结果
     */
    private void executeTest(JSONObject param, String testCase) {
        String body = param.toString();
        String url = Common.OpUrl + Common.opSkuUpdatePriceUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);

        logger.info("开始执行测试用例：{}", testCase);
        logger.info("请求URL：{}", url);
        logger.info("请求参数：{}", body);

        String result = HttpUtil.createPost(url)
                .addHeaders(headers)
                .body(body)
                .execute()
                .body();

        logger.info("响应结果：{}", result);

        JSONObject jsonResult = new JSONObject(result);

        // 验证接口可用性
        CommonUtil.assertAvailable(jsonResult, body, url, Config.FuliYunYingPro, scene);

        // 验证返回结果不为空
        Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                String.format(Config.result_message, Config.FuliYunYingPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, body, jsonResult));

        // 验证更新是否成功
        Assert.assertEquals(jsonResult.getStr("code"), "1001", "价格更新失败");
    }

}

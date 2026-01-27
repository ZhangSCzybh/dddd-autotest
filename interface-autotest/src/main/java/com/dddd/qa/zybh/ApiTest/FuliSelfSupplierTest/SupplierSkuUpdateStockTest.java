package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

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
 * 供应商商品库存更新接口测试
 * @author zhangsc
 * @date 2024年04月29日
 */
public class SupplierSkuUpdateStockTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "供应商商品库存更新模块";
    private static final String BASE_URL = "/enterpriseadmin/product/sku/";
    private static final String UPDATE_STOCK_URI = "/updatestock";
    private static final String DEFAULT_STOCK = "10";

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        // 登录获取token
        Common.SelfsupplierToken = LoginUtil.loginSelfSupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri);
        logger.info("执行登录获取自建供应商平台的token：" + Common.SelfsupplierToken);
    }

    /**
     * 正常更新库存测试
     */
    @Test(description = "正常更新库存")
    public void testNormalUpdateStock() {
        JSONObject param = buildBaseParams(DEFAULT_STOCK);
        executeTest(param, "正常更新库存场景");
    }

    /**
     * 零库存测试
     */
    @Test(description = "零库存测试")
    public void testZeroStock() {
        JSONObject param = buildBaseParams("0");
        executeTest(param, "零库存测试");
    }

    /**
     * 大数值库存测试
     */
    @Test(description = "大数值库存测试")
    public void testLargeStock() {
        JSONObject param = buildBaseParams("999999");
        executeTest(param, "大数值库存测试");
    }
    /**
     * 减少库存测试
     */
    @Test(description = "减少库存测试")
    public void testReduceStock() {
        JSONObject param = buildBaseParams("-999999");
        executeTest(param, "减少库存测试");
    }

    /**
     * 构建基础请求参数
     */
    private JSONObject buildBaseParams(String stock) {
        JSONObject param = JSONUtil.createObj();
        param.put("goodsStorge", stock);
        return param;
    }

    /**
     * 执行测试并验证结果
     */
    private void executeTest(JSONObject param, String testCase) {
        String body = param.toString();
        String url = Common.SelfsupplierUrl + BASE_URL + Common.SKU_ID + UPDATE_STOCK_URI;
        headers.put("enterprise-cache", Common.SelfsupplierToken);

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
        CommonUtil.assertAvailable(jsonResult, body, url, Config.SelfSupplierPro, scene);
        
        // 验证返回结果不为空
        //Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                //String.format(Config.result_message, Config.SelfSupplierPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, body, jsonResult));

        // 验证更新是否成功
        //Assert.assertEquals(jsonResult.getStr("code"), "1001", "库存更新失败");
    }
} 
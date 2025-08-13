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
 * 供应商商品状态更新接口测试
 * @author zhangsc
 * @date 2024年04月29日
 */
public class SupplierSkuUpdateStateTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "供应商商品状态更新模块";
    private static final String SKU_ID = "10142410";
    private static final String BASE_URL = "/enterpriseadmin/product/sku/";
    private static final String UPDATE_STATE_URI = "/updateState";

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        // 登录获取token
        Common.SelfsupplierToken = LoginUtil.loginSelfSupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri);
        logger.info("执行登录获取自建供应商平台的token：" + Common.SelfsupplierToken);
    }

    /**
     * 暂停销售商品测试
     */
    @Test(description = "暂停销售商品")
    public void testNormalOffShelf() {
        JSONObject param = buildBaseParams(0);
        executeTest(param, "暂停销售商品场景");
    }

    /**
     * 开启销售商品测试
     */
    @Test(description = "开启销售商品")
    public void testNormalOnShelf() {
        JSONObject param = buildBaseParams(1);
        executeTest(param, "正常开启销售商品场景");
    }

    /**
     * 开启销售商品测试
     */
    //@Test(description = "开启销售商品")
    public void testInvalidStatus() {
        JSONObject param = buildBaseParams(2);
        executeTest(param, "正常开启销售商品场景");
    }

    /**
     * 构建基础请求参数
     */
    private JSONObject buildBaseParams(int status) {
        JSONObject param = JSONUtil.createObj();
        param.put("status", status);
        return param;
    }

    /**
     * 执行测试并验证结果
     */
    private void executeTest(JSONObject param, String testCase) {
        String body = param.toString();
        String url = Common.SelfsupplierUrl + BASE_URL + SKU_ID + UPDATE_STATE_URI;
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

    }
} 
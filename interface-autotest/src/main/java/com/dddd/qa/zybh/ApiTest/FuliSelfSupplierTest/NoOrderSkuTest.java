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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 无交易量商品查询接口测试
 * @author zhangsc
 * @date 2024年04月29日
 */
public class NoOrderSkuTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "无交易量商品查询模块";

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
        // 登录获取token
        Common.SelfsupplierToken = LoginUtil.loginSelfSupplierToken(Common.SelfsupplierUrl + Common.enterpriseSelfsupplierLoginuri);
        logger.info("执行登录获取自建供应商平台的token：" + Common.SelfsupplierToken);
    }

    /**
     * 正常查询场景测试
     */
    @Test(description = "正常查询无交易量商品")
    public void testNormalQuery() {
        JSONObject param = buildBaseParams();
        executeTest(param, "正常查询场景");
    }

    /**
     * 分页参数测试
     */
    @Test(description = "分页参数测试")
    public void testPagination() {
        JSONObject param = buildBaseParams();
        param.put("page", 2);
        param.put("pageSize", 5);
        executeTest(param, "分页参数测试");
    }

    /**
     * 销售状态测试
     */
    @Test(description = "销售状态测试")
    public void testSaleStatus() {
        JSONObject param = buildBaseParams();
        param.put("saleStatus", 0); // 测试不同销售状态
        executeTest(param, "销售状态测试");
    }

    /**
     * 上架时间类型测试
     */
    @Test(description = "上架时间类型测试")
    public void testFirstUpStatusTimeType() {
        JSONObject param = buildBaseParams();
        param.put("firstUpStatusTimeType", 1); // 测试不同上架时间类型
        executeTest(param, "上架时间类型测试");
    }

    /**
     * 构建基础请求参数
     */
    private JSONObject buildBaseParams() {
        JSONObject param = JSONUtil.createObj();
        param.put("page", 1);
        param.put("pageSize", 10);
        param.put("supplierId", Common.supplierId);
        param.put("firstUpStatusTimeType", "0");
        param.put("saleStatus", 1);
        param.put("firstOpenSaleTime", "");

        List<Integer> supplierIds = new ArrayList<>();
        supplierIds.add(Common.supplierId);
        param.put("supplierIds", supplierIds);

        List<Integer> enterpriseIds = new ArrayList<>();
        enterpriseIds.add(Integer.valueOf(Common.enterprId));
        param.put("enterpriseIds", enterpriseIds);

        return param;
    }

    /**
     * 执行测试并验证结果
     */
    private void executeTest(JSONObject param, String testCase) {
        String body = param.toString();
        String url = Common.SelfsupplierUrl + Common.queryNoOrderSkuPagesUri;
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
        Assert.assertTrue(StrUtil.isNotEmpty(jsonResult.get("result").toString()),
                String.format(Config.result_message, Config.SelfSupplierPro, scene, ErrorEnum.ISEMPTY.getMsg(), url, body, jsonResult));

        // 验证返回数据结构
        //Assert.assertNotNull(jsonResult.getJSONObject("result").getJSONArray("records"), "返回数据中records字段为空");
    }
} 
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

/**
 * @author zhangsc
 * @date 2025年11月20日 15:32:29
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className BrandReviewTest
 */
public class BrandReviewTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene2 = "品牌管理";

    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }

    //审批驳回

    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest.BrandTest.brandlist", description = "品牌审批驳回")
    public void brandRejectedTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 1);
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = Common.OpUrl + Common.opAdminBrandreviewListUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        JSONArray jsonArray =new JSONArray(new JSONObject(jsonresult.get("result")).get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String brandIdZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
            System.out.println("brandIdZuiXin:" + brandIdZuiXin);
            System.out.println("config.brandid:" + Config.brandId);
            if (Config.brandId.equals(brandIdZuiXin)){
                JSONObject param1 = JSONUtil.createObj();
                param1.put("id", Config.brandId);
                param1.put("status", 3);
                String body1 = param1.toString();
                String createUrl1 = Common.OpUrl + Common.opAdminBrandreviewUpdateStatusUri;
                headers.put("fuli-cache", Common.fuliOperationPlatformToken);
                String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
                //接口可行性
                JSONObject jsonresult1 = new JSONObject(result1);
                CommonUtil.assertAvailable(jsonresult1, body1, createUrl1, Config.FuliYunYingPro, scene2);
                logger.info("驳回品牌成功: brandId={}, bnrandName={}", Config.brandId, Config.brandName);
                break;
            }
        }

    }

    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest.BrandTest.updateBrandTest", description = "品牌审批通过")
    public void brandApprovedTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("type", 1);
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = Common.OpUrl + Common.opAdminBrandreviewListUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        JSONArray jsonArray =new JSONArray(new JSONObject(jsonresult.get("result")).get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String brandIdZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
            if (Config.brandId.equals(brandIdZuiXin)){
                JSONObject param1 = JSONUtil.createObj();
                param1.put("id", Config.brandId);
                param1.put("status", 2);
                String body1 = param1.toString();
                String createUrl1 = Common.OpUrl + Common.opAdminBrandreviewUpdateStatusUri;
                headers.put("fuli-cache", Common.fuliOperationPlatformToken);
                String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
                //接口可行性
                JSONObject jsonresult1 = new JSONObject(result1);
                CommonUtil.assertAvailable(jsonresult1, body1, createUrl1, Config.FuliYunYingPro, scene2);
                logger.info("修改品牌信息后审批通过: brandId={}, bnrandName={}", Config.brandId, Config.brandName);
                break;
            }
        }

    }


    @Test(dependsOnMethods = "brandApprovedTest",description = "删除品牌")
    public void delBrandTest(){
        //
        JSONObject param = JSONUtil.createObj();
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = Common.OpUrl + Common.opAdminBrandListUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        JSONArray jsonArray =new JSONArray(new JSONObject(jsonresult.get("result")).get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String brandNameZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("name").toString();
            if (Config.brandName.equals(brandNameZuiXin)){
                String delBrandId  = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
                String createUrl1 = Common.OpUrl+Common.opAdminBrandDelUri + "/"+ delBrandId ;
                headers.put("fuli-cache", Common.fuliOperationPlatformToken);
                String result1 =HttpUtil.createGet(createUrl1).addHeaders(headers).execute().body();
                //接口可行性
                JSONObject jsonresult1 = new JSONObject(result1);
                CommonUtil.assertAvailable(jsonresult1, delBrandId, createUrl1, Config.FuliYunYingPro, scene2);

                logger.info("品牌删除结果: {}", result1);

            }

        }

    }

}

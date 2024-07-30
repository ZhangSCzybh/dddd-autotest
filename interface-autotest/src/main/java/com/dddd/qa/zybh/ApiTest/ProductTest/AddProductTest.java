package com.dddd.qa.zybh.ApiTest.ProductTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2024年07月30日 15:47:35
 * @className AddProductTest
 * @describe 创建商品，上架spu，开启销售sku
 */
public class AddProductTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static HashMap<String, String> headers =new HashMap<>();

    private static String productDetailsParameters = "dddd/productDetailsParameters";;
    private static String spuCode;
    private static String skuCode;



    @DataProvider(name = "supplierInformation")
    public Object[][] testData() {
        return new Object[][] {
                { "889605", "测试平台供应商"} // 数据集1
        };
    }

    //创建商品
    @Test(invocationCount = 2)
    public void addProduct(){
        com.alibaba.fastjson.JSONObject param1 = GetCaseUtil.getAllCases(productDetailsParameters);
        param1.put("supplierId",889605);
        param1.put("supplierName","测试平台供应商");
        param1.put("goodsName", "再也不会" + Config.getTimestamp + "商品" + Config.getSysdateStr );
        System.out.println(Config.getTimestamp+Config.getSysdateStr );
        String body1 = param1.toString();
        String createUrl1 = Common.fuliOperationPlatformUrl+Common.fuliOperationPlatformAddProductUri;
        headers.put("Fuli-Cache",Common.fuliOperationPlatformToken);
        String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result1);
        logger.info("创建商品：" + jsonresult.get("msg").toString());

        getSkuList();//获取第一个商品的skuid和spucode
        updateSpuState();//上架spu
        updateSkuState();//开启销售sku

    }

    //获取sku列表最新创建的商品,获取第一个商品的skuid和spucode
    public void getSkuList(){
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", 10);
        headers.put("Fuli-Cache",Common.fuliOperationPlatformToken);
        String getSkuListUrl = Common.fuliOperationPlatformUrl+Common.fuliOperationPlatformSkuListUri;
        String result= HttpUtil.createGet(getSkuListUrl).addHeaders(headers).form(map).execute().body();
        //logger.info(result);
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);

        spuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("spuCode").toString();
        skuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("id").toString();
        logger.info("spuid：" + spuCode + ";skuid：" + skuCode);
    }

    //上架spu
    public void updateSpuState(){
        JSONObject param = JSONUtil.createObj();//存放参数
        param.put("goodsState", 1);
        param.put("spuCode", spuCode);
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = Common.fuliOperationPlatformUrl+Common.fuliOperationPlatformUpdateSpuStateUri;
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "spu:" + spuCode +";上架状态:" + jsonresult.get("msg").toString());

    }

    //开启销售sku
    public void updateSkuState(){
        JSONObject param = JSONUtil.createObj();
        param.put("status", 1);
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = Common.fuliOperationPlatformUrl+Common.fuliOperationPlatformUpdateSkuStateUri+skuCode+"/updateState";
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "sku:" + skuCode +";销售状态:" + jsonresult.get("msg").toString());
    }


}

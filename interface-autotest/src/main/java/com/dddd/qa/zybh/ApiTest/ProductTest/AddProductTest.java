package com.dddd.qa.zybh.ApiTest.ProductTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2024年07月30日 15:47:35
 * @className AddProductTest
 * @describe //todo 修改供应商id和名称
 */
public class AddProductTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();

    private static final String productDetailsParameters = "dddd/productDetailsParameters";;
    private static final int createNumber = 2;
    private static String spuCode;
    private static String skuCode;


    @DataProvider(name = "supplierInformation")
    public Object[][] supplierData() {
        return new Object[][] {
            //商品数量，供应商id，供应商名称
            { createNumber ,"889227", "再也不会供应商"}//test
            //{ createNumber ,"889605", "测试平台供应商"}, //pre
            //{ createNumber ,"889605", "测试平台供应商"}, //pro-fuli
            //{ createNumber ,"889605", "测试平台供应商"}//pro-huika
        };

    }
    //创建商品
    @Test(dataProvider = "supplierInformation")
    public void addProduct(int number, String supplierId,String supplierName){
        for (int i = 0; i < number; i++){
            com.alibaba.fastjson.JSONObject param1 = GetCaseUtil.getAllCases(productDetailsParameters);
            param1.put("supplierId",supplierId);
            param1.put("supplierName",supplierName);
            param1.put("goodsName", "再也不会" + System.currentTimeMillis() + "商品" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );
            param1.put("brandId",Common.brandId);
            param1.put("brandName",Common.brandName);
            param1.put("categoryId",Common.categoryId);
            param1.put("categoryId2",Common.categoryId2);
            param1.put("categoryId3",Common.categoryId3);
            String body1 = param1.toString();
            String createUrl1 = Common.OpUrl+Common.fuliOpAddProductUri;
            headers.put("Fuli-Cache",Common.fuliOperationPlatformToken);
            String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
            cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result1);
            logger.info("创建商品：" + jsonresult.get("msg").toString());

            //单个商品上架销售
            //getSkuList();//获取第一个商品的skuid和spucode
            //GetCaseUtil.updateSpuState(spuCode);//上架spu
            //GetCaseUtil.updateSkuState(skuCode);//开启销售sku
            try {
                Thread.sleep(1000); // 暂停1000毫秒，
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                System.out.println("Loop interrupted.");
                break; // 可选择退出循环或继续处理
            }
        }
    }

    //批量上架spu/开启销售sku
    @Test(dependsOnMethods = "addProduct")
    public void updateState(){
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", createNumber);
        headers.put("Fuli-Cache",Common.fuliOperationPlatformToken);
        String getSkuListUrl = Common.OpUrl+Common.fuliOpSkuListUri;
        String result= HttpUtil.createGet(getSkuListUrl).addHeaders(headers).form(map).execute().body();

        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        //获取数组长度
        String data = jsonresult.get("result").toString();
        cn.hutool.json.JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("list"));
        int length = jsonArray.toArray().length;

        for(int i = 0; i < length; i++){
            spuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("spuCode").toString();
            skuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
            GetCaseUtil.updateSpuState(spuCode);
            GetCaseUtil.updateSkuState(skuCode);
            System.out.println("************************第"+ i + "个商品开启上架销售**********************");
            try {
                Thread.sleep(100); // 暂停100毫秒，
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                System.out.println("Loop interrupted.");
                break; // 可选择退出循环或继续处理
            }
        }

    }

    //获取sku列表最新创建的商品,获取第一个商品的skuid和spucode
    public static void getSkuList(){
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", 10);
        headers.put("Fuli-Cache",Common.fuliOperationPlatformToken);
        String getSkuListUrl = Common.OpUrl+Common.fuliOpSkuListUri;
        String result= HttpUtil.createGet(getSkuListUrl).addHeaders(headers).form(map).execute().body();

        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);

        spuCode = (new cn.hutool.json.JSONObject((new cn.hutool.json.JSONArray((new cn.hutool.json.JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("spuCode").toString();
        skuCode = (new cn.hutool.json.JSONObject((new cn.hutool.json.JSONArray((new cn.hutool.json.JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("id").toString();
        logger.info("spuid：" + spuCode + ";skuid：" + skuCode);

    }
}

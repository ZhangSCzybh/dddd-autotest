package com.dddd.qa.zybh.ApiTest.SourceTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2025年11月26日 16:33:08
 * @packageName com.dddd.qa.zybh.others
 * @className CreateBrandAndProductTools
 * @describe 新增品牌创建商品，小程序品牌列表数据显示校验
 */
public class CreateBrandAndProductTools {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene2 = "品牌管理";
    private static  String brandIdTest;
    private static final String addProduct = "source/addProduct";
    private static final String SupplierCache = "ZFVvaG3t7F9d9T7V7s8Za18Uaz6efj9Fc0AO9X7hf0EO9E3Z3E5VbT7dbECV0V5paG5c1HNYeHB0UWFQdUR4djhsWGEwV2NsR2RWSmh0dldMWlphdnJtR3BDSEVVTFR0RDZoZmNxWjE4ZUtuKzFPckVVS3V4ZHV2bURqSTVJbHYwUksva2M2MDNTR3ZXZFRsQ0oyYm9UVzFSNzdCNFVaSHAzYWNDakRMbVZLRFNRZGM1WE1qTUxkczRQMTN6dFgvMFB1WGZmNmFMZmQ0eHhiaby9OR0JOOWxW";
    private static final String FuliCache = "RjRmT10Nah6ReGAN2N7b4D8Eb22W5U6tfD9U7G31fU1beG5h1z8b1XAV4DDb7mBd7R2SeGtjWVNVR0JzZnZmcU5NL0NzYVpycGp5MG8xRmxEcnZLRGFRaDVJUW4wNzZXaTUreWVuMlE4dXh6Qy9mbzRHMEwrVkVBVDMrOFA5OER4RWM9";



    //@DataProvider(name = "staffFuliTokenProvider")
    public Object[][] staffFuliTokenFromCSV() {
        return new Object[][] {
                {"A品牌"+ DateUtil.RandomSixDigit()},
                {"B品牌"+ DateUtil.RandomSixDigit()},
                {"C品牌"+ DateUtil.RandomSixDigit()},
                {"D品牌"+ DateUtil.RandomSixDigit()},
                {"E品牌"+ DateUtil.RandomSixDigit()},
                {"F品牌"+ DateUtil.RandomSixDigit()},
                {"G品牌"+ DateUtil.RandomSixDigit()},
                {"H品牌"+ DateUtil.RandomSixDigit()},
                {"I品牌"+ DateUtil.RandomSixDigit()},
                {"J品牌"+ DateUtil.RandomSixDigit()},
                {"K品牌"+ DateUtil.RandomSixDigit()},
                {"L品牌"+ DateUtil.RandomSixDigit()},
                {"M品牌"+ DateUtil.RandomSixDigit()},
                {"N品牌"+ DateUtil.RandomSixDigit()},
                {"O品牌"+ DateUtil.RandomSixDigit()},
                {"P品牌"+ DateUtil.RandomSixDigit()},
                {"Q品牌"+ DateUtil.RandomSixDigit()},
                {"R品牌"+ DateUtil.RandomSixDigit()},
                {"S品牌"+ DateUtil.RandomSixDigit()},
                {"T品牌"+ DateUtil.RandomSixDigit()},
                {"U品牌"+ DateUtil.RandomSixDigit()},
                {"V品牌"+ DateUtil.RandomSixDigit()},
                {"W品牌"+ DateUtil.RandomSixDigit()},
                {"X品牌"+ DateUtil.RandomSixDigit()},
                {"Y品牌"+ DateUtil.RandomSixDigit()},
                {"Z品牌"+ DateUtil.RandomSixDigit()}
                //{ new Integer[] {123456, 789012} } // 数据集2
        };
    }

    @Test(dataProvider = "staffFuliTokenProvider",description = "新增品牌创建商品，测试小程序品牌列表数据")
    public void test(String name){
        JSONObject param = JSONUtil.createObj();
        param.put("name",name);
        param.put("nameEn","testAdd"+ DateUtil.RandomSixDigit());
        String body = param.toString();
        String createUrl = "https://backdev.lixiangshop.com/supplieradmin/brandReview/add";
        headers.put("supplier-cache", SupplierCache);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.PlatformSupplierPro, scene2);
        logger.info("提交新增品牌:{}", result);

        ////////////////////////
        //获取审批中的id和name
        //////////////////////////////////

        JSONObject param1 = JSONUtil.createObj();
        param1.put("type","1");
        param1.put("status","1");
        param1.put("page","1");
        param1.put("pageSize","10");
        String createUrl1 = "https://backdev.lixiangshop.com/supplieradmin/brandReview/list";
        headers.put("supplier-cache", SupplierCache);
        String result1 = HttpUtil.createGet(createUrl1).addHeaders(headers).form(param1).execute().body();
        System.out.println(result1);
        JSONObject jsonresult1 = new JSONObject(result1);
        Config.brandId = (new JSONObject((new JSONArray((new JSONObject(jsonresult1.get("result"))).get("list"))).get(0))).get("id").toString();
        Config.brandName = (new JSONObject((new JSONArray((new JSONObject(jsonresult1.get("result"))).get("list"))).get(0))).get("name").toString();
        Assert.assertNotNull(Config.brandId, "审核页面中第一个品牌id不为空");
        CommonUtil.assertAvailable(jsonresult1, null, createUrl1,Config.PlatformSupplierPro, scene2);
        logger.info("第一个审核中的品牌: id={},name={}", Config.brandId,Config.brandName);


        /////////////////////////////////////
        //审批通过
        //////////////////////////////////

        JSONObject param2 = JSONUtil.createObj();
        param2.put("type", 1);
        param2.put("page", 1);
        param2.put("pageSize", 10);
        String createUrl2 = "https://backdev.lixiangshop.com/admin/brandReview/list";
        headers.put("fuli-cache", FuliCache);
        String result2 = HttpUtil.createGet(createUrl2).addHeaders(headers).form(param2).execute().body();
        System.out.println(result2);
        JSONObject jsonresult2 = new JSONObject(result2);
        JSONArray jsonArray =new JSONArray(new JSONObject(jsonresult2.get("result")).get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String brandIdZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult2.get("result"))).get("list"))).get(i))).get("id").toString();
            if (Config.brandId.equals(brandIdZuiXin)){
                JSONObject param3 = JSONUtil.createObj();
                param3.put("id", Config.brandId);
                param3.put("status", 2);
                String body3 = param3.toString();
                String createUrl3 = "https://backdev.lixiangshop.com/admin/brandReview/updateStatus";
                headers.put("fuli-cache", FuliCache);
                String result3 = HttpUtil.createPost(createUrl3).addHeaders(headers).body(body3).execute().body();
                //接口可行性
                JSONObject jsonresult3 = new JSONObject(result3);
                CommonUtil.assertAvailable(jsonresult3, body3, createUrl1, Config.FuliYunYingPro, scene2);
                logger.info("品牌审批通过: brandId={}, bnrandName={}", Config.brandId, Config.brandName);
                break;
            }
        }

        /////////////////////////////////////
        //获取品牌id
        //////////////////////////////////

        JSONObject param4 = JSONUtil.createObj();
        param4.put("type", 1);
        param4.put("page", 1);
        param4.put("pageSize", 10);
        String createUrl4 = "https://backdev.lixiangshop.com/admin/brand/list";
        headers.put("fuli-cache", FuliCache);
        String result4 = HttpUtil.createGet(createUrl4).addHeaders(headers).form(param4).execute().body();
        System.out.println(result4);
        JSONObject jsonresult4 = new JSONObject(result4);
        brandIdTest = (new JSONObject((new JSONArray((new JSONObject(jsonresult4.get("result"))).get("list"))).get(0))).get("id").toString();
        logger.info("品牌brandId={}", brandIdTest);

        //////////////////////////////////
        //创建商品
        //////////////////////////////////

        com.alibaba.fastjson.JSONObject param5 = GetCaseUtil.getAllCases(addProduct);
        param5.put("brandId",brandIdTest);
        param5.put("goodsName", "再也不会平台商品-" +Config.brandName);
        String body5 = param5.toString();
        String createUrl5= "https://backdev.lixiangshop.com/admin/product/add";
        headers.put("fuli-cache", FuliCache);
        String result5 = HttpUtil.createPost(createUrl5).addHeaders(headers).body(body5).execute().body();
        cn.hutool.json.JSONObject jsonresul5 = new cn.hutool.json.JSONObject(result5);
        logger.info("创建商品：" + jsonresul5.get("msg").toString());

        //单个商品上架销售
        //getSkuList();//获取第一个商品的skuid和spucode
        //GetCaseUtil.updateSpuState(spuCode);//上架spu
        //GetCaseUtil.updateSkuState(skuCode);//开启销售sku
        try {
            Thread.sleep(100); // 暂停1000毫秒，
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            System.out.println("Loop interrupted.");
        }

        ///////////////////////////////////////////////////
        //上架 开启销售
        ///////////////////////////////////////////////////
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", 1);
        headers.put("fuli-cache", FuliCache);
        String getSkuListUrl = "https://backdev.lixiangshop.com"+ Common.fuliOpSkuListUri;
        String result6= HttpUtil.createGet(getSkuListUrl).addHeaders(headers).form(map).execute().body();

        cn.hutool.json.JSONObject jsonresult6= new cn.hutool.json.JSONObject(result6);
        //获取数组长度
        String data = jsonresult6.get("result").toString();
        cn.hutool.json.JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray1 =new JSONArray(datajson.get("list"));
        int length1 = jsonArray1.toArray().length;

        for(int i = 0; i < length1; i++){
            String spuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult6.get("result"))).get("list"))).get(i))).get("spuCode").toString();
            String skuCode = (new JSONObject((new JSONArray((new JSONObject(jsonresult6.get("result"))).get("list"))).get(i))).get("id").toString();
            updateSpuState(spuCode);
            updateSkuState(skuCode);
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


    //福粒运营平台--上架spu 不用
    public static void updateSpuState(String spuCode){
        cn.hutool.json.JSONObject param = JSONUtil.createObj();//存放参数
        param.put("goodsState", 1);
        param.put("spuCode", spuCode);
        headers.put("fuli-cache", FuliCache);
        //headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = "https://backdev.lixiangshop.com"+Common.fuliOpUpdateSpuStateUri;
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "spu:" + spuCode +";上架状态:" + jsonresult.get("msg").toString());

    }

    //福粒运营平台--开启销售sku 不用
    public static void updateSkuState(String skuCode ){
        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("status", 1);
        headers.put("fuli-cache", FuliCache);
        //headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String body = param.toString();
        String updateSpuStateUrl = "https://backdev.lixiangshop.com"+Common.fuliOpUpdateSkuStateUri+skuCode+"/updateState";
        String result= HttpUtil.createPost(updateSpuStateUrl).addHeaders(headers).body(body).execute().body();
        cn.hutool.json.JSONObject jsonresult = new cn.hutool.json.JSONObject(result);
        logger.info( "sku:" + skuCode +";销售状态:" + jsonresult.get("msg").toString());
    }
}

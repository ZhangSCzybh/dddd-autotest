package com.dddd.qa.zybh.ApiTest.FuliOpTest;

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
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangsc
 * @date 2025年03月23日 15:47:38
 * @packageName com.dddd.qa.zybh.ApiTest.FuliOpTest
 * @className VoucherTest
 * @describe
 */
public class VoucherTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String createVoucherInfo = "test-dddd/createVoucherInfo";
    private static final String vouchersName="再也不会提货券" + Config.getSysdateStr;
    private static final String scene = "提货券列表";
    private static String vouchersListFirstId;
    private static String vouchersSalesListFirstId;
    private static String vouchersSaleItemsId;


    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
    }

    @Test(description = "创建提货券")
    public void addVoucher(){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(createVoucherInfo);//getAllCases需要换成生产环境的参数
        param.put("name", vouchersName);
        String body = param.toString();
        String createUrl = Common.OpUrl+Common.addVoucherUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);

        //校验接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        //测试
        String status = jsonresult.getStr("code");
        Assert.assertEquals(status, "1001", "提货券创建成功");
        logger.info("提货券创建成功",result);
    }

    @Test(dependsOnMethods = {"addVoucher"}, description = "获取提货券销售列表第一个提货券")
    public void  vouchersList(){
        //JSONObject param = JSONUtil.createObj();
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", 10);
        String body = map.toString();
        String createUrl = Common.OpUrl+Common.voucherlistUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        //String result = HttpUtil.createGet(createUrl).addHeaders(headers).body(body).execute().body();
        String result =HttpUtil.createGet(createUrl).addHeaders(headers).form(body).execute().body();
        //获取第一个提货券
        JSONObject jsonresult = new JSONObject(result);
        vouchersListFirstId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("id").toString();
        Assert.assertNotNull(vouchersListFirstId, "提货券列表第一个提货券id不为空");
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("第一份提货券: id={}",vouchersListFirstId);
    }

    @Test(dependsOnMethods = {"vouchersList"}, description = "销售第一个提货券")
    public void voucherSales(){
        JSONObject param = JSONUtil.createObj();//需要换成生产环境的参数
        param.put("agentId",1);
        param.put("enterprId",995);
        param.put("relatedEnterprId",995);
        param.put("remark","名称:" + vouchersName);
        Map<String, Object> saleItem = new HashMap<>();
        saleItem.put("voucherId", vouchersListFirstId);
        saleItem.put("buyNum", 5);
        saleItem.put("salePrice", 99);
        saleItem.put("settlementPrice", 99);
        param.put("saleItems", new Object[]{saleItem});

        String body = param.toString();
        String createUrl = Common.OpUrl+Common.salesVoucherUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();

        //校验接口可行性
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("销售提货券结果: {}", result);
    }


    @Test(dependsOnMethods = {"voucherSales"},description = "提货券发放列表获取第一个")
    public void voucherSalesList(){
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 1);
        map.put("pageSize", 10);
        String body = map.toString();
        String createUrl = Common.OpUrl+Common.salesVoucherUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        //String result = HttpUtil.createGet(createUrl).addHeaders(headers).body(body).execute().body();
        String result =HttpUtil.createGet(createUrl).addHeaders(headers).form(body).execute().body();
        //获取第一个id
        JSONObject jsonresult = new JSONObject(result);
        vouchersSalesListFirstId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("id").toString();
        Assert.assertNotNull(vouchersSalesListFirstId, "提货券发放第一个提货券id不为空");
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("发放提货券列表第一个: id={}",vouchersSalesListFirstId);

    }

    @Test(dependsOnMethods = {"voucherSalesList"},description = "提货券信息saleItems第一个Id")
    public void vouchersSaleItemsId(){
        String createUrl = Common.OpUrl+Common.salesVoucherUri + "/"+ vouchersSalesListFirstId ;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        String result =HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        vouchersSaleItemsId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("saleItems"))).get(0))).get("id").toString();
        Assert.assertNotNull(vouchersSaleItemsId, "提货券信息提货券信息saleItems第一个Id不为空");
        CommonUtil.assertAvailable(jsonresult, null, createUrl,Config.FuliYunYingPro, scene);
        logger.info("提货券信息第一个saleItems: id={}",vouchersSaleItemsId);
    }

    @Test(dependsOnMethods = {"vouchersSaleItemsId"},description = "指定发放提货券给员工")
    public void grantVouchers(){
        JSONObject param = JSONUtil.createObj();//需要换成生产环境的参数vouchersSalesListFirstId
        param.put("saleId",vouchersSalesListFirstId);
        //appointGrant 数组
        List<Map<String, Object>> appointGrantList = new ArrayList<>();

        Map<String, Object> appointGrant = new HashMap<>();
        appointGrant.put("itemId",vouchersSaleItemsId );//vouchersSaleItemsId
        appointGrant.put("blessing", "");

        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(10052723);//17858800052
        appointGrant.put("employeeIds", employeeIds);

        appointGrantList.add(appointGrant);
        param.put("appointGrant", appointGrantList);
        String body = param.toString();

        String createUrl = Common.OpUrl+Common.grantVoucherUri;
        headers.put("Fuli-Cache", Common.fuliOperationPlatformToken);
        //String result = HttpUtil.createGet(createUrl).addHeaders(headers).body(body).execute().body();
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.FuliYunYingPro, scene);
        logger.info("提货券发放结果: {}", result);

    }

}

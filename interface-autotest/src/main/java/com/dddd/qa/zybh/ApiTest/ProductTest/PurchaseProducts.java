package com.dddd.qa.zybh.ApiTest.ProductTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.BaseTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


/**
 * @author zhangsc
 * @date 2024年07月16日 14:59:26
 * @className PurchaseProductsTool
 *
 * staffFuliTokenFromCSV 下单账号
 * supplierTokenFromCSV 供应商账号
 * orderProdDetails 订单参数配置
 * selectArrayByDay 每日sku列表
 * listEmployeeData 智采积分发放人员
 */
public class PurchaseProducts extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();

    /**********************************生产环境的benefits/order/ubmitNew接口参数配置***********************************/
    private static String orderProdDetails;
    private static String credit;
    private static String creditName;
    private static String mallEmployeeId;
    private static final String scene1 = "商品下单";
    private static final String employeePointsParameters = "dddd/employeePointsParameters";

    //根据星期选择不同的sku和json文件
    private static String[] selectArrayByDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                orderProdDetails="dddd/createProdOrder11";
                return Common.array1;
            case TUESDAY:
                orderProdDetails="dddd/createProdOrder12";
                return Common.array2;
            case WEDNESDAY:
                orderProdDetails="dddd/createProdOrder13";
                return Common.array3;
            case THURSDAY:
                orderProdDetails="dddd/createProdOrder14";
                return Common.array4;
            case FRIDAY:
                orderProdDetails="dddd/createProdOrder15";
                return Common.array5;
            // 如果是周六或周日，你可以选择默认数组或抛出异常
            case SATURDAY:
            case SUNDAY:
                throw new IllegalStateException("Arrays not defined for Saturday or Sunday");
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }


    //商品下单：账号编号、token、地址id配置
    @DataProvider(name = "staffFuliTokenProvider")
    public Object[][] staffFuliTokenFromCSV() {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Common.jenkinsUrl+"/src/main/resources/dddd/staffFuliToken.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("num")) { // 跳过标题行
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    //自建供应商发货：token参数
    @DataProvider(name = "supplierTokenProvider")
    public Object[][] supplierTokenFromCSV() {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Common.jenkinsUrl+"/src/main/resources/dddd/supplierToken.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("num")) { // 跳过标题行
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    //智采企业平台员工id
    @DataProvider(name = "EmployeeIdData")
    public Object[][] listEmployeeIdData() {
        return new Object[][] {
                {"128638",1}, // 发放积分账号
                {"112714",2},// 发放积分账号
                {"124236",3}, // 发放积分账号
                {"112716",4},// 发放积分账号
                {"113546",5}// 发放积分账号
                //{ new Integer[] {123456, 789012} } // 数据集2
        };
    }


    //description = "商品下单"
    @Test(dataProvider = "staffFuliTokenProvider")
    public void purchaseGoods(String num, String tokendata,String addressId ) throws InterruptedException {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 获取当前是星期几
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        // 根据星期几选择对应的数组
        String[] selectedArray = selectArrayByDay(dayOfWeek);

        //存放参数
        JSONObject param = JSONUtil.createObj();
        for (String skuId : selectedArray) {
            param.put("checked", "0");
            param.put("count", "1");
            GetCaseUtil.updateSkuStock(skuId);
            param.put("skuId", skuId);
            String body = param.toString();
            String createUrl = Common.MallUrl+Common.addCartUri;
            //存放请求头，可以存放多个请求头
            headers.put("Yian-Cache", tokendata);
            String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            logger.info("加入购物车：" + result);
        }

        //对购物车里的商品进行提交订单
        com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases1(orderProdDetails);
        param2.put("userReceiveAddrId", addressId);
        String body2 = param2.toString();
        String createUrl2 = Common.MallUrl+Common.submitOrderUri;
        headers.put("Yian-Cache", tokendata);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(body2).execute().body();
        logger.info("创建订单：" + result2);

        //获取订单编号
        JSONObject jsonresult = new JSONObject();
        jsonresult = new JSONObject(result2);
        String orderNumber = jsonresult.getStr("data");
        System.out.println("订单编号：" + orderNumber);
        String status = jsonresult.getStr("msg");

        //确认下单
        String createUrl3 = Common.MallUrl+Common.comfirmOrderUri + "?orderNumber=" + orderNumber;
        headers.put("Yian-Cache", tokendata);
        String result3 = HttpUtil.createPost(createUrl3).addHeaders(headers).execute().body();
        logger.info("确认下单："+result3);
        JSONObject jsonresult3 = new JSONObject();
        jsonresult3 = new JSONObject(result3);
        String status3 = jsonresult3.getStr("msg");

        Assert.assertNotNull(orderNumber,String.format(Config.result_message, Config.FuliPro, scene1, ErrorEnum.ISEMPTY.getMsg(), Common.comfirmOrderUri, orderNumber, jsonresult3));
        caveat("第" + num + "个账号: " + tokendata + "\n"
                +"创建订单：" + status + "\n"
                +"订单编号：" + orderNumber + "\n"
                + "确认下单："+ status3);

    }

    //description = "自建供应商订单发货"
    @Test(dataProvider = "supplierTokenProvider",dependsOnMethods = {"purchaseGoods"})
    public  void supplierOrderDelivery(String num, String supplierTokenData) throws Exception {
        JSONObject param = JSONUtil.createObj();
        param.put("status", 2);
        param.put("page", 1);
        param.put("pageSize", 200);
        String body = param.toString();
        String createUrl = Common.SupplierUrl+Common.supplierOrderUri;
        headers.put("Supplier-Cache", supplierTokenData);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();

        //获取订单编号
        JSONObject jsonresult = new JSONObject();
        jsonresult = new JSONObject(result);

        //获取数组长度
        String data = jsonresult.get("data").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("rows"));
        int length = jsonArray.toArray().length;

        for(int i = 0; i < length; i++){
            String number = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("number").toString();
            GetCaseUtil.sendPostRequest(Common.SupplierUrl+Common.supplierOrderShipUri + number,supplierTokenData);
            //logger.info("第" + num + "个供应商发货完成：" + number);
            logger.info("第" + num + "个供应商--商品" + i + "--发货完成：" + number);
            try {
                Thread.sleep(100); // 暂停100毫秒，
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                System.out.println("Loop interrupted.");
                break; // 可选择退出循环或继续处理
            }
        }

        caveat("发货情况：第" + num + "个供应商--商品发货完成！！！");

    }

    //description = "查询智采企业平台员工积分，少于5w的员工补发积分"
    @Test(dataProvider = "EmployeeIdData")
    public void checkZhicaiEmployeePoints(String employeeId,int num){
        JSONObject param = JSONUtil.createObj();
        //Map<String, Object> param = new HashMap<>();//存放参数
        headers.put("session-token", Common.zhicaiHrToken);
        param.put("employeeId", employeeId);
        param.put("enterpriseId", 289);
        String createUrl = Common.zhicaiHrUrl+Common.checkZcEmployeePointsUri;
        String response= HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        String balance = new JSONObject(new JSONArray(new JSONObject(response).get("result")).get(0)).get("balance").toString();

        if ((int) Double.parseDouble(balance) <= 50000){
            int margin = 50000-(int) Double.parseDouble(balance);
            caveat( "=========智采员工积分详情=========" + "\n"+ "员工编号:" + employeeId + "\n" + "通用积分:" + balance + "\n" + "当前员工通用积分少于50000，将自动补发积分！！！");
            GetCaseUtil.giveEmployeePoints(new Integer[] {Integer.valueOf(employeeId)}, String.valueOf(margin));
            logger.info("第" + num +"个账号积分少于50000，已补发积分" + margin );
        }else{
            caveat( "=========智采员工积分详情=========" + "\n"+ "员工编号:" + employeeId + "\n" + "通用积分:" + balance + "\n" + "当前员工通用积分充足，请放心购买！！！");
            logger.info("第" + num +"个账号积分充足，请放心购买！！！");
            System.out.println(Arrays.toString(new Integer[]{Integer.valueOf(employeeId)}));

        }

    }



    //暂时不需要执行//1
    //智采员工id和发放积分数量
    @DataProvider(name = "EmployeeData")
    public Object[][] listEmployeeData() {
        return new Object[][] {
                {new Integer[] {128638, 112714, 124236, 112716, 113546}, "18"} // 发放积分账号，积分额度
                //{ new Integer[] {123456, 789012} } // 数据集2
        };
    }

    //description = "智采员工发放积分"
    //@Test(dataProvider = "EmployeeData")
    public void giveEmployeePoints(Integer[] list,String amount){
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases1(employeePointsParameters);
        param.put("list", list);
        param.put("amount", amount);
        String body = param.toString();
        String createUrl = Common.zhicaiHrUrl+Common.sendEmployeePointsUri;
        headers.put("Session-Token", Common.zhicaiHrToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String data = jsonresult.get("result").toString();
        logger.info( data + ":员工积分发放成功！");
        caveat( "===========智采员工积分===========" + "\n"+ "发放额度:" + amount + "积分" + "\n" + "发放结果:" + data);
    }

    //description = "查询商城员工积分"
    //@Test(dataProvider = "staffFuliTokenProvider" )
    public void checkMallEmployeePoints(String num ,String tokendata,String addressId){
        headers.put("yian-cache", tokendata);
        String pointsUrl = Common.MallUrl+Common.checkMallEmployeePointsUri;
        String response= HttpUtil.createGet(pointsUrl).addHeaders(headers).execute().body();

        JSONObject jsonresult  = new JSONObject(response);
        //获取数组长度
        String result = jsonresult.get("result").toString();
        JSONObject datajson = new JSONObject(result);
        JSONArray jsonArray =new JSONArray(datajson.get("employeeCreditDtoList"));
        int length = jsonArray.toArray().length;

        for(int i = 0; i < length; i++){
            mallEmployeeId = new JSONObject(result).get("employeeId").toString();
            credit = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("employeeCreditDtoList"))).get(i))).get("credit").toString();
            creditName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("employeeCreditDtoList"))).get(i))).get("creditName").toString();
            logger.info("员工编号:" + mallEmployeeId +"\n"+creditName+ ":" + credit);
        }
        caveat("===========商城员工积分===========" + "\n"+"第"+num +"个员工：" + mallEmployeeId +"\n"+creditName+ "：" + credit);
    }
}

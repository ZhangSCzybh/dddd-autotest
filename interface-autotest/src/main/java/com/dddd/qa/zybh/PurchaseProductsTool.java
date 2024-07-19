package com.dddd.qa.zybh;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;


/**
 * @author zhangsc
 * @date 2024年07月16日 14:59:26
 * @packageName com.dddd.qa.zybh
 * @className PurchaseProductsTool
 * @describe
 * todo 本地运行 需要修改getAllCases1 -->getAllCases
 * tokenDataProvider1 下单账号
 * tokenDataProvider2 供应商账号
 * orderProdDetails 订单参数配置
 * array 每日sku列表
 */
public class PurchaseProductsTool extends BaseTest {



    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static String skuListfile = "dddd/skuList";
    private static String supplierOrderList = "dddd/supplierOrderList";
    private static HashMap<String, String> headers =new HashMap<>();

    /**********************************生产环境的benefits/order/ubmitNew接口参数配置***********************************/
    private static String orderProdDetails;

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


    //商品下单参数：账号编号、token、地址id配置
    @DataProvider(name = "tokenDataProvider-Prod")
    public Object[][] tokenDataProvider1() {
        return new Object[][]{
                {1, Common.staffFuliToken01, 11951},//ceshi 测试
                {2, Common.staffFuliToken02, 11946},//karen 吴女士
                {3, Common.staffFuliToken03, 11967}, //蒋美娣 13858653282
                {4, Common.staffFuliToken04, 11965},//赵秀、WXY13666605555
                {5, Common.staffFuliToken05, 11969}//仙齐、18767176714

        };
    }

    //自建供应商token参数
    @DataProvider(name = "supplierTokenDataProvider-Prod")
    public Object[][] tokenDataProvider2() {
        return new Object[][]{
                //信达办公用品经营部
                {1, Common.supplierToken01},
                //万客隆商贸有限公司
                {2, Common.supplierToken02},
                //宇轩图文设计中心
                {3, Common.supplierToken03},
                //智惠恒数码有限公司
                {4, Common.supplierToken04}
        };
    }

    //,description = "商品下单"
    @Test(dataProvider = "tokenDataProvider-Prod")
    public void purchaseGoods(int num, String Tokendata,int addressId ) throws InterruptedException {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 获取当前是星期几
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        // 根据星期几选择对应的数组
        String[] selectedArray = selectArrayByDay(dayOfWeek);

        //存放参数
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases1(skuListfile);
        for (String skuId : selectedArray) {
            param.put("skuId", skuId);
            String body = param.toString();
            String createUrl = Common.FuliUrl+Common.addCartUri;
            //存放请求头，可以存放多个请求头
            headers.put("Yian-Cache", Tokendata);
            String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            logger.info("加入购物车：" + result);
        }

        //对购物车里的商品进行提交订单
        com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases1(orderProdDetails);
        param2.put("userReceiveAddrId", addressId);
        String body2 = param2.toString();
        String createUrl2 = Common.FuliUrl+Common.submitOrderUri;
        headers.put("Yian-Cache", Tokendata);
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(body2).execute().body();
        logger.info("创建订单：" + result2);

        //获取订单编号
        cn.hutool.json.JSONObject jsonresult = new JSONObject();
        jsonresult = new cn.hutool.json.JSONObject(result2);
        String orderNumber = jsonresult.getStr("data");
        System.out.println("订单编号：" + orderNumber);
        String status = jsonresult.getStr("msg");

        //确认下单
        String createUrl3 = Common.FuliUrl+Common.comfirmOrderUri + "?orderNumber=" + orderNumber;
        headers.put("Yian-Cache", Tokendata);
        String result3 = HttpUtil.createPost(createUrl3).addHeaders(headers).execute().body();
        logger.info("确认下单："+result3);
        cn.hutool.json.JSONObject jsonresult3 = new JSONObject();
        jsonresult3 = new cn.hutool.json.JSONObject(result3);
        String status3 = jsonresult3.getStr("msg");

        Assert.assertNotNull(orderNumber);
        caveat("第" + num + "个账号: " + Tokendata + "\n"
                +"创建订单：" + status + "\n"
                +"订单编号：" + orderNumber + "\n"
                + "确认下单："+ status3);

    }


    //description = "自建供应商订单发货"
    @Test(dataProvider = "supplierTokenDataProvider-Prod",dependsOnMethods = {"purchaseGoods"})
    public  void supplierOrderDelivery(int num, String supplierTokenData) throws Exception {
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases1(supplierOrderList);
        String body = param.toString();
        String createUrl = Common.SupplierUrl+Common.supplierOrderUri;
        headers.put("Supplier-Cache", supplierTokenData);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();

        //获取订单编号
        cn.hutool.json.JSONObject jsonresult = new JSONObject();
        jsonresult = new cn.hutool.json.JSONObject(result);

        //获取数组长度
        String data = jsonresult.get("data").toString();
        cn.hutool.json.JSONObject datajson = new JSONObject(data);
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

}

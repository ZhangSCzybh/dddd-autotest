package com.dddd.qa.zybh;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.DateUtil;
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
 * 1、修改common第15行的配置文件--test/prod 或者修改test.properties 福粒域名
 * 2、修改array  周一array1、周二array2、周三array3、周四array4、、周五array5
 * 3、修改orderProdDetails字段 周一至周五
 * 不同账号新增token
 * 修改sku列表
 * 修改dataProvider = "tokenDataProvider-Test"  /"tokenDataProvider-Prod"
 */
public class PurchaseProductsTool extends BaseTest {



    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static String skuListfile = "dddd/skuList";
    private static String supplierOrderList = "dddd/supplierOrderList";
    private static HashMap<String, String> headers =new HashMap<>();

    /**********************************测试环境的token、json文件、地址id配置*******************************************/
    /**********************************测试环境的benefits/order/submitNew在这里添加***********************************/
    private static String orderTestDetails01 = "dddd/createTestOrder01";
    private static String orderTestDetails02 = "dddd/createTestOrder02";
    @DataProvider(name = "tokenDataProvider-Test")
    public Object[][] tokenDataProvider() {
        return new Object[][]{
                {1, "11a39b391b9b4de4ae38785499c5dd10",orderTestDetails01}, //ceshi
                {2, "ff7b2d6f1d9f43dfbf183c9dff226092",orderTestDetails02}  //karen
        };
    }



    /**********************************生产环境的benefits/order/submitNew在这里添加***********************************/
    //周一：array1     orderProdDetails=dddd/createProdOrder11
    //周二：array2     orderProdDetails=dddd/createProdOrder12
    //周三：array3     orderProdDetails=dddd/createProdOrder13
    //周四：array4     orderProdDetails=dddd/createProdOrder14
    //周五：array5     orderProdDetails=dddd/createProdOrder15
    private static String orderProdDetails;
    private static String[] array1 = {"9147280", "9147301", "9147304", "9147239"};
    private static String[] array2 = {"9147167", "9147172", "9147194", "9147221","9147420","9147423","9147173","9147205","9147224","9147232","9147251","9147254","9147211","9147346","9147354","9147268","9147283","9147300"};
    private static String[] array3 = {"9147309", "9147239", "9147343", "9147346","9147268","9147274","9147280","9147301","9147304"};
    private static String[] array4 = {"9147325", "9147417", "9147418", "9147422","9147424","9147253","9147254","9147255","9147257","9147262","9147357","9147292","9147295","9147298","9147302"};
    private static String[] array5 = {"9147305", "9147309", "9147419", "9147215","9147240","9147248","9147347","9147357","9147269","9147277","9147297"};

    //根据星期选择不同的sku和json文件
    private static String[] selectArrayByDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                orderProdDetails="dddd/createProdOrder11";
                return array1;
            case TUESDAY:
                orderProdDetails="dddd/createProdOrder12";
                return array2;
            case WEDNESDAY:
                orderProdDetails="dddd/createProdOrder13";
                return array3;
            case THURSDAY:
                orderProdDetails="dddd/createProdOrder14";
                return array4;
            case FRIDAY:
                orderProdDetails="dddd/createProdOrder15";
                return array5;
            // 如果是周六或周日，你可以选择默认数组或抛出异常
            case SATURDAY:
            case SUNDAY:
                throw new IllegalStateException("Arrays not defined for Saturday or Sunday");
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }


    //生产环境的账号编号、token、json文件、地址id配置
    @DataProvider(name = "tokenDataProvider-Prod")
    public Object[][] tokenDataProvider1() {
        return new Object[][]{
                {1, "fe84dd3442044fb4bc691d6c839f8722", 11951}//ceshi 测试
                //{2, "a06a29079c1541989b5db3ebe8b38d6c", 11946},//karen 吴女士
                //{3, "0f621cc6b3de4f0ebd5b9735020e8aa6", 11967}, //蒋美娣 13858653282
                //{4, "2b1c7c89596844e4901a8d4d5bb0fe11", 11965},//赵秀、WXY13666605555
                //{5, "53d8c099e12c460a812e6f3b940cdb52", 11969}//仙齐、18767176714

        };
    }

    //自建供应商token
    @DataProvider(name = "supplierTokenDataProvider-Prod")
    public Object[][] tokenDataProvider2() {
        return new Object[][]{
                //信达办公用品经营部
                {1, "Y2pmZDBdbi2adN0T7h7ZcaDD3R0jeMCm6t2adMAG3pFX9Y7WbU9zbR3n0BA42W8FfQF48aVdBMUdRMVRnQU5KNmtOTFU4QTFKL2g5VjNkVGhweE5jeUN6TXo3WmtFUTZlWno5czNTMzdaaVc0Uy9TL0xDdUEzUjdReW9YdXpyRUVxY21CNVlWdjIrVHozVy8yOEp1dXhVY3dtWHJoNjl1aXBwOHpjT0pKVmJlMDdhR3hVa1lzeE1XME5ubUpJb2VMWVBVPQ"},
                //万客隆商贸有限公司
                {2, "cUxGbW194l7U0EDh1VCT4DCI236d4T7lfZER41DB0j9Ra17pf39M3l4Yc0CSfj1h72AY7lRFSkpmSE5RT3lwRDdQWW5YMG5xbEl3M0pzZThiaUEFla3NTeUpkcUVLeFliabFBSNDdSZTZYdkQ1aDN3UHZPcGtCMXUrdDhsNzQ0STc3c1RXQWFDQldoL1lQV0x6WGN4VklFaVlFcmFiaQ0lENW9ycEQ2d09renp5ZzF3ZENZa2h0RzlaVEQ2Q0gzdGpmSFlFPQ"},
                //宇轩图文设计中心
                {3, "L1lPZUEJ2BDNcmBF9iFa8a1G1d82cZFHeh614QFW1ZFu0d2E0xEY8VBS59E6ec7DbU82aOEsycWhzaHlOclg2YTRWSXc1Vm4vTEZFbGxtWjNVeHJsVW5KNFNLQ0I0Wia9jRmlYVFdFdEpKemt6T284blYxdW1rc3d1SUc0WnBiaWnZxT0RIc20xK2RjRCs3aEYrbW5RVnZuWDNoVTZ4aEpQMmdRS3llNUZ6d2tiaMzJRPT0"},
                //智惠恒数码有限公司
                {4, "ZStXRT5l2R3cfHFNemFY0nEAf35R5275dyFW0n0hcjAZ3TAF3HBU6W6V5nEUdm0J6a1T8kttdCsrdFJycERLREhXY21vQlZ0UVpTWk51bkFRU21PNFBxMmpLRmprWnd0bEExanNUc3pZMmVGS0phNjArUU9uOHhaL2JUWHpsOEw4U0oxUjZKbDdsQ0FuaFcrNjRIM3Rhc1JFYjdURkFZUWVYemdjbTkveStSbURGVWdSUDRlRXU0dy9TOERjclRpUmNRPQ"}
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
        //List<String> list = Arrays.asList(array3);
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
        caveat("第" + num + "个Token账号: " + Tokendata + "\n"
                +"创建订单：" + status + "\n"
                +"订单编号：" + orderNumber + "\n"
                + "确认下单："+ status3);

    }


    //description = "自建供应商订单发货"
    @Test(dataProvider = "supplierTokenDataProvider-Prod",dependsOnMethods = {"purchaseGoods"}, alwaysRun = true)
    public  void SupplierOrderDelivery(int num, String supplierTokenData) throws Exception {
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
            logger.info("第" + num + "账号商品发货完成：" + number);
            //caveat("第" + num + "账号商品发货完成：" + number);
            try {
                Thread.sleep(100); // 暂停100毫秒，
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                System.out.println("Loop interrupted.");
                break; // 可选择退出循环或继续处理
            }
        }
    }

}

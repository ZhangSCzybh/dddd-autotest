package com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
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

import java.util.*;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2026年01月23日 17:28:49
 * @packageName com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest
 * @className PlatformSupplierStatementTest
 * @describe TODO 每个接口都需要替换成生成环境的数据
 */
public class PlatformSupplierStatementTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "对账单模块";

    private static String statementNo;//对账单号
    @BeforeClass
    public static void setUp() {
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
        // 登录获取token
        Common.supplierToken = LoginUtil.loginSupplierToken(Common.SupplierUrl+Common.supplierLoginUri,Common.loginSupplierInfo);
        logger.info("执行登录获取供应商平台的token：" + Common.supplierToken);
    }

    @Test(description = "创建对账单")
    public void createWaitStatementTest(){
        // 存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("page", "1");
        param.put("pageSize", "10");
        param.put("statementStatus", "0");

        List<Long> orderNumbers = new ArrayList<>();
        orderNumbers.add(Long.valueOf(Config.itemOrderNumber));
        orderNumbers.add(Long.valueOf(Config.itemOrderNumber1));
        orderNumbers.add(Long.valueOf(Config.itemOrderNumber2));
        orderNumbers.add(Long.valueOf(Config.itemOrderNumber3));
        param.put("numbers", orderNumbers);

        List<Long> refundNumbers = new ArrayList<>();
        refundNumbers.add(Long.valueOf(Objects.requireNonNull(getrefundApplyNumber(Config.itemOrderNumber))));
        refundNumbers.add(Long.valueOf(Objects.requireNonNull(getrefundApplyNumber(Config.itemOrderNumber1))));
        refundNumbers.add(Long.valueOf(Objects.requireNonNull(getrefundApplyNumber(Config.itemOrderNumber2))));
        refundNumbers.add(Long.valueOf(Objects.requireNonNull(getrefundApplyNumber(Config.itemOrderNumber3))));
        param.put("refundNumbers", refundNumbers);


        String body = param.toString();
        String createUrl =Common.SupplierUrl + Common.supplierStatementCreateWaitStatementUri;
        // 存放请求头
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.PlatformSupplierPro, scene);
        logger.info("发起对账单结果: {}", result);

    }

    @Test(dependsOnMethods = "createWaitStatementTest", description = "获取对账单号")
    public void getStatementNoTest(){
        // 存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("page", "0");
        param.put("pageSize", "10");
        param.put("statementStatus", "1");
        param.put("number", Config.itemOrderNumber);
        String body = param.toString();
        String createUrl =Common.SupplierUrl + Common.supplierStatementGetStatementAccountDetailUri;
        // 存放请求头
        headers.put("supplier-cache", Common.supplierToken);

        //headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.PlatformSupplierPro, scene);
        statementNo = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(0))).get("statementNo").toString();
        logger.info("对账单号获取成功: statementNo={}", statementNo);
    }

    @Test(dependsOnMethods = "getStatementNoTest", description = "根据对账单号获取对账单id")
    public void getStatementIdTest(){
        JSONObject param = JSONUtil.createObj();
        param.put("tab", 1);
        param.put("page", 1);
        param.put("pageSize", 100);
        String createUrl = Common.SupplierUrl +Common.supplierStatementListUri;
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);
        //接口可行性
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.PlatformSupplierPro, scene);

        String data = jsonresult.get("result").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("list"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String statementNoZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("statementNo").toString();
            if (statementNo.equals(statementNoZuiXin)){
                Config.statementId= (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
                logger.info("对账单号对应的id: statementNo={}, statementId={}", statementNo, Config.statementId);
                break;
            }

        }
    }

    @Test(dependsOnMethods= "getStatementIdTest",description = "校验对账单正确性")
    public void checkStatementCorrectnessTest(){
        String createUrl = Common.SupplierUrl + Common.supplierStatementInfoUri+ Config.statementId;
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createGet(createUrl).addHeaders(headers).execute().body();
        System.out.println(result);
        JSONObject jsonresult = new JSONObject(result);

        //接口可行性
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.PlatformSupplierPro, scene);

        //校验结算金额正确性
        //整数用Integer
        //Integer actualityAmount =new JSONObject(jsonresult.get("result")).getInt("actualityAmount");
        //Assert.assertEquals(10, actualityAmount.intValue(), String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), createUrl, null, jsonresult));

        //校验订单数量正确性
        Integer orderCount =new JSONObject(jsonresult.get("result")).getInt("orderCount");
        Assert.assertEquals(4, orderCount.intValue(), String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //如果接口返回可能是小数，或者为了保险，使用 Double
        Double settlementAmount = new JSONObject(jsonresult.get("result")).getDouble("settlementAmount");
        Assert.assertEquals(0.08, settlementAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验售后订单数量正确性
        Integer orderRefundCount = new JSONObject(jsonresult.get("result")).getInt("orderRefundCount");
        Assert.assertEquals(3, orderRefundCount.intValue(), String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验扣除金额正确性
        Double deductAmount = new JSONObject(jsonresult.get("result")).getDouble("deductAmount");
        Assert.assertEquals(-0.06, deductAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验应收金额正确性
        Double receivableAmount = new JSONObject(jsonresult.get("result")).getDouble("receivableAmount");
        Assert.assertEquals(0.02, receivableAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));

        //校验实收金额正确性
        Double actualityAmount = new JSONObject(jsonresult.get("result")).getDouble("actualityAmount");
        Assert.assertEquals(0.02, actualityAmount, String.format(Config.result_message, Config.PlatformSupplierPro, scene, ErrorEnum.ISWRONG.getMsg(), createUrl, null, jsonresult));


    }


    @Test(dependsOnMethods = "checkStatementCorrectnessTest", description = "对账单确认核对")
    public void updateStatusTest(){
        // 存放参数
        JSONObject param = JSONUtil.createObj();
        param.put("statementStatus", 2);

        List<Integer> ids = new ArrayList<>();
        ids.add(Integer.valueOf(Config.statementId));
        param.put("ids", ids);

        String body = param.toString();
        String createUrl = Common.SupplierUrl+ Common.supplierStatementUpdateStatusUri;
        // 存放请求头
        headers.put("supplier-cache", Common.supplierToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, body, createUrl,Config.PlatformSupplierPro, scene);
        logger.info("对账单确认核对结果: {}", result);
    }




    public static String getrefundApplyNumber(String itemOrderNumber){
        JSONObject param = JSONUtil.createObj();
        param.put("isRefund", true);
        param.put("page", 1);
        param.put("pageSize", 10);
        String createUrl = Common.OpUrl + Common.opAdminRefundListUri;
        headers.put("fuli-cache", Common.fuliOperationPlatformToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(param).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.FuliYunYingPro, scene);
        //获取长度
        String data = jsonresult.get("data").toString();
        JSONObject datajson = new JSONObject(data);
        JSONArray jsonArray =new JSONArray(datajson.get("rows"));
        int length = jsonArray.toArray().length;
        for(int i = 0; i < length; i++) {
            String itemOrderNumberZuixin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("itemOrderNumber").toString();
            if (itemOrderNumber.equals(itemOrderNumberZuixin)) {
                String refundApplyNumber = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("rows"))).get(i))).get("refundApplyNumber").toString();
                logger.info("获取到的退款订单号refundNumbers: itemOrderNumber={},refundApplyNumber={}", itemOrderNumberZuixin, refundApplyNumber);
                return refundApplyNumber;
            }else {
                logger.info("没有匹配到的退款订单号和退款id，不进行退款操作：itemOrderNumber={}, refundApplyNumber={}", itemOrderNumberZuixin, null);
            }
        }
        return null;
    }

}

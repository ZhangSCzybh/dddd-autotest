package com.dddd.qa.zybh.others;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2025年06月19日 15:41:18
 * @packageName com.dddd.qa.zybh.others
 * @className StressTest
 * @describe TODO
 */
public class StressTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static String orderProdDetails="test-dddd/createProdOrder";
    private static final String scene1 = "商品下单";


    @DataProvider(name = "staffFuliTokenProvider")
    public Object[][] staffFuliTokenFromCSV() {
        return new Object[][] {
                {1,"4743565889a542c5bb04385c3ba4312b","31687"}, //

        };
    }


    @Test(dataProvider = "staffFuliTokenProvider")
    public void purchaseGoods(int num, String tokendata,String addressId ) throws InterruptedException {

        //存放参数
        JSONObject param = JSONUtil.createObj();
            param.put("skuId", 4245769);
            param.put("checked", 0);
            param.put("count", 1);
            String body = param.toString();
            String createUrl = Common.MallUrl+Common.addCartUri;
            //存放请求头，可以存放多个请求头
            headers.put("Yian-Cache", tokendata);
            String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            logger.info("加入购物车：" + result);


        //对购物车里的商品进行提交订单
        com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases(orderProdDetails);
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

        Assert.assertNotNull(orderNumber,String.format(Config.result_message, Config.MallPro, scene1, ErrorEnum.ISEMPTY.getMsg(), Common.comfirmOrderUri, orderNumber, jsonresult3));
        caveat("第" + num + "个账号: " + tokendata + "\n"
                +"创建订单：" + status + "\n"
                +"订单编号：" + orderNumber + "\n"
                + "确认下单："+ status3);

    }
}

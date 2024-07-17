package com.dddd.qa.zybh.ApiTest.ProductTest;

import cn.hutool.http.HttpUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangsc
 * @date 2024年07月16日 11:25:32
 * @packageName com.dddd.qa.zybh.ApiTest.ProductTest
 * @className PurchaseTest
 * @describe TODO
 */
public class PurchaseTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    //private String orderDetails = "dddd/createOrder";
    //private String skuListfile = "dddd/skuList";
    //private static HashMap<String, String> headers =new HashMap<>();
    //private static String scene = "福粒商城";


    /**
     * @Description: TODO methods
     * @param
     * @return void
     * @author Zhangsc
     * @date 2024/7/16 15:03

    @Test(description = "加入购物车")
    public  void addShoppingCart(){
        //存放参数
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(skuListfile);
        List<String> list = Arrays.asList("3995042", "3995035");
        for (String skuId : list) {
            param.put("skuId", skuId);
            String body = param.toString();
            String createUrl = Common.FuliUrl+Common.addCartUri;
            //存放请求头，可以存放多个请求头
            headers.put("Yian-Cache", Common.FuliToken);
            String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            logger.info(result);
        }

    }

     */

    //创建订单
    @Test
    public void createOreder(){
    }
    //确认支付
    @Test
    public void confirmPay(){
    }




}

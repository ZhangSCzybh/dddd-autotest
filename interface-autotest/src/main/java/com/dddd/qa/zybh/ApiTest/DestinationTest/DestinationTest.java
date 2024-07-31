package com.dddd.qa.zybh.ApiTest.DestinationTest;


import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class DestinationTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);

    @BeforeClass
    public static void setUp(){
        Common.DDingDDangToken = LoginUtil.loginToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri,Common.loginDDingDDangInfo);
        logger.info("执行登录获取智采企业平台的token" + Common.DDingDDangToken);
    }

    @Test
    public void test(){

    }
}

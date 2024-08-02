package com.dddd.qa.zybh.ApiTest.SettingTest;


import cn.hutool.core.date.DateUtil;
import com.dddd.qa.zybh.BaseTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author zhangsc
 * @date 2024年06月18日 13:37:01
 * @packageName com.dddd.qa.zybh.ApiTest.SettingTest
 * @className LoginZCTest
 * @describe
 */
public class LoginZCTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangToken = LoginUtil.loginToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri,Common.loginDDingDDangInfo);
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);
    }


    @Test
    public void loginDDingDDangHR(){
        String firstTenChars = Common.DDingDDangToken.substring(0,8);
        caveat("===========测试开始==========="+ "\n" + "登录账号:" + firstTenChars + "*******************");

    }


}


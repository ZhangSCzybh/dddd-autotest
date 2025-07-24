package com.dddd.qa.zybh.ApiTest.SettingTest;


import cn.hutool.core.date.DateUtil;
import com.dddd.qa.zybh.BaseTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.checkerframework.checker.units.qual.C;
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
        Common.DDingDDangPCToken = LoginUtil.loginYGPCToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri , Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc平台的token：" + Common.DDingDDangPCToken);

        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);

        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);

    }


    @Test
    public void loginDDingDDangYGPC(){
        String firstTenChars1 = Common.DDingDDangPCToken.substring(0,8);
        String firstTenChars2 = Common.fuliOperationPlatformToken.substring(0,8);
        String firstTenChars3 = Common.DDingDDangToken.substring(0,8);
        caveat("===========测试开始==========="+ "\n" + "智采员工PC:" + firstTenChars1 + "*******************" + "\n" + "慧卡运营平台:" + firstTenChars2 + "*******************" + "\n" + "智采企业平台:" + firstTenChars3 + "*******************");
    }
}


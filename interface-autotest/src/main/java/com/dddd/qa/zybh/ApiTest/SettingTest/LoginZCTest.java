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
        try {
            String loginUrl = Common.zhicaiHrUrl + Common.loginDDingDDangUri;
            // 1. 创建 URL 对象
            URL url = new URL(loginUrl);
            // 2. 打开 HttpURLConnection 连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 3. 设置请求方法为 POST
            connection.setRequestMethod("POST");
            // 4. 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");
            // 5. 允许写出和输入
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 6. 写出请求体
            try (OutputStream os = connection.getOutputStream()) {
                // 将请求体数据写入输出流
                byte[] input= Common.loginDDingDDangInfo.getBytes("UTF-8");
                //byte[] input = ("{ \"identityType\": \"3\" ,\"password\": \"zybh123456\",\"username\": \"17858805009\"}").getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 7. 获取响应状态码
            //int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);
            // 8. 获取响应头
            //Map<String, List<String>> headers = connection.getHeaderFields();
            //for (Map.Entry<String, java.util.List<String>> entry : headers.entrySet()) {
            //    System.out.println(entry.getKey() + ": " + entry.getValue());
            //}
            Common.DDingDDangToken = connection.getHeaderField("session-token");
            logger.info("获取到access-token:" + Common.DDingDDangToken);
            caveat("access-token:" + Common.DDingDDangToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


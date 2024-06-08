package com.yolocast.qa.zsc.ApiTest.SettingTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yolocast.qa.zsc.Constant.Common;
import com.yolocast.qa.zsc.Constant.Config;
import com.yolocast.qa.zsc.utils.ErrorEnum;
import com.yolocast.qa.zsc.BaseTest;
import com.yolocast.qa.zsc.Constant.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class loginTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static String scene = "登录模块";
    //private ResourceBundle bundle;


    @BeforeTest
    public void beforeTest(){
        //bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        //yolocastUrl = bundle.getString("yolocast.prod.url");
        caveat("******************** \n Env:" + Common.profiesEnv + " \n 域名：" + Common.DDingDDdangUrl + " \n********************");
    }


    @Test(groups = "login")
    public void loginYolocast(){

        //String param = "{\"email\":\"zhangsc@yunxi.tv\",\"password\":\"zsc123456\"}";
        JSONObject param = JSONUtil.createObj();
        param.put("email", Common.loginYolocastEmail);
        param.put("password", Common.loginYolocastPassword);

        String loginUrl = Common.yolocastUrl + Common.loginUri;
        String body = param.toString();

        cn.hutool.json.JSONObject jsonresult = new JSONObject();
        String result;

        try {
            result = HttpUtil.post(loginUrl, body);
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, loginUrl, scene);
        }catch(NullPointerException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), loginUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }
        /********************************************************************接口可用性检查结束********************************************************************/

        try{
            Assert.assertEquals(true, StrUtil.isNotBlank(jsonresult.get("data").toString()),String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), loginUrl, body, jsonresult));

        }catch (Exception e){
            logger.error("data为空");

        }
        //获取data里的token
        //JSONObject resultJsonData = new JSONObject(jd.get("data"));
        Common.Cookies = (String) (new JSONObject(jsonresult.get("data"))).get("token");
        logger.info("cast-auth:" + Common.Cookies);
           // DingTalkRobotUtil.sendMessageText("https://oapi.dingtalk.com/robot/send?access_token=93293101f693bc9548a0d86ed62610fb2f7cb9cc74d98bb18e3072bd07d81a35",Common.Cookies+"text", "text", Collections.singletonList("17858803001"),false);

    }

    @Test
    public void test0607(){
        try {
            String loginUrl = Common.DDingDDdangUrl + Common.loginDDingDDangUri;
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
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            // 8. 获取响应头
            //Map<String, List<String>> headers = connection.getHeaderFields();
            //for (Map.Entry<String, java.util.List<String>> entry : headers.entrySet()) {
            //    System.out.println(entry.getKey() + ": " + entry.getValue());
            //}
            Common.DDingDDangToken = connection.getHeaderField("session-token");
            logger.info("access-token:" + Common.DDingDDangToken);
            caveat("access-token:" + Common.DDingDDangToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


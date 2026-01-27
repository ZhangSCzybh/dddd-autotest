package com.dddd.qa.zybh.ApiTest.FuliHrTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @author zhangsc
 * @date 2025年08月16日 08:10:54
 * @packageName com.dddd.qa.zybh.ApiTest.SourceTest
 * @className BatchImportEmployeeJumpLogin
 * @describe TODO 批量创建员工，获取商城token。不同环境福粒HR平台的账号密码 ,Count员工数量,HrUrl、MallUrl,loginName、password需要替换
 */
public class BatchImportEmployeeJumpLogin {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final HashMap<String, String> headers2 =new HashMap<>();
    private static final HashMap<String, String> headers3 =new HashMap<>();


    private static String employeeLoginName;
    private static final String password= "addzaiyebuhui"+ Config.NowYmd;
    private static final int Count= 2;//员工数量


    @BeforeClass
    public static void setUp() {
        Common.FuliHrToken = LoginUtil.loginFuliHrToken( Common.HrUrl + Common.loginHrUri , Common.loginHrInfo);
        logger.info("执行登录获取福粒HR平台的token：" + Common.FuliHrToken);
    }


    @Test(description = "福粒Hr平台新增员工")
    public void addEmployees() throws InterruptedException {
        for(int i = 0; i < Count; i++) {
            //存放参数
            JSONObject param = JSONUtil.createObj();
            param.put("email", password + DateUtil.RandomSixDigit()+ "@163.com");
            param.put("employeeName", DateUtil.RandomSixDigit() +Config.NowYmd);
            param.put("employeeNo", DateUtil.RandomSixDigit() + Config.NowYmd);
            param.put("loginName", password+DateUtil.RandomSixDigit());
            param.put("mobile", DateUtil.RandomNumberGenerator());
            param.put("password", password);
            param.put("resPassword", password);
            param.put("deptId", 0);

            String body = param.toString();
            String createUrl = Common.HrUrl+ "/enterpriseadmin/employees";
            headers.put("enterprise-cache", Common.FuliHrToken);
            String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            logger.info("新增员工：" + result);
            Thread.sleep(100); // 等待 10 秒


        }
    }
    @Test(dependsOnMethods = "addEmployees", description = "员工账号登录商城，获取登录token，新增地址")
    public void importEmployeesJumpAmall(){
            JSONObject param = JSONUtil.createObj();
            param.put("page", 1);
            param.put("pageSize", Count);
            String createUrl = Common.HrUrl + "/enterpriseadmin/employees";
            headers.put("enterprise-cache", Common.FuliHrToken);
            String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
            JSONObject jsonresult = new JSONObject(result);

            // 定义目标文件路径
            File file = new File("");
            String filePath = file.getAbsolutePath() + Common.mallTokenInfo;//不同环境修改此处
            //String filePath = Common.jenkinsUrl + "/src/main/resources/test-dddd/token.json";//不同环境修改此处

            //循环获取
            for(int j = 0; j < Count; j++) {
                String employeeName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(j))).get("employeeName").toString();
                employeeLoginName = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(j))).get("loginName").toString();
                logger.info("员工信息: employeeName={},loginName={}", employeeName,employeeLoginName);

                //登录商城，新增地址
                String loginMallToken=  loginFuliMallToken( Common.MallUrl+"/enterprise/account/login");

                //新增地址
                com.alibaba.fastjson.JSONObject param2 = GetCaseUtil.getAllCases(Common.mallAddressInfo);
                String body2 = param2.toString();
                String createUrl2 = Common.MallUrl + "/enterprise/address/addAddress";
                headers2.put("employee-cache", loginMallToken);
                String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers2).body(body2).execute().body();
                logger.info("新增地址：" + result2);

                //获取地址id
                JSONObject param3 = JSONUtil.createObj();
                String body3 = param3.toString();
                String createUrl3 = Common.MallUrl + "/enterprise/address/getAddressList";
                headers3.put("employee-cache", loginMallToken);
                String result3 = HttpUtil.createGet(createUrl3).addHeaders(headers3).form(body3).execute().body();
                JSONObject jsonresult3 = new JSONObject(result3);
                JSONArray data3 = jsonresult3.getJSONArray("result");
                JSONObject firstOrder = data3.getJSONObject(0);
                String addressId =  firstOrder.get("id").toString();
                logger.info("地址Id：" + addressId);

                // 使用 try-with-resources 自动关闭资源，并启用追加模式
                try (FileWriter writer = new FileWriter(filePath, true)) {
                    // 写入格式为 "employeeName,loginName" 的字符串，并换行
                    writer.write(loginMallToken + ","+ addressId + "\n");
                    System.out.println("数据已成功追加写入文件：" + filePath);
                } catch (IOException e) {
                    // 捕获并处理写入文件时可能发生的异常
                    System.err.println("写入文件失败：" + e.getMessage());
                    e.printStackTrace();
                }

                //caveat("登录账号: " + employeeLoginName + "\n" + "登录密码：" + password + "\n" +"employee-cache：" + loginMallToken + "\n" + "地址Id："+ addressId);



            }


    }

    //账号密码登录商城
    public static String loginFuliMallToken(String loginUrl) {
        try {
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
                //byte[] input = userinfo.getBytes("UTF-8");
                //byte[] input = ("{ \"selfOperatedPlatform\": \"1\" ,\"password\": \"zaiyebuhui01\",\"loginName\": \"zaiyebuhui01\",\"agreePrivacyPolicy\": \"1\"}").getBytes("utf-8");
                JSONObject json = new JSONObject();
                json.put("selfOperatedPlatform", "1");
                json.put("password",password );
                json.put("loginName", employeeLoginName);
                json.put("agreePrivacyPolicy", "1");
                byte[] input = json.toString().getBytes("UTF-8");

                os.write(input, 0, input.length);
            }
            return connection.getHeaderField("employee-cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

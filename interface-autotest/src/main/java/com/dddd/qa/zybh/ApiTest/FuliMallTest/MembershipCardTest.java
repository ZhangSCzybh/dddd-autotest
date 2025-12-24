package com.dddd.qa.zybh.ApiTest.FuliMallTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.JDBCUtil;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;


/**
 * @author zhangsc
 * @date 2025年12月10日 11:22:45
 * @packageName com.dddd.qa.zybh.ApiTest.FuliMallTest
 * @className MembershipCardTest
 * @describe 只需要传一个会员卡卡密 pre环境测试 切库的话需要替换创建会员卡企业
 */
public class MembershipCardTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String scene = "会员卡领取";

    private static final String MallUrl = "https://serverdev.lixiangshop.com";
    private static String verifyNumber;
    private static String imageCode;
    private static String smsCode;
    private static final int TIMEOUT_MINUTES = 2;

    @Test(dependsOnMethods = "com.dddd.qa.zybh.ApiTest.FuliOpTest.CreateCardsTest.CardDeliverTest", description = "识别图形验证码，获取imagecode和smscode")
    public void ImageCodeTest(){
        verifyNumber = getCardVerifyNumber(Config.cardNumber);

        Instant startTime = Instant.now(); // 记录开始时间
        boolean found = false; // 标记是否找到目标值
        while (Duration.between(startTime, Instant.now()).toMinutes() < TIMEOUT_MINUTES) {
            String url = MallUrl + "/common/imagecode/register?base64=true";
            String result = HttpUtil.createGet(url)
                    .addHeaders(headers)
                    .execute()
                    .body();
            JSONObject jsonResult = new JSONObject(result);
            String codeKey = (new JSONObject(jsonResult.get("result")).get("codeKey")).toString();
            String base64Image = (new JSONObject(jsonResult.get("result")).get("base64Image")).toString();
            logger.info("获取到codeKey:{}", codeKey);
            logger.info("获取到base64Image:{}", base64Image);

            try {
                // 解码 Base64 字符串
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // 保存为 JPEG 文件
                try (FileOutputStream fos = new FileOutputStream("captcha.jpg")) {
                    fos.write(imageBytes);
                }

                System.out.println("验证码图像已保存为 captcha.jpg");
                System.out.println("请在当前目录下查看 captcha.jpg 文件");
            } catch (IOException e) {
                System.err.println("解码或保存失败: " + e.getMessage());
                e.printStackTrace();
            }

            System.setProperty("jna.library.path", "/opt/homebrew/lib");
            String dataPath = "/opt/homebrew/Cellar/tesseract/5.5.1_1/share/tessdata";
            String picturePath = "captcha.jpg";
            // 安全处理（如果字符串长度不足4位）
            if (baseVerCode(dataPath, picturePath).length() >= 4) {
                imageCode = baseVerCode(dataPath, picturePath).substring(0, 4);
                logger.info("识别到图形验证码:{}", imageCode);
            } else {
                System.out.println("字符串长度不足4位");
            }

            //获取手机验证码
            JSONObject param1 = JSONUtil.createObj();
            param1.put("codeKey", codeKey);
            param1.put("imageCode", imageCode);
            param1.put("verifyNumber",verifyNumber);
            param1.put("mobile", "17858800001");
            String body1 = param1.toString();
            String createUrl1 = MallUrl + "/enterprise/cards/smscode";
            String result1 = HttpUtil.createPost(createUrl1).addHeaders(headers).body(body1).execute().body();
            JSONObject jsonresult1 = new JSONObject(result1);
            System.out.println(jsonresult1);
            //smsCode = (new JSONObject(jsonresult1.get("result")).get("smsCode")).toString();
            //logger.info("获取到手机验证码:{}", smsCode);
            int code = (int) jsonresult1.get("code");
            if (code == 1001) {
                smsCode = (new JSONObject(jsonresult1.get("result")).get("smsCode")).toString();
                logger.info("获取到手机验证码:{}", smsCode);
                found = true;
            }
            if (found) {
                break;
            }
            // 等待一段时间后重试
            try {
                Thread.sleep(1000); // 等待1秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("线程被中断", e);
            }
        }
        // 如果超时仍未识别成功图形验证码，标记测试失败
        if (!found) {
            Assert.fail("在 " + TIMEOUT_MINUTES + " 分钟内未识别到图形验证和手机验证码: " + null);
        }
    }


    @Test(dependsOnMethods = "ImageCodeTest", description = "登录商城领取会员卡积分")
    public void smsCodeCheckTest(){
        JSONObject param2 = JSONUtil.createObj();
        param2.put("verifyNumber", verifyNumber);
        param2.put("mobile", "17858800001");
        param2.put("smsCode", smsCode);
        String body2 = param2.toString();
        String createUrl2 = MallUrl + "/enterprise/cards/smsCodeCheck";
        String result2 = HttpUtil.createPost(createUrl2).addHeaders(headers).body(body2).execute().body();
        JSONObject jsonresult2 = new JSONObject(result2);
        System.out.println(jsonresult2);
        //校验接口可行性
        CommonUtil.assertAvailable(jsonresult2, body2, createUrl2, Config.MallPro, scene);
    }


    //无干扰项的字母数字图片验证码识别
    public static String baseVerCode(String dataPath, String picturePath) {
        String result = null;
        Tesseract tesseract = new Tesseract();
        tesseract.setVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz");

        tesseract.setDatapath(dataPath); // 设置tessdata文件夹的路径

        // 其他配置，如语言、OCR引擎等
        try {
            result = tesseract.doOCR(new File(picturePath)); // 识别图片
            //  System.out.println(result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }

    //JDBC连接数据库通过卡号获取卡密
    public static String getCardVerifyNumber(String cardNumber) { // 移除了不必要的 throws SQLException，因为内部处理了
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String verifyNumber = null;
        try {
            conn = JDBCUtil.getConn(); // 使用工具类获取连接
            if (conn != null) {
                conn.setAutoCommit(false); // 开启事务

                String sql_select = "SELECT verify_number FROM fulidev.card_storage WHERE card_number = ?";
                pstmt = conn.prepareStatement(sql_select);
                pstmt.setString(1, cardNumber); // 设置参数

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    verifyNumber = rs.getString("verify_number");
                    System.out.println("查询结果卡密: " + verifyNumber);
                }

                conn.commit(); // 提交事务 (如果没有修改数据，这步可省略或放在最后)

            } else {
                System.err.println("未能获取到数据库连接");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("数据库操作出错: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                    System.out.println("事务已回滚");
                } catch (SQLException rollbackEx) {
                    System.err.println("回滚事务时出错: " + rollbackEx.getMessage());
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            // 使用工具类关闭所有资源
            JDBCUtil.closeResource(rs, pstmt, conn);
        }
        return verifyNumber;
    }

}



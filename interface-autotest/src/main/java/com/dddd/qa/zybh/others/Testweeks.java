package com.dddd.qa.zybh.others;


import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.LoginUtil;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;


/**
 * @author zhangsc
 * @date 2024年07月17日 11:41:00
 * @packageName com.dddd.qa.zybh
 * @className Testweeks
 */
public class Testweeks {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    //ceshi测试
    //karen吴女士
    //蒋美娣13858653282
    //WXY13666605555
    //许林彪18767176714
    //信达办公用品经营部
    //万客隆商贸有限公司
    //宇轩图文设计中心
    //智惠恒数码有限公司

    //数据驱动
    @DataProvider(name = "testData")
    public Object[][] testData() {
        return new Object[][] {
                {2} // 数据集1
        };
    }
    //获取csv文件数据
    public static List<String> readCSV(String path) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }


    @Test
    public void test(){
        System.out.println(new File("").getAbsolutePath());
        //4个自建供应商供应商-发货 登录获取token
        //获取员工employee-cache
        Common.mallToken = LoginUtil.loginFuliMallToken("https://serverdev.lixiangshop.com/enterprise/account/login",Common.loginMallInfo1);
        logger.info("获取虞信品员工商城的token：{}", Common.mallToken);

    }

    @Test
    public void test1(){
        Common.fuliOperationPlatformToken = LoginUtil.loginOperationPlatformToken(Common.OpUrl + Common.loginOPUri , Common.loginOPInfo);
        logger.info("执行登录获取慧卡运营平台的token：" + Common.fuliOperationPlatformToken);
        // 登录获取token
        Common.supplierToken = LoginUtil.loginSupplierToken(Common.SupplierUrl+Common.supplierLoginUri,Common.loginSupplierInfo);
        logger.info("执行登录获取供应商平台的token：" + Common.supplierToken);
    }


    public static void main(String[] args) {
        System.setProperty("jna.library.path", "/opt/homebrew/lib");
        String dataPath = "/opt/homebrew/Cellar/tesseract/5.5.1_1/share/tessdata";
        String picturePath = "/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/captcha.jpg";
        System.out.println(baseVerCode(dataPath,picturePath));

        /////////////////////////////////////////////////////////////////////////////////////////////

        try {
            // 读取图像
            File imageFile = new File("/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/captcha.jpg");
            BufferedImage image = ImageIO.read(imageFile);

            // 预处理图像
            BufferedImage processedImage = preprocessImage(image);

            // 保存预处理后的图像（用于调试）
            ImageIO.write(processedImage, "jpg", new File("processed_captcha.jpg"));

            // 初始化Tesseract
            Tesseract tess = new Tesseract();

            // 请根据实际安装路径修改
            String tessDataPath = "/opt/homebrew/Cellar/tesseract/5.5.1_1/share/tessdata";
            tess.setDatapath(tessDataPath);
            tess.setLanguage("eng");
            // 正确的PageSegMode使用方式
            //tess.setPageSegMode(Tesseract.PageSegMode.PSM_SINGLE_CHAR);

            // 进行OCR识别
            String result = tess.doOCR(processedImage);

            // 清理结果
            String cleanedResult = result.replaceAll("[^a-zA-Z0-9]", "");

            // 输出结果
            System.out.println("验证码识别结果: " + result);
            System.out.println("清理后的识别结果: " + cleanedResult);

        } catch (Exception e) {
            System.err.println("识别过程中出错: " + e.getMessage());
            e.printStackTrace();
        }


    }

    //无干扰项的字母数字图片验证码识别
    public static String baseVerCode(String dataPath,String picturePath){
        String result = null;
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath); // 设置tessdata文件夹的路径
        // 其他配置，如语言、OCR引擎等
        try {
            result = tesseract.doOCR(new File(picturePath)); // 识别图片
            //  System.out.println(result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return  result;
    }

    private static BufferedImage preprocessImage(BufferedImage image) {
        // 1. 转换为灰度图
        BufferedImage grayImage = toGrayscale(image);

        // 2. 二值化处理
        BufferedImage binaryImage = binarize(grayImage);

        // 3. 去噪处理
        BufferedImage denoisedImage = denoise(binaryImage);

        return denoisedImage;
    }

    private static BufferedImage toGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                grayImage.setRGB(x, y, gray);
            }
        }
        return grayImage;
    }

    private static BufferedImage binarize(BufferedImage image) {
        BufferedImage binaryImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        int threshold = 128;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                int binary = (pixel > threshold) ? 255 : 0;
                binaryImage.setRGB(x, y, binary);
            }
        }
        return binaryImage;
    }

    private static BufferedImage denoise(BufferedImage image) {
        BufferedImage denoisedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                int left = image.getRGB(x - 1, y) & 0xFF;
                int right = image.getRGB(x + 1, y) & 0xFF;
                int top = image.getRGB(x, y - 1) & 0xFF;
                int bottom = image.getRGB(x, y + 1) & 0xFF;

                // 如果周围有3个以上黑色像素，则保留为黑色
                int blackCount = (left == 0 ? 1 : 0) + (right == 0 ? 1 : 0) + (top == 0 ? 1 : 0) + (bottom == 0 ? 1 : 0);
                if (blackCount >= 3) {
                    denoisedImage.setRGB(x, y, 0); // 黑色
                } else {
                    denoisedImage.setRGB(x, y, pixel); // 保持原色
                }
            }
        }
        return denoisedImage;
    }

}
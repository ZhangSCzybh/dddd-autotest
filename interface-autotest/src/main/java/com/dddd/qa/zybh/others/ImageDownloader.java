package com.dddd.qa.zybh.others;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.Config;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsc
 * @date 2024年08月30日 10:45:46
 * @packageName com.dddd.qa.zybh.others
 * @className ImageDownloader
 * @describe unsplash网站下载图片
 */


public class ImageDownloader {

    private static final String imageApi = "https://unsplash.com/napi/topics/";

    //商品下单：账号编号、token、地址id配置
    @DataProvider(name = "imageTypeprovider")
    public Object[][] imageTypeFromCSV() {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/dddd/imageType.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("imageType")) { // 跳过标题行
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "imageTypeprovider")
    public void imageDownloadeTest (String imageType) throws Exception {
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("page", 2);
        map.put("per_page", 30);
        String getImageLink =imageApi + imageType +"/photos";
        String result= HttpUtil.createGet(getImageLink).form(map).execute().body();
        JSONArray jsonArray =new JSONArray(result);
        int length = jsonArray.toArray().length;

        String outPath = "/Users/zhangshichao/Documents/Pictures/" + imageType + "/"; //图片类型
        createFolderIfNotExists(outPath);//判断文件夹是否存在

        for(int i = 0; i < length; i++){
            JSONObject jsoni = new JSONObject(jsonArray.get(i));
            String imageUrl = new JSONObject(jsoni.get("links")).get("download").toString();
            //图片地址
            Path outputPath = Paths.get(outPath + Config.getTimestampAfterTenMinutes+ i + ".jpg");
            try {
                downloadImage1(imageUrl, outputPath);
                System.out.println("Image downloaded successfully." + i);
            } catch (IOException e) {
                System.err.println("Failed to download the image: " + e.getMessage());
            }
        }


    }


    //下载图片输入输出地址
    public static void downloadImage(String imageUrl, Path outputPath) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, outputPath);
        }
    }
    public static void downloadImage1(String imageUrl, Path outputPath) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.deleteIfExists(outputPath); // Delete the file if it exists
            Files.createFile(outputPath); // Create the file

            try (OutputStream out = Files.newOutputStream(outputPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
                byte[] buffer = new byte[8192]; // Increase buffer size for better performance
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    //判断文件夹是否存在
    public static void createFolderIfNotExists(String folderPath) {
        Path path = Paths.get(folderPath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Folder created: " + folderPath);
            } catch (IOException e) {
                System.err.println("Failed to create the folder: " + e.getMessage());
            }
        } else {
            System.out.println("Folder already exists: " + folderPath);
        }
    }
}
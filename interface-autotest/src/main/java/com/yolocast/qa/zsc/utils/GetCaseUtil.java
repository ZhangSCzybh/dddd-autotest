package com.yolocast.qa.zsc.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author zhangsc
 * @date 2022-05-03 下午5:37
 */
public class GetCaseUtil {
    //读取json格式案例
    public static JSONObject getCase(String caseName, String fileName) {
        try {
            File file = new File("");
            String filePath = file.getAbsolutePath() + "/src/main/resources/" + fileName + ".json";

            InputStream inputStream = new FileInputStream(filePath);
            String caseTags = IOUtils.toString(inputStream, "utf8");
            JSONObject config = JSONObject.parseObject(caseTags, Feature.OrderedField);
            return config.getJSONObject(caseName);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public static JSONObject getAllCases(String fileName) {
        JSONObject result = new JSONObject();
        try {
            File file = new File("");
            String filePath = file.getAbsolutePath() + "/src/main/resources/" + fileName + ".json";

            InputStream inputStream = new FileInputStream(filePath);
            String caseTags = IOUtils.toString(inputStream, "utf8");
            result = JSONObject.parseObject(caseTags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

package com.yolocast.qa.zsc.Constant;


import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yolocast.qa.zsc.BaseTest;
import com.yolocast.qa.zsc.utils.ErrorEnum;
import org.junit.rules.ErrorCollector;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.is;

public class CommonUtil {

    public static String filedDetmessage = "报警信息 \n运行环境：" + Common.profiesEnv + "\n接口：%s\n报错类型：%s \n入参信息：%s\n详细信息: %s";


    /**
     * @param result    接口返回JSONObject 仅接受JSONObject
     * @param params    入参
     * @param url       接口
     * @param scene     业务场景
     */
    public static void assertAvailable(JSONObject result, String params, String url, String scene){
        //状态码异常判断
        try {
            if (result.isEmpty() || result == null) {
                String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), url, params, result);
                BaseTest.caveat(wrong);
                Assert.fail(wrong);
            }
            int code = (int) result.get("code");
            if (code != 200) {
                String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISCODE.getMsg(), url, params, result);
                BaseTest.caveat(wrong);
                Assert.fail(wrong);
            }
            if (!result.get("success").toString().equals(true)) {
                String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISNOSUCCESS.getMsg(), url, params, result);
                BaseTest.caveat(wrong);
                Assert.fail(wrong);
            }
        } catch (Exception e) {
            Assert.fail(String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), url, params, result + "\n" + e.getMessage()));
        }


    }


    /**
     * 校验关键字段是否均有
     *
     * @param path      接口地址
     * @param collector 错误收集ErrorCollector
     * @param input     接口入参
     * @param json      接口返回JSONObject 仅接受JSONObject
     * @param keys      关键字段
     */
    public static boolean filedExistDet(ErrorCollector collector, ArrayList<String> keys, Object json, String path, Object input) {
        AtomicBoolean flag = new AtomicBoolean(true);
        if (json instanceof com.alibaba.fastjson.JSONObject) {
            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) json;
            keys.forEach(key -> {
                collector.checkThat(String.format(filedDetmessage, path, ErrorEnum.ISLACK.getMsg(), input, "jsonObject:" + jsonObject + "\n缺失key：" + key), jsonObject.containsKey(key), is(true));
                if (!jsonObject.containsKey(key)) {
                    flag.set(false);
                }
            });
        } else {
            collector.checkThat(String.format(filedDetmessage, path, ErrorEnum.ISTYPEWRONG.getMsg(), input, json), json instanceof com.alibaba.fastjson.JSONObject, is(true));


        }
        return flag.get();
    }


    /**
     * 关键字段数据不为null校验
     *
     * @param path      接口地址
     * @param collector 错误收集ErrorCollector
     * @param input     接口入参
     * @param json      接口返回JSONObject或JSONArray
     * @param keys      关键字段
     */
    public static void keyDataDet(ErrorCollector collector, ArrayList<String> keys, Object json, String path, Object input) {
        if (json instanceof com.alibaba.fastjson.JSONObject) {
            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) json;
            for (Object key : jsonObject.keySet()) {
                String keyStr = (String) key;
                Object keyValue = jsonObject.get(keyStr);
                if (keys.contains(keyStr)) {
                    //判断keyValue是否为空/null
                    if (keyValue != null) {
                        collector.checkThat(String.format(filedDetmessage, path, ErrorEnum.ISEMPTY.getMsg(), input, keyStr + ":" + keyValue), keyValue.toString().isEmpty(), is(false));
                    }
                    collector.checkThat(String.format(filedDetmessage, path, ErrorEnum.ISEMPTY.getMsg(), input, keyStr + ":" + keyValue), keyValue == null, is(false));
                } else if (keyValue instanceof com.alibaba.fastjson.JSONObject || keyValue instanceof JSONArray) {
                    keyDataDet(collector, keys, keyValue, path, input);
                }
            }
        } else if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            if (jsonArray.size() != 0) {
                jsonArray.forEach(value -> {
                    keyDataDet(collector, keys, value, path, input);
                });
            }
        } else {
            collector.checkThat(String.format(filedDetmessage, path, ErrorEnum.ISEMPTY.getMsg(), input, json), json.toString().isEmpty() || json == null, is(false));
        }

    }

}

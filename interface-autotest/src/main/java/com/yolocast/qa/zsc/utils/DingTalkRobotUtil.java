package com.yolocast.qa.zsc.utils;

import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.yolocast.qa.zsc.Constant.Config;

/**
 * @author zhangsc
 * @date 2022-04-28 下午3:10
 */
public class DingTalkRobotUtil {

    public static List<String> attchments = Lists.newArrayList();
    private static final int timeout =10000;

    /**
     * 单人mobile
     * @author zhangsc
     * @date 2022/4/28 下午3:37
     * @param webHookTokenUrl
     * @param content
     * @param mobile
     */

    public static void sendMessage(String webHookTokenUrl,String content,String mobile) throws IOException {

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webHookTokenUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

        JSONObject textContent = new JSONObject();
        textContent.put("content", content);
        JSONObject atMobilesContent = new JSONObject();
        atMobilesContent.put("atMobiles", Collections.singletonList(mobile));
        atMobilesContent.put("isAtAll",false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        jsonObject.put("text", textContent);
        jsonObject.put("at",atMobilesContent);

        String textMsg = jsonObject.toJSONString();
        System.out.println(textMsg);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);

        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
        }

    }



/**
 *
 * @author zhangsc
 * @date 2022/4/28 下午3:48
 * @param webHookTokenUrl
 * @param content
 * @param msgtype
 * @param atMobiles
 * @param isAtAll
 */


    public static void sendMessageText(String webHookTokenUrl, String content, String msgtype, List<String> atMobiles,
                                       boolean isAtAll) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webHookTokenUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

        JSONObject textContent = new JSONObject();
        textContent.put("content", content+"\n 当前时间："+ LocalDateTime.now());
        JSONObject atMobilesContent = new JSONObject();
        atMobilesContent.put("atMobiles",atMobiles);
        atMobilesContent.put("isAtAll",isAtAll);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", msgtype);
        jsonObject.put("text", textContent);
        jsonObject.put("at",atMobilesContent);

        String textMsg = jsonObject.toJSONString();
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);

        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
        }
    }


    //报警人添加
    public static void addAttch() {
        attchments.add(Config.zsc);
    }

    //测试钉钉tongzhi
    public static void main(String[] args) throws IOException {
        System.out.println("11");
        DingTalkRobotUtil.sendMessageText("https://oapi.dingtalk.com/robot/send?access_token=93293101f693bc9548a0d86ed62610fb2f7cb9cc74d98bb18e3072bd07d81a35","这个是测试消息，请忽略text", "text",attchments,false);
    }


    }





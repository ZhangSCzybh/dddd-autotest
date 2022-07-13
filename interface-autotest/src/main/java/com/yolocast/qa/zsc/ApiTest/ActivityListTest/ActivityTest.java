package com.yolocast.qa.zsc.ApiTest.ActivityListTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.yolocast.qa.zsc.ApiTest.SettingTest.loginTest;
import com.yolocast.qa.zsc.BaseTest;
import com.yolocast.qa.zsc.Constant.Common;
import com.yolocast.qa.zsc.Constant.CommonUtil;
import com.yolocast.qa.zsc.Constant.Config;
import com.yolocast.qa.zsc.utils.DateUtil;
import com.yolocast.qa.zsc.utils.ErrorEnum;
import com.yolocast.qa.zsc.utils.GetCaseUtil;
import com.yolocast.qa.zsc.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

public class ActivityTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private String fileName = "yolocast/createActivity";
    private static String scene = "活动列表";
    private static HashMap<String, String> headers =new HashMap<>();

    public static ArrayList<String> KeyExistList = new ArrayList<>(Arrays.asList("videoId", "url", "covers", "title", "dataTime", "duration")); //关键字段
    public static ArrayList<String> KeyList = new ArrayList<>(Arrays.asList("videoId", "url", "default", "title", "dataTime", "duration")); //关键字段

    private static String activityId1;
    private static String activityId2;

    @BeforeClass
    public static void setUp() {
        System.out.println("执行登录获取cookie操作");
        Common.Cookies = LoginUtil.loginCookie(Common.yolocastUrl + Common.loginUri, Common.loginYolocastEmail, Common.loginYolocastPassword);
    }


    /**
     *
     * @author zhangsc
     * @date 2022/5/1 下午12:52
     * 获取预约活动列表
     */
    @Test(description = "获取前几个预约活动的activityId")
    public void getScheduleEventsList(){

        //拼接最终的测试地址
        //String uri = bundle.getString("ScheduleEventsList.uri");
        //String ScheduleEventsListUrl = this.yolocastUrl + uri;
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("pageNo", 1);
        map.put("pageSize", 10);
        map.put("status", 3);
        //headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        String body = map.toString();
        String scheduleEventsListUrl = Common.yolocastUrl+Common.scheduleEventsListuri;
        String result;

        cn.hutool.json.JSONObject jsonresult = new JSONObject();


        try{
            //发送get请求并接收响应数据
            result= HttpUtil.createGet(scheduleEventsListUrl).addHeaders(headers).form(map).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, scheduleEventsListUrl, scene);
        }catch (NoSuchElementException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), scheduleEventsListUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }
        /********************************************************************接口可用性检查结束********************************************************************/


        /* 获取data里items[0]里的activityId
        String data = jsonresult.get("data").toString();
        System.out.println(data);//等同于 jsonresult.get("data")

        cn.hutool.json.JSONObject datajson = new JSONObject(data);
        System.out.println(datajson);

        String items = datajson.get("items").toString();
        System.out.println(items);

        JSONArray jsonArray =new JSONArray(datajson.get("items"));
        String firstActivity = jsonArray.get(0).toString();
        System.out.println(firstActivity);

        cn.hutool.json.JSONObject firstActivityjson = new JSONObject(firstActivity);
        activityId1 = firstActivityjson.get("activityId").toString();

         */

        //获取预约活动前两个activityId
        activityId1 = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("items"))).get(0))).get("activityId").toString();
        activityId2 = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("data"))).get("items"))).get(1))).get("activityId").toString();


    }



    @Test(description = "创建obs推流的活动")
    public void createEvent(){

        //存放参数
        com.alibaba.fastjson.JSONObject param = GetCaseUtil.getAllCases(fileName);
        param.put("startTime",Config.getTimestampAfterTenMinutes);
        param.put("endTime",Config.getTimestampAfterFortyMinutes);
        param.put("title","Create by"+DateUtil.getSysdateStr());

        String body = param.toString();
        String createUrl = Common.yolocastUrl+Common.activityCreateuri;

        //存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        String result;
        cn.hutool.json.JSONObject jsonresult = new JSONObject();

        try {
            result= HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, createUrl, scene);


        }catch (NoSuchElementException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), createUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }
        /********************************************************************接口可用性检查结束********************************************************************/

        try{
            Assert.assertEquals(true, StrUtil.isNotEmpty(jsonresult.get("data").toString()),String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), createUrl, body, jsonresult));

        }catch (Exception e){
            logger.error("data不为true");

        }



    }



    @Test
    public void checkData(){

        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        String checkEventUrl = Common.yolocastUrl+Common.activityCreateuri + "908978164045189120";
        String body =null;
        String result;
        cn.hutool.json.JSONObject jsonresult = new JSONObject();


        try{
            //发送get请求并接收响应数据
            result= HttpUtil.createGet(checkEventUrl).addHeaders(headers).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, checkEventUrl, scene);
        }catch (NoSuchElementException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), checkEventUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }

        JSONObject data = jsonresult.getJSONObject("data");
        /*
         *
         * @author zhangsc
         * @date 2022/6/13 下午1:36

        data.forEach(json -> {
            CommonUtil.filedExistDet(collector, KeyExistList, (json), checkEventUrl, body);
            //字段非空校验
            CommonUtil.keyDataDet(collector, KeyList, (json), checkEventUrl, body);
        });
         */




    }

    @Test
    public void emptyTest(){


    }



}

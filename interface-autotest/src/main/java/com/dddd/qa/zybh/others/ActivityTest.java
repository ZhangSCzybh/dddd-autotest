package com.dddd.qa.zybh.others;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.BaseTest;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class ActivityTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private String fileName = "dddd/createActivity";
    private static String business = "福粒运营";
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
            CommonUtil.assertAvailable(jsonresult, body, scheduleEventsListUrl, business,scene);
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
            CommonUtil.assertAvailable(jsonresult, body, createUrl,business, scene);


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
            CommonUtil.assertAvailable(jsonresult, body, checkEventUrl, business, scene);
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
    public void getToken() throws IOException {
        String phone =  "17858805009";
        JSONObject param = JSONUtil.createObj();
        param.put("phone", phone);
        param.put("type", "2");

        String smsUrl = "http://hrtest.ddingddang.com/api/enterprise/account/sms";
        String body = param.toString();
        String result = HttpUtil.createPost(smsUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        String smsCode = (String) jsonresult.get("result");
        System.out.println(smsCode);

        JSONObject param2 = JSONUtil.createObj();
        param2.put("code", smsCode);
        param2.put("identityType", "3");
        param2.put("username", phone);

        String loginUrl = "http://hrtest.ddingddang.com/api/enterprise/account/login";
        String body2 = param2.toString();
        String result2 = HttpUtil.createPost(loginUrl).addHeaders(headers).body(body2).execute().body();
        JSONObject jsonresult2 = new JSONObject(result2);
        System.out.println("*********************"+jsonresult2);
        String name = jsonresult2.getStr("msg");
        System.out.println("zheshmsg:"+ name);
        //jsonobject中嵌套了json对象
        JSONObject addJsonObject = jsonresult2.getJSONObject("result");
        String nickName = addJsonObject.getStr("nickName");
        System.out.println(nickName);

    }

}
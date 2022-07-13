package com.yolocast.qa.zsc.ApiTest.SourceTest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yolocast.qa.zsc.Constant.Common;
import com.yolocast.qa.zsc.utils.ErrorEnum;
import com.yolocast.qa.zsc.ApiTest.SettingTest.loginTest;
import com.yolocast.qa.zsc.BaseTest;
import com.yolocast.qa.zsc.Constant.CommonUtil;
import com.yolocast.qa.zsc.Constant.Config;
import com.yolocast.qa.zsc.utils.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;


public class SourceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static JSONObject param;
    private static String scene = "source页面";
    private static String deletesourceId;
    private static String updatesourceId;

/*
    @Test
    public void addSource(){
        param = JSONUtil.createObj();
        param.put("name", "create"+Config.getSysdateStr);
        param.put("description", "create by" + Config.getSysdateStr);

        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);
        //发送post请求并接收响应数据
        String result= HttpUtil.createPost(Common.yolocastUrl+Common.sourceListuri).addHeaders(headers).body(param).execute().body();
        logger.info("添加Source接口返回信息：" + result);

        cn.hutool.json.JSONObject resultJson = new cn.hutool.json.JSONObject(result);
        String message = (String) resultJson.get("message");
        //判断预期结果和实际运行返回的结果
        Assert.assertEquals("successful",message);
    }

 */

    @BeforeClass
    public static void setUp() {
        System.out.println("执行登录获取cookie操作");
        Common.Cookies = LoginUtil.loginCookie(Common.yolocastUrl + Common.loginUri, Common.loginYolocastEmail, Common.loginYolocastPassword);
    }


    //@Test(dependsOnMethods = {"addSource"})
    @Test(description = "获取前两个SourceId",groups = "source")
    public void getSourceList(){


        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        String  soureListUrl = Common.yolocastUrl+Common.sourceListuri;
        //发送get请求并接收响应数据
        String result;

        try{
            result= HttpUtil.createGet(soureListUrl).addHeaders(headers).execute().body();
            cn.hutool.json.JSONObject Jsonresult = new cn.hutool.json.JSONObject(result);
            //发送post请求并接收响应数据
            //String result= HttpUtil.createPost(url).addHeaders(headers).form(map).execute().body();
            //logger.info("获取Source列表接口返回信息：" + result);
            CommonUtil.assertAvailable(Jsonresult, null, soureListUrl, scene);
        }catch(NullPointerException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), soureListUrl, null, "NullPointerException");
            Assert.fail(wrong);

        }

        /********************************************************************接口可用性检查结束********************************************************************/

        cn.hutool.json.JSONObject resultJson = new cn.hutool.json.JSONObject(HttpUtil.createGet(soureListUrl).addHeaders(headers).execute().body());

        //data不为空
        //Assert.assertTrue(StrUtil.isNotBlank(resultJson.get("data").toString()));

        JSONArray jsonArraydata = new JSONArray(resultJson.get("data"));
        try{

            //判断source列表是否大于3个，小于三个
            if(jsonArraydata.size() < 3){
                //logger.info(String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISLACK.getMsg(), Common.yolocastUrl+Common.sourceListuri, param, "Source列表个数小于3个"));

                caveat(String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISMissingQUANTITY.getMsg(),Common.yolocastUrl+Common.sourceListuri, param,resultJson)+"\n Source列表个数小于3个，触发新增source接口");

                for (int i=jsonArraydata.size(); i < 3; i++){
                    param = JSONUtil.createObj();
                    param.put("name", "create"+Config.getSysdateStr);
                    param.put("description", "create by" + Config.getSysdateStr);
                    //发送post请求并接收响应数据
                    String addSourceResult= HttpUtil.createPost(Common.yolocastUrl+Common.sourceListuri).addHeaders(headers).body(param).execute().body();
                }

            }else{
                cn.hutool.json.JSONObject listResultJson = new cn.hutool.json.JSONObject(HttpUtil.createGet(soureListUrl).addHeaders(headers).execute().body());
                caveat(String.format(Config.common_message, Common.yolocastUrl+Common.sourceListuri, null, "Source个数大于3个!" + listResultJson));

            }

            //获取source列表前两个sourceId
            deletesourceId = (String) new JSONObject(jsonArraydata.get(0).toString()).get("sourceId");
            updatesourceId = (String) new JSONObject(jsonArraydata.get(1).toString()).get("sourceId");

        }catch(NoSuchElementException e1){

        }



    }



/**
 * 
 * @author zhangsc
 * @date 2022/4/25 下午6:04  
 */
    @Test(dependsOnMethods = {"getSourceList"})
    public void deleteFirstSource(){

        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        String deleteUrl = Common.yolocastUrl+Common.sourceListuri + "?sourceId=" + deletesourceId;
        String body =null;


        String result;
        cn.hutool.json.JSONObject jsonresult = new JSONObject();
        try{
            //发送delete请求并接收响应数据
            result= HttpUtil.createRequest(Method.DELETE, deleteUrl).addHeaders(headers).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, deleteUrl, scene);

        }catch(NullPointerException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), deleteUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }

        /********************************************************************接口可用性检查结束********************************************************************/


        try{
            Assert.assertEquals(true, jsonresult.get("data"),String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), deleteUrl, body, jsonresult));

        }catch (Exception e){
            logger.error("data不为true");

        }

    }


    
    
    /**
     *
     * @author zhangsc
     * @date 2022/4/25 下午6:02
     */
    @Test(dependsOnGroups = {"source"})
    public void updateSource (){
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        param = JSONUtil.createObj();
        param.put("sourceId", updatesourceId);
        param.put("name", "update by "+ updatesourceId);
        param.put("description", "update+" + Config.getSysdateStr);

        String body = param.toString();

        String result;
        cn.hutool.json.JSONObject jsonresult = new JSONObject();


        try{
            //发送PUT请求并接收响应数据
            result= HttpUtil.createRequest(Method.PUT, Common.yolocastUrl+Common.sourceListuri).addHeaders(headers).body(param).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, Common.yolocastUrl+Common.sourceListuri, scene);

        }catch (NoSuchElementException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), Common.yolocastUrl+Common.sourceListuri, body, "NullPointerException");
            Assert.fail(wrong);
        }
        /********************************************************************接口可用性检查结束********************************************************************/

        try{
            Assert.assertEquals(true, StrUtil.isNotBlank(jsonresult.get("data").toString()),String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), Common.yolocastUrl+Common.sourceListuri, body, jsonresult));

        }catch (Exception e){
            logger.error("data不为true");

        }


    }

    
    /** 
     * 
     * @author zhangsc
     * @date 2022/4/25 下午7:00  
     */
    @Test(dependsOnGroups ={"source"} )
    public void updateSourceStreamKey(){
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("cast-auth", Common.Cookies);

        param = JSONUtil.createObj();
        param.put("sourceId", updatesourceId);


        String body = param.toString();
        String updateStreamKeyUrl = Common.yolocastUrl+Common.sourceResetKeyuri + updatesourceId ;

        String result;
        cn.hutool.json.JSONObject jsonresult = new JSONObject();


        try{
            //发送PUT请求并接收响应数据
            result= HttpUtil.createRequest(Method.PUT, updateStreamKeyUrl).addHeaders(headers).execute().body();
            jsonresult = new cn.hutool.json.JSONObject(result);
            CommonUtil.assertAvailable(jsonresult, body, updateStreamKeyUrl, scene);
        }catch (NoSuchElementException e){
            String wrong = String.format(Config.availableInfo, Config.Pro, scene, ErrorEnum.ISFAILED.getMsg(), updateStreamKeyUrl, body, "NullPointerException");
            Assert.fail(wrong);
        }

        /********************************************************************接口可用性检查结束********************************************************************/

        try{
            Assert.assertEquals(true, jsonresult.get("data"),String.format(Config.result_message, Config.Pro, scene, ErrorEnum.ISEMPTY.getMsg(), Common.yolocastUrl+Common.sourceListuri, body, jsonresult));

        }catch (Exception e){
            logger.error("data不为true");

        }


    }

}

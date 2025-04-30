package com.dddd.qa.zybh.ApiTest.YGPCTest;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.ApiTest.SourceTest.SourceTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import com.dddd.qa.zybh.utils.LoginUtil;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.dddd.qa.zybh.BaseTest.caveat;

/**
 * @author zhangsc
 * @date 2025年04月01日 17:46:12
 * @packageName com.dddd.qa.zybh.ApiTest.YGPCTest
 * @className ApprovalTest
 * @describe 邀请供应商审批
 */
public class ApprovalTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    private static final String scene = "审批模块";
    private static String approvalEmployeeId;
    private static final int TIMEOUT_MINUTES = 4;

    @BeforeClass
    public static void setUp() {
        Common.DDingDDangPCToken = LoginUtil.loginYGPCToken(Common.zhicaiYgUrl + Common.loginDDingDDangYGPCUri , Common.loginDDingDDangYGPCInfo);
        logger.info("执行登录获取智采员工pc平台的token：" + Common.DDingDDangPCToken);

        Common.DDingDDangToken = LoginUtil.loginDingdangZCToken(Common.zhicaiHrUrl + Common.loginDDingDDangUri , Common.loginDDingDDangInfo );
        logger.info("执行登录获取智采企业平台的token：" + Common.DDingDDangToken);

    }

    @Test(dependsOnMethods = {"com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest.EnterpriseSelfsupplierTest.approvalList"}, description = "员工pc端获取邀请供应商的审批单，依赖自建供应商新建/邀请")
    public void approvalPendingList(){
        Instant startTime = Instant.now(); // 记录开始时间
        boolean found = false; // 标记是否找到目标值
        while (Duration.between(startTime, Instant.now()).toMinutes() < TIMEOUT_MINUTES){
            logger.info("查看供应商那边传过来的approvalNo:{}", Config.approvalNo);
            JSONObject param = JSONUtil.createObj();
            param.put("status", 1);
            param.put("page", 1);
            param.put("pageSize", 100);
            String createUrl = Common.zhicaiYgUrl+ Common.approvalPendingUri;
            headers.put("session-token", Common.DDingDDangPCToken);
            String result = HttpUtil.createGet(createUrl).addHeaders(headers).form(param).execute().body();
            JSONObject jsonresult = new JSONObject(result);
            //接口可行性
            CommonUtil.assertAvailable(jsonresult, null, createUrl, Config.YGPCPro, scene);
            //获取审批数组长度
            String data = jsonresult.get("result").toString();
            JSONObject datajson = new JSONObject(data);
            JSONArray jsonArray =new JSONArray(datajson.get("list"));
            int length = jsonArray.toArray().length;
            for(int i = 0; i < length; i++) {
                String approvalNoZuiXin = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("approvalNo").toString();
                if (Config.approvalNo.equals(approvalNoZuiXin)) {
                    approvalEmployeeId = (new JSONObject((new JSONArray((new JSONObject(jsonresult.get("result"))).get("list"))).get(i))).get("id").toString();
                    logger.info("获取到的审批编号和id: approvalNo={}, id={}", approvalNoZuiXin, approvalEmployeeId);
                    found = true;
                    break;
                }
            }
            if (found){
                break;
            }
            // 等待一段时间后重试
            try {
                Thread.sleep(10 * 1000); // 等待 10 秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("线程被中断", e);
            }
        }
        // 如果超时仍未找到目标值，标记测试失败
        if (!found) {
            Assert.fail("在 " + TIMEOUT_MINUTES + " 分钟内未找到目标 approvalNo: " + null);
        }
    }
    @Test(dependsOnMethods = {"approvalPendingList"},description = "审批通过邀请供应商")
    public void processHandle(){
        JSONObject param = JSONUtil.createObj();
        param.put("approvalEmployeeId", approvalEmployeeId);
        param.put("status", 2);
        String body = param.toString();
        String createUrl = Common.zhicaiYgUrl + Common.approvalProcessHandleUri;
        headers.put("session-token", Common.DDingDDangPCToken);
        String result = HttpUtil.createPost(createUrl).addHeaders(headers).body(body).execute().body();
        JSONObject jsonresult = new JSONObject(result);
        logger.info("供应商审核通过" + result);
        caveat("供应商审核通过" + result);

        //接口可行性
        CommonUtil.assertAvailable(jsonresult, body, createUrl, Config.YGPCPro, scene);
    }

}

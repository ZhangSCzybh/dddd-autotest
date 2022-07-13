package com.yolocast.qa.zsc;

import com.google.common.collect.Lists;
import com.yolocast.qa.zsc.ApiTest.SettingTest.loginTest;
import com.yolocast.qa.zsc.Constant.Common;
import com.yolocast.qa.zsc.Constant.Config;
import com.yolocast.qa.zsc.utils.DingTalkRobotUtil;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangsc
 * @date 2022-04-26 下午3:57
 */
public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    public static List<String> attchments = Lists.newArrayList();

    @Rule
    public ErrorCollector collector = new ErrorCollector();


    public static void setUp() {
        try {
            addAttch();
        } catch (NullPointerException e) {
            logger.error("信息初始化失败");
            Assert.fail("信息初始化失败");
        } catch (Exception e) {
            logger.error("信息初始化失败"+e.getMessage());
            Assert.fail("信息初始化失败"+e.getMessage());
        }

    }
    //报警人添加
    public static void addAttch() {
        attchments.add(Config.zsc);
    }


    public static void caveat(String message) {
        setUp();
        try {
            DingTalkRobotUtil.sendMessageText(Common.privateDingtalkUrl,
                    message,
                    "text",
                    attchments,false);
        } catch (IOException e) {
            logger.error("报警出错"+e.getMessage());
        }
    }

}

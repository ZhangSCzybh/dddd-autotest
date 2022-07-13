package com.yolocast.qa.zsc.Constant;

import com.yolocast.qa.zsc.ApiTest.SettingTest.loginTest;
import com.yolocast.qa.zsc.utils.DateUtil;
import com.yolocast.qa.zsc.utils.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangsc
 * @date 2022-04-27 下午7:55
 */
public class JDBCTest {

    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);

    @Test(description = "统计盒子用户的场均时长")
    public void boxUserAverageTime() throws SQLException {
        Connection conn = JdbcUtil.getConn();

        try{
            Statement stat = conn.createStatement();
            //不自动提交
            conn.setAutoCommit(false);

            //String sql_update = " insert into user (name,age) values(\"张三\",100) ";
            //stat.executeUpdate(sql_update);
            String sql_selete = " select yolo_activity.activity.start_time,yolo_activity.activity.end_time from yolo_activity.activity ";

            //String sql_selete = " SELECT a.org_id,a.activity_id,a.title,a.start_time,a.end_time\n" +
            //        "FROM yolo_activity.activity a INNER JOIN yolo_usercenter.org b \n" +
            //        "ON a.org_id=b.org_id and b.type = 99 ";

            ResultSet re = stat.executeQuery(sql_selete);

            int timeSum = 0;
            int num = 0;
            while(re.next()){
                String start_time = re.getString("start_time");
                String end_time = re.getString("end_time");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    Date startDate = sdf.parse(start_time);
                    Date endDate = sdf.parse(end_time);
                    String timeDifference = DateUtil.convert(startDate, endDate);
                    logger.info("单场活动时长："+timeDifference);

                    timeSum = timeSum+Integer.parseInt(timeDifference);
                    num++;
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("日期格式化失败");
                }

            }

            System.out.println("总活动时长：" + timeSum + "s" + " 总活动场数：" + num);
            int avg = timeSum / num;
            int mins = avg / 60;
            int sec = avg % 60;
            System.out.println("平均时长:" + mins + "m" + sec +"s");
           // conn.commit(); //提交
            JdbcUtil.close();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }

    }

    @Test
    public void trafficData(){

    }

    }

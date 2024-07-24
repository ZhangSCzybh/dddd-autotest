package com.dddd.qa.zybh;

import cn.hutool.json.JSONObject;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.Constant.CommonUtil;
import com.dddd.qa.zybh.Constant.Config;
import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.ErrorEnum;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.apache.http.conn.util.PublicSuffixList;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangsc
 * @date 2024年07月17日 11:41:00
 * @packageName com.dddd.qa.zybh
 * @className Testweeks
 * @describe TODO
 */
public class Testweeks {
    private static String skuListfile = "dddd/skuList";
    private static String orderProdDetails;
    private static final String[] array1 = {"9147280", "9147301", "9147304", "9147239"};
    private static final String[] array2 = {"9147167", "9147172", "9147194", "9147221", "9147420", "9147423", "9147173", "9147205", "9147224", "9147232", "9147251", "9147254", "9147211", "9147346", "9147354", "9147268", "9147283", "9147300"};
    private static final String[] array3 = {"9147309", "9147239", "9147343", "9147346", "9147268", "9147274", "9147280", "9147301", "9147304"};
    private static final String[] array4 = {"9147325", "9147417", "9147418", "9147422", "9147424", "9147253", "9147254", "9147255", "9147257", "9147262", "9147357", "9147292", "9147295", "9147298", "9147302"};
    private static final String[] array5 = {"9147305", "9147309", "9147419", "9147215", "9147240", "9147248", "9147347", "9147357", "9147269", "9147277", "9147297"};


    private static String[] selectArrayByDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                orderProdDetails = "dddd/createProdOrder11";
                return array1;
            case TUESDAY:
                orderProdDetails = "dddd/createProdOrder12";
                return array2;
            case WEDNESDAY:
                orderProdDetails = "dddd/createProdOrder13";
                return array3;
            case THURSDAY:
                orderProdDetails = "dddd/createProdOrder14";
                return array4;
            case FRIDAY:
                orderProdDetails = "dddd/createProdOrder15";
                return array5;
            // 如果是周六或周日，你可以选择默认数组或抛出异常
            case SATURDAY:
            case SUNDAY:
                throw new IllegalStateException("Arrays not defined for Saturday or Sunday");
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }

    public static void main(String[] args) throws Exception {


        String csvFilePath = "/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/dddd/111.csv"; // 替换为你的CSV文件路径
        List<String> dataRows = readCSV(csvFilePath);

        // 获取当前的星期几
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        int index = dayOfWeek.getValue() - 1; // DayOfWeek的值从1（星期一）开始，而List的索引从0开始

        // 根据星期几的索引选择数据
        if (index >= 0 && index < dataRows.size()) {
            String selectedRow = dataRows.get(index);
            System.out.println("Selected row for today: " + selectedRow);
        } else {
            System.out.println("No data available for today.");
        }

        File file = new File("interface-autotest/1.text");
        System.out.println(file.getAbsolutePath());
        //GetCaseUtil.getAllCases(skuListfile);
        //System.out.println(GetCaseUtil.getAllCases(skuListfile));
        ///Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/dddd/skuList.json

    }

    @DataProvider(name = "supplierTokenProvider")
    public Object[][] supplierTokenFromCSV() {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/dddd/111.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("num")) { // 跳过标题行
                    String[] values = line.split(",");
                    data.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "supplierTokenProvider")
    public void testUserData(String num, String token) {
        System.out.println("Testing num: " + num + ", token: " + token);
        //System.out.println("Testing num: " + num + ", token: " + token );
        // 在这里进行你的测试逻辑

    }

    //获取csv文件数据
    public static List<String> readCSV(String path) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
package com.dddd.qa.zybh.others;

/**
 * @author zhangsc
 * @date 2025年09月22日 15:12:32
 * @packageName com.dddd.qa.zybh.others
 * @className PriceCalculator
 * @describe TODO
 */
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dddd.qa.zybh.ApiTest.SettingTest.loginTest;
import com.dddd.qa.zybh.Constant.Common;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PriceCalculator {
    private static final Logger logger = LoggerFactory.getLogger(loginTest.class);
    private static final HashMap<String, String> headers =new HashMap<>();
    //计算毛利率
    /**
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择计算方式：");
        System.out.println("1. 固定毛利率计算公式");
        System.out.println("2. 毛利率折扣计算公式");
        System.out.println("3. 指导价折扣计算公式");

        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("错误：请选择有效的选项（1/2/3）");
            scanner.close();
            return;
        }

        double salePrice = 0.0;

        switch (choice) {
            case 1:
                System.out.print("请输入成本价（元）: ");
                double costPrice = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("请输入固定毛利率（%）: ");
                double grossMargin = Double.parseDouble(scanner.nextLine().trim()) / 100.0; // 转换为小数

                // 计算公式: 销售价 = 成本价 / (1 - 毛利率)
                salePrice = costPrice / (1 - grossMargin);
                System.out.printf("销售价 = %.2f 元\n", salePrice);
                break;

            case 2:
                System.out.print("请输入成本价（元）: ");
                costPrice = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("请输入毛利率（%）: ");
                grossMargin = Double.parseDouble(scanner.nextLine().trim()) / 100.0;

                System.out.print("请输入折扣率（%）: ");
                double discountRate = Double.parseDouble(scanner.nextLine().trim()) / 100.0;

                // 计算公式: 销售价 = 成本价 / (1 - 毛利率 * 折扣率)
                salePrice = costPrice / (1 - grossMargin * discountRate);
                System.out.printf("销售价 = %.2f 元\n", salePrice);
                break;

            case 3:
                System.out.print("请输入指导价（元）: ");
                double guidePrice = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("请输入折扣率（%）: ");
                discountRate = Double.parseDouble(scanner.nextLine().trim()) / 100.0;

                // 计算公式: 销售价 = 指导价 * 折扣率
                salePrice = guidePrice * discountRate;
                System.out.printf("销售价 = %.2f 元\n", salePrice);
                break;

            default:
                System.out.println("错误：请选择有效的选项（1/2/3）");
        }

        scanner.close();
    }

    */

    //sgnet商品导入
    @DataProvider(name = "sgnetSKU")
    public Object[][] supplierTokenFromCSV() {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/test-dddd/sku.csv"))) {
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

    @Test(dataProvider = "sgnetSKU")
    public void GoodsImport(String skuCode){
        //对指定的sku进行批量同步
        JSONObject param2 = JSONUtil.createObj();
        param2.put("skuCode", skuCode);
        String createUrl2 = "https://backdev.lixiangshop.com/admin/jdManagement/goodsImport";
        headers.put("fuli-cache", "a3diadCE5pEEee9neA7w9QAm2t0V8Q4W951w6N30dd7W7Q5k7hCG3TFGcV0Obc332FEo3QXBoQkhNTS9saFRzQ21uMS9sNUVOYm9razZocUtJendqdURXZDZwWkhTU1F6NWQyeFhtUmNoa2dkMnd2ZmFzSTJFZ2I4N0pkeGY4ZGFVajQ9");
        String result2= HttpUtil.createGet(createUrl2).addHeaders(headers).form(param2).execute().body();
        logger.info("商品导入：" + result2);
    }
}

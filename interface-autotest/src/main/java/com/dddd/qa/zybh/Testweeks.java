package com.dddd.qa.zybh;

import com.dddd.qa.zybh.utils.DateUtil;
import com.dddd.qa.zybh.utils.GetCaseUtil;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author zhangsc
 * @date 2024年07月17日 11:41:00
 * @packageName com.dddd.qa.zybh
 * @className Testweeks
 * @describe TODO
 */
public class Testweeks {
    private static String orderProdDetails;
    private static final String[] array1 = {"9147280", "9147301", "9147304", "9147239"};
    private static final String[] array2 = {"9147167", "9147172", "9147194", "9147221","9147420","9147423","9147173","9147205","9147224","9147232","9147251","9147254","9147211","9147346","9147354","9147268","9147283","9147300"};
    private static final String[] array3 = {"9147309", "9147239", "9147343", "9147346","9147268","9147274","9147280","9147301","9147304"};
    private static final String[] array4 = {"9147325", "9147417", "9147418", "9147422","9147424","9147253","9147254","9147255","9147257","9147262","9147357","9147292","9147295","9147298","9147302"};
    private static final String[] array5 = {"9147305", "9147309", "9147419", "9147215","9147240","9147248","9147347","9147357","9147269","9147277","9147297"};



    private static String[] selectArrayByDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                orderProdDetails="dddd/createProdOrder11";
                return array1;
            case TUESDAY:
                orderProdDetails="dddd/createProdOrder12";
                return array2;
            case WEDNESDAY:
                orderProdDetails="dddd/createProdOrder13";
                return array3;
            case THURSDAY:
                orderProdDetails="dddd/createProdOrder14";
                return array4;
            case FRIDAY:
                orderProdDetails="dddd/createProdOrder15";
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
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 获取当前是星期几
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        // 根据星期几选择对应的数组
        String[] selectedArray = selectArrayByDay(dayOfWeek);

        // 输出数组内容
        for (String skuid : selectedArray) {
            System.out.println(skuid);
        }



        System.out.println( DateUtil.getTodayCurrent());
        String number = "77326728723";
        String token = "Y2pmZDBdbi2adN0T7h7ZcaDD3R0jeMCm6t2adMAG3pFX9Y7WbU9zbR3n0BA42W8FfQF48aVdBMUdRMVRnQU5KNmtOTFU4QTFKL2g5VjNkVGhweE5jeUN6TXo3WmtFUTZlWno5czNTMzdaaVc0Uy9TL0xDdUEzUjdReW9YdXpyRUVxY21CNVlWdjIrVHozVy8yOEp1dXhVY3dtWHJoNjl1aXBwOHpjT0pKVmJlMDdhR3hVa1lzeE1XME5ubUpJb2VMWVBVPQ";
        GetCaseUtil.sendPostRequest("http://cardback.ddingddang.com/supplieradmin/order/ship/" + number , token);

    }
}

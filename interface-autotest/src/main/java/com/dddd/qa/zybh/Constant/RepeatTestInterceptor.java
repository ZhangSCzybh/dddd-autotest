package com.dddd.qa.zybh.Constant;


import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsc
 * @date 2025年08月15日 21:50:56
 * @packageName com.dddd.qa.zybh.Constant
 * @className RepeatTestInterceptor
 * @describe TODO
 */
public class RepeatTestInterceptor implements IMethodInterceptor {

    // 设置重复次数
    private static final int REPEAT_COUNT = 5;

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> result = new ArrayList<>();
        for (IMethodInstance method : methods) {
            for (int i = 0; i < REPEAT_COUNT; i++) {
                result.add(method);
            }
        }
        return result;
    }
}

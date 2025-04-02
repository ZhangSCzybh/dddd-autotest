package com.dddd.qa.zybh.Constant;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * @author zhangsc
 * @date 2025年04月02日 17:10:32
 * @packageName com.dddd.qa.zybh.Constant
 * @className DelayListener
 * @describe TODO
 */
public class DelayListener implements ITestListener {
    private boolean isClassACompleted = false;

    @Override
    public void onTestSuccess(ITestResult result) {
        if (result.getTestClass().getName().equals("com.dddd.qa.zybh.ApiTest.FuliSelfSupplierTest.EnterpriseSelfsupplierTest")) {
            System.out.println("EnterpriseSelfsupplierTest completed successfully.");
            isClassACompleted = true;
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    @Override
    public void onTestStart(ITestResult result) {
        if (result.getTestClass().getName().equals("com.dddd.qa.zybh.ApiTest.YGPCTest.ApprovalTest") && isClassACompleted) {
            try {
                System.out.println("ApprovalTest - Waiting for 4 minutes...");
                Thread.sleep(2 * 60 * 1000); // 延迟 4 分钟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

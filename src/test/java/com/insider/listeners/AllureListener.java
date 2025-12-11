package com.insider.listeners;

import com.insider.base.BaseTest;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

public class AllureListener implements ITestListener {

    private static final Logger logger = Logger.getLogger(AllureListener.class.getName());

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("[ALLURE] onTestFailure triggered for: " + result.getMethod().getMethodName());

        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            logger.warning("[ALLURE] Test instance is not BaseTest, skipping screenshot");
            return;
        }

        WebDriver driver = baseTest.getDriver();
        if (driver == null) {
            logger.warning("[ALLURE] WebDriver is null, cannot take screenshot");
            return;
        }

        attachScreenshot(result.getMethod().getMethodName(), driver);
    }

    private void attachScreenshot(String testName, WebDriver driver) {
        try {
            logger.info("[ALLURE] Capturing screenshot for failed test: " + testName);
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment("Failure screenshot - " + testName,
                    new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            logger.warning("[ALLURE] Failed to capture screenshot: " + e.getMessage());
        }
    }
}

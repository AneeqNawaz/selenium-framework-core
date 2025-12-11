package com.insider.base;

import com.insider.config.ConfigManager;
import com.insider.driver.DriverFactory;
import com.insider.listeners.AllureListener;
import com.insider.pages.*;
import com.insider.testdata.CareersPageData;
import com.insider.testdata.HomePageData;
import com.insider.testdata.LeverQaJobsData;
import com.insider.utils.TestDataLoader;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

@Listeners({AllureListener.class, AllureTestNg.class})
public class BaseTest {

    protected WebDriver driver;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected HomePage homePage;
    protected HomePageData homeData;

    protected CareersPage careersPage;
    protected CareersPageData careersData;

    protected LeverQaJobsPage qaJobsPage;
    protected LeverQaJobsData leverQaData;

    protected LeverJobDetailPage leverJobDetailPage;
    protected LeverApplyJobPage leverApplyPage;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = ConfigManager.getBrowser();
        logger.info("[SETUP] Starting browser: " + browser);

        driver = DriverFactory.createDriver();

        homePage = new HomePage(driver);
        homeData = TestDataLoader.load(
                "testdata/homepage.json",
                HomePageData.class);

        careersPage = new CareersPage(driver);
        careersData = TestDataLoader.load(
                "testdata/careerspage.json",
                CareersPageData.class
        );

        qaJobsPage = new LeverQaJobsPage(driver);

        leverJobDetailPage = new LeverJobDetailPage(driver);
        leverQaData = TestDataLoader.load(
                "testdata/leverQAjobs.json",
                LeverQaJobsData.class
        );

        leverApplyPage = new LeverApplyJobPage(driver);

        logger.info("[SETUP] Driver and page objects initialized");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            logger.info("[TEARDOWN] Quitting driver");
            driver.quit();
            logger.info("[TEARDOWN] Driver quit successfully");
        }
    }
}

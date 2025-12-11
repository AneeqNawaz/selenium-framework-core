package com.insider.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class BasePage {

    protected final WebDriver driver;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(10);

    // Cookie banner locators
    private final By cookieBar = By.id("cookie-law-info-bar");
    private final By acceptNecessaryButton = By.id("wt-cli-accept-btn");

    public BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver is null for " + getClass().getSimpleName());
        }
        this.driver = driver;
    }

    // ---------- Navigation ----------

    public void open(String url) {
        logger.info("[BasePage] Navigating to: " + url);
        driver.get(url);
        acceptCookiesIfPresent();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    // ---------- Generic actions ----------

    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected void click(By locator) {
        logger.info("[BasePage] Clicking element: " + locator);

        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        try {
            scrollIntoView(locator);
            element.click();
        } catch (ElementClickInterceptedException e) {
            logger.warning("[BasePage] Click intercepted, using JS click for: " + locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected boolean isVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void waitUntilVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void scrollToBottom() {
        logger.info("[BasePage] Scrolling to bottom of page");
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    protected void scrollIntoView(By locator) {
        logger.info("[BasePage] Scrolling element into view: " + locator);
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // ---------- Cookies ----------

    protected void acceptCookiesIfPresent() {
        logger.info("[COOKIE] Trying to handle cookie banner (up to 10s)");

        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);

            WebElement banner = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(cookieBar)
            );
            logger.info("[COOKIE] Banner is visible");

            WebElement acceptBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(acceptNecessaryButton)
            );
            logger.info("[COOKIE] Clicking 'Only Necessary'");
            acceptBtn.click();

            try {
                wait.until(ExpectedConditions.invisibilityOf(banner));
                logger.info("[COOKIE] Banner is now hidden after accepting");
            } catch (Exception ignore) {
                logger.info("[COOKIE] Banner still visible after click (maybe animation)");
            }

        } catch (TimeoutException e) {
            logger.info("[COOKIE] Banner not shown within timeout, skipping");
        } catch (Exception e) {
            logger.warning("[COOKIE] Cookie banner handling skipped due to exception: " + e.getMessage());
        }
    }
}

package com.insider.pages;

import com.insider.base.BasePage;
import com.insider.config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    // Simple "page loaded" element
    private final By header = By.tagName("header");

    // COMPANY heading
    private final By companySectionHeading = By.xpath(
            "//div[@class='footer-links']//h3[contains(text(),'COMPANY')]"
    );

    // All links under COMPANY
    private final By companyLinks = By.xpath(
            "//h3[text()='COMPANY']/ancestor::div[@class='footer-links-col-item']//a"
    );

    // "We're hiring" link under COMPANY
    private final By wereHiringLink = By.xpath(
            "//h3[text()='COMPANY']/ancestor::div[@class='footer-links-col-item']//a[@href='/careers/']"
    );

    public HomePage(WebDriver driver) {
        super(driver);
    }
    @Step("Open Insider home page")
    public void openHomePage() {
        logger.info("[HomePage] Opening home page");
        open(ConfigManager.getBaseUrl());
    }

    @Step("Wait for home page to be loaded")
    public void waitForHomePageLoaded() {
        waitUntilVisible(header);
    }

    @Step("Get current URL")
    public String currentUrl() {
        return getCurrentUrl();
    }

    @Step("Get current page title")
    public String currentTitle() {
        return getTitle();
    }

    @Step("Scroll to COMPANY section in the footer")
    public void scrollToCompanySection() {
        scrollIntoView(companySectionHeading);
    }

    @Step("Check if COMPANY section heading is visible in footer")
    public boolean isCompanySectionVisible() {
        return isVisible(companySectionHeading);
    }

    @Step("Verify all link texts under COMPANY section")
    public List<String> getCompanyLinksTexts() {
        List<WebElement> elements = driver.findElements(companyLinks);
        return elements.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    @Step("Click \"We're hiring\" link under COMPANY section")
    public void clickWereHiring() {
        click(wereHiringLink);
    }
}

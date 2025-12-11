package com.insider.pages;

import com.insider.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LeverJobDetailPage extends BasePage {

    private final By jobTitle       = By.cssSelector(".posting-header h2");
    private final By location       = By.cssSelector(".posting-header .posting-categories .location");
    private final By department     = By.cssSelector(".posting-header .posting-categories .department");
    private final By commitment     = By.cssSelector(".posting-header .posting-categories .commitment");
    private final By workplaceTypes = By.cssSelector(".posting-header .posting-categories .workplaceTypes");

    private final By applyForThisJobButton = By.cssSelector(".postings-btn-wrapper .postings-btn");

    public LeverJobDetailPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get job title on detail page")
    public String getJobTitle() {
        return driver.findElement(jobTitle).getText().trim();
    }

    @Step("Get location on detail page")
    public String getLocation() {
        return driver.findElement(location).getText().trim();
    }

    @Step("Get department on detail page")
    public String getDepartment() {
        return driver.findElement(department).getText().trim();
    }

    @Step("Get commitment on detail page (optional)")
    public String getCommitment() {

        var elements = driver.findElements(commitment);

        if (elements.isEmpty()) {
            logger.info("No commitment displayed on job detail page");
            return "";
        }

        String text = elements.get(0).getText().trim();
        logger.info("Job commitment on detail page: [" + text + "]");
        return text;
    }
    @Step("Get workplace type on detail page")
    public String getWorkplaceType() {
        return driver.findElement(workplaceTypes).getText().trim();
    }

    @Step("Check 'Apply for this job' button is visible")
    public boolean isApplyForThisJobButtonVisible() {
        return driver.findElement(applyForThisJobButton).isDisplayed();
    }

    @Step("Click 'Apply for this job' on detail page")
    public void clickApplyForThisJob() {
        driver.findElement(applyForThisJobButton).click();
    }
}

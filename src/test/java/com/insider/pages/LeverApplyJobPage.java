package com.insider.pages;

import com.insider.base.BasePage;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LeverApplyJobPage extends BasePage {

    private final By jobTitle       = By.cssSelector(".posting-header h2");
    private final By location       = By.cssSelector(".posting-header .posting-categories .location");
    private final By department     = By.cssSelector(".posting-header .posting-categories .department");
    private final By commitment     = By.cssSelector(".posting-header .posting-categories .commitment");
    private final By workplaceTypes = By.cssSelector(".posting-header .posting-categories .workplaceTypes");

    private final By submitYourApplicationHeading =
            By.xpath("(//form[@id='application-form']//h4)[1]");

    public LeverApplyJobPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get job title on apply page")
    public String getJobTitle() {
        return driver.findElement(jobTitle).getText().trim();
    }

    @Step("Get location on apply page")
    public String getLocation() {
        return driver.findElement(location).getText().trim();
    }

    @Step("Get department on apply page")
    public String getDepartment() {
        return driver.findElement(department).getText().trim();
    }

    @Step("Get commitment on apply page")
    public String getCommitment() {

        var elements = driver.findElements(commitment);

        if (elements.isEmpty()) {
            logger.info("No commitment displayed on apply page");
            return "";
        }

        String text = elements.get(0).getText().trim();
        logger.info("Job commitment on apply page: [" + text + "]");
        return text;
    }

    @Step("Get workplace type on apply page")
    public String getWorkplaceType() {
        return driver.findElement(workplaceTypes).getText().trim();
    }

//    @Step("Check 'Submit your application' section is visible")
//    public boolean isSubmitYourApplicationVisible() {
//        waitUntilVisible(submitYourApplicationHeading);
//
//        var headingEl = driver.findElement(submitYourApplicationHeading);
//        String text = headingEl.getText().trim();
//
//        logger.info("Apply form heading text: [" + text + "]");
//        boolean visible = headingEl.isDisplayed() && text.equals("Submit your application");
//        logger.info("'Submit your application' visible? " + visible);
//
//        return visible;
//    }

}

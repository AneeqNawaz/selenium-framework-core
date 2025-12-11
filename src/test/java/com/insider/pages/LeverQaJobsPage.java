package com.insider.pages;

import com.insider.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class LeverQaJobsPage extends BasePage {

    public LeverQaJobsPage(WebDriver driver) {
        super(driver);
    }

    // Top filters
    private final By filterBar            = By.cssSelector(".filter-bar");
    private final By filterButtonsWrapper = By.cssSelector(".filter-bar .filter-button-wrapper");
    private final By teamFilterButton     = By.cssSelector("div[role='button'][aria-label*='Filter by Team'] .filter-button-mlp");

    // Location filter (wrapper + option)
    private final By locationFilterWrapper = By.xpath("//div[contains(@aria-label,'Filter by Location:')]");

    // posting / list
    private final By postingsGroupTitle = By.cssSelector(".postings-group .posting-category-title");
    private final By postingCards       = By.cssSelector(".postings-group .posting");

    // posting details on listing
    private final By postingCard          = By.cssSelector(".postings-group .posting");
    private final By postingTitle         = By.xpath("//h5[@data-qa=\"posting-name\"]");
    private final By postingLocation      = By.cssSelector(".postings-group .location");
    private final By postingWorkPlaceType = By.cssSelector(".postings-group .workplaceTypes");
    private final By postingCommitment    = By.cssSelector(".postings-group .commitment");
    private final By postingTitleLink     = By.cssSelector(".postings-group  a.posting-title");
    private final By postingApplyBtn      = By.cssSelector(".postings-group a.posting-btn-submit");

    // ---------- Actions / assertions ----------

    @Step("Open Lever QA jobs page: {url}")
    public void openQaJobsPage(String url) {
        open(url);
        waitUntilVisible(filterBar);
        waitUntilVisible(postingCards);
    }

    @Step("Verify we are on QA jobs page URL")
    public boolean isOnQAJobsPage() {
        String url = driver.getCurrentUrl();
        return url.startsWith("https://jobs.lever.co/insiderone")
                && url.contains("team=Quality%20Assurance");
    }

    @Step("Verify top 4 filters are visible")
    public boolean areFiltersVisible() {
        var filters = driver.findElements(filterButtonsWrapper);
        if (filters.size() != 4) {
            return false;
        }
        return filters.stream().allMatch(WebElement::isDisplayed);
    }

    @Step("Check Team filter is selected as Quality Assurance")
    public boolean isTeamFilterSelectedAsQA() {

        WebElement teamFilter = driver.findElement(teamFilterButton);
        String text = teamFilter.getText().trim();
        boolean result = text.toLowerCase().contains("quality assurance");
        return result;
    }

    @Step("Select Location: {locationText}")
    public void selectLocation(String locationText) {
        driver.findElement(locationFilterWrapper).click();
        driver.findElement(By.linkText(locationText)).click();
        waitUntilVisible(postingCards);
    }




    @Step("Check posting group title is visible")
    public boolean isPostingGroupTitleVisible() {
        return driver.findElement(postingsGroupTitle).isDisplayed();
    }

    @Step("Get posting group title text")
    public String getPostingGroupTitleText() {
        String text = driver.findElement(postingsGroupTitle).getText().trim();
        logger.info("Posting group title text: [" + text + "]");
        return text;
    }

    @Step("Check posting card is visible")
    public boolean isPostingCardVisible() {
        return driver.findElement(postingCard).isDisplayed();
    }

    @Step("Get job title from list")
    public String getJobTitle() {
        return driver.findElement(postingTitle).getText().trim();
    }

    @Step("Get job location from list")
    public String getJobLocation() {
        return driver.findElement(postingLocation).getText().trim();
    }

    @Step("Get job work type from list")
    public String getJobWorkplaceType() {
        return driver.findElement(postingWorkPlaceType).getText().trim();
    }

    @Step("Get job commitment from list")
    public String getJobCommitment() {

        var elements = driver.findElements(postingCommitment);

        if (elements.isEmpty()) {
            logger.info("No commitment displayed on job listing card");
            return "";
        }

        String text = elements.get(0).getText().trim();
        logger.info("Job commitment on listing: [" + text + "]");
        return text;
    }

    @Step("Get job link href from list")
    public String getJobLinkHref() {
        return driver.findElement(postingTitleLink).getAttribute("href");
    }

    @Step("Check job Apply button is visible")
    public boolean isJobApplyButtonDisplayed() {
        return driver.findElement(postingApplyBtn).isDisplayed();
    }

    @Step("Click Apply button on first job card")
    public void clickJobApply() {
        driver.findElement(postingApplyBtn).click();
    }
    public record JobCardView(
            String title,
            String location,
            String workplaceType,
            String commitment,
            String detailHref,
            String applyHref
    ) {}

    @Step("Get all job cards on QA jobs listing")
    public List<JobCardView> getAllJobCards() {
        return driver.findElements(postingCards).stream()
                .map(card -> {
                    String title = card.findElement(By.cssSelector("h5[data-qa='posting-name']"))
                            .getText().trim();

                    String location = card.findElement(By.cssSelector(".location"))
                            .getText().trim();

                    String workType = "";
                    var wt = card.findElements(By.cssSelector(".workplaceTypes"));
                    if (!wt.isEmpty()) {
                        workType = wt.get(0).getText().trim();
                    }

                    String commitment = "";
                    var cm = card.findElements(By.cssSelector(".commitment"));
                    if (!cm.isEmpty()) {
                        commitment = cm.get(0).getText().trim();
                    }

                    String detailHref = card.findElement(By.cssSelector("a.posting-title"))
                            .getAttribute("href");

                    String applyHref = card.findElement(By.cssSelector(".posting-btn-submit"))
                            .getAttribute("href");

                    return new JobCardView(title, location, workType, commitment, detailHref, applyHref);
                })
                .collect(Collectors.toList());
    }
}

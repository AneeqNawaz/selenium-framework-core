package com.insider.pages;

import com.insider.base.BasePage;
import com.insider.config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;

import java.util.List;
import java.util.stream.Collectors;

public class CareersPage extends BasePage {

    // ---------- Life at Insider One section ----------

    private final By lifeSection = By.cssSelector(".insiderone-gallery-slider-container");
    private final By lifeTitle = By.cssSelector(".insiderone-gallery-slider-main-heading h2");
    private final By lifeSubtitle = By.cssSelector(".insiderone-gallery-slider-sub-heading p");
    private final By lifeSlides = By.cssSelector(".insiderone-gallery-slider-slides .swiper-slide");
    private final By activeLifeSlide = By.cssSelector(".insiderone-gallery-slider-slides .swiper-slide.swiper-slide-active");
    private final By lifeRightArrow = By.cssSelector(".insiderone-gallery-slider-right-arrow");
    private final By lifeLeftArrow = By.cssSelector(".insiderone-gallery-slider-left-arrow");

    public CareersPage(WebDriver driver) {
        super(driver);
    }

    // ---------- Navigation ----------

    @Step("Open Careers page")
    public void openCareersPage() {
        String url = ConfigManager.getBaseUrl() + "/careers/";
        logger.info("[CareersPage] Opening Careers page: " + url);
        open(url);
    }

    // ---------- Life at Insider One ----------

    @Step("Wait for 'Life at Insider One' section to be visible")
    public void waitForLifeSection() {
        scrollIntoView(lifeSection);
        waitUntilVisible(lifeSection);
    }

    @Step("Get 'Life at Insider One' title text")
    public String getLifeTitle() {
        return driver.findElement(lifeTitle).getText().trim();
    }

    @Step("Get 'Life at Insider One' subtitle text")
    public String getLifeSubtitle() {
        return driver.findElement(lifeSubtitle).getText().trim();
    }

    @Step("Get total count of Life at Insider slides")
    public int getLifeSlidesCount() {
        return driver.findElements(lifeSlides).size();
    }

    @Step("Check Life slider right arrow is displayed")
    public boolean isRightArrowDisplayed() {
        return isVisible(lifeRightArrow);
    }

    @Step("Check Life slider left arrow is displayed")
    public boolean isLeftArrowDisplayed() {
        return isVisible(lifeLeftArrow);
    }

    private WebElement getRightArrow() {
        return driver.findElement(lifeRightArrow);
    }

    private WebElement getLeftArrow() {
        return driver.findElement(lifeLeftArrow);
    }

    @Step("Click Life slider right arrow")
    public void clickLifeRightArrow() {
        getRightArrow().click();
    }

    @Step("Click Life slider left arrow")
    public void clickLifeLeftArrow() {
        getLeftArrow().click();
    }

    @Step("Check all Life slides have img with non-empty src")
    public boolean allLifeSlidesHaveNonEmptyImgSrc() {
        List<WebElement> slides = driver.findElements(lifeSlides);
        for (WebElement slide : slides) {
            WebElement img = slide.findElement(By.cssSelector(".insiderone-gallery-slider-item-img img"));
            String src = img.getAttribute("src");
            if (src == null || src.trim().isEmpty()) {
                logger.warning("[CareersPage] Found slide with empty img src");
                return false;
            }
        }
        return true;
    }

    @Step("Get count of active Life slides")
    public int getActiveLifeSlidesCount() {
        return driver.findElements(activeLifeSlide).size();
    }


    // ---------- Teams section ----------

    private final By teamsSection = By.cssSelector(".insiderone-icon-cards-heading h2");
    private final By teamsSubtitle = By.cssSelector(".insiderone-gallery-icon-cards-sub-heading p");

    // Top (collapsed) cards: direct children of the grid
    private final By collapsedTeamCards = By.cssSelector(".insiderone-icon-cards-grid > .insiderone-icon-cards-grid-item");

    // All cards (top + inside see-more container)
    private final By allTeamCards = By.cssSelector(".insiderone-icon-cards-grid-item");

    // See-more container and its cards
    private final By seeMoreContainer = By.cssSelector(".insiderone-icon-cards-see-more-div");
    private final By seeMoreTeamCards = By.cssSelector(".insiderone-icon-cards-see-more-div .insiderone-icon-cards-grid-item");

    private final By teamCardTitle = By.xpath(
            "//div[contains(@class,'insiderone-icon-cards-grid-item-title')]//h3");
    private final By teamCardDescription = By.xpath(
            "//div[contains(@class,'insiderone-icon-cards-grid-item-description')]//p");
    private final By teamCardButton = By.xpath(
            "//a[contains(@class,'insiderone-icon-cards-grid-item-btn')]");

    private final By seeAllTeamsButton = By.cssSelector(".insiderone-icon-cards-button-group .see-more");

    @Step("Wait for 'Explore open roles' section to be visible")
    public void waitForTeamsSection() {
        scrollIntoView(teamsSection);
        waitUntilVisible(teamsSection);
    }

    @Step("Get 'Explore open roles' title")
    public String getTeamsTitle() {
        return driver.findElement(teamsSection).getText().trim();
    }

    @Step("Get 'Explore open roles' subtitle")
    public String getTeamsSubtitle() {
        return driver.findElement(teamsSubtitle).getText().trim();
    }

    @Step("Get count of collapsed (top 3x2 grid) team cards")
    public int getCollapsedTeamsCount() {
        return driver.findElements(collapsedTeamCards).size();
    }

    @Step("Get count of 'See more' team cards (inside see-more container)")
    public int getSeeMoreTeamsCount() {
        return driver.findElements(seeMoreTeamCards).size();
    }

    @Step("Get ALL team card details (top + see-more container)")
    public List<TeamCardView> getAllTeamCards() {
        return driver.findElements(allTeamCards).stream()
                .map(card -> {
                    String name = card.findElement(teamCardTitle).getText().trim();
                    String desc = card.findElement(teamCardDescription).getText().trim();
                    WebElement btn = card.findElement(teamCardButton);
                    String buttonText = btn.getText().trim();
                    String href = btn.getAttribute("href");
                    return new TeamCardView(name, desc, buttonText, href);
                })
                .collect(Collectors.toList());
    }

    @Step("Click 'See all teams' / 'See less' toggle button")
    public void toggleSeeAllTeams() {
        click(seeAllTeamsButton);
    }

    @Step("Check 'See all teams' button is visible")
    public boolean isSeeAllTeamsVisible() {
        return isVisible(seeAllTeamsButton);
    }

    @Step("Get current text of 'See all teams' toggle button")
    public String getSeeAllButtonText() {
        return driver.findElement(seeAllTeamsButton).getText().trim();
    }

    @Step("Get 'See all teams' aria-expanded attribute")
    public String getSeeAllButtonAriaExpanded() {
        return driver.findElement(seeAllTeamsButton).getAttribute("aria-expanded");
    }

    @Step("Check if see-more container has 'open' class")
    public boolean isSeeMoreContainerOpen() {
        String classes = driver.findElement(seeMoreContainer).getAttribute("class");
        return classes != null && classes.contains("open");
    }

    @Step("Get team card view by index: {index}")
    public TeamCardView getTeamCardByIndex(int index) {
        String baseXpath = "(//div[contains(@class,'insiderone-icon-cards-grid-item')])[" + index + "]";
        WebElement card = driver.findElement(By.xpath(baseXpath));

        String name = card.findElement(teamCardTitle).getText().trim();
        String desc = card.findElement(teamCardDescription).getText().trim();

        WebElement btn = card.findElement(teamCardButton);
        String openPositionsText = btn.getText().trim();
        String href = btn.getAttribute("href") != null ? btn.getAttribute("href").trim() : "";

        return new TeamCardView(name, desc, openPositionsText, href);
    }


    // Small DTO for what we read from the UI
    public record TeamCardView(String name,
                               String description,
                               String openPositionsText,
                               String href) {

    }

    // ------------------------ LOCATION --------------------------------


    private final By locationsSection = By.cssSelector(".insiderone-locations-slider-container");
    private final By locationsTitle = By.cssSelector(".insiderone-locations-slider-main-heading h2");
    private final By locationsSubtitle = By.cssSelector(".insiderone-locations-slider-sub-heading p");

    // Each slide wrapper
    private final By locationSlides = By.cssSelector(".insiderone-locations-slider-slides .swiper-slide");

    @Step("Wait for 'Our locations' section to be visible")
    public void waitForLocationsSection() {
        scrollIntoView(locationsSection);
        waitUntilVisible(locationsSection);
    }

    @Step("Get 'Our locations' title")
    public String getLocationsTitle() {
        return driver.findElement(locationsTitle).getText().trim();
    }

    @Step("Get 'Our locations' subtitle")
    public String getLocationsSubtitle() {
        return driver.findElement(locationsSubtitle).getText().trim();
    }

    @Step("Get total count of location cards in slider")
    public int getLocationsCount() {
        return driver.findElements(locationSlides).size();
    }

    @Step("Get location card view by index: {index}")
    public LocationCardView getLocationCardByIndex(int index) {
        // base for this card
        String baseXpath = "//div[contains(@class,'insiderone-locations-slider-item')]";

        By nameLocator    = By.xpath("(" + baseXpath + "//h3)[" + index + "]");
        By addressLocator = By.xpath("(" + baseXpath + "//p)[" + index + "]");
        By emailLocator   = By.xpath("(" + baseXpath + "//div[contains(@class,'email')]//a)[" + index + "]");
        By mapsLocator    = By.xpath("(" + baseXpath + "//div[contains(@class,'link')]//a)[" +index +"]");

                // name (mandatory)
        String nameText = driver.findElement(nameLocator).getText().trim();

        // address (mandatory)
        String addressText = driver.findElement(addressLocator).getText().trim();

        // email (optional)
        String emailText = "";
        String emailHref = "";
        List<WebElement> emailAnchors = driver.findElements(emailLocator);
        if (!emailAnchors.isEmpty()) {
            WebElement emailAnchor = emailAnchors.get(0);
            emailText = emailAnchor.getText().trim();
            String href = emailAnchor.getAttribute("href");
            emailHref = href != null ? href.trim() : "";
        }

        // maps link (mandatory)
        WebElement mapsAnchor = driver.findElement(mapsLocator);
        String mapsHref = mapsAnchor.getAttribute("href") != null
                ? mapsAnchor.getAttribute("href").trim()
                : "";

        return new LocationCardView(
                nameText,
                addressText,
                emailText,
                emailHref,
                mapsHref
        );
    }

    public record LocationCardView(
            String name,
            String address,
            String emailText,
            String emailHref,
            String mapsHref
    ) {}


    // ---------- Generic Careers page helpers ----------

    @Step("Check Locations section is visible on Careers page")
    public boolean isLocationsSectionVisible() {
        return isVisible(locationsSection);
    }

    @Step("Check Teams section is visible on Careers page")
    public boolean isTeamsSectionVisible() {
        return isVisible(teamsSection);
    }

    @Step("Check 'Life at Insider One' section is visible on Careers page")
    public boolean isLifeAtInsiderSectionVisible() {
        return isVisible(lifeSection);
    }
}
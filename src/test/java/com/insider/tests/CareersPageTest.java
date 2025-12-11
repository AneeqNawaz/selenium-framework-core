package com.insider.tests;

import com.insider.base.BaseTest;
import com.insider.listeners.AllureListener;
import com.insider.pages.CareersPage;
import com.insider.testdata.CareersPageData;
import com.insider.utils.TestDataLoader;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.regex.Pattern;


@Epic("Insider Site")
@Feature("Careers Page")
@Owner("Aneeq Nawaz")
@Listeners({AllureTestNg.class, AllureListener.class})
public class CareersPageTest extends BaseTest {

    @Test(
            description = "Verify 'Life at Insider One' gallery and basic slider behavior",
            groups = {"smoke","regression"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            ### 🎞 Life at Insider One - Gallery
            
            Steps:
            1. Open Careers page.
            2. Verify the section title matches JSON.
            3. Verify the subtitle matches JSON.
            4. Verify slide/image count matches JSON.
            5. Verify left/right arrows are visible.
            6. Verify each slide has an <img> with non-empty src.
            7. Verify only one .swiper-slide-active is present, even after navigation.
            """)
    public void verifyLifeAtInsiderGallery() {

        Allure.step("Open Careers page", careersPage::openCareersPage);
        Allure.step("Wait for Life at Insider section", careersPage::waitForLifeSection);

        // 1) Title
        String expectedTitle = careersData.getLifeAtInsider().getTitle();
        String actualTitle = careersPage.getLifeTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Life at Insider title mismatch");

        // 2) Subtitle
        String expectedSubtitle = careersData.getLifeAtInsider().getSubtitle();
        String actualSubtitle = careersPage.getLifeSubtitle();
        Assert.assertEquals(actualSubtitle, expectedSubtitle, "Life at Insider subtitle mismatch");

        // 3) Image/slide count
        int expectedCount = careersData.getLifeAtInsider().getExpectedImageCount();
        int actualCount = careersPage.getLifeSlidesCount();
        Assert.assertEquals(actualCount, expectedCount, "Life at Insider slides count mismatch");

        // 4) Arrows visible
        Assert.assertTrue(careersPage.isRightArrowDisplayed(), "Right arrow should be visible");
        Assert.assertTrue(careersPage.isLeftArrowDisplayed(), "Left arrow should be visible");

        // 5) Each slide has <img> with non-empty src
        Allure.step("Verify each Life slide has non-empty image src", () -> {
            Assert.assertTrue(
                    careersPage.allLifeSlidesHaveNonEmptyImgSrc(),
                    "All Life at Insider slides should have an <img> with non-empty src"
            );
        });

        // 6) Only one .swiper-slide-active at any time (initial + after a couple of moves)
        Allure.step("Verify only one active Life slide exists at all times", () -> {
            int initialActiveCount = careersPage.getActiveLifeSlidesCount();
            Assert.assertEquals(
                    initialActiveCount, 1,
                    "Initially, there should be exactly one active Life slide"
            );

            // Move right once and check again
            careersPage.clickLifeRightArrow();
            int activeAfterRight = careersPage.getActiveLifeSlidesCount();
            Assert.assertEquals(
                    activeAfterRight, 1,
                    "After clicking right arrow, there should still be exactly one active Life slide"
            );

            // Move left once and check again
            careersPage.clickLifeLeftArrow();
            int activeAfterLeft = careersPage.getActiveLifeSlidesCount();
            Assert.assertEquals(
                    activeAfterLeft, 1,
                    "After clicking left arrow, there should still be exactly one active Life slide"
            );
        });
    }

    // ------------------------- Team Section ------------------------

    @Test(
            description = "Verify 'Explore open roles' teams section including See all / See less behavior and links",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
        ### 👥 Explore open roles - Teams
        
        Validations:
        - Section title & subtitle from JSON.
        - Collapsed state: 6 cards in main grid, remaining under see-more container.
        - After expand, JSON team cards match UI cards.
        - Open positions text follows pattern '<number> Open Positions'.
        - Href sanity: non-empty, http/https, Insider or Lever domain.
        - 'See all teams' expands list (aria-expanded + see-more container 'open' class).
        - Optional: collapse back and verify aria-expanded & counts.
        """)
    public void verifyTeamsSectionAndSeeAllBehavior() {
        CareersPage page = new CareersPage(driver);
        page.openCareersPage();
        page.waitForTeamsSection();

        CareersPageData.TeamsSectionData teamsData = careersData.getTeams();
        int totalExpectedTeams = teamsData.getTeams().size();

        // ---------- 1) Section title & subtitle ----------

        String actualTitle = page.getTeamsTitle();
        Assert.assertEquals(actualTitle, teamsData.getTitle(),
                "Teams section title mismatch");

        String actualSubtitle = page.getTeamsSubtitle();
        Assert.assertEquals(actualSubtitle, teamsData.getSubtitle(),
                "Teams section subtitle mismatch");

        // ---------- 2) Collapsed state: top grid (6) + see-more container ----------

        int topCount = page.getCollapsedTeamsCount();
        int seeMoreCount = page.getSeeMoreTeamsCount();

        Assert.assertTrue(
                topCount <= totalExpectedTeams,
                "Top (collapsed) teams count should be <= total teams in JSON"
        );
        Assert.assertTrue(
                topCount <= 6,
                "Top (collapsed) teams count should be <= 6 (3x2 grid)"
        );

        Assert.assertEquals(
                seeMoreCount,
                totalExpectedTeams - topCount,
                "See-more teams count should be totalExpected - topCount"
        );

        // ---------- 3) Expand: 'See all teams' behavior & aria-expanded / open class ----------

        Assert.assertTrue(page.isSeeAllTeamsVisible(),
                "'See all teams' button should be visible");

        String beforeToggleText = page.getSeeAllButtonText();
        String ariaBefore = page.getSeeAllButtonAriaExpanded();
        Assert.assertEquals(
                ariaBefore, "false",
                "'See all teams' aria-expanded should initially be 'false'"
        );

        page.toggleSeeAllTeams(); // click & wait inside POM

        String ariaAfterExpand = page.getSeeAllButtonAriaExpanded();
        Assert.assertEquals(
                ariaAfterExpand, "true",
                "'See all teams' aria-expanded should be 'true' after expand"
        );

        Assert.assertTrue(
                page.isSeeMoreContainerOpen(),
                "See-more container should have 'open' class after expanding"
        );

        // Now all cards should be visible
        List<CareersPage.TeamCardView> allCardsAfterExpand = page.getAllTeamCards();
        int expandedCount = allCardsAfterExpand.size();

        Assert.assertEquals(
                expandedCount, totalExpectedTeams,
                "Expanded should show exactly all teams from JSON"
        );

        // ---------- 4) Card data verification on ALL cards (JSON vs UI, index-based) ----------

        SoftAssert soft = new SoftAssert();
        Pattern openPositionsPattern = Pattern.compile("\\d+\\s+Open Positions");

        List<CareersPageData.TeamCardData> expectedTeams = teamsData.getTeams();

        for (int i = 0; i < expectedTeams.size(); i++) {

            int cardIndex = i + 1;
            CareersPageData.TeamCardData expected = expectedTeams.get(i);
            CareersPage.TeamCardView card = page.getTeamCardByIndex(cardIndex);

            String uiName = card.name().trim();
            String uiDesc = card.description().trim();
            String uiOpenText = card.openPositionsText().trim().replaceAll("\\s+", " ");
            String uiHref = card.href() != null ? card.href().trim() : "";

            // ✔ Validate name
            soft.assertEquals(uiName, expected.getName(),
                    "Team name mismatch at index " + cardIndex);

            // ✔ Validate description
            soft.assertEquals(uiDesc, expected.getDescription(),
                    "Description mismatch for: " + expected.getName());

            // ✔ Validate pattern only (not the value)
            soft.assertTrue(
                    openPositionsPattern.matcher(uiOpenText).matches(),
                    "Invalid open positions text for: " + expected.getName()
                            + " -> '" + uiOpenText + "'"
            );

            // ✔ Validate URL exists & matches JSON
            soft.assertTrue(!uiHref.isEmpty(),
                    "URL empty for: " + expected.getName());

            soft.assertTrue(uiHref.startsWith("http"),
                    "URL must start with http/https for: " + expected.getName());

            soft.assertEquals(uiHref, expected.getUrl(),
                    "URL mismatch for: " + expected.getName());
        }

        soft.assertAll();


        // ---------- 5) Collapse back, aria-expanded, and counts ----------

        String afterExpandText = page.getSeeAllButtonText();

        if (!beforeToggleText.equalsIgnoreCase(afterExpandText)) {
            page.toggleSeeAllTeams(); // collapse

            String ariaAfterCollapse = page.getSeeAllButtonAriaExpanded();
            Assert.assertEquals(
                    ariaAfterCollapse, "false",
                    "'See all teams' aria-expanded should be 'false' after collapse"
            );

            int topCountAfterCollapse = page.getCollapsedTeamsCount();
            Assert.assertEquals(
                    topCountAfterCollapse, topCount,
                    "Collapsed topCount after 'See less' should equal initial topCount"
            );
        }
    }


    //  ------------------------- LOCATION SECTION --------------------------

    @Test(
            description = "Verify 'Our locations' header, total count and sample cards from JSON (index-based loop)",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
    ### 🌍 Our locations (index-based)

    Validations:
    - Section title & subtitle from JSON.
    - Total locations count vs JSON.expectedLocationsCount.
    - For each entry in JSON.sampleLocations (index-based):
      * Name matches.
      * Address matches.
      * Email behavior:
        - If JSON email == "" -> UI email may be empty.
        - Otherwise: email text matches and href starts with mailto:.
      * 'Get Direction' href matches JSON.mapsUrl.
    """)
    public void verifyLocationsSectionIndexBased() {
        CareersPage page = new CareersPage(driver);

        page.openCareersPage();
        page.waitForLocationsSection();

        SoftAssert soft = new SoftAssert();

        CareersPageData.LocationsSectionData locationsData = careersData.getLocations();

        // 1) Header text
        soft.assertEquals(
                page.getLocationsTitle(),
                locationsData.getTitle(),
                "Locations section title mismatch"
        );

        soft.assertEquals(
                page.getLocationsSubtitle(),
                locationsData.getSubtitle(),
                "Locations section subtitle mismatch"
        );

        // 2) Total card count
        int actualCount = page.getLocationsCount();
        soft.assertEquals(
                actualCount,
                locationsData.getExpectedLocationsCount(),
                "Total locations count mismatch"
        );

        List<CareersPageData.LocationData> locations = locationsData.getLocationsList();

        for (int i = 0; i < locations.size(); i++) {
            CareersPageData.LocationData expected = locations.get(i);
            int cardIndex = i + 1; // 1-based for XPath

            CareersPage.LocationCardView card = page.getLocationCardByIndex(cardIndex);

            String uiName = card.name().trim();
            String uiAddress = card.address().trim();
            String uiEmailText = card.emailText() != null ? card.emailText().trim() : "";
            String uiEmailHref = card.emailHref() != null ? card.emailHref().trim() : "";
            String uiMapsHref = card.mapsHref() != null ? card.mapsHref().trim() : "";

            String expectedName = expected.getName().trim();
            String expectedAddress = expected.getAddress().trim();
            String expectedEmail = expected.getEmail() == null ? "" : expected.getEmail().trim();
            String expectedMapsUrl = expected.getMapsUrl().trim();

            // Name
            soft.assertEquals(
                    uiName,
                    expectedName,
                    "Location name mismatch for index " + cardIndex
            );

            // Address
            soft.assertEquals(
                    uiAddress,
                    expectedAddress,
                    "Location address mismatch for: " + expectedName
            );

            if (expectedEmail.isEmpty()) {
                soft.assertTrue(
                        uiEmailText.isEmpty(),
                        "Email should be empty for: " + expectedName + " but was: " + uiEmailText
                );
            } else {
                soft.assertEquals(
                        uiEmailText,
                        expectedEmail,
                        "Email text mismatch for: " + expectedName
                );
                soft.assertTrue(
                        uiEmailHref.toLowerCase().startsWith("mailto:"),
                        "Email href should start with mailto: for: " + expectedName
                                + " but was: " + uiEmailHref
                );
            }

            // Get Direction URL
            soft.assertEquals(
                    uiMapsHref,
                    expectedMapsUrl,
                    "Maps URL mismatch for: " + expectedName
            );
        }
        soft.assertAll();
    }
}
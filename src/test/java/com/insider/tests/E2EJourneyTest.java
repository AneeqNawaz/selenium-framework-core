package com.insider.tests;

import com.insider.base.BaseTest;
import com.insider.listeners.AllureListener;
import com.insider.pages.HomePage;
import com.insider.pages.CareersPage;
import com.insider.pages.LeverQaJobsPage;
import com.insider.pages.LeverJobDetailPage;
import com.insider.pages.LeverApplyJobPage;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Epic("Insider Site")
@Feature("End-to-End QA Hiring Journey")
@Story("User navigates from Insider homepage to QA job apply form")
@Owner("Aneeq Nawaz")
@Severity(SeverityLevel.BLOCKER)
@Listeners({AllureTestNg.class, AllureListener.class})
public class E2EJourneyTest extends BaseTest {

    @Test(
            description = "Full journey: Home → Careers → Lever QA listing → Job Detail → Apply form (Istanbul, Turkiye)",
            groups = {"e2e"}
    )
    @Description("""
        ### 🧪 E2E – QA Hiring Journey (Istanbul, Turkiye)

        Steps:
        1. Open Insider home page.
        2. Scroll to footer COMPANY section and click **We're hiring**.
        3. On Careers page, verify Locations / Teams / Life at Insider sections are visible.
        4. Navigate to Lever QA jobs listing.
        5. On Lever, verify URL, filters, and team = Quality Assurance.
        6. Filter by Location = Istanbul, Turkiye.
        7. Validate first job card (title, location, Remote work type, Apply button).
        8. Click **Apply** → verify job detail header.
        9. Click **Apply for this job** → verify apply page header and form.
        """)
    public void verifyHomeToLeverApplyForIstanbulQaJob() {

        HomePage homePage = new HomePage(driver);
        CareersPage careersPage = new CareersPage(driver);
        LeverQaJobsPage leverQaJobsPage = new LeverQaJobsPage(driver);
        LeverJobDetailPage leverJobDetailPage = new LeverJobDetailPage(driver);
        LeverApplyJobPage leverApplyPage = new LeverApplyJobPage(driver);

        // ---------------- 1) Home page ----------------
        Allure.step("Open Insider home page & verify COMPANY section", () -> {
            homePage.openHomePage();
            homePage.waitForHomePageLoaded();
            Assert.assertTrue(
                    homePage.isCompanySectionVisible(),
                    "COMPANY section in footer should be visible on home page"
            );
        });

        Allure.step("Scroll to COMPANY footer and click 'We're hiring'", () -> {
            homePage.scrollToCompanySection();
            Assert.assertTrue(
                    homePage.getCompanyLinksTexts().contains("We're hiring"),
                    "'We're hiring' link should exist in COMPANY footer"
            );
            homePage.clickWereHiring();
        });

        // ---------------- 2) Careers page ----------------
        Allure.step("Verify core sections on Careers page (Locations / Teams / Life at Insider)", () -> {
            careersPage.waitForLifeSection();
            Assert.assertTrue(
                    careersPage.isLocationsSectionVisible(),
                    "Locations section should be visible on Careers page"
            );
            Assert.assertTrue(
                    careersPage.isTeamsSectionVisible(),
                    "Teams section should be visible on Careers page"
            );
            Assert.assertTrue(
                    careersPage.isLifeAtInsiderSectionVisible(),
                    "Life at Insider section should be visible on Careers page"
            );
        });

        // ---------------- 3) Go to Lever QA jobs listing ----------------
        Allure.step("Open Lever QA jobs listing (team=Quality Assurance)", () -> {
            // Reuse same method & data as in LeverQaJobsTest
            leverQaJobsPage.openQaJobsPage(leverQaData.getListingUrl());
        });

        // ---------------- 4) Lever QA jobs listing basic checks ----------------
        Allure.step("Verify URL, filters and team filter on Lever QA jobs page", () -> {
            Assert.assertTrue(
                    leverQaJobsPage.isOnQAJobsPage(),
                    "Should be on Lever QA jobs page with team=Quality Assurance"
            );
            Assert.assertTrue(
                    leverQaJobsPage.areFiltersVisible(),
                    "Top 4 filters should be visible on Lever QA jobs page"
            );
            Assert.assertTrue(
                    leverQaJobsPage.isTeamFilterSelectedAsQA(),
                    "Team filter should be set to 'Quality Assurance'"
            );

            String groupTitle = leverQaJobsPage.getPostingGroupTitleText();
            Assert.assertEquals(
                    groupTitle,
                    leverQaData.getPostingGroupTitle(),
                    "Posting group title should be '" + leverQaData.getPostingGroupTitle() + "'"
            );
        });


        class ListingInfo {
            String title;
            String location;
            String workType;
        }

        ListingInfo listing = new ListingInfo();

        // ---------------- 5) Filter Istanbul & read listing card ----------------
        final String targetLocationFilter = "Istanbul, Turkiye";
        Allure.step("Filter QA jobs by Location = " + targetLocationFilter, () -> {
            leverQaJobsPage.selectLocation(targetLocationFilter);

            Assert.assertTrue(
                    leverQaJobsPage.isPostingCardVisible(),
                    "At least one QA posting should be visible after filtering by Istanbul, Turkiye"
            );

            // Reuse same getters as LeverQaJobsTest
            final String listTitle = leverQaJobsPage.getJobTitle();
            final String listLocation = leverQaJobsPage.getJobLocation();
            final String listWorkType = leverQaJobsPage.getJobWorkplaceType();

            // Basic validations on listing
            Assert.assertFalse(
                    listTitle.isEmpty(),
                    "Job listing title should not be empty"
            );

            Assert.assertTrue(
                    listTitle.toLowerCase().contains("quality assurance"),
                    "Job listing title should contain 'Quality Assurance' but was: " + listTitle
            );

            Assert.assertTrue(
                    listLocation.toUpperCase().contains("ISTANBUL"),
                    "Job listing location should contain 'Istanbul' but was: " + listLocation
            );

            Assert.assertTrue(
                    listWorkType.toLowerCase().contains("remote"),
                    "Job listing workplace type should contain 'Remote' but was: " + listWorkType
            );

            Assert.assertTrue(
                    leverQaJobsPage.isJobApplyButtonDisplayed(),
                    "Apply button should be visible on first listing card"
            );

            listing.title    = leverQaJobsPage.getJobTitle();
            listing.location = leverQaJobsPage.getJobLocation();
            listing.workType = leverQaJobsPage.getJobWorkplaceType();
        });

        // ---------------- 6) Job detail page ----------------
        Allure.step("Click 'Apply' on listing and verify Lever job detail header", () -> {
            leverQaJobsPage.clickJobApply();

            String detailTitle = leverJobDetailPage.getJobTitle();
            String detailLocation = leverJobDetailPage.getLocation();
            String detailDepartment = leverJobDetailPage.getDepartment();
            String detailWorkType = leverJobDetailPage.getWorkplaceType();

            // Title should match listing exactly
            Assert.assertEquals(
                    detailTitle,
                    listing.title,
                    "Job detail title should match listing title"
            );

            // Location should match, but be case-insensitive
            Assert.assertTrue(
                    detailLocation.equalsIgnoreCase(listing.location),
                    "Job detail location should match listing location (case-insensitive). " +
                            "Expected: " + listing.location + ", Actual: " + detailLocation
            );

            // Department should contain "Quality Assurance"
            Assert.assertTrue(
                    detailDepartment.toLowerCase().contains("quality assurance"),
                    "Job detail department should contain 'Quality Assurance' but was: " + detailDepartment
            );

            // Work type should still be Remote-ish, but don't be strict on formatting
            Assert.assertTrue(
                    detailWorkType.toLowerCase().contains("remote"),
                    "Job detail workplace type should contain 'Remote' but was: " + detailWorkType
            );

            Assert.assertTrue(
                    leverJobDetailPage.isApplyForThisJobButtonVisible(),
                    "'Apply for this job' button should be visible on job detail page"
            );
        });

        // ---------------- 7) Apply page & form ----------------
        Allure.step("Click 'Apply for this job' and verify application page header + form", () -> {
            leverJobDetailPage.clickApplyForThisJob();

            String applyTitle = leverApplyPage.getJobTitle();
            String applyLocation = leverApplyPage.getLocation();
            String applyDepartment = leverApplyPage.getDepartment();
            String applyWorkType = leverApplyPage.getWorkplaceType();

            Assert.assertEquals(
                    applyTitle,
                    listing.title,
                    "Apply page title should match listing title"
            );

            Assert.assertTrue(
                    applyLocation.equalsIgnoreCase(listing.location),
                    "Apply page location should match listing location (case-insensitive). " +
                            "Expected: " + listing.location + ", Actual: " + applyLocation
            );

            Assert.assertTrue(
                    applyDepartment.toLowerCase().contains("quality assurance"),
                    "Apply page department should contain 'Quality Assurance' but was: " + applyDepartment
            );

            Assert.assertTrue(
                    applyWorkType.toLowerCase().contains("remote"),
                    "Apply page workplace type should contain 'Remote' but was: " + applyWorkType
            );
        });
    }
}

package com.insider.tests;

import com.insider.base.BaseTest;
import com.insider.listeners.AllureListener;
import com.insider.pages.LeverQaJobsPage;
import com.insider.testdata.LeverQaJobsData;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;   // <-- add this


@Epic("Insider Site")
@Feature("QA JOBS Navigation")
@Story("Navigate to QA Jobs and verify lever pages")
@Owner("Aneeq Nawaz")
@Severity(SeverityLevel.CRITICAL)
@Listeners({AllureTestNg.class, AllureListener.class})

public class LeverQaJobsTest extends BaseTest {


    @Test(
            description = "Verify initial QA jobs listing layout and data for all postings",
            groups = {"regression"}
    )
    @Description("""
        1. Open QA jobs listing URL (team=Quality Assurance) from JSON
        2. Verify group title and filters
        3. Verify we have at least expectedInitialJobsCount (5) postings
        4. For each job card:
           - Title not empty
           - Location not empty
           - Workplace type: if present, contains 'Remote'
           - Links (detail/apply) are Lever insiderone URLs
        """)
    public void verifyAllQaJobsListingLayoutFromJson() {

        qaJobsPage.openQaJobsPage(leverQaData.getListingUrl());

        // Group title
        String groupTitle = qaJobsPage.getPostingGroupTitleText();
        Assert.assertEquals(
                groupTitle,
                leverQaData.getPostingGroupTitle(),
                "Posting group title mismatch"
        );

        // Filters visible
        Assert.assertTrue(
                qaJobsPage.areFiltersVisible(),
                "Top 4 filters should be visible"
        );

        var cards = qaJobsPage.getAllJobCards();

        Assert.assertTrue(
                cards.size() >= leverQaData.getExpectedInitialJobsCount(),
                "Expected at least " + leverQaData.getExpectedInitialJobsCount()
                        + " QA jobs, but found: " + cards.size()
        );

        SoftAssert soft = new SoftAssert();

        for (LeverQaJobsPage.JobCardView card : cards) {

            String title = card.title().trim();
            String location = card.location().trim();
            String workType = card.workplaceType() != null ? card.workplaceType().trim() : "";
            String detail = card.detailHref() != null ? card.detailHref().trim() : "";
            String apply = card.applyHref() != null ? card.applyHref().trim() : "";

            // Title
            soft.assertFalse(title.isEmpty(), "Job title should not be empty");

            // Location
            soft.assertFalse(location.isEmpty(), "Job location should not be empty");

            // Workplace type: optional but if present, should contain Remote
            if (!workType.isEmpty()) {
                soft.assertTrue(
                        normalizeWorkType(workType)
                                .contains(leverQaData.getWorkTypeContains().toLowerCase()),
                        "Workplace type should contain '" + leverQaData.getWorkTypeContains()
                                + "' but was: '" + workType + "'"
                );
            }

            // Detail link
            soft.assertTrue(
                    detail.startsWith("https://jobs.lever.co/insiderone/"),
                    "Detail link invalid: " + detail
            );

            // Apply link
            soft.assertTrue(
                    apply.startsWith("https://jobs.lever.co/insiderone/"),
                    "Apply link invalid: " + apply
            );
        }

        soft.assertAll();
    }
        @Test(
                description = "Happy path – QA jobs detail & apply flow on Lever for all configured locations",
                groups = {"regression"}
        )
        public void verifyQaJobDetailAndApplyFlowForLocations() {

            for (LeverQaJobsData.LocationFlow flow : leverQaData.getLocationFlows()) {

                logger.info("=== Executing QA jobs flow for location: " + flow.getName()
                        + " (" + flow.getLocationFilterText() + ") ===");

                SoftAssert softAssert = new SoftAssert();

                // 1) Open listing
                qaJobsPage.openQaJobsPage(leverQaData.getListingUrl());
                Assert.assertTrue(
                        qaJobsPage.isOnQAJobsPage(),
                        "Not on QA jobs page after opening listing"
                );

                // 2) Apply location filter from JSON
                qaJobsPage.selectLocation(flow.getLocationFilterText());

                var cards = qaJobsPage.getAllJobCards();
                Assert.assertFalse(
                        cards.isEmpty(),
                        "Expected at least one job for location filter: " + flow.getLocationFilterText()
                );

                LeverQaJobsPage.JobCardView first = cards.get(0);

                String listTitle      = first.title().trim();
                String listLocation   = first.location().trim();
                String listWorkType   = first.workplaceType() != null ? first.workplaceType().trim() : "";
                String listCommitment = first.commitment() != null ? first.commitment().trim() : "";

                // ----- Listing assertions -----

                softAssert.assertFalse(
                        listTitle.isEmpty(),
                        "[" + flow.getName() + "] Listing title should not be empty"
                );

                softAssert.assertTrue(
                        listLocation.toUpperCase().contains(flow.getLocationShouldContain()),
                        "[" + flow.getName() + "] Listing location mismatch. Expected to contain '" +
                                flow.getLocationShouldContain() + "' but was: '" + listLocation + "'"
                );

                if (!listWorkType.isEmpty()) {
                    softAssert.assertTrue(
                            normalizeWorkType(listWorkType)
                                    .contains(leverQaData.getWorkTypeContains().toLowerCase()),
                            "[" + flow.getName() + "] Listing workplace type should contain '" +
                                    leverQaData.getWorkTypeContains() + "' but was: '" + listWorkType + "'"
                    );
                }

                if (!listCommitment.isEmpty()) {
                    softAssert.assertTrue(
                            listCommitment.toLowerCase().contains("full-time"),
                            "[" + flow.getName() + "] Listing commitment should contain 'Full-Time' but was: '" + listCommitment + "'"
                    );
                }

                // ----- Detail page -----

                qaJobsPage.clickJobApply();

                String detailTitle      = leverJobDetailPage.getJobTitle();
                String detailLocation   = leverJobDetailPage.getLocation();
                String detailDepartment = leverJobDetailPage.getDepartment();
                String detailWorkType   = leverJobDetailPage.getWorkplaceType();
                String detailCommitment = leverJobDetailPage.getCommitment(); // may be ""

                softAssert.assertEquals(
                        detailTitle,
                        listTitle,
                        "[" + flow.getName() + "] Detail page title mismatch"
                );

                softAssert.assertTrue(
                        detailLocation.equalsIgnoreCase(listLocation),
                        "[" + flow.getName() + "] Detail page location mismatch. Expected: '" +
                                listLocation + "' but was: '" + detailLocation + "'"
                );

                softAssert.assertTrue(
                        detailDepartment.contains(leverQaData.getDepartmentContains()),
                        "[" + flow.getName() + "] Detail department mismatch. Expected to contain '" +
                                leverQaData.getDepartmentContains() + "' but was: '" + detailDepartment + "'"
                );

                if (!listWorkType.isEmpty()) {
                    softAssert.assertEquals(
                            normalizeWorkType(detailWorkType),
                            normalizeWorkType(listWorkType),
                            "[" + flow.getName() + "] Detail workplace type mismatch (normalized)"
                    );
                }

                if (!listCommitment.isEmpty() && !detailCommitment.isEmpty()) {
                    softAssert.assertEquals(
                            normalizeCommitment(detailCommitment),
                            normalizeCommitment(listCommitment),
                            "[" + flow.getName() + "] Detail commitment mismatch (normalized)"
                    );
                }

                // ----- Apply page -----

                softAssert.assertTrue(
                        leverJobDetailPage.isApplyForThisJobButtonVisible(),
                        "[" + flow.getName() + "] 'Apply for this job' button should be visible"
                );

                leverJobDetailPage.clickApplyForThisJob();

                String applyTitle      = leverApplyPage.getJobTitle();
                String applyLocation   = leverApplyPage.getLocation();
                String applyDepartment = leverApplyPage.getDepartment();
                String applyWorkType   = leverApplyPage.getWorkplaceType();
                String applyCommitment = leverApplyPage.getCommitment();

                softAssert.assertEquals(
                        applyTitle,
                        listTitle,
                        "[" + flow.getName() + "] Apply page title mismatch"
                );

                softAssert.assertTrue(
                        applyLocation.equalsIgnoreCase(listLocation),
                        "[" + flow.getName() + "] Apply page location mismatch. Expected: '" +
                                listLocation + "' but was: '" + applyLocation + "'"
                );

                softAssert.assertTrue(
                        applyDepartment.contains(leverQaData.getDepartmentContains()),
                        "[" + flow.getName() + "] Apply page department mismatch. Expected to contain '" +
                                leverQaData.getDepartmentContains() + "' but was: '" + applyDepartment + "'"
                );

                if (!listWorkType.isEmpty()) {
                    softAssert.assertEquals(
                            normalizeWorkType(applyWorkType),
                            normalizeWorkType(listWorkType),
                            "[" + flow.getName() + "] Apply page workplace type mismatch (normalized)"
                    );
                }

                if (!listCommitment.isEmpty() && !applyCommitment.isEmpty()) {
                    softAssert.assertEquals(
                            normalizeCommitment(applyCommitment),
                            normalizeCommitment(listCommitment),
                            "[" + flow.getName() + "] Apply page commitment mismatch (normalized)"
                    );

                }
                softAssert.assertAll("Failures in QA jobs flow for location: " + flow.getName());
            }
        }


    private String normalizeWorkType(String raw) {
        if (raw == null) return "";
        String cleaned = raw.trim().replace("\u00A0", " "); // non-breaking spaces
        String[] parts = cleaned.split("—");
        return parts[0].trim().toLowerCase(); // "Remote —" -> "remote"
    }

    private String normalizeCommitment(String value) {
        if (value == null) {
            return "";
        }

        // Remove trailing slash if present, normalize spaces and case
        return value
                .replace("/", "")                // remove "/" (like " /")
                .replace("\u00A0", " ")          // replace non-breaking spaces if any
                .trim()
                .replaceAll("\\s+", " ")         // collapse multiple spaces
                .toLowerCase();                  // case-insensitive comparison
    }
}

package com.insider.tests;

import com.insider.base.BaseTest;
import com.insider.listeners.AllureListener;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import com.insider.testdata.HomePageData;
import com.insider.utils.TestDataLoader;

@Epic("Insider Site")
@Feature("Homepage Footer Navigation")
@Story("Navigate to Careers from 'We're hiring' link in COMPANY section")
@Owner("Aneeq Nawaz")
@Severity(SeverityLevel.CRITICAL)

@Listeners({AllureTestNg.class, AllureListener.class})
public class HomePageTest extends BaseTest {

    @Test(
            description = "Verify Homepage Load & \"We're hiring\" link at footer navigates to Careers page",
            groups = {"smoke"}
    )
    @Description("""
        ### 🔗Careers Navigation

        Steps:
        1. Open Insider home page.
        2. Verify the URL.
        3. Verify the title.
        4. Ensure the page is loaded (header is visible).
        5. Scroll to the COMPANY section in the footer.
        6. Verify the COMPANY list contains the expected links, including **We're hiring**.
        7. Click **We're hiring** and navigate to the Careers page.
        """)
    public void verifyWereHiringFooterLinkNavigatesToCareers() {



        homePage.openHomePage();
        homePage.waitForHomePageLoaded();

        // 2) Verify URL from JSON
        String actualUrl = homePage.currentUrl();
        Assert.assertEquals(
                actualUrl,
                homeData.getHomeUrl(),
                "Home page URL mismatch"
        );

        // 3) Verify title from JSON
        String actualTitle = homePage.currentTitle();
        Assert.assertEquals(
                actualTitle,
                homeData.getTitle(),
                "Home page title mismatch"
        );

        // 4) Scroll to COMPANY section and verify it's visible
        homePage.scrollToCompanySection();
        Assert.assertTrue(
                homePage.isCompanySectionVisible(),
                "COMPANY section heading should be visible in footer"
        );

        // 5) Verify COMPANY list contains expected links from JSON
        List<String> expectedLinks = homeData.getFooter().getCompanyLinks();
        List<String> actualLinks = homePage.getCompanyLinksTexts();

        Assert.assertTrue(
                actualLinks.containsAll(expectedLinks),
                "Footer COMPANY link texts mismatch. Expected at least: " + expectedLinks +
                        " but was: " + actualLinks
        );

        Assert.assertTrue(
                actualLinks.contains("We're hiring"),
                "'We're hiring' should be present in COMPANY footer links"
        );

        Assert.assertTrue(
                actualLinks.containsAll(expectedLinks),
                "Footer COMPANY link texts mismatch. Expected at least: " + expectedLinks +
                        " but was: " + actualLinks
        );

        Assert.assertTrue(
                actualLinks.contains("We're hiring"),
                "'We're hiring' should be present in COMPANY footer links"
        );

        // 6) Click "We're hiring"
        homePage.clickWereHiring();

        // Optional: verify we really navigated to Careers
        String careersUrl = homePage.currentUrl();
        Assert.assertTrue(
                careersUrl.toLowerCase().contains("/careers"),
                "Expected Careers page URL to contain '/careers' but was: " + careersUrl
        );
    }
}

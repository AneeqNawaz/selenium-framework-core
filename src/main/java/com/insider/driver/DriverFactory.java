package com.insider.driver;

import com.insider.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    public static WebDriver createDriver() {

        String browser = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();
        String seleniumUrl = System.getenv("SELENIUM_URL");

        if (browser == null || browser.isBlank()) {
            browser = "chrome";
        }

        browser = browser.toLowerCase();

        if (seleniumUrl != null && !seleniumUrl.isBlank()) {
            return createRemoteDriver(browser, headless, seleniumUrl);
        }

        return createLocalDriver(browser, headless);
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless, String seleniumUrl) {
        try {
            if ("chrome".equals(browser)) {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                return new RemoteWebDriver(new URL(seleniumUrl), options);
            }

            throw new RuntimeException("Remote grid currently supports only Chrome for this setup.");

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid SELENIUM_URL: " + seleniumUrl, e);
        }
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {

        WebDriver driver;

        switch (browser) {

            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                    options.addArguments("--width=1920");
                    options.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(options);
                break;
            }

            case "edge": {
                driver = new EdgeDriver();
                break;
            }

            case "chrome":
            default: {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                driver = new ChromeDriver(options);
                break;
            }
        }

        if (!headless) {
            try {
                driver.manage().window().maximize();
            } catch (Exception ignored) {
            }
        }

        return driver;
    }
}

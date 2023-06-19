package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import org.openqa.selenium.JavascriptExecutor;

public class GuideslySample {

    private RemoteWebDriver driver;
    private String Status = "failed";
    private String githubURL = System.getenv("GITHUB_URL");


    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("browserName", "chrome");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "TestNG With Java");
        caps.setCapability("name", m.getName() + " - " + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");
        caps.setCapability("smartUI.project", "guidesly-project");
        caps.setCapability("selenium_version", "4.0.0");
        // caps.setCapability("smartUI.baseline", true);

        if (githubURL != null) {
            Map < String, String > github = new HashMap < String, String > ();
            github.put("url", githubURL);
            caps.setCapability("github", github);
        }
        System.out.println(caps);
        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);

    }

    @Test
    public void basicTest() throws InterruptedException {
        String spanText;

        //Step 1
        System.out.println("Loading Home  Page");

        driver.get("https://guidesly.com");
        Thread.sleep(2000);
        driver.executeScript("smartui.takeFullPageScreenshot,{\"screenshotName\":\"home-page\", \"smartScroll\":true }");

        System.out.println("home-page Saved");
        Thread.sleep(1000);

        //Step 2
        System.out.println("Loading Bio Page");

        driver.get("https://guidesly.com/book-a-guide/guide-details/Tuna-Wahoo-Charters?tripDate=2023-04-21&guests=1");

        driver.executeScript("smartui.takeFullPageScreenshot,{\"screenshotName\":\"guide-bio-page\", \"smartScroll\":true }");
        Thread.sleep(1000);

        System.out.println("guide-bio-page Saved");
        //Step 3
        System.out.println("Loading Search  Page");

        driver.get("https://guidesly.com/search?address=Tambaram%2C+Tamil+Nadu&latitude=12.94336&longitude=80.1570816&guest=1&category=alltrips&page=1");
        Thread.sleep(2000);
        driver.executeScript("smartui.takeFullPageScreenshot,{\"screenshotName\":\"search-page\", \"smartScroll\":true }");

        Thread.sleep(1000);

        System.out.println("search-page Saved");
        this.Status= "passed";

    }

    public void scrollToBottom(int lastPageWait) {
        try {
            Long height = getJavaScriptReturnValue("document.body.scrollHeight");
            System.out.println("height: " + height);
            int heightOfPage = height.intValue();
            System.out.println("heightOfPage: " + heightOfPage);

            int size = 500;
            int noOfLoop = heightOfPage / size;
            System.out.println("noOfLoop: " + noOfLoop);
            for (int i = 1; i <= noOfLoop; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + (i - 1 * size) + "," + (i * size) + ")");
                Thread.sleep(1000);
                if (i == noOfLoop) {
                    System.out.println("End of loop : " + i * size + "to " + heightOfPage);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + (i * size) + "," + heightOfPage + ")");
                    Thread.sleep(lastPageWait);
                }
            }
            // Now scroll at the top
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
            Thread.sleep(5000);
            System.out.println("Scroll Completed");
        } catch (Exception e) {
            System.out.println("Got some errors" + e.toString());
            e.printStackTrace();
        }
    }


    public void quickScrollToBottom(int lastPageWait) {
        try {
            Long height = getJavaScriptReturnValue("document.body.scrollHeight");
            System.out.println("height: " + height);
            int heightOfPage = height.intValue();
            System.out.println("heightOfPage: " + heightOfPage);

            int size = 200;
            int noOfLoop = heightOfPage / size;
            System.out.println("noOfLoop: " + noOfLoop);
            for (int i = 1; i <= noOfLoop; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + (i - 1 * size) + "," + (i * size) + ")");
                Thread.sleep(1000);
                if (i == noOfLoop) {
                    System.out.println("End of loop : " + i * size + "to " + heightOfPage);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + (i * size) + "," + heightOfPage + ")");
                    Thread.sleep(lastPageWait);
                }
            }
            // Now scroll at the top
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
            Thread.sleep(10000);
            System.out.println("Scroll Completed");
        } catch (Exception e) {
            System.out.println("Got some errors" + e.toString());
            e.printStackTrace();
        }
    }

    public Long getJavaScriptReturnValue(String javaScript) {
        return (Long) driver.executeScript("return " + javaScript);
    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }

}
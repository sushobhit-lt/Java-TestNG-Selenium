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
        caps.setCapability("smartUI.project", "testng-smartui-project");
        caps.setCapability("selenium_version", "4.8.0");

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
        System.out.println("Loading Url");

        driver.get("https://guidesly.com/book-a-guide/guide-details/Tuna-Wahoo-Charters?tripDate=2023-04-21&guests=1");
        Thread.sleep(2000);
        scrollToBottom();

        driver.executeScript("smartui.takeFullPageScreenshot=fullpage");
        Thread.sleep(1000);

        System.out.println("TestFinished");

    }

    public void scrollToBottom() {
        try {
            Long height = getJavaScriptReturnValue("document.body.scrollHeight");
            System.out.println("height: " + height);
            int heightOfPage = height.intValue();
            System.out.println("heightOfPage: " + heightOfPage);

            int noOfLoop = heightOfPage / 400;
            for (int i = 0; i < noOfLoop; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + (i * 400) + ")");
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + heightOfPage + ")");
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
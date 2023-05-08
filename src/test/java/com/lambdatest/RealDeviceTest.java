package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.*;  
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.*;

public class RealDeviceTest {
    private String Status = "failed";
    private String userName = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
    private String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");


    @Test
    public void basicTest() throws Exception {

  
        DesiredCapabilities capabilities = new DesiredCapabilities();

        HashMap < String, Object > ltOptions = new HashMap < String, Object > ();
        ltOptions.put("w3c", true);
        ltOptions.put("platformName", "android");
        ltOptions.put("deviceName", "Pixel.*");

        ltOptions.put("isRealMobile", true);
        // ltOptions.put("app", "android_appurl");
        ltOptions.put("smartUI.project", "test");
        ltOptions.put("smartUI.baseline", true);

        capabilities.setCapability("lt:options", ltOptions);
        try{
        AndroidDriver driver = new AndroidDriver(
            new URL("https://" + userName + ":" + accessKey + "@mobile-hub.lambdatest.com/wd/hub"),
            capabilities);

                    System.out.println("Driver Initiated");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(2));
        // WebElement searchElement = (WebElement) wait
        //     .until(ExpectedConditions
        //         .elementToBeClickable(AppiumBy.accessibilityId("Search Wikipedia")));
        // searchElement.click();
        // WebElement insertTextElement = (WebElement) wait
        //     .until(ExpectedConditions.elementToBeClickable(
        //         AppiumBy.id("org.wikipedia.alpha:id/search_src_text")));
        // insertTextElement.sendKeys("LambdaTest");
        // Thread.sleep(5000);

        // List < WebElement > allProductsName = driver.findElements(AppiumBy.className("android.widget.TextView"));
        // assert(allProductsName.size() > 0);
        // System.out.println(driver.getContext());
        driver.executeScript("smartui.takeScreenshot=demo");

        Status = "passed";
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
        System.out.println("TestFinished");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}
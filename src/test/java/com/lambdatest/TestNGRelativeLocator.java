package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class TestNGRelativeLocator {

    private RemoteWebDriver driver;
    private String Status = "failed";

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        ;
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("version", "94");
        caps.setCapability("build", "demo jay");
        caps.setCapability("name", m.getName() + " - " + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");
        caps.setCapability("selenium_version", "4.0.0-beta-3");

        String[] Tags = new String[] { "Feature", "Falcon", "Severe" };
        caps.setCapability("tags", Tags);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);

    }

    @Test
    public void relativeLocatorTest() throws InterruptedException {

        driver.get("http://cookbook.seleniumacademy.com/mobilebmicalculator.html");

        // find the height and weight labels using css selector
        WebElement heightLabel = driver.findElement(By.cssSelector("label[for='heightCMS']"));
        WebElement weightLabel = driver.findElement(By.cssSelector("label[for='weightKg']"));

        // find the height input using toRightOf relative locator
        // input is right of height label
        WebElement heightInput =  driver.findElement(withTagName("input")
                .toRightOf(heightLabel));

        heightInput.sendKeys("181");
        Thread.sleep(3000);

        // find the weight input using combination of below and toRightOf relative locator
        // weight input is below height input and right of weight label
        WebElement weightInput =   driver.findElement(withTagName("input")
                .below(heightInput).toRightOf(weightLabel));

        weightInput.sendKeys("75");
        Thread.sleep(3000);

        // find the calculate button which is below the weight label
        WebElement calculateButton = driver.findElement(withTagName("input")
                .below(weightLabel));

        calculateButton.click();
        Thread.sleep(3000);

        // find the read only input below calculate button to verify value
        Assert.assertEquals("22.9", driver.findElement(withTagName("input")
                .below(calculateButton)).getAttribute("value"));
    
        Status = "passed";
        Thread.sleep(150);

        System.out.println("TestFinished");

    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }

}
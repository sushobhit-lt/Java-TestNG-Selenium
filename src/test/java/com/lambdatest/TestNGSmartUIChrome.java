package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestNGSmartUIChrome {

  private RemoteWebDriver driver;
  private String Status = "failed";
  private String githubURL = System.getenv("GITHUB_URL");
  public static String generatedString = RandomStringUtils.randomAlphanumeric(
    10
  );

  @BeforeMethod
  public void setup(Method m, ITestContext ctx) throws MalformedURLException {
    String username = System.getenv("LT_USERNAME") == null
      ? "Your LT Username"
      : System.getenv("LT_USERNAME");
    String authkey = System.getenv("LT_ACCESS_KEY") == null
      ? "Your LT AccessKey"
      : System.getenv("LT_ACCESS_KEY");
    String hub = "@hub.lambdatest.com/wd/hub";

    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platform", "Windows 10");
    caps.setCapability("browserName", "chrome");
    caps.setCapability("version", "latest");
    caps.setCapability("build", "TestNG With Java");
    caps.setCapability("name", m.getName() + " - " + this.getClass().getName());
    caps.setCapability("plugin", "git-testng");
    caps.setCapability("smartUI.project", "testng-smartui-web-project-parallel");
    caps.setCapability("smartUI.build", generatedString);
    caps.setCapability("selenium_version", "4.8.0");

    if (githubURL != null) {
      Map<String, String> github = new HashMap<String, String>();
      github.put("url", githubURL);
      caps.setCapability("github", github);
    }
    System.out.println(caps);
    driver =
      new RemoteWebDriver(
        new URL("https://" + username + ":" + authkey + hub),
        caps
      );
  }

  @Test
  public void basicTest() throws InterruptedException {
    String spanText;
    System.out.println("Loading Url");

    driver.get("https://www.lambdatest.com/");
    Thread.sleep(5000);

    driver.executeScript("smartui.takeScreenshot=home-page");
    Thread.sleep(1000);

    driver.get("https://www.lambdatest.com/pricing");
    Thread.sleep(5000);

    driver.executeScript("smartui.takeScreenshot=pricing-page");
    Thread.sleep(1000);

    driver.get("https://www.lambdatest.com/support/docs/");
    Thread.sleep(5000);
    driver.executeScript("smartui.takeScreenshot=docs");
    Status = "passed";
    System.out.println("TestNGSmartUIChrome TestFinished");
  }

  @AfterMethod
  public void tearDown() {
    driver.executeScript("lambda-status=" + Status);
    driver.quit();
  }
}

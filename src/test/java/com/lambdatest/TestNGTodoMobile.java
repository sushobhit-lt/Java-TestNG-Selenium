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

public class TestNGTodoMobile {

  private RemoteWebDriver driver;
  private String Status = "failed";

  @BeforeMethod
  public void setup(Method m, ITestContext ctx) throws MalformedURLException {
    String username = System.getenv("LT_USERNAME") == null
      ? "Your LT Username"
      : System.getenv("LT_USERNAME");
    String authkey = System.getenv("LT_ACCESS_KEY") == null
      ? "Your LT AccessKey"
      : System.getenv("LT_ACCESS_KEY");
    String hub = "@mobile-hub.lambdatest.com/wd/hub";
    DesiredCapabilities caps = new DesiredCapabilities();

    caps.setCapability("platformName", "ios");
    caps.setCapability("deviceName", "iPhone 12");
    caps.setCapability("platformVersion", "16");
    caps.setCapability("isRealMobile", true);
    caps.setCapability("visual", true);
    caps.setCapability("build", "TestNG With Java1");
    caps.setCapability("name", m.getName() + this.getClass().getName());
    caps.setCapability("smartUI.project", "testng-smartui-app-project");
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
    driver.get("https://lambdatest.github.io/sample-todo-app/");
    Thread.sleep(2000);
    System.out.println("Taking ToDo Page Screenshot");
    driver.executeScript("smartui.takeScreenshot=first");
    Status = "passed";
    System.out.println("TestFinished");
  }

  @AfterMethod
  public void tearDown() {
    driver.executeScript("lambda-status=" + Status);
    driver.quit();
  }
}

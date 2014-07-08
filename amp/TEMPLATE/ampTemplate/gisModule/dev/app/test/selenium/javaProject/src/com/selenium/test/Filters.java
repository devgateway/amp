package com.selenium.test;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class Filters {
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  protected static DesiredCapabilities dCaps;

	  @Before
	  public void setUp() throws Exception {
		  
		  dCaps = new DesiredCapabilities();
		  dCaps.setJavascriptEnabled(true);
		  dCaps.setCapability("takesScreenshot", false);
		  
		  //driver = new PhantomJSDriver(dCaps);
		  driver = new FirefoxDriver();		  
		  baseUrl = "http://localhost:3000/";
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  }

  @Test
  public void testFilters() throws Exception {
    driver.get(baseUrl + "/");
    Thread.sleep(1000);

    driver.findElement(By.cssSelector("#tool-filters > div.accordion-heading > a.accordion-toggle.collapsed > h3")).click();

    try {
      assertEquals("Sectors", driver.findElement(By.cssSelector("h4.filter-title > label")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Programs", driver.findElement(By.xpath("//div[@id='collapse-filters']/div/div[2]/div/div[2]/h4/label")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    
    driver.findElement(By.xpath("//div[@id='collapse-filters']/div/div[2]/div/div[3]/h4/label")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("Apply".equals(driver.findElement(By.xpath("(//button[@type='button'])[3]")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    try {
      assertTrue(isElementPresent(By.xpath("(//button[@type='button'])[3]")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

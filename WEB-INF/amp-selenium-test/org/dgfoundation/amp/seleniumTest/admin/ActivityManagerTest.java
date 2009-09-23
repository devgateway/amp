package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class ActivityManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(ActivityManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testActivityManager(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String actName = "Selenium Activity Manager Test " + testTime;
		selenium.open("/");
		selenium.type("j_username", "UATtl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"javascript:addActivity()\")]")) {
			selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
			selenium.waitForPageToLoad("120000");
			try {
				selenium.select("planning.statusId", "index=1");
			} catch (Exception e) {
				logger.info("Field \"Status\" is not available.");
			}
			
			selenium.click("//a[@href='javascript:gotoStep(2)']");
			selenium.waitForPageToLoad("50000");
			
			//Add Primary Sector
			try {
				selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,1);']");
				SeleniumTestUtil.waitForElement(selenium,"sector", 90);
				try {
					selenium.select("sector", "index=1");
					Thread.sleep(5000);
					selenium.click("addButton");
					selenium.waitForPageToLoad("50000");					
				} catch (Exception e) {
					logger.info("Sectors no found for Primary Sector");
				}
			} catch (Exception e) {
				logger.info("Option \"Add Primary Sector\" is not available.");
			}
			try {
				selenium.type("activitySectors[0].sectorPercentage", "100");
			} catch (Exception e) {}
			//Add Secondary Sector
			try {
				selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,2);']");
				SeleniumTestUtil.waitForElement(selenium,"sector", 90);
				try {
					selenium.select("sector", "index=1");
					Thread.sleep(5000);
					selenium.click("addButton");
					selenium.waitForPageToLoad("50000");					
				} catch (Exception e) {
					logger.info("Sectors no found for Secondary Sector");
				}
			} catch (Exception e) {
				logger.info("Option \"Add Secondary Sector\" is not available.");
			}
			try {
				selenium.type("activitySectors[1].sectorPercentage", "100");
			} catch (Exception e) {}
			selenium.click("//input[@onclick='saveClicked()']");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			
			selenium.type("j_username", "admin@amp.org");
			selenium.type("j_password", "admin");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			if (selenium.isElementPresent("//a[contains(@href, \"/aim/activityManager.do\")]")) {
				selenium.click("//a[contains(@href, \"/aim/activityManager.do\")]");
				selenium.waitForPageToLoad("30000");
				selenium.type("keyword", actName);
				selenium.click("//input[@onclick=\"return searchActivity()\"]");
				selenium.waitForPageToLoad("30000");
				selenium.click("//a[@onclick=\"return deleteIndicator()\"]");
				selenium.getConfirmation();
				selenium.waitForPageToLoad("30000");
			} else {
				logger.info("Activity Manager is not available");
			}
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Activity Manager Test Finished Successfully");
	}
}

package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class CalendarManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(CalendarManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testCalendarManager(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String calendarName ="Calendar Test " + testTime;
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/aim/fiscalCalendarManager.do\")]")) {
			selenium.click("//a[contains(@href, \"/aim/fiscalCalendarManager.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/editFiscalCalendar.do~action=create\")]");
			selenium.waitForPageToLoad("30000");
			selenium.type("fiscalCalName", calendarName);
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("link="+calendarName);
			selenium.waitForPageToLoad("30000");
			selenium.type("startMonthNum", "6");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/GlobalSettings.do\")]");
			selenium.waitForPageToLoad("30000");
			int cnt = 2;
			boolean done = false;
			while (!done) {
				if (selenium.getAttribute("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Default Calendar")) {
					selenium.select("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/select", calendarName);
					done = true;
				}
				cnt++;
				if (cnt==10) {
					done = true;
				}
			}
			selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/fiscalCalendarManager.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("link="+calendarName);
			selenium.waitForPageToLoad("30000");
			selenium.click("//input[@onclick=\"msg()\"]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
			assertTrue(!selenium.isElementPresent("link="+calendarName));
		} else {
			logger.info("Calendar Manager is not available");
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Calendar Manager Test Finished Successfully");
	}
}

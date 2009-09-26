package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class EventTypeManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(EventTypeManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testEventTypeManager(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String typeName ="Type Test " + testTime;
		
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/calendar/eventTypes.do\")]")) {
			selenium.click("//a[contains(@href, \"/calendar/eventTypes.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.type("eventTypeName", typeName);
			int hexColor = 123456;
			int cnt = 1;
			while (selenium.isElementPresent("eventTypeNameColor"+cnt)) {
				if (selenium.getAttribute("eventTypeNameColor"+cnt+"@value").equals("#"+hexColor)) {
					hexColor++;
					cnt=1;
				}
				cnt++;				
			}
			selenium.type("hexColorNum", "#"+hexColor);
			selenium.click("//input[@onclick=\"setActionMethod('addType')\"]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			
			selenium.type("j_username", "UATtl@amp.org");
			selenium.type("j_password", "abc");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=UAT Management Workspace");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]");
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.isTextPresent(typeName));
			assertTrue(selenium.isElementPresent("//div[@style=\"border: 1px solid Black; height: 15px; width: 24px; background-color: rgb(18, 52, 86);\"]"));
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			
			selenium.type("j_username", "admin@amp.org");
			selenium.type("j_password", "admin");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/calendar/eventTypes.do\")]");
			selenium.waitForPageToLoad("30000");
			cnt = 2;
			boolean done = false;
			while (!done) {
				if (selenium.getAttribute("//table[@id=\"dataTable\"]/tbody/tr["+cnt+"]/td[1]/input@value").equals(typeName)) {
					selenium.click("//table[@id=\"dataTable\"]/tbody/tr["+cnt+"]/td[6]/input");
					selenium.getConfirmation();
					done = true;
					selenium.waitForPageToLoad("30000");			
				}
				cnt++;
				if (cnt==1000) {
					done = true;
				}
			}
		} else {
			logger.info("Event Type Manager is not available");
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Event Type Manager Test Finished Successfully");
		
	}
}


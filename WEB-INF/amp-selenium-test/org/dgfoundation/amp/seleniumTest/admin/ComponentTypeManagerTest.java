package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class ComponentTypeManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(ComponentTypeManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testComponentTypeManager(Selenium selenium) throws Exception {
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/aim/updateComponentType.do\")]")) {
			selenium.click("//a[contains(@href, \"/aim/updateComponentType.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("submitButton");
			//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "30000");
			Thread.sleep(10000);
			selenium.selectWindow(selenium.getAllWindowTitles()[1]);
			selenium.type("name", "Selenium Component Type");
			selenium.type("code", "SCT");
			selenium.click("addBtn");
			selenium.selectWindow("null");
			selenium.waitForPageToLoad("30000");
			int tId = 0;
			for (int i = 200; i > 0; i--) {
				if (selenium.isElementPresent("//a[contains(@href, 'javascript:editType("+i+")')]")) {
					tId = i;
					break;
				}
			}
			selenium.click("//a[contains(@href, 'javascript:editType("+tId+")')]");
			//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "30000");
			Thread.sleep(10000);
			selenium.selectWindow(selenium.getAllWindowTitles()[1]);
			selenium.type("name", "Selenium Component Type 1");
			selenium.click("addBtn");
			selenium.selectWindow("null");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			
			selenium.type("j_username", "UATtl@amp.org");
			selenium.type("j_password", "abc");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=UAT Team Workspace");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
			selenium.waitForPageToLoad("30000");
			if (selenium.isElementPresent("//a[@href='javascript:gotoStep(5)']")) {
				selenium.click("//a[@href='javascript:gotoStep(5)']");
				selenium.waitForPageToLoad("30000");
				selenium.click("//input[@onclick=\"addComponents()\"]");
				Thread.sleep(5000);
				selenium.select("selectedType", "Selenium Component Type 1");
				selenium.type("newCompoenentName", "Selenium Component");
				selenium.click("//div[@id='new']/div[3]");				
			}
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			
			selenium.type("j_username", "admin@amp.org");
			selenium.type("j_password", "admin");
			selenium.click("submitButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/updateComponentType.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, 'javascript:deleteType("+tId+");')]");
			selenium.getConfirmation();
		} else {
			logger.info("Component Type Manager is not available");
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Component Type Manager Test Finished Successfully");

	}
}


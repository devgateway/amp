package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class CategoryManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(CategoryManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testCategoryManager(Selenium selenium) throws Exception {
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/categorymanager/categoryManager.do\")]");
		selenium.waitForPageToLoad("30000");
		boolean done = false;
		int cnt = 0;
		int catId = 0;
		while (!done) {
			if (selenium.isElementPresent("//a[contains(@href, \"/categorymanager/categoryManager.do~edit="+cnt+"\")]")) {
				selenium.click("//a[contains(@href, \"/categorymanager/categoryManager.do~edit="+cnt+"\")]");
				catId = cnt;
				done = true;
			} else {
				cnt++;
			}
		}
		selenium.waitForPageToLoad("30000");
		String catName = selenium.getValue("categoryName");
		selenium.type("categoryName", catName + " Selenium");
		selenium.click("//button[@type='button' and @onclick='addNewValue(-1)']");
		selenium.waitForPageToLoad("30000");
		done = false;
		cnt = 0;
		int typeId = 0;
		while (!done) {
			if (selenium.isElementPresent("field"+cnt)) {
				String readonly = "";
				try {
					readonly = selenium.getAttribute("field"+cnt+"@readonly");
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!readonly.equalsIgnoreCase("readonly")) {
					selenium.type("field"+cnt, "SeleniumType");
					typeId = cnt;
					done = true;
				} 
			}
			cnt++;
		}
		selenium.click("//button[@type='submit']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent(catName+" Selenium"));
		assertTrue(selenium.isTextPresent("SeleniumType"));
		selenium.click("//a[contains(@href, \"/categorymanager/categoryManager.do~edit="+catId+"\")]");
		selenium.waitForPageToLoad("30000");
		selenium.type("categoryName", catName);
		selenium.click("//span[@id='delete"+typeId+"']/a/img");
		selenium.getConfirmation();
		selenium.click("//button[@type='submit']");
		selenium.waitForPageToLoad("30000");
		assertTrue(!selenium.isTextPresent(catName+" Selenium"));
		assertTrue(!selenium.isTextPresent("SeleniumType"));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Category Manager Test Finished Successfully");

	}
}


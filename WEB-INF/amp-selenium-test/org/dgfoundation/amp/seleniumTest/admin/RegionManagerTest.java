package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class RegionManagerTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(RegionManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testRegionManager(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String countryName = "Selenium Country";
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/aim/dynLocationManager.do\")]")) {
			selenium.click("//a[contains(@href, \"/aim/dynLocationManager.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//ul[@id=\"tree_ul_0\"]/li/img[3]");
			selenium.waitForPageToLoad("30000");
			selenium.type("name", countryName);
			selenium.type("iso", "SL");
			selenium.type("iso3", "SLM");
			selenium.click("//input[@onclick=\"addLoc()\"]");
			selenium.waitForPageToLoad("30000");
			selenium.click("hide_empty_countries");
			selenium.waitForPageToLoad("30000");
			int cnt = 1;
			boolean done = false;
			int li = 0;
			while (!done) {
				if (selenium.getText("//ul[@id=\"tree_ul_1\"]/li["+cnt+"]/a").equals(countryName)) {				
					li = cnt;
					done = true;
				}
				cnt++;
				if (cnt>500) {
					done = true;
				}
			}
			if (li>0) {
				selenium.click("//ul[@id=\"tree_ul_1\"]/li["+li+"]/img[3]");
				selenium.waitForPageToLoad("30000");
				selenium.type("name", "Sel Region");
				selenium.click("//input[@onclick=\"addLoc()\"]");
				selenium.waitForPageToLoad("30000");
				selenium.click("hide_empty_countries");
				selenium.waitForPageToLoad("30000");
				selenium.click("//ul[@id=\"tree_ul_1\"]/li["+li+"]/ul/li/img[3]");
				selenium.waitForPageToLoad("30000");
				selenium.type("name", "Sel Zone");
				selenium.click("//input[@onclick=\"addLoc()\"]");
				selenium.waitForPageToLoad("30000");
				selenium.click("hide_empty_countries");
				selenium.waitForPageToLoad("30000");
				selenium.click("//ul[@id=\"tree_ul_1\"]/li["+li+"]/ul/li/ul/li/img[3]");
				selenium.waitForPageToLoad("30000");
				selenium.type("name", "Sel District");
				selenium.click("//input[@onclick=\"addLoc()\"]");
				selenium.waitForPageToLoad("30000");
				selenium.click("hide_empty_countries");
				selenium.waitForPageToLoad("30000");
				
				selenium.click("//ul[@id=\"tree_ul_1\"]/li["+li+"]/img[5]");
				selenium.getConfirmation();
				selenium.waitForPageToLoad("30000");
				selenium.click("hide_empty_countries");
				selenium.waitForPageToLoad("30000");
				assertTrue(!selenium.isTextPresent(countryName));
			}
		} else {
			logger.info("Region Manager is not available");
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Region Manager Test Finished Successfully");

	}
}


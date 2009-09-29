package org.dgfoundation.amp.seleniumTest;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class SeleniumFeaturesConfiguration extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(SeleniumFeaturesConfiguration.class);
	
	private static Hashtable<String, Boolean> moduleVisibility = new Hashtable<String, Boolean>();
	private static Hashtable<String, Boolean> featureVisibility = new Hashtable<String, Boolean>();
	private static Hashtable<String, Boolean> fieldVisibility = new Hashtable<String, Boolean>();

	public static void getConfigurationFromFM(Selenium selenium) throws Exception {
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/visibilityManager.do\")]");
		selenium.waitForPageToLoad("30000");
		boolean done = false;
		int cnt = 0;
		while (!done) {
			if (selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]") && !selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=delete\")]")) {
				selenium.click("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]");
				selenium.waitForPageToLoad("30000");
				done = true;
			}
			cnt++;
		}
		int modulesCounter = 0;
		int featuresCounter = 0;
		int fieldsCounter = 0;
		for (int i = 0; i < 1000; i++) {
			if (selenium.isElementPresent("moduleVis:"+i)) {
				moduleVisibility.put(selenium.getText("module:"+i), selenium.isChecked("moduleVis:"+i));
				modulesCounter++;
			}
			if (selenium.isElementPresent("featureVis:"+i)) {
				featureVisibility.put(selenium.getText("feature:"+i), selenium.isChecked("featureVis:"+i));
				featuresCounter++;
			}
			if (selenium.isElementPresent("fieldVis:"+i)) {
				fieldVisibility.put(selenium.getText("field:"+i), selenium.isChecked("fieldVis:"+i));
				fieldsCounter++;
			}			
		}
		logger.info("Modules found: " + modulesCounter);
		logger.info("Features found: " + featuresCounter);
		logger.info("Fields found: " + fieldsCounter);
	}
	
	public static boolean getModuleState(String key) throws Exception {
		if (moduleVisibility.containsKey(key)) {
			return moduleVisibility.get(key);
		} else {
			return false;
		}
	}
	
	public static boolean getFeatureState(String key) throws Exception {
		if (featureVisibility.containsKey(key)) {
			return featureVisibility.get(key);
		} else {
			return false;
		}
	}
	
	public static boolean getFieldState(String key) throws Exception {
		if (fieldVisibility.containsKey(key)) {
			return fieldVisibility.get(key);
		} else {
			return false;
		}
	}
}

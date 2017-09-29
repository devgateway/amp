package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class IndicatorManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(IndicatorManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testIndicatorManager(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String indicName ="Indicator Test " + testTime;
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (SeleniumFeaturesConfiguration.getModuleState("National Planning Dashboard")){
            if (selenium.isElementPresent("//a[contains(@href, \"/aim/viewIndicators.do\")]")) {
                selenium.click("//a[contains(@href, \"/aim/viewIndicators.do\")]");
                selenium.waitForPageToLoad("30000");
                if (SeleniumFeaturesConfiguration.getFieldState("Add New Indicator")){
                    if (selenium.isElementPresent("addBtn")) {
                        selenium.click("addBtn");
                        Thread.sleep(10000);
                        String title = selenium.getAllWindowTitles()[1];
                        selenium.selectWindow(title);
                        selenium.type("txtName", indicName);
                        selenium.type("txtCode", "TS");
                        selenium.click("//input[@onclick=\"addSectors();\"]");
                        Thread.sleep(10000);
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                        selenium.select("sector", "index=1");
                        selenium.click("submitButton");
                        selenium.selectWindow(selenium.getAllWindowTitles()[0]);
                        Thread.sleep(10000);
                        //selenium.waitForPageToLoad("30000");
                        if (SeleniumFeaturesConfiguration.getFieldState("Add New Indicator")){
                            if (selenium.isElementPresent("submitButton")) {
                                selenium.click("submitButton");
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("30000");
                                String iId = selenium.getAttribute("link="+indicName+"@href");
                                iId = iId.substring(iId.indexOf("or('")+4, iId.indexOf("')"));
                                selenium.click("//a[contains(@href, \"/aim/removeIndicator.do~indicatorId="+iId+"\")]");
                                selenium.getConfirmation();
                                selenium.waitForPageToLoad("30000");
                                if (selenium.isElementPresent("link="+indicName)) {
                                    logger.error("Indicator added is not in the list");
                                }
                            } else {
                                selenium.close();
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("30000");
                                logger.error("Field \"Add New Indicator\" is active in Feature Manager but is not available.");
                            }
                        } else {
                            selenium.close();
                            selenium.selectWindow("null");
                            selenium.waitForPageToLoad("30000");
                            logger.info("Field \"Add New Indicator\" is not available.");
                        }
                    } else {
                        logger.error("Field \"Add New Indicator\" is active in Feature Manager but is not available.");
                    }
                } else {
                    logger.info("Field \"Add New Indicator\" is not available.");
                }
            } else {
                logger.error("Module \"National Planning Dashboard\" is active in Feature Manager but is not available.");
            }
        } else {
            logger.info("Module \"National Planning Dashboard\" is not available.");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Indicator Manager Test Finished Successfully");

    }
}

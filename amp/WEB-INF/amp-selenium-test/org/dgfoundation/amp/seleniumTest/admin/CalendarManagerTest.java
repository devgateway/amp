package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class CalendarManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(CalendarManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testCalendarManager(LoggingSelenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String calendarName ="Calendar Test " + testTime;
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (SeleniumFeaturesConfiguration.getModuleState("Fiscal Calendar Manager")){
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
                        try {
                            selenium.select("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/select", calendarName);
                        } catch (Exception e) {
                            logger.error("Calendar just added is not present in Default Calendar options on Global Settings");
                            selenium.logAssertion("assertTrue", "Calendar just added is not present in Default Calendar options on Global Settings", "condition=false");
                        }
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
                if (selenium.isElementPresent("link="+calendarName)) {
                    logger.error("Calendar wasn't deleted from Calendar Manager");
                    selenium.logAssertion("assertTrue", "Calendar wasn't deleted from Calendar Manager", "condition=false");
                }
                
            } else {
                logger.error("Module \"Fiscal Calendar Manager\" is active in Feature Manager but is not available.");
                selenium.logAssertion("assertTrue", "Module \"Fiscal Calendar Manager\" is active in Feature Manager but is not available.", "condition=false");
            }
        } else {
            logger.info("Module \"Fiscal Calendar Manager\" is not available.");
            selenium.logComment("Module \"Fiscal Calendar Manager\" is not available.");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Calendar Manager Test Finished Successfully");
        selenium.logComment("Calendar Manager Test Finished Successfully");
    }
}

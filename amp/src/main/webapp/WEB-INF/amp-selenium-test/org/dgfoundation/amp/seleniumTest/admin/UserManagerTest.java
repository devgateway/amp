package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class UserManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(UserManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testUserManager(LoggingSelenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/um/viewAllUsers.do~reset=true\")]")) {
            selenium.click("//a[contains(@href, \"/um/viewAllUsers.do~reset=true\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", "uattl@amp.org");
            selenium.click("//input[@type=\"submit\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Edit user");
            selenium.waitForPageToLoad("30000");
            selenium.type("newPassword", "123");
            selenium.type("confirmNewPassword", "123");
            selenium.click("//input[@onclick=\"validate('Either fields are blank or their values do not match','changePassword');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "uattl@amp.org");
            selenium.type("j_password", "123");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/um/viewAllUsers.do~reset=true\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", "uattl@amp.org");
            selenium.click("//input[@type=\"submit\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Edit user");
            selenium.waitForPageToLoad("30000");
            selenium.type("newPassword", "abc");
            selenium.type("confirmNewPassword", "abc");
            selenium.click("//input[@onclick=\"validate('Either fields are blank or their values do not match','changePassword');\"]");
            selenium.waitForPageToLoad("30000");
        } else {
            logger.error("Module \"User Manager\" is not available.");
            selenium.logAssertion("assertTrue", "Module \"User Manager\" is not available.", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("User Manager Test Finished Successfully");
        selenium.logComment("User Manager Test Finished Successfully");
    }
}


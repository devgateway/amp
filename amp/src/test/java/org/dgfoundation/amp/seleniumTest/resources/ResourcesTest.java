package org.dgfoundation.amp.seleniumTest.resources;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class ResourcesTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(ResourcesTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testResources(Selenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/contentrepository/documentManager.do\")]")) {
            selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button' and @onclick=\"setType('private');configPanel(0,'','','', false); showMyPanel(0, 'addDocumentDiv'); \"]");
            Thread.sleep(5000);
            selenium.click("//input[@name='webResource' and @value='true']");
            selenium.type("docTitle", "Test Resource");
            selenium.type("docDescription", "Test created by TL");
            selenium.type("webLink", "www.google.com");
            selenium.click("//input[@onclick=\"return validateAddDocumentLocal()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//li[@id='tab2']/a/div");
            selenium.click("//button[@type='button' and @onclick=\"setType('team'); configPanel(0,'','','', false);showMyPanel(0, 'addDocumentDiv');\"]");
            selenium.click("//input[@name='webResource' and @value='true']");
            selenium.type("docTitle", "Test Team Resource");
            selenium.type("docDescription", "Team Resource created by TL");
            selenium.type("webLink", "www.yahoo.com");
            selenium.click("//input[@onclick=\"return validateAddDocumentLocal()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "uattm@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button' and @onclick=\"setType('private');configPanel(0,'','','', false); showMyPanel(0, 'addDocumentDiv'); \"]");
            Thread.sleep(5000);
            selenium.click("//input[@name='webResource' and @value='true']");
            selenium.type("docTitle", "Test Resource TM");
            selenium.type("docDescription", "created by a TM");
            selenium.type("webLink", "http://docs.ampdev.net");
            selenium.click("//input[@onclick=\"return validateAddDocumentLocal()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//li[@id='tab2']/a/div");
            if (selenium.isElementPresent("//button[@type='button' and @onclick=\"setType('team'); configPanel(0,'','','', false);showMyPanel(0, 'addDocumentDiv');\"]")) {
                logger.error("Error on resources shown");
                //selenium.logAssertion"assertTrue", "Error on resources shown", "condition=false");
            }
            if (!selenium.isElementPresent("link=exact:http://www.yahoo.com")) {
                logger.error("Link added is not available");
                //selenium.logAssertion"assertTrue", "Link added is not available", "condition=false");
            }
            selenium.click("//li[@id='tab1']/a/div");
            String rId = selenium.getAttribute("//a[@onclick=\"window.open('http://docs.ampdev.net')\" and @style=\"cursor: pointer; text-decoration: underline; color: blue;\"]@id");
            rId = rId.substring(1);
            selenium.click("//li[@id='tab1']/a/div");
            selenium.click("//a[@id='plus"+rId+"']/img");
            Thread.sleep(3000);
            selenium.type("webLink", "http://ampdev.net");
            selenium.click("//input[@onclick=\"return validateAddDocumentLocal()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@id='H"+rId+"']/img");
            Thread.sleep(3000);
            if (!selenium.isElementPresent("link=exact:http://docs.ampdev.net")) {
                logger.error("Link added is not available");
                //selenium.logAssertion"assertTrue", "Link added is not available", "condition=false");
            }
            selenium.click("//a[@id='a"+rId+"']/img");
            selenium.getConfirmation();
            Thread.sleep(5000);
            selenium.click("//div[@id='aPanel1']/span");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
            selenium.waitForPageToLoad("30000");
            rId = selenium.getAttribute("//a[@onclick=\"window.open('http://www.google.com')\" and @style=\"cursor: pointer; text-decoration: underline; color: blue;\"]@id");
            rId = rId.substring(1);
            selenium.click("//a[@id='a"+rId+"']/img");
            selenium.getConfirmation();
            Thread.sleep(5000);
            selenium.click("//li[@id='tab2']/a/div");
            rId = selenium.getAttribute("//a[@onclick=\"window.open('http://www.yahoo.com')\" and @style=\"cursor: pointer; text-decoration: underline; color: blue;\"]@id");
            rId = rId.substring(1);
            selenium.click("//a[@id='a"+rId+"']/img");
            selenium.getConfirmation();
            Thread.sleep(5000);
        } else {
            logger.error("Option 'RESOURCES' is not available.");
            //selenium.logAssertion"assertTrue", "Option 'RESOURCES' is not available.", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Resources Test Finished Successfully");
       //selenium.logComment("Resources Test Finished Successfully");
    }
}

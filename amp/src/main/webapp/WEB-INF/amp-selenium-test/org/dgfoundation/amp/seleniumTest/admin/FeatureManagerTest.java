package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.publicView.PublicViewTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class FeatureManagerTest extends SeleneseTestCase {
        
    private static Logger logger = Logger.getLogger(FeatureManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testFeatureManager(LoggingSelenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String templateName ="Test Template " + testTime;
        String actualTemplate = "";
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/visibilityManager.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/visibilityManager.do~action=add\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("templateName", templateName);
        selenium.click("newTemplate");
        selenium.waitForPageToLoad("30000");
        String tId = selenium.getAttribute("link="+templateName+"@href");
        tId = tId.substring(tId.indexOf("eId=")+4, tId.indexOf("~action"));
        
        selenium.click("link="+templateName);
        selenium.waitForPageToLoad("30000");
        for (int i = 0; i < 14; i++) {
            if (selenium.isElementPresent("//a[@indextab='"+i+"']")) {
                String index = selenium.getAttribute("//a[@indextab='"+i+"']@href");
                index = index.substring(index.indexOf("dule")+4);
                selenium.click("moduleVis:"+index);
            }       
        }
        selenium.click("//li[@title='Project Title']/input");       
        selenium.click("//li[@title='Tab Generator']/input");       
        selenium.click("saveTreeVisibility");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/GlobalSettings.do\")]");
        selenium.waitForPageToLoad("30000");
        int cnt = 2;
        boolean done = false;
        while (!done) {
            if (selenium.getAttribute("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Visibility Template")) {
                actualTemplate = selenium.getSelectedIndex("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select");
                selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", templateName);
                done = true;
            }
            cnt++;
            if (cnt==20) {
                done = true;
            }
        }
        selenium.click("saveAll");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, "
                + "\"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=T\")]")) {
            logger.error("Add Tab option is disabled from Feature Manager but is available");
            selenium.logAssertion("assertTrue", "Add Tab option is disabled from Feature Manager but is available", "condition=false");
        }
        selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
        selenium.waitForPageToLoad("120000");
        if (selenium.getAttribute("identification.title@disabled").equalsIgnoreCase("disabled")) {
            logger.error("Field Title is disabled Feature Manager but is available on Activity form");
            selenium.logAssertion("assertTrue", "Field Title is disabled Feature Manager but is available on Activity form", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/GlobalSettings.do\")]");
        selenium.waitForPageToLoad("30000");
        cnt = 2;
        done = false;
        while (!done) {
            if (selenium.getAttribute("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Visibility Template")) {
                selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index="+actualTemplate);
                done = true;
            }
            cnt++;
            if (cnt==20) {
                done = true;
            }
        }
        selenium.click("saveAll");
        selenium.waitForPageToLoad("30000");
        
        selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/visibilityManager.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+tId+"~action=delete\")]");
        selenium.getConfirmation();
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("link="+templateName)) {
            logger.error("Template wasn�t deleted");
            selenium.logAssertion("assertTrue", "Template wasn�t deleted", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Feature Manager Test Finished Successfully");
        selenium.logComment("Feature Manager Test Finished Successfully");
    }
}

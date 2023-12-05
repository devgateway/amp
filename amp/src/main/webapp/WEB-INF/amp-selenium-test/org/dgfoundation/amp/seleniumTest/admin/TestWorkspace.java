package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class TestWorkspace extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(TestWorkspace.class);
    
    public void setUp() throws Exception {
        //setUp("http://localhost:8080/", "*chrome");
        setUp("http://senegal.staging.ampdev.net/", "*chrome");     
    }
    
    public void testAddWorkspace(Selenium selenium) throws Exception {
        
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/j_spring_logout\")]")) {
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("50000");
        }
        
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"/aim/workspaceManager.do~page=1\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("keyword", "Workspace for Selenium tests");
        selenium.click("//input[@class=\"dr-menu\" and @type=\"submit\"]");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("link=Workspace for Selenium tests")) {
            selenium.click("//a[contains(@href, \"/aim/createWorkspace.do~dest=admin\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("teamName", "Workspace for Selenium tests");
            selenium.select("workspaceType", "index=2");
            selenium.type("description", "Workspace for Selenium tests");
            selenium.click("//input[@onclick=\"update('add')\"]");
            selenium.waitForPageToLoad("30000");
            String val = selenium.getAttribute("link=Workspace for Selenium tests@href");
            String teamId = val.substring(val.indexOf("tId")+4,val.indexOf("~event"));
            selenium.click("//a[contains(@href, \"/aim/teamMembers.do~teamId=" + teamId + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + teamId + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=atl@amp.org");
            selenium.select("role", "index=1");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
        } else {
            logger.info("Workspace for Selenium tests already exists.");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
    }

    public void runTest() {
        try {
            testAddWorkspace(selenium);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        TestWorkspace tst = new TestWorkspace();
        try {
            tst.setUp();
            tst.runTest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }

}

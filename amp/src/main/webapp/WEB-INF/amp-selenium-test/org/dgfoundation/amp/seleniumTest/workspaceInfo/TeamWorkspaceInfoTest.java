package org.dgfoundation.amp.seleniumTest.workspaceInfo;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class TeamWorkspaceInfoTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(TeamWorkspaceInfoTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testTeamWorkspaceInfo(LoggingSelenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]")) {
            selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
            selenium.waitForPageToLoad("30000");
            String currId1 = selenium.getSelectedLabel("currencyId");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
            selenium.waitForPageToLoad("30000");
            String currId2 = selenium.getSelectedLabel("currencyId");
            
            selenium.select("currencyId", "index=2");
            String currId3 = selenium.getSelectedLabel("currencyId");
            
            selenium.click("//input[@onclick='validade();']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "uattm@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
            selenium.waitForPageToLoad("120000");
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(3)']")) {
                selenium.click("//a[@href='javascript:gotoStep(3)']");
                selenium.waitForPageToLoad("50000");
                selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(5000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);        
                SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                selenium.click("//input[@onclick='return searchOrganization()']");
                SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                selenium.click("selOrganisations"); 
                selenium.click("//input[@onclick='return selectOrganization()']");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("50000");
                String val = selenium.getAttribute("selFundingOrgs@value");
                selenium.click("//input[@onclick=\"addFunding('" + val + "')\"]"); 
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.click("//input[@onclick='addFundingDetail(0)']");
                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[0].currencyCode", 90);
                if (!selenium.getSelectedValue("fundingDetail[0].currencyCode").equals(currId3)) {
                    logger.error("Currency is not according default settings");
                    //selenium.logAssertion"assertTrue", "Currency is not according default settings", "condition=false");
                }
                selenium.click("//input[@onclick=\"closeWindow()\"]");
                selenium.selectWindow("null");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
                
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace - Computed");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
            selenium.waitForPageToLoad("30000");
            if (!selenium.getSelectedLabel("currencyId").equals(currId1)) {
                logger.error("Currency is not according default settings");
                //selenium.logAssertion"assertTrue", "Currency is not according default settings", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("currencyId", "label="+currId2);
            selenium.click("//input[@onclick='validade();']");
            selenium.waitForPageToLoad("30000");
        } else {
            logger.warn("Workspace Info is not available");
           //selenium.logComment("Workspace Info is not available");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Workspace Info Finished Successfully");
       //selenium.logComment("Workspace Info Finished Successfully");
    }
}

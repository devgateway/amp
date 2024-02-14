package org.dgfoundation.amp.seleniumTest.admin;

import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class OrganizationManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(OrganizationManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testOrganizationManager(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String organizationName = "Selenium Organization " + testTime;
        String orgTypeName = "Selenium Type Org " + testTime;
        String orgGroupName = "Selenium Group Org " + testTime;
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/organisationManager.do~orgSelReset=true\")]")) {
            selenium.click("//a[contains(@href, \"/aim/organisationManager.do~orgSelReset=true\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/orgTypeManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/editOrgType.do~action=create\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("orgType", orgTypeName);
            selenium.type("orgTypeCode", "STO");
            selenium.click("//input[@onclick=\"check()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/organisationManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/orgGroupManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/editOrgGroup.do~action=create\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("orgGrpName", orgGroupName);
            selenium.type("orgGrpCode", "SOG");
            selenium.select("orgTypeId", "label="+orgTypeName);
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/organisationManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/editOrganisation.do~actionFlag=create~mode=resetMode\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("name", organizationName);
            selenium.type("acronym", "selorg");
            selenium.select("ampOrgTypeId", "label="+orgTypeName);
            Thread.sleep(5000);
            selenium.select("ampOrgGrpId", "label="+orgGroupName);
            selenium.type("orgCode", testTime);
            selenium.type("budgetOrgCode", testTime);
            selenium.click("//input[@onclick=\"return check()\"]");
            selenium.waitForPageToLoad("30000");
            //selenium.select("tempNumResults", "label=All");
            //selenium.click("//input[@value='GO']");
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            
            selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
            selenium.waitForPageToLoad("120000");
            if (selenium.isElementPresent("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitAfterSelectingOrg();~PARAM_COLLECTION_NAME=selectedOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]")) {
                selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitAfterSelectingOrg();~PARAM_COLLECTION_NAME=selectedOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(5000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
               
                selenium.select("ampOrgTypeId", "label="+orgTypeName);
                selenium.type("keyword", organizationName);
                selenium.click("//input[@onclick=\"return searchOrganization()\"]");
                SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                selenium.click("selOrganisations"); 
                selenium.click("//input[@onclick='return selectOrganization()']");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("50000");
                
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/organisationManager.do~orgSelReset=true\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", organizationName);
            selenium.click("//input[@onclick=\"return searchOrganization()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+organizationName);
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@onclick=\"return msg()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("link="+organizationName)) {
                logger.error("Organization wasn't deleted");
                //selenium.logAssertion"assertTrue", "Organization wasn't deleted", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/aim/orgGroupManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", orgGroupName);
            selenium.click("//input[@onclick=\"return searchOrganization()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+orgGroupName);
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@onclick=\"msg()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("link="+orgGroupName)) {
                logger.error("Organization Group wasn't deleted");
                //selenium.logAssertion"assertTrue", "Organization Group wasn't deleted", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/aim/organisationManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/orgTypeManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+orgTypeName);
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@onclick=\"msg()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("link="+orgTypeName)) {
                logger.error("Organization Type wasn't deleted");
                //selenium.logAssertion"assertTrue", "Organization Type wasn't deleted", "condition=false");
            }
        } else {
            logger.error("Organization Manager is not available.");
            //selenium.logAssertion"assertTrue", "Organization Manager is not available.", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Organization Manager Test Finished Successfully");
       //selenium.logComment("Organization Manager Test Finished Successfully");
    }
}


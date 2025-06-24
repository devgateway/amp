package org.dgfoundation.amp.seleniumTest;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class SeleniumTestUtil extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(SeleniumTestUtil.class);
    public static final String VERSION_HEAD = "1.14";
    public static final String VERSION_BRANCH = "1.13";
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*firefox");
//      setUp("http://senegal.staging.ampdev.net/", "*chrome");
    }
    
    public static void waitForElement (Selenium sel, String element, int seconds){
        for (int second = 0; second < seconds; second++) {
            //if (second >= seconds) fail("timeout");
            try {
                if (sel.isElementPresent(element)) 
                    break; 
            } 
            catch (Exception e) {
                logger.info("Error on waitForElement");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void setupUsersAndWorkspace(Selenium selenium) throws InterruptedException {
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        String version = selenium.getText("//div[@class=\"footerText\"]");
        version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);      
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/organisationManager.do~orgSelReset=true\")]");
        selenium.waitForPageToLoad("30000");
        selenium.select("tempNumResults", "value=-1");
        selenium.click("//input[@onclick='return searchOrganization()']");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("link=World Bank")) {
            selenium.click("//a[contains(@href, \"/aim/editOrganisation.do~actionFlag=create~mode=resetMode\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("name", "World Bank");
            selenium.type("acronym", "WB");
            selenium.select("ampOrgTypeId", "index=1");
            Thread.sleep(10000);
            selenium.select("ampOrgGrpId", "index=1");
            selenium.type("orgCode", "123");
            selenium.type("budgetOrgCode", "123");
            selenium.click("//input[@onclick='return check()']");
            selenium.waitForPageToLoad("30000");        
        }
        selenium.select("tempNumResults", "value=-1");
        selenium.click("//input[@onclick='return searchOrganization()']");
        selenium.waitForPageToLoad("30000");
        String orgHref = selenium.getAttribute("link=World Bank@href");
        String orgId = orgHref.substring(orgHref.indexOf("gId=")+4, orgHref.indexOf("~actionFlag"));
        selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/um/viewAllUsers.do~reset=true\")]");
        selenium.waitForPageToLoad("30000");
        selenium.select("tempNumResults", "value=-1");
        selenium.click("//input[@type='submit']");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isTextPresent("uattl@amp.org")) {
            selenium.click("//a[contains(@href, \"/aim/../um/addUser.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("firstNames", "UATtl");
            selenium.type("lastName", "UATtl");
            selenium.type("email", "UATtl@amp.org");
            selenium.type("emailConfirmation", "UATtl@amp.org");
            selenium.type("password", "abc");
            selenium.type("passwordConfirmation", "abc");
            selenium.select("selectedOrgType", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrgGroup", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrganizationId", "index=1");
            selenium.click("//input[@onclick='return validate()']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/um/viewAllUsers.do\")]");
            selenium.waitForPageToLoad("30000");            
        }
        selenium.select("tempNumResults", "value=-1");
        selenium.click("//input[@type='submit']");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isTextPresent("uattmc@amp.org")) {
            selenium.click("//a[contains(@href, \"/aim/../um/addUser.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("firstNames", "UATtmc");
            selenium.type("lastName", "UATtmc");
            selenium.type("email", "UATtmc@amp.org");
            selenium.type("emailConfirmation", "UATtmc@amp.org");
            selenium.type("password", "abc");
            selenium.type("passwordConfirmation", "abc");
            selenium.select("selectedOrgType", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrgGroup", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrganizationId", "index=1");
            selenium.click("//input[@onclick='return validate()']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/um/viewAllUsers.do\")]");
            selenium.waitForPageToLoad("30000");
        }
        selenium.select("tempNumResults", "value=-1");
        selenium.click("//input[@type='submit']");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isTextPresent("uattm@amp.org")) {
            selenium.click("//a[contains(@href, \"/aim/../um/addUser.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("firstNames", "UATtm");
            selenium.type("lastName", "UATtm");
            selenium.type("email", "UATtm@amp.org");
            selenium.type("emailConfirmation", "UATtm@amp.org");
            selenium.type("password", "abc");
            selenium.type("passwordConfirmation", "abc");
            selenium.select("selectedOrgType", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrgGroup", "index=1");
            Thread.sleep(10000);
            selenium.select("selectedOrganizationId", "index=1");
            selenium.click("//input[@onclick='return validate()']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/um/viewAllUsers.do\")]");
            selenium.waitForPageToLoad("30000");
        }
        selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/workspaceManager.do~page=1\")]");
        selenium.waitForPageToLoad("30000");
        selenium.select("numPerPage", "value=-1");
        selenium.click("//input[@type='submit']");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("link=UAT Team Workspace")) {
            selenium.click("//a[contains(@href, \"/aim/createWorkspace.do~dest=admin\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("teamName", "UAT Team Workspace");
            selenium.select("workspaceType", "index=2");
            Thread.sleep(2000);
            selenium.type("description", "UAT Team Workspace");
            selenium.click("//input[@onclick=\"update('add')\"]");
            selenium.waitForPageToLoad("30000");
            String href = selenium.getAttribute("link=UAT Team Workspace@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattl@amp.org");
            selenium.select("role", "index=1");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattm@amp.org");
            selenium.select("role", "index=2");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattmc@amp.org");
            selenium.select("role", "index=2");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
            selenium.waitForPageToLoad("30000");
        } else {
            String href = selenium.getAttribute("link=UAT Team Workspace@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            if (!selenium.isElementPresent("link=UATtl UATtl")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.select("email", "label=uattl@amp.org");
                selenium.select("role", "index=1");
                selenium.click("submitButton");             
                selenium.waitForPageToLoad("30000");
            }
            if (!selenium.isElementPresent("link=UATtm UATtm")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.waitForPageToLoad("30000");
                selenium.select("email", "label=uattm@amp.org");
                selenium.select("role", "index=2");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
            }
            if (!selenium.isElementPresent("link=UATtmc UATtmc")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.waitForPageToLoad("30000");
                selenium.select("email", "label=uattmc@amp.org");
                selenium.select("role", "index=2");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
            }
            selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
            selenium.waitForPageToLoad("30000");
        }
        if (!selenium.isElementPresent("link=UAT Team Workspace - Computed")) {
            selenium.click("//a[contains(@href, \"/aim/createWorkspace.do~dest=admin\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("teamName", "UAT Team Workspace - Computed");
            selenium.select("workspaceType", "index=2");
            Thread.sleep(2000);
            selenium.type("description", "UAT Team Workspace - Computed");
            selenium.click("computation");
            Thread.sleep(2000);
            selenium.click("//input[@onclick='addChildOrgs()']");
            selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
            selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
            selenium.click("//input[@name='selChildOrgs' and @value='" + orgId + "']");
            selenium.click("//input[@onclick='return childOrgsAdded()']");
            selenium.selectWindow("null");
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@onclick=\"update('add')\"]");
            selenium.waitForPageToLoad("30000");
            String href = selenium.getAttribute("link=UAT Team Workspace - Computed@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattl@amp.org");
            selenium.select("role", "index=1");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattm@amp.org");
            selenium.select("role", "index=2");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattmc@amp.org");
            selenium.select("role", "index=2");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
            selenium.waitForPageToLoad("30000");
        } else {
            String href = selenium.getAttribute("link=UAT Team Workspace - Computed@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            if (!selenium.isElementPresent("link=UATtl UATtl")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.select("email", "label=uattl@amp.org");
                selenium.select("role", "index=1");
                selenium.click("submitButton");             
                selenium.waitForPageToLoad("30000");
            }
            if (!selenium.isElementPresent("link=UATtm UATtm")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.waitForPageToLoad("30000");
                selenium.select("email", "label=uattm@amp.org");
                selenium.select("role", "index=2");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
            }
            if (!selenium.isElementPresent("link=UATtmc UATtmc")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.waitForPageToLoad("30000");
                selenium.select("email", "label=uattmc@amp.org");
                selenium.select("role", "index=2");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
            }
            selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
            selenium.waitForPageToLoad("30000");
        }
        if (!selenium.isElementPresent("link=UAT Management Workspace")) {
            selenium.click("//a[contains(@href, \"/aim/createWorkspace.do~dest=admin\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("teamName", "UAT Management Workspace");
            selenium.select("workspaceType", "index=1");
            selenium.type("description", "UAT Management Workspace");
            selenium.click("//input[@onclick='addChildWorkspaces()']");
            selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
            selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
            selenium.click("//input[@name='selChildWorkspaces']");
            selenium.click("//input[@onclick='return childWorkspacesAdded()']");
            selenium.selectWindow("null");
            selenium.waitForPageToLoad("30000");
            selenium.click("//input[@onclick=\"update('add')\"]");
            selenium.waitForPageToLoad("30000");
            String href = selenium.getAttribute("link=UAT Management Workspace@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("email", "label=uattl@amp.org");
            selenium.select("role", "index=1");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
            selenium.waitForPageToLoad("30000");
        } else {
            String href = selenium.getAttribute("link=UAT Management Workspace@href");
            String tid = href.substring(href.indexOf("tId=")+4, href.indexOf("~event"));
            selenium.click("//a[contains(@href, '/aim/teamMembers.do~teamId=" + tid + "')]");
            selenium.waitForPageToLoad("30000");
            if (!selenium.isElementPresent("link=UATtl UATtl")) {
                selenium.click("//a[contains(@href, \"/aim/showAddTeamMember.do~fromPage=1~teamId=" + tid + "\")]");
                selenium.select("email", "label=uattl@amp.org");
                selenium.select("role", "index=1");
                selenium.click("submitButton");             
                selenium.waitForPageToLoad("30000");
            }
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
    }
}

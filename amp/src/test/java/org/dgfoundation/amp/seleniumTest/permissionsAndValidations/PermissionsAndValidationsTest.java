package org.dgfoundation.amp.seleniumTest.permissionsAndValidations;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class PermissionsAndValidationsTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(PermissionsAndValidationsTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*firefox");
//      setUp("http://senegal.staging.ampdev.net/", "*chrome");
    }
    public static void testPermissionsAndValidations(Selenium selenium) throws Exception {
        
        String testTime =  String.valueOf(System.currentTimeMillis());
        String activityName ="Activity of testing setup " + testTime;
        
        //Login as TM WS Team (create an activity)
        selenium.open("/");
        selenium.type("j_username", "uattm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        addActivity(selenium, activityName);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");

        //Login as TL WS Team - Computed (create a tab and check that the activity is NOT present)
        logger.info("Permissions and Validation UAT Step 2'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TL " + testTime, testTime);
        if (selenium.isTextPresent(activityName)) {
            logger.error("Activity is available for a wrong user");
            //selenium.logAssertion"assertTrue", "Activity is available for a wrong user", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Team (create a tab and validate the activity)
        logger.info("Permissions and Validation UAT Step 3'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TL " + testTime, testTime);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Team - Computed (use the tab created and check the activity)
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
        Thread.sleep(12000);
        if (!selenium.isTextPresent(activityName)) {
            logger.error("Activity is not available");
            //selenium.logAssertion"assertTrue", "Activity is not available", "condition=false");
        }
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
//      selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
//      selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Management (create a tab and check the activity)
        // aca hay un issue cuando creas un tab en un managment workspace filtra por draft=true en settings
        /*
        logger.info("Permissions and Validation UAT Step 4'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Management Workspace");
        selenium.waitForPageToLoad("30000");
        addTab("Test Tab TL " + testTime, testTime);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        */
        
        //Login as Admin and change the permissions on feature manager 
        logger.info("Permissions and Validation UAT Step 5'");
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
        String cId = "";
        if (selenium.isElementPresent("//li[@title='Project Title']/input")) {
            cId = selenium.getAttribute("//li[@title='Project Title']/input@id");       
            cId = cId.substring(cId.indexOf("is:")+3);
        } else if (selenium.isElementPresent("//li[@title='project title']/input")) {
            cId = selenium.getAttribute("//li[@title='project title']/input@id");       
            cId = cId.substring(cId.indexOf("is:")+3);
        } else if (selenium.isElementPresent("//li[@title='Project title']/input")) {
            cId = selenium.getAttribute("//li[@title='Project title']/input@id");       
            cId = cId.substring(cId.indexOf("is:")+3);
        }
        
        selenium.click("//a[@onclick= \"openFieldPermissionsPopup("+cId+")\"]"); // Project Title
        //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
        Thread.sleep(5000);
        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
        selenium.uncheck("faRead");
        selenium.uncheck("faEdit");
        selenium.click("//input[@onclick='javascript:savePermissions();']");
        selenium.selectWindow("null"); 
        Thread.sleep(5000);
        if (selenium.isElementPresent("//li[@title='Status']/input")) {
            cId = selenium.getAttribute("//li[@title='Status']/input@id");      
            cId = cId.substring(cId.indexOf("is:")+3);
        } else if (selenium.isElementPresent("//li[@title='status']/input")) {
            cId = selenium.getAttribute("//li[@title='status']/input@id");      
            cId = cId.substring(cId.indexOf("is:")+3);
        } else if (selenium.isElementPresent("//li[@title='STATUS']/input")) {
            cId = selenium.getAttribute("//li[@title='STATUS']/input@id");      
            cId = cId.substring(cId.indexOf("is:")+3);
        }
        
        selenium.click("//a[@onclick= \"openFieldPermissionsPopup("+cId+")\"]"); // Project Status
        //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
        Thread.sleep(5000);
        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
        selenium.uncheck("faRead");
        selenium.uncheck("faEdit");
        selenium.click("//input[@onclick='javascript:savePermissions();']");
        selenium.selectWindow("null"); 
        Thread.sleep(5000);
        selenium.click("//input[@name='saveTreeVisibility']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TM WS Team (create a tab and check if the Project Title and Status are enabled)
        logger.info("Permissions and Validation UAT Step 6'");
        selenium.type("j_username", "uattm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TM " + testTime, testTime);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        try {
            String dis = selenium.getAttribute("identification.title@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Activity Title field is disabled");
                //selenium.logAssertion"assertTrue", "Activity Title field is disabled", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'identification.title'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'identification.title'");
        }
        try {
            String dis = selenium.getAttribute("planning.statusId@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Status field is disabled");
                //selenium.logAssertion"assertTrue", "Status field is disabled", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'planning.statusId'");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Team (use the tab created and check if the Project Title and Status are enabled)
        logger.info("Permissions and Validation UAT Step 7'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
        Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        try {
            String dis = selenium.getAttribute("identification.title@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Activity Title field is disabled");
                //selenium.logAssertion"assertTrue", "Activity Title field is disabled", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'identification.title'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'identification.title'");
        }
        try {
            String dis = selenium.getAttribute("planning.statusId@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Status field is disabled");
                //selenium.logAssertion"assertTrue", "Status field is disabled", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'planning.statusId'");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TMC WS Team (create a tab and check if the Project Title and Status are disabled)
        logger.info("Permissions and Validation UAT Step 8'");
        selenium.type("j_username", "uattmc@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TMC " + testTime, testTime);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        try {
            String dis = selenium.getAttribute("identification.title@disabled");
            if (!dis.equalsIgnoreCase("disabled")) {
                logger.error("Activity Title field is enabled");
                //selenium.logAssertion"assertTrue", "Activity Title field is enabled", "condition=false");
            }
        } catch (Exception e) {
            logger.error("Activity Title field is enabled");
            //selenium.logAssertion"assertTrue", "Activity Title field is enabled", "condition=false");
        }
        try {
            String dis = selenium.getAttribute("planning.statusId@disabled");
            if (!dis.equalsIgnoreCase("disabled")) {
                logger.error("Status field is enabled");
                //selenium.logAssertion"assertTrue", "Status field is enabled", "condition=false");
            }
        } catch (Exception e) {
            logger.error("Status field is enabled");
            //selenium.logAssertion"assertTrue", "Status field is enabled", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
      
        //Login as TL WS Team (change the application settings)
        logger.info("Permissions and Validation UAT Step 9'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.select("validation", "index=0");
        selenium.click("//input[@onclick='validade();']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
      
        //Login as TM WS Team (use the tab created, add an activity and check if the title is green and with an *)
        String testTime2 =  String.valueOf(System.currentTimeMillis());
        String activityName2 ="Activity of testing setup 2 " + testTime2;
        selenium.type("j_username", "uattm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        addActivity(selenium, activityName2);
        TabTest.addBasicTab(selenium, "Test Tab TL 2 " + testTime2, testTime2);
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        if (!selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("GREEN")) {
            logger.error("Wrong font color for activity status");
            //selenium.logAssertion"assertTrue", "Wrong font color for activity status", "condition=false");
        }
        if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*")) {
            logger.error("Activity name is shown without *");
            //selenium.logAssertion"assertTrue", "Activity name is shown without *", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Team (create a tab and validate the activity)
        logger.info("Permissions and Validation UAT Step 10'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TL 2 " + testTime2, testTime2);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        try {
            if (!selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("")) {
                logger.error("Wrong font color for activity status");
                //selenium.logAssertion"assertTrue", "Wrong font color for activity status", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'color' is not available");
           //selenium.logComment("Attribute 'color' is not available");
        }
        if (selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*")) {
            logger.error("Activity name is shown with *");
            //selenium.logAssertion"assertTrue", "Activity name is shown with *", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");

        //Login as TL WS Team (change the application settings)
        logger.info("Permissions and Validation UAT Step 12'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.select("validation", "index=1");
        selenium.click("//input[@onclick='validade();']");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
      
        //Login as TM WS Team (use the tab created and check if the Project Title and Status are enabled)
        selenium.type("j_username", "uattm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.type("identification.title", activityName2 + " mod");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        if (!selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("GREEN")) {
            logger.error("Wrong font color for activity status");
            //selenium.logAssertion"assertTrue", "Wrong font color for activity status", "condition=false");
        }
        if (selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*")) {
            logger.error("Activity name is shown with *");
            //selenium.logAssertion"assertTrue", "Activity name is shown with *", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TL WS Team (create a tab and validate the activity)
        logger.info("Permissions and Validation UAT Step 13 and 14'");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("//a[contains(@href, \"mailto:uattm@amp.org\")]")) {
            logger.error("Activity creator is wrong");
            //selenium.logAssertion"assertTrue", "Activity creator is wrong", "condition=false");
        }
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveAsDraftClicked()']");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        if (!selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("RED")) {
            logger.error("Wrong font color for activity status");
            //selenium.logAssertion"assertTrue", "Wrong font color for activity status", "condition=false");
        }
        if (selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*")) {
            logger.error("Activity name is shown with *");
            //selenium.logAssertion"assertTrue", "Activity name is shown with *", "condition=false");
        }
        
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("//a[contains(@href, \"mailto:uattm@amp.org\")]")) {
            logger.error("Activity creator is wrong");
            //selenium.logAssertion"assertTrue", "Activity creator is wrong", "condition=false");
        }
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@onclick='saveClicked()']");
        selenium.waitForPageToLoad("50000");
        logger.info("Permissions and Validation UAT Step 15'");
        selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/teamActivityList.do~dest=teamLead~tId=-1~subtab=0\")]");
        selenium.waitForPageToLoad("30000");
        String aId = "";
        done = false;
        cnt = 2;
        while (!done){
            if (selenium.isElementPresent("link=" + activityName2+ " mod")) {
                aId = selenium.getAttribute("link=" + activityName2 + " mod@href");
                aId = aId.substring(aId.indexOf("yId=")+4, aId.indexOf("~page"));
                selenium.check("//input[@name='selActivities' and @value='"+aId+"']");
                selenium.click("//input[@onclick='return confirmDelete()']");
                selenium.getConfirmation();
                selenium.waitForPageToLoad("30000");
                done = true;
            } else {
                if (selenium.isElementPresent("//a[contains(@href, \"javascript:page("+cnt+")\")]")) {
                    selenium.click("//a[contains(@href, \"javascript:page("+cnt+")\")]");
                    selenium.waitForPageToLoad("30000");
                    cnt++;
                } else {
                    done = true;
                }
            }
        }
    
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        if (selenium.isTextPresent(activityName2)) {
            logger.error("Activity is available for a wrong user");
            //selenium.logAssertion"assertTrue", "Activity is available for a wrong user", "condition=false");
        }
        
        logger.info("Permissions and Validation UAT Step 16'");
        selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/teamActivityList.do~dest=teamLead~tId=-1~subtab=0\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/updateTeamActivity.do~dest=teamLead~tId=-1~subtab=0\")]");
        selenium.waitForPageToLoad("30000");
        done = false;
        cnt = 2;
        while (!done){
            if (selenium.isElementPresent("//input[@name='selActivities' and @value='"+aId+"']")) {
                selenium.check("//input[@name='selActivities' and @value='"+aId+"']");
                selenium.select("memberId", "UATtmc UATtmc");
                selenium.click("//input[@onclick='return checkSelActivities()']");
                selenium.waitForPageToLoad("30000");
                done = true;
            } else {
                if (selenium.isElementPresent("//a[contains(@href, \"/aim/updateTeamActivity.do~page="+cnt+"\")]")) {
                    selenium.click("//a[contains(@href, \"/aim/updateTeamActivity.do~page="+cnt+"\")]");
                    selenium.waitForPageToLoad("30000");
                    cnt++;
                } else {
                    done = true;
                }
            }
        }
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
        Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        if (!selenium.isElementPresent("//a[contains(@href, \"mailto:uattmc@amp.org\")]")) {
            logger.error("Activity owner is wrong");
            //selenium.logAssertion"assertTrue", "Activity owner is wrong", "condition=false");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        //Login as TMC WS Team (create a tab and check if the Project Title and Status are enabled)
        logger.info("Permissions and Validation UAT Step 17'");
        selenium.type("j_username", "uattmc@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"showDesktop.do\")]");
        selenium.waitForPageToLoad("30000");
        TabTest.addBasicTab(selenium, "Test Tab TMC 2 " + testTime2, testTime2);
        //selenium.click("//a[@id='Tab-Test Tab TMC " + testTime + "']/div");
        //Thread.sleep(12000);
        selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
        selenium.waitForPageToLoad("30000");
        try {
            String dis = selenium.getAttribute("identification.title@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Activity Title field is disabled");
                //selenium.logAssertion"assertTrue", "Activity Title field is disabled", "condition=false");
            }
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'identification.title'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'identification.title'");
        }
        try {
            String dis = selenium.getAttribute("planning.statusId@disabled");
            if (dis.equalsIgnoreCase("disabled")) {
                logger.error("Status field is disabled");
                //selenium.logAssertion"assertTrue", "Status field is disabled", "condition=false");
            }           
        } catch (Exception e) {
            logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
           //selenium.logComment("Attribute 'Disabled' is not available for 'planning.statusId'");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
        selenium.waitForPageToLoad("50000");
        TabTest.deleteAllTabs(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "uattm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
        selenium.waitForPageToLoad("50000");
        TabTest.deleteAllTabs(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "uattmc@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
        selenium.waitForPageToLoad("50000");
        TabTest.deleteAllTabs(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
        selenium.waitForPageToLoad("50000");
        TabTest.deleteAllTabs(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/activityManager.do\")]")) {
            selenium.click("//a[contains(@href, \"/aim/activityManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", activityName);
            selenium.click("//input[@onclick=\"return searchActivity()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return deleteIndicator()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", activityName);
            selenium.click("//input[@onclick=\"return searchActivity()\"]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[@onclick=\"return deleteIndicator()\"]")) {
                logger.error("Activity wasn't deleted");
                //selenium.logAssertion"assertTrue", "Activity wasn't deleted", "condition=false");
            }
            selenium.type("keyword", activityName2);
            selenium.click("//input[@onclick=\"return searchActivity()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return deleteIndicator()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            selenium.type("keyword", activityName2);
            selenium.click("//input[@onclick=\"return searchActivity()\"]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[@onclick=\"return deleteIndicator()\"]")) {
                logger.error("Activity wasn't deleted");
                //selenium.logAssertion"assertTrue", "Activity wasn't deleted", "condition=false");
            }
        } else {
            logger.info("Activity Manager is not available");
           //selenium.logComment("Activity Manager is not available");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        logger.info("Permissions and Validations Test Finished Successfully");
       //selenium.logComment("Permissions and Validations Test Finished Successfully");
    }
    
    
    
    /**
     * 
     * @param activityName
     * @throws Exception 
     */
    private static void addActivity (Selenium selenium, String activityName) throws Exception{
        boolean addAvailable = false;
        if (selenium.isElementPresent("//a[contains(@href, \"javascript:addActivity()\")]")) {
            selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
            selenium.waitForPageToLoad("120000");
            addAvailable = true;
        } else {
            logger.error("Option \"Add Activity\" is not available.");
            //selenium.logAssertion"assertTrue", "Option \"Add Activity\" is not available.", "condition=false");
        }
        if (addAvailable) {
            selenium.type("identification.title", activityName);
            if (selenium.isElementPresent("planning.statusId")) {
                selenium.select("planning.statusId", "index=1");
            } else {
                logger.info("Field \"Status\" is not available.");
               //selenium.logComment("Field \"Status\" is not available.");
            }
            
            selenium.click("//a[@href='javascript:gotoStep(2)']");
            selenium.waitForPageToLoad("50000");
            
            if (SeleniumFeaturesConfiguration.getFeatureState("Sectors")){
                //Add Primary Sector
                if (SeleniumFeaturesConfiguration.getFieldState("Primary Sector")){
                    if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addSectors(false,1);']")) {
                        selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,1);']");
                        SeleniumTestUtil.waitForElement(selenium,"sector", 90);
                        if (selenium.isElementPresent("sector")) {
                            selenium.select("sector", "index=1");
                            Thread.sleep(5000);
                            selenium.click("addButton");
                            selenium.waitForPageToLoad("50000");    
                            if (selenium.isElementPresent("activitySectors[0].sectorPercentage")) {
                                selenium.type("activitySectors[0].sectorPercentage", "100");
                            } else {
                                logger.info("Add Primary Sector Fail ");
                               //selenium.logComment("Add Primary Sector Fail ");
                            }
                        } else {
                            logger.info("Sectors no found for Primary Sector");
                           //selenium.logComment("Sectors no found for Primary Sector");
                        }
                    } else {
                        logger.error("Field \"Primary Sector\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Primary Sector\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Primary Sector\" is not available.");
                   //selenium.logComment("Field \"Primary Sector\" is not available.");
                }
                //Add Secondary Sector
                if (SeleniumFeaturesConfiguration.getFieldState("Secondary Sector")){
                    if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addSectors(false,2);']")) {
                        selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,2);']");
                        SeleniumTestUtil.waitForElement(selenium,"sector", 90);
                        if (selenium.isElementPresent("sector")) {
                            selenium.select("sector", "index=1");
                            Thread.sleep(5000);
                            selenium.click("addButton");
                            selenium.waitForPageToLoad("50000");
                            if (selenium.isElementPresent("activitySectors[1].sectorPercentage")) {
                                selenium.type("activitySectors[1].sectorPercentage", "100");
                            } else {
                                logger.info("Add Secondary Sector Fail ");
                               //selenium.logComment("Add Secondary Sector Fail ");
                            }
                        } else {
                            logger.info("Sectors no found for Secondary Sector");
                           //selenium.logComment("Sectors no found for Secondary Sector");
                        }
                    } else {
                        logger.error("Field \"Secondary Sector\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Secondary Sector\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Secondary Sector\" is not available.");
                   //selenium.logComment("Field \"Secondary Sector\" is not available.");
                }
            } else {
                logger.info("Feature \"Sectors\" is not available.");
               //selenium.logComment("Feature \"Sectors\" is not available.");
            }
            
            
            //Add Funding
            boolean fundingAvailable = false;
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(3)']")) {
                selenium.click("//a[@href='javascript:gotoStep(3)']");
                selenium.waitForPageToLoad("50000");
                fundingAvailable = true;
            } else {
                logger.info("Step \"Funding\" is not available.");
               //selenium.logComment("Step \"Funding\" is not available.");
            }
            
            if (SeleniumFeaturesConfiguration.getFeatureState("Funding Information")){
                
                if (SeleniumFeaturesConfiguration.getFieldState("Add Donor Organization")){
                    if (selenium.isElementPresent("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]")) {
                        selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                        //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                        Thread.sleep(10000);
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
                       
                        SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                        selenium.type("keyword", "World Bank");
                        selenium.click("//input[@onclick='return searchOrganization()']");
                        SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                        selenium.click("selOrganisations"); 
                        selenium.click("//input[@onclick='return selectOrganization()']");
                        selenium.selectWindow("null");
                        selenium.waitForPageToLoad("50000");
                    } else {
                        logger.error("Field \"Add Donor Organization\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Add Donor Organization\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Add Donor Organization\" is not available.");
                   //selenium.logComment("Field \"Add Donor Organization\" is not available.");
                }
            } else {
                logger.info("Feature \"Funding Information\" is not available.");
               //selenium.logComment("Feature \"Funding Information\" is not available.");
            }
            
            selenium.click("//input[@onclick='saveClicked()']");
            selenium.waitForPageToLoad("50000");
            if (selenium.isElementPresent("//input[@onclick='saveClicked()']")) {
                logger.error("Save Activity Fail"); 
                //selenium.logAssertion"assertTrue", "Save Activity Fail", "condition=false");
            }
            if (selenium.isElementPresent("//input[@onclick='overwrite()']")) {
                selenium.click("//input[@onclick='overwrite()']");          
            }
        }
    }
}

package org.dgfoundation.amp.seleniumTest;

import org.dgfoundation.amp.seleniumTest.activityForm.ActivityFormTest;
import org.dgfoundation.amp.seleniumTest.admin.*;
import org.dgfoundation.amp.seleniumTest.permissionsAndValidations.PermissionsAndValidationsTest;
import org.dgfoundation.amp.seleniumTest.publicView.PublicViewTest;
import org.dgfoundation.amp.seleniumTest.reports.ReportTest;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;
import org.dgfoundation.amp.seleniumTest.resources.ResourcesTest;
import org.dgfoundation.amp.seleniumTest.workspaceInfo.TeamWorkspaceInfoTest;
import org.apache.log4j.Logger;

import org.digijava.module.aim.action.OrganisationManager;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.SeleneseTestCase;

public class SeleniumTestRunner  extends SeleneseTestBase{

    private static Logger logger = Logger.getLogger(SeleniumTestRunner.class);
    
    public void setUp() throws Exception {
//      setUp("http://generic.ampdev.net", "*firefox");
//      setUp("http://drc.ampdev.net", "*firefox");
//      setUp("http://senegal.ampdev.net", "*firefox");
//      setUp("http://senegal.staging.ampdev.net", "*firefox");
        setUp("http://localhost:8085/", "*chrome",555);
        
    }
    
    /**
     * @param args
     * @throws Exception 
     */
    public void runTestSuite() throws Exception {
        boolean configOk = true;
        try {
            SeleniumTestUtil.setupUsersAndWorkspace(selenium);
            configOk = true;
        } catch (Exception e) {
            logger.error("Error on config for selenium tests");
            configOk = false;
        }
        if (configOk) {
            try {
                PublicViewTest.testPublicView(selenium);
            } catch (Exception e) {
                logger.error("testPublicView Error");
                e.printStackTrace();
                logout();
            }
            try {
                PermissionsAndValidationsTest.testPermissionsAndValidations(selenium);
            } catch (Exception e) {
                logger.error("testPermissionsAndValidations Error");
                e.printStackTrace();
                logout();
            }
            try {
                ActivityFormTest.testAddActivity(selenium);
            } catch (Exception e) {
                logger.error("testAddActivity Error");
                e.printStackTrace();
                logout();
            }
            try {
                ReportTest.testAddReport(selenium);
            } catch (Exception e) {
                logger.error("testAddReport Error");
                e.printStackTrace();
                logout();
            }
            try{
                TabTest.testAddTab(selenium);
            } catch (Exception e) {
                logger.error("testAddTab Error");
                e.printStackTrace();
                logout();
            }
            try{
                EventTypeManagerTest.testEventTypeManager(selenium);
            } catch (Exception e) {
                logger.error("testEventTypeManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                ResourcesTest.testResources(selenium);
            } catch (Exception e) {
                logger.error("testResources Error");
                e.printStackTrace();
                logout();
            }
            try{
                FeatureManagerTest.testFeatureManager(selenium);
            } catch (Exception e) {
                logger.error("testFeatureManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                GlobalSettingsTest.testGlobalSettings(selenium);
            } catch (Exception e) {
                logger.error("testGlobalSettings Error");
                e.printStackTrace();
                logout();
            }
            try{
                WorkspaceManagerTest.testWorkspaceManager(selenium);
            } catch (Exception e) {
                logger.error("testWorkspaceManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                CategoryManagerTest.testCategoryManager(selenium);
            } catch (Exception e) {
                logger.error("testCategoryManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                UserManagerTest.testUserManager(selenium);
            } catch (Exception e) {
                logger.error("testUserManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                ComponentTypeManagerTest.testComponentTypeManager(selenium);
            } catch (Exception e) {
                logger.error("testComponentTypeManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                MultiProgramManagerTest.testMultiProgramManager(selenium);
            } catch (Exception e) {
                logger.error("testMultiProgramManager Error");
                e.printStackTrace();
                logout();
            }
            
//          IndicatorManagerTest.testIndicatorManager(selenium);//falla
            try{
                CalendarManagerTest.testCalendarManager(selenium);
            } catch (Exception e) {
                logger.error("testCalendarManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                CurrencyManagerTest.testCurrencyManager(selenium);
            } catch (Exception e) {
                logger.error("testAddActivity Error");
                e.printStackTrace();
                logout();
            }
            try{
                RegionManagerTest.testRegionManager(selenium);
            } catch (Exception e) {
                logger.error("testRegionManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                SectorManagerTest.testSectorManager(selenium);
            } catch (Exception e) {
                logger.error("testSectorManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                OrganizationManagerTest.testOrganizationManager(selenium);
            } catch (Exception e) {
                logger.error("testOrganizationManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                ActivityManagerTest.testActivityManager(selenium);
            } catch (Exception e) {
                logger.error("testActivityManager Error");
                e.printStackTrace();
                logout();
            }
            try{
                TeamWorkspaceInfoTest.testTeamWorkspaceInfo(selenium);
            } catch (Exception e) {
                logger.error("testTeamWorkspaceInfo Error");
                e.printStackTrace();
                logout();
            }
            
        }
    }
    
    private void logout() {
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/j_spring_logout\")]")) {
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
        }
        
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        SeleniumTestRunner runner = new SeleniumTestRunner();
        try {
            runner.setUp();
            runner.runTestSuite();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }
}

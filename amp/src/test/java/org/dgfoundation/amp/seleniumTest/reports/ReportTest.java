package dgfoundation.amp.seleniumTest.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.activityForm.ActivityFormTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class ReportTest extends SeleneseTestCase{

    private static Logger logger = Logger.getLogger(ReportTest.class);
    
    public void setUp() throws Exception {
//      setUp("http://generic.ampdev.net", "*firefox");
//      setUp("http://drc.ampdev.net", "*firefox");
//      setUp("http://senegal.ampdev.net", "*firefox");
//      setUp("http://localhost:8080/", "*firefox");
        setUp("http://senegal.staging.ampdev.net/", "*chrome");
    }
    
    public static void testAddReport(LoggingSelenium selenium) throws Exception {
        
        String testTime =  String.valueOf(System.currentTimeMillis());
        String reportName ="Report for selenium test " + testTime;
        String user = "uattl@amp.org";
        String password = "abc";
        boolean measActualComm = false;
        boolean measActualDist = false;
        boolean measPlannedComm = false;
        boolean measPlannedDist = false;
        
        
        String actNameFilter = ActivityFormTest.getFirstActivityName(selenium);
        if (actNameFilter != null) {
            actNameFilter = actNameFilter.split(" ")[4];
        }else{
            ActivityFormTest.testAddActivity(selenium);
            actNameFilter = ActivityFormTest.getFirstActivityName(selenium);
            actNameFilter = actNameFilter.split(" ")[4];
        }
        
        selenium.open("/");
        selenium.type("j_username", user);
        selenium.type("j_password", password);
        selenium.click("submitButton");
        selenium.waitForPageToLoad("50000");
        
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("50000");
        
        //ADD REPORT
        boolean addReportAvailable = false;
        if (SeleniumFeaturesConfiguration.getModuleState("Report Generator")){
            if (selenium.isElementPresent("//a[contains(@href, "
                    + "\"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=R\")]")) {
                selenium.click("//a[contains(@href, " 
                        + "\"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=R\")]");
                selenium.waitForPageToLoad("360000");
                addReportAvailable = true;
            } else {
                logger.error("Module \"Report Generator\" is active in Feature Manager but is not available.");
                selenium.logAssertion("assertTrue", "Module \"Report Generator\" is active in Feature Manager but is not available.", "condition=false");
            }           
        } else {
            logger.info("Module \"Report Generator\" is not available.");
        }
        
        if (addReportAvailable) {   
            //SET THE PERIOD OF THE REPORT TO QUARTERLY
            selenium.click("//input[@name='reportPeriod' and @value='Q']");
            selenium.click("//li[@id='columns_tab_label']/a/div");
            //selenium.click("//li[@id='DHTMLSuite_treeNode2']/img[1]");
            if (selenium.isElementPresent("fieldVis:4")) {
                selenium.click("fieldVis:4");
            } else {
                selenium.click("fieldVis:12");
                logger.error("Option \"Project Title\" is not available.");
                selenium.logAssertion("assertTrue", "", "condition=false");
            }
            selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')\"]");
            selenium.click("//li[@id='measures_tab_label']/a/div");
            if (SeleniumFeaturesConfiguration.getFeatureState("Actual Commitments")){
                if (selenium.isElementPresent("//li[@id='measure_1']/input")) {
                    selenium.click("//li[@id='measure_1']/input");
                    measActualComm = true;
                } else {
                    logger.error("Field \"Actual Commitments\" is active in Feature Manager but is not available.");
                    selenium.logAssertion("assertTrue", "Field \"Actual Commitments\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                measActualComm = false;
                logger.info("Field \"Actual Commitments\" is not available.");
                selenium.logComment("Field \"Actual Commitments\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFeatureState("Actual Disbursements")){
                if (selenium.isElementPresent("//li[@id='measure_2']/input")) {
                    selenium.click("//li[@id='measure_2']/input");
                    measActualDist = true;
                } else {
                    logger.error("Field \"Actual Disbursements\" is active in Feature Manager but is not available.");
                    selenium.logAssertion("assertTrue", "Field \"Actual Disbursements\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                measActualDist = false;
                logger.info("Field \"Actual Disbursements\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFeatureState("Planned Commitments")){
                if (selenium.isElementPresent("//li[@id='measure_4']/input")) {
                    selenium.click("//li[@id='measure_4']/input");
                    measPlannedComm = true;
                } else {
                    logger.error("Field \"Planned Commitments\" is active in Feature Manager but is not available.");
                    selenium.logAssertion("assertTrue", "Field \"Planned Commitments\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                measPlannedComm = false;
                logger.info("Field \"Planned Commitments\" is not available.");
                selenium.logComment("Field \"Planned Commitments\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFeatureState("Planned Disbursements")){
                if (selenium.isElementPresent("//li[@id='measure_5']/input")) {
                    selenium.click("//li[@id='measure_5']/input");
                    measPlannedDist = true;
                } else {
                    logger.error("Field \"Planned Disbursements\" is active in Feature Manager but is not available.");
                    selenium.logAssertion("assertTrue", "Field \"Planned Disbursements\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                measPlannedDist = false;
                logger.info("Measure \"Planned Disbursements\" is not available.");
                selenium.logComment("Measure \"Planned Disbursements\" is not available.");
            }
            selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
            if (SeleniumFeaturesConfiguration.getFeatureState("Filter Button")){
                if (selenium.isElementPresent("step3_add_filters_button")) {
                    selenium.click("step3_add_filters_button");
                    SeleniumTestUtil.waitForElement(selenium, "indexString", 90);
                    selenium.type("indexString",actNameFilter);
                    selenium.click("filterPickerSubmitButton");
                    Thread.sleep(30000);
                } else {
                    logger.error("Field \"Filter Button\" is active in Feature Manager but is not available.");
                    selenium.logAssertion("assertTrue", "Field \"Filter Button\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                logger.info("Field \"Filter Button\" is not available.");
                selenium.logComment("Field \"Filter Button\" is not available.");
            }
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.typeKeys("reportTitle", reportName);
            selenium.click("last_save_button");
            selenium.waitForPageToLoad("50000");
            selenium.click("//p[@title='" + reportName + "']");
            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
            Thread.sleep(30000);
            for (int i = 0; i < selenium.getAllWindowTitles().length; i++) {
                if (selenium.getAllWindowTitles()[i].contains(reportName)) {
                    selenium.selectWindow(selenium.getAllWindowTitles()[i]);
                    break;
                }
            }
            //selenium.selectWindow("AMP : Reports "+reportName); 
            
            String filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
            if (!filters.contains(actNameFilter)) {
                logger.error("Filter by index string error");
                selenium.logAssertion("assertTrue", "Filter by index string error", "condition=false");
            }
            int tdCnt = 2;
            for (int i = 0; i < ActivityFormTest.ACTUAL_COMMITMENTS.length; i++) {
                if (measActualComm) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_COMMITMENTS[i])) {
                        logger.error("Error on ACTUAL_COMMITMENTS shown");
                        selenium.logAssertion("assertTrue", "Error on ACTUAL_COMMITMENTS shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measActualDist) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_DISBURSEMENT[i])) {
                        logger.error("Error on ACTUAL_DISBURSEMENT shown");
                        selenium.logAssertion("assertTrue", "Error on ACTUAL_DISBURSEMENT shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measPlannedComm) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_COMMITMENTS[i])) {
                        logger.error("Error on PLANNED_COMMITMENTS shown");
                        selenium.logAssertion("assertTrue", "Error on PLANNED_COMMITMENTS shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measPlannedDist) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_DISBURSEMENT[i])) {
                        logger.error("Error on PLANNED_DISBURSEMENT shown");
                        selenium.logAssertion("assertTrue", "Error on PLANNED_DISBURSEMENT shown", "condition=false");
                    }
                    tdCnt++;
                }               
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[22]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS)) {
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[23]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)) {
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[24]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS)) {
                logger.error("Error on TOTAL_PLANNED_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[25]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT)) {
                logger.error("Error on TOTAL_PLANNED_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_DISBURSEMENT shown", "condition=false");
            }
            
            selenium.close();
            selenium.selectWindow("null");

            //CHANGE THE PERIOD OF THE REPORT TO MONTHLY
            boolean done = false;
            int cnt = 0;
            while (!done){
                if (selenium.isElementPresent("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]")) {
                    String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
                    if (repname.equals(reportName)) {
                        selenium.click("//a[contains(@href, "
                                + "\"/TEMPLATE/reampv2/build/index.html#/report_generator/" + cnt + "\")]");
                        done = true;
                        selenium.waitForPageToLoad("30000");                    
                    }
                }
                cnt++;
            }
            selenium.click("//input[@name='reportPeriod' and @value='M']");
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//p[@title='" + reportName + "']");
            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
            Thread.sleep(10000);
            for (int i = 0; i < selenium.getAllWindowTitles().length; i++) {
                if (selenium.getAllWindowTitles()[i].contains(reportName)) {
                    selenium.selectWindow(selenium.getAllWindowTitles()[i]);
                    break;
                }
            }
            //selenium.selectWindow("AMP : Reports "+reportName); 
            filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
            if (!filters.contains(actNameFilter)) {
                logger.error("Filter by index string error");
                selenium.logAssertion("assertTrue", "Filter by index string error", "condition=false");
            }
            tdCnt = 2;
            for (int i = 0; i < ActivityFormTest.ACTUAL_COMMITMENTS.length; i++) {
                if (measActualComm) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_COMMITMENTS[i])) {
                        logger.error("Error on ACTUAL_COMMITMENTS shown");
                        selenium.logAssertion("assertTrue", "Error on ACTUAL_COMMITMENTS shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measActualDist) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_DISBURSEMENT[i])) {
                        logger.error("Error on ACTUAL_DISBURSEMENT shown");
                        selenium.logAssertion("assertTrue", "Error on ACTUAL_DISBURSEMENT shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measPlannedComm) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_COMMITMENTS[i])) {
                        logger.error("Error on PLANNED_COMMITMENTS shown");
                        selenium.logAssertion("assertTrue", "Error on PLANNED_COMMITMENTS shown", "condition=false");
                    }
                    tdCnt++;
                }
                if (measPlannedDist) {
                    if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_DISBURSEMENT[i])) {
                        logger.error("Error on PLANNED_DISBURSEMENT shown");
                        selenium.logAssertion("assertTrue", "Error on PLANNED_DISBURSEMENT shown", "condition=false");
                    }
                    tdCnt++;
                }               
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[22]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS)) {
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[23]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)) {
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[24]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS)) {
                logger.error("Error on TOTAL_PLANNED_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[25]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT)) {
                logger.error("Error on TOTAL_PLANNED_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_DISBURSEMENT shown", "condition=false");
            }
            
            selenium.close();
            selenium.selectWindow("null");

            //CHANGE THE PERIOD OF THE REPORT TO ANNUAL
            done = false;
            cnt = 0;
            while (!done){
                if (selenium.isElementPresent("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]")) {
                    String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
                    if (repname.equals(reportName)) {
                        selenium.click("//a[contains(@href, "
                                + "\"/TEMPLATE/reampv2/build/index.html#/report_generator/" + cnt + "\")]");
                        done = true;
                        selenium.waitForPageToLoad("30000");                    
                    }
                }
                cnt++;
            }
            selenium.click("//input[@name='reportPeriod' and @value='A']");
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//p[@title='" + reportName + "']");
            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
            Thread.sleep(10000);
            for (int i = 0; i < selenium.getAllWindowTitles().length; i++) {
                if (selenium.getAllWindowTitles()[i].contains(reportName)) {
                    selenium.selectWindow(selenium.getAllWindowTitles()[i]);
                    break;
                }
            }
            //selenium.selectWindow("AMP : Reports "+reportName); 
            filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
            if (!filters.contains(actNameFilter)) {
                logger.error("Filter by index string error");
                selenium.logAssertion("assertTrue", "Filter by index string error", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS_2008)) {
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS_2008 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS_2008 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[3]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT_2008)) {
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT_2008 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT_2008 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[4]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS_2008)) {
                logger.error("Error on TOTAL_PLANNED_COMMITMENTS_2008 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_COMMITMENTS_2008 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[5]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT_2008)) {
                logger.error("Error on TOTAL_PLANNED_DISBURSEMENT_2008 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_DISBURSEMENT_2008 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[6]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS_2009)) {
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS_2009 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS_2009 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[7]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT_2009)) {
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT_2009 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT_2009 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[8]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS_2009)) {
                logger.error("Error on TOTAL_PLANNED_COMMITMENTS_2009 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_COMMITMENTS_2009 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[9]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT_2009)) {
                logger.error("Error on TOTAL_PLANNED_DISBURSEMENT_2009 shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_DISBURSEMENT_2009 shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[10]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS)) {
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[11]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)) {
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[12]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS)) {
                logger.error("Error on TOTAL_PLANNED_COMMITMENTS shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_COMMITMENTS shown", "condition=false");
            }
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[13]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT)) {
                logger.error("Error on TOTAL_PLANNED_DISBURSEMENT shown");
                selenium.logAssertion("assertTrue", "Error on TOTAL_PLANNED_DISBURSEMENT shown", "condition=false");
            }
            
            selenium.close();
            selenium.selectWindow("null");

            done = false;
            cnt = 0;
            while (!done){
                if (selenium.isElementPresent("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]")) {
                    String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
                    if (repname.equals(reportName)) {
                        selenium.click("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=0~rid=" + cnt + "~event=edit\")]");
                        selenium.getConfirmation();
                        done = true;                        
                        selenium.waitForPageToLoad("30000");    
                        
                    }
                }
                cnt++;
            }
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Report Test Finished Successfully");
        selenium.logComment("Report Test Finished Successfully");
    }
}

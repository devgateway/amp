package org.dgfoundation.amp.seleniumTest.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.activityForm.ActivityFormTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class ReportTest extends SeleneseTestCase{

	private static Logger logger = Logger.getLogger(ReportTest.class);
	
	public void setUp() throws Exception {
//		setUp("http://generic.ampdev.net", "*firefox");
//		setUp("http://drc.ampdev.net", "*firefox");
//		setUp("http://senegal.ampdev.net", "*firefox");
//		setUp("http://localhost:8080/", "*firefox");
		setUp("http://senegal.staging.ampdev.net/", "*chrome");
	}
	
	public static void testAddReport(Selenium selenium) throws Exception {
		
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
		
		String version = selenium.getText("//div[@class=\"footerText\"]");
		version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);
		
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("50000");
		
		//ADD REPORT
		boolean addReportAvailable = false;
		try {
			selenium.click("//a[contains(@href, \"/reportWizard.do?tabs=false&reset=true\")]");
			selenium.waitForPageToLoad("360000");
			addReportAvailable = true;
		} catch (Exception e) {
			logger.error("Option \"Add Report\" is not available.");
		}
		
		if (addReportAvailable) {	
			//SET THE PERIOD OF THE REPORT TO QUARTERLY
			selenium.click("//input[@name='reportPeriod' and @value='Q']");
			selenium.click("//li[@id='columns_tab_label']/a/div");
			//selenium.click("//li[@id='DHTMLSuite_treeNode2']/img[1]");
			try {
				selenium.click("fieldVis:4");
			} catch (Exception e) {
				selenium.click("fieldVis:12");
				logger.error("Option \"Project Title\" is not available.");
			}
			selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')\"]");
			selenium.click("//li[@id='measures_tab_label']/a/div");
			try {
				selenium.click("//li[@id='measure_1']/input");
				measActualComm = true;
			} catch (Exception e) {
				measActualComm = false;
				logger.info("Measure \"Actual Commitments\" is not available.");
			}
			try {
				selenium.click("//li[@id='measure_2']/input");
				measActualDist = true;
			} catch (Exception e) {
				measActualDist = false;
				logger.info("Measure \"Actual Disbursement\" is not available.");
			}
			try {
				selenium.click("//li[@id='measure_4']/input");
				measPlannedComm = true;
			} catch (Exception e) {
				measPlannedComm = false;
				logger.info("Measure \"Planned Commitments\" is not available.");
			}
			try {
				selenium.click("//li[@id='measure_5']/input");
				measPlannedDist = true;
			} catch (Exception e) {
				measPlannedDist = false;
				logger.info("Measure \"Planned Disbursement\" is not available.");
			}
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
			selenium.click("step3_add_filters_button");
			SeleniumTestUtil.waitForElement(selenium, "indexString", 90);
			selenium.type("indexString",actNameFilter);
			selenium.click("filterPickerSubmitButton");
			Thread.sleep(30000);
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.typeKeys("reportTitle", reportName);
			selenium.click("last_save_button");
			selenium.waitForPageToLoad("50000");
			selenium.click("//p[@title='" + reportName + "']");
			//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
			Thread.sleep(30000);
			selenium.selectWindow("AMP : Reports "+reportName); 
			
			String filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
			if (!filters.contains(actNameFilter)) {
				logger.error("Filter by index string error");
			}
			int tdCnt = 2;
			for (int i = 0; i < ActivityFormTest.ACTUAL_COMMITMENTS.length; i++) {
				if (measActualComm) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_COMMITMENTS[i]));
					tdCnt++;
				}
				if (measActualDist) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_DISBURSEMENT[i]));
					tdCnt++;
				}
				if (measPlannedComm) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_COMMITMENTS[i]));
					tdCnt++;
				}
				if (measPlannedDist) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_DISBURSEMENT[i]));
					tdCnt++;
				}				
			}
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[22]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[23]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[24]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[25]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT));
			
			selenium.close();
			selenium.selectWindow("null");

			//CHANGE THE PERIOD OF THE REPORT TO MONTHLY
			boolean done = false;
			int cnt = 0;
			while (!done){
				try {
					String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
					if (repname.equals(reportName)) {
						selenium.click("//a[contains(@href, \"/aim/reportWizard.do~editReportId=" + cnt + "\")]");
						done = true;
						selenium.waitForPageToLoad("30000");					
					}
				} catch (Exception e) {}
				cnt++;
			}
			selenium.click("//input[@name='reportPeriod' and @value='M']");
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//p[@title='" + reportName + "']");
			//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
			Thread.sleep(10000);
			selenium.selectWindow("AMP : Reports "+reportName); 
	        filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
			if (!filters.contains(actNameFilter)) {
				logger.error("Filter by index string error");
			}
			tdCnt = 2;
			for (int i = 0; i < ActivityFormTest.ACTUAL_COMMITMENTS.length; i++) {
				if (measActualComm) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_COMMITMENTS[i]));
					tdCnt++;
				}
				if (measActualDist) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.ACTUAL_DISBURSEMENT[i]));
					tdCnt++;
				}
				if (measPlannedComm) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_COMMITMENTS[i]));
					tdCnt++;
				}
				if (measPlannedDist) {
					assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[" + tdCnt + "]/div").equals(ActivityFormTest.PLANNED_DISBURSEMENT[i]));
					tdCnt++;
				}				
			}
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[22]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[23]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[24]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[25]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT));
			
			selenium.close();
			selenium.selectWindow("null");

			//CHANGE THE PERIOD OF THE REPORT TO ANNUAL
			done = false;
			cnt = 0;
			while (!done){
				try {
					String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
					if (repname.equals(reportName)) {
						selenium.click("//a[contains(@href, \"/aim/reportWizard.do~editReportId=" + cnt + "\")]");
						done = true;
						selenium.waitForPageToLoad("30000");					
					}
				} catch (Exception e) {}
				cnt++;
			}
			selenium.click("//input[@name='reportPeriod' and @value='A']");
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//p[@title='" + reportName + "']");
			//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
			Thread.sleep(10000);
			selenium.selectWindow("AMP : Reports "+reportName); 
	        filters = selenium.getText("//div[@id='currentDisplaySettings']/table/tbody/tr[1]/td[1]");
			if (!filters.contains(actNameFilter)) {
				logger.error("Filter by index string error");
			}
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS_2008));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[3]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT_2008));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[4]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS_2008));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[5]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT_2008));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[6]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS_2009));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[7]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT_2009));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[8]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS_2009));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[9]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT_2009));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[10]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[11]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[12]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS));
			assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[13]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT));
			
			selenium.close();
			selenium.selectWindow("null");

			done = false;
			cnt = 0;
			while (!done){
				try {
					String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
					if (repname.equals(reportName)) {
						selenium.click("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=0~rid=" + cnt + "~event=edit\")]");
						assertNotNull(selenium.getConfirmation());
						done = true;						
						selenium.waitForPageToLoad("30000");					
					}
				} catch (Exception e) {}
				cnt++;
			}
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Report Test Finished Successfully");
	}
}

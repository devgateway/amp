package org.dgfoundation.amp.seleniumTest.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.activityForm.ActivityFormTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class TabTest extends SeleneseTestCase{

	private static Logger logger = Logger.getLogger(TabTest.class);
	
	public void setUp() throws Exception {
//		setUp("http://generic.ampdev.net", "*firefox");
//		setUp("http://drc.ampdev.net", "*firefox");
//		setUp("http://senegal.ampdev.net", "*firefox");
//		setUp("http://localhost:8080/", "*firefox");
		setUp("http://senegal.staging.ampdev.net/", "*chrome");
		
	}
	
	public static void testAddTab(Selenium selenium) throws Exception {
		
		String testTime =  String.valueOf(System.currentTimeMillis());
		String tabName ="Tab for selenium test " + testTime;
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
		
		if (selenium.isElementPresent("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]")) {
			selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
			selenium.waitForPageToLoad("50000");
			deleteAllTabs(selenium);
		}
		
		//ADD TAB
		boolean addTabAvailable = false;
		try {
			selenium.click("//a[contains(@href, \"/reportWizard.do?tab=true&reset=true\")]");
			selenium.waitForPageToLoad("50000");
			addTabAvailable = true;
		} catch (Exception e) {
			logger.error("Option \"Add Tab\" is not available.");
		}
		
		if (addTabAvailable) {			
			selenium.click("//li[@id='columns_tab_label']/a/div");
			//selenium.click("//li[@id='DHTMLSuite_treeNode2']/img[1]");
			int cnt = 0;
			try {
				selenium.click("fieldVis:4");
				cnt++;
			} catch (Exception e) {
			}
			try {
				selenium.click("fieldVis:12");
				cnt++;
			} catch (Exception e) {
			}
			try {
				selenium.click("fieldVis:14");
				cnt++;
			} catch (Exception e) {
			}
			try {
				selenium.click("fieldVis:9");
				cnt++;
			} catch (Exception e) {
			}
			try {
				selenium.click("fieldVis:6");
				cnt++;
			} catch (Exception e) {
			}
			selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')\"]");
			Thread.sleep(1000);
			if (cnt>3) {
				if (selenium.getText("//span[@id=\"columnsLimit\"]")==null) {
					logger.error("Message warning the Columns Limit is not shown");
				}
			}
			for (int i = 0; i < cnt; i++) {
				
				try {selenium.click("//ul[@id=\"dest_col_ul\"]/li[" + (i+1) + "]/input");} catch (Exception e) {}
			}
			selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.deselectObjs('dest_col_ul')\"]");			
			Thread.sleep(1000);
			try {
				selenium.click("fieldVis:4");
			} catch (Exception e) {
				selenium.click("fieldVis:12");
				logger.error("Option \"Project Title\" is not available.");
			}
			selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')\"]");
			Thread.sleep(1000);
			
			selenium.click("//li[@id='measures_tab_label']/a/div");
			cnt = 0;
			if (SeleniumFeaturesConfiguration.getFieldState("Actual Commitments")){
				if (selenium.isElementPresent("//li[@id='measure_1']/input")) {
					selenium.click("//li[@id='measure_1']/input");
					cnt++;
					measActualComm = true;
				} else {
					logger.error("Field \"Actual Commitments\" is active in Feature Manager but is not available.");
				}
			} else {
				measActualComm = false;
				logger.info("Field \"Actual Commitments\" is not available.");
			}
			if (SeleniumFeaturesConfiguration.getFieldState("Actual Disbursements")){
				if (selenium.isElementPresent("//li[@id='measure_2']/input")) {
					selenium.click("//li[@id='measure_2']/input");
					cnt++;
					measActualDist = true;
				} else {
					logger.error("Field \"Actual Disbursements\" is active in Feature Manager but is not available.");
				}
			} else {
				measActualDist = false;
				logger.info("Field \"Actual Disbursements\" is not available.");
			}
			if (SeleniumFeaturesConfiguration.getFieldState("Planned Commitments")){
				if (selenium.isElementPresent("//li[@id='measure_4']/input")) {
					selenium.click("//li[@id='measure_4']/input");
					cnt++;
					measPlannedComm = true;
				} else {
					logger.error("Field \"Planned Commitments\" is active in Feature Manager but is not available.");
				}
			} else {
				measPlannedComm = false;
				logger.info("Field \"Planned Commitments\" is not available.");
			}
			if (SeleniumFeaturesConfiguration.getFieldState("Planned Disbursements")){
				if (selenium.isElementPresent("//li[@id='measure_5']/input")) {
					selenium.click("//li[@id='measure_5']/input");
					cnt++;
					measPlannedDist = true;
				} else {
					logger.error("Field \"Planned Disbursements\" is active in Feature Manager but is not available.");
				}
			} else {
				measPlannedDist = false;
				logger.info("Field \"Planned Disbursements\" is not available.");
			}
			
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
			Thread.sleep(1000);
			if (cnt>3) {
				if (selenium.getText("//span[@id=\"columnsLimit\"]")==null) {
					logger.error("Message warning the Columns Limit is not shown");
				}				
			}
			for (int i = 0; i < cnt; i++) {
				try {selenium.click("//ul[@id=\"dest_measures_ul\"]/li[" + (i+1) + "]/input");} catch (Exception e) {}
			}
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')\"]");			
			Thread.sleep(1000);
			if (SeleniumFeaturesConfiguration.getFieldState("Actual Commitments")){
				if (selenium.isElementPresent("//li[@id='measure_1']/input")) {
					selenium.click("//li[@id='measure_1']/input");
					measActualComm = true;
				} else {
					logger.error("Field \"Actual Commitments\" is active in Feature Manager but is not available.");
				}
			} else {
				measActualComm = false;
				logger.info("Field \"Actual Commitments\" is not available.");
			}
			if (SeleniumFeaturesConfiguration.getFieldState("Actual Disbursements")){
				if (selenium.isElementPresent("//li[@id='measure_2']/input")) {
					selenium.click("//li[@id='measure_2']/input");
					measActualDist = true;
				} else {
					logger.error("Field \"Actual Disbursements\" is active in Feature Manager but is not available.");
				}
			} else {
				measActualDist = false;
				logger.info("Field \"Actual Disbursements\" is not available.");
			}

			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
			Thread.sleep(1000);
						
			if (SeleniumFeaturesConfiguration.getFieldState("Filter Button")){
				if (selenium.isElementPresent("step3_add_filters_button")) {
					selenium.click("step3_add_filters_button");
					SeleniumTestUtil.waitForElement(selenium, "indexString", 90);
					selenium.type("indexString",actNameFilter);
					selenium.click("filterPickerSubmitButton");
					Thread.sleep(30000);
				} else {
					logger.error("Field \"Filter Button\" is active in Feature Manager but is not available.");
				}
			} else {
				logger.info("Field \"Filter Button\" is not available.");
			}
			
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.typeKeys("reportTitle", tabName);
			selenium.click("last_save_button");
			selenium.waitForPageToLoad("50000");
			selenium.click("//a[@onclick='initializeTabManager()']");
			SeleniumTestUtil.waitForElement(selenium, "tabsId", 90);
			selenium.select("tabsId", "label=" + tabName);
			selenium.click("tabManagerButton");
			SeleniumTestUtil.waitForElement(selenium, "//a[contains(@href, \"showDesktop.do\")]", 90);
			selenium.click("//a[contains(@href, \"showDesktop.do\")]");
			selenium.waitForPageToLoad("50000");
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(30000);
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(30000);
			
			if (measActualComm) {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS)) {
					logger.error("Error on TOTAL_ACTUAL_COMMITMENTS shown");
				}
			} else {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)) {
					logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown");
				}
			}
			if (measActualDist & measActualComm) {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[3]/div").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)) {
					logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown");
				}
			}
			selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
			selenium.waitForPageToLoad("50000");
			/*int lastId = 0;
			for (int i = 0; i < 1000; i++) {
				if (selenium.isElementPresent("//a[contains(@href, \"/aim/reportWizard.do~editReportId=" + i + "\")]")) {
					lastId = i;
				}
			}
			selenium.click("//a[contains(@href, \"/aim/reportWizard.do~editReportId=" + lastId + "\")]");*/
			selenium.click("//img[@src=\"/repository/message/view/images/edit.gif\"]");
			selenium.waitForPageToLoad("50000");
			selenium.click("//li[@id='measures_tab_label']/a/div");
			try {selenium.click("//li[@id='measure_1']/input");} catch (Exception e) {}
			try {selenium.click("//li[@id='measure_2']/input");} catch (Exception e) {}
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')\"]");			
			Thread.sleep(1000);
			if (SeleniumFeaturesConfiguration.getFieldState("Planned Commitments")){
				if (selenium.isElementPresent("//li[@id='measure_4']/input")) {
					selenium.click("//li[@id='measure_4']/input");
					cnt++;
					measPlannedComm = true;
				} else {
					logger.error("Field \"Planned Commitments\" is active in Feature Manager but is not available.");
				}
			} else {
				measPlannedComm = false;
				logger.info("Field \"Planned Commitments\" is not available.");
			}
			if (SeleniumFeaturesConfiguration.getFieldState("Planned Disbursements")){
				if (selenium.isElementPresent("//li[@id='measure_5']/input")) {
					selenium.click("//li[@id='measure_5']/input");
					cnt++;
					measPlannedDist = true;
				} else {
					logger.error("Field \"Planned Disbursements\" is active in Feature Manager but is not available.");
				}
			} else {
				measPlannedDist = false;
				logger.info("Field \"Planned Disbursements\" is not available.");
			}
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
			Thread.sleep(1000);
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.waitForPageToLoad("50000");
			selenium.click("//a[contains(@href, \"showDesktop.do\")]");
			selenium.waitForPageToLoad("50000");
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(30000);
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(30000);
			
			if (measPlannedComm) {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_PLANNED_COMMITMENTS)) {
					logger.error("Error on TOTAL_PLANNED_COMMITMENTS shown");
				}
			} else {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT)) {
					logger.error("Error on TOTAL_PLANNED_DISBURSEMENT shown");
				}
			}
			if (measPlannedDist & measPlannedComm) {
				if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[3]/div").equals(ActivityFormTest.TOTAL_PLANNED_DISBURSEMENT)) {
					logger.error("Error on TOTAL_PLANNED_DISBURSEMENT shown");
				}
			}
			selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
			selenium.waitForPageToLoad("50000");
			/*selenium.click("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=1~rid=" + lastId + "~event=edit\")]");
			assertNotNull(selenium.getConfirmation());
			selenium.waitForPageToLoad("50000");*/
			deleteAllTabs(selenium);
			selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
			selenium.waitForPageToLoad("30000");
			logger.info("Tab Test Finished Successfully");

		}
	}

	public static void deleteAllTabs(Selenium selenium) {
		while (selenium.isElementPresent("//a[@onclick=\"return confirmFunc()\"]")) {
			selenium.click("//a[@onclick=\"return confirmFunc()\"]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
		}		
	}
	
	/**
	 * 
	 * @param tabName
	 * @param filter
	 * @throws InterruptedException
	 */
	public static void addBasicTab (Selenium selenium, String tabName, String filter) throws InterruptedException{
		boolean addTabAvailable = false;
		try {
			selenium.click("//a[contains(@href, \"/reportWizard.do?tab=true&reset=true\")]");
			selenium.waitForPageToLoad("50000");
			addTabAvailable = true;
		} catch (Exception e) {
			logger.error("Option \"Add Tab\" is not available.");			
		}
		
		if (addTabAvailable) {			
			selenium.click("//li[@id='columns_tab_label']/a/div");
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
			} catch (Exception e) {
				logger.info("Measure \"Actual Commitments\" is not available.");
			}
			try {
				selenium.click("//li[@id='measure_2']/input");
			} catch (Exception e) {
				logger.info("Measure \"Actual Disbursement\" is not available.");
			}
			selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
			selenium.click("step3_add_filters_button");
			SeleniumTestUtil.waitForElement(selenium, "indexString", 90);
			selenium.type("indexString", filter);
			selenium.click("filterPickerSubmitButton");
			Thread.sleep(12000);
			selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
			selenium.typeKeys("reportTitle", tabName);
			selenium.click("last_save_button");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[@onclick='initializeTabManager()']");
			SeleniumTestUtil.waitForElement(selenium, "tabsId", 90);
			Thread.sleep(2500);
			selenium.select("tabsId", "label=" + tabName);
			selenium.click("tabManagerButton");
			SeleniumTestUtil.waitForElement(selenium, "//a[contains(@href, \"showDesktop.do\")]", 90);
			Thread.sleep(2500);
			selenium.click("//a[contains(@href, \"showDesktop.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(12000);
			selenium.click("//a[@id='Tab-" + tabName + "']/div");
			Thread.sleep(12000);
			
		}
	}
}

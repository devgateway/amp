package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class GlobalSettingsTest extends SeleneseTestCase {
		
	private static Logger logger = Logger.getLogger(GlobalSettingsTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testGlobalSettings(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		boolean enbabledPublicView = false;
		boolean alertSum = false;
		String endYear = "";
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/GlobalSettings.do\")]");
		selenium.waitForPageToLoad("30000");
		int cnt = 2;
		boolean done = false;
		while (!done) {
			if (selenium.getAttribute("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Public View")) {
				if (selenium.getSelectedIndex("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select").equals("0")) {
					selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=1");
					enbabledPublicView = false;
				} else {
					selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=0");
					enbabledPublicView = true;	
				}
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		cnt = 2;
		done = false;
		while (!done) {
			if (selenium.getAttribute("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Alert if sum of disbursments is bigger than sum of commitments")) {
				if (selenium.getSelectedIndex("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/select").equals("0")) {
					selenium.select("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/select", "index=1");
					alertSum = false;
				} else {
					selenium.select("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/select", "index=0");
					alertSum = true;	
				}
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		cnt = 2;
		done = false;
		while (!done) {
			if (selenium.getAttribute("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Change Range Default End Value")) {
				endYear = selenium.getSelectedValue("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/select");
				selenium.select("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/select", "value=2029");
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		selenium.click("saveAll");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		if (enbabledPublicView) {
			assertTrue(selenium.isElementPresent("//a[contains(@href, \"/aim/reportsPublicView.do\")]"));
		} else {
			assertTrue(!selenium.isElementPresent("//a[contains(@href, \"/aim/reportsPublicView.do\")]"));
		}
		selenium.type("j_username", "UATtl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		if (!selenium.isElementPresent("MyTabs")) {
			SeleniumTestUtil.addBasicTab(selenium, "Test Tab TMC 2 " + testTime, testTime);
		}
		assertTrue(selenium.getText("//div[@id=\"currentDisplaySettings\"]/table/tbody/tr[2]/td").contains("2029"));
		selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
		selenium.waitForPageToLoad("120000");
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
		//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
		Thread.sleep(10000);
		selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
        SeleniumTestUtil.waitForElement(selenium,"funding.assistanceType", 90);
		selenium.select("funding.assistanceType", "index=1");
		selenium.type("orgFundingId", "12345");
		selenium.select("funding.modality", "index=1");
		selenium.click("//input[@onclick='addFundingDetail(0)']"); //add a commitment (actual)
		SeleniumTestUtil.waitForElement(selenium,"fundingDetail[0].adjustmentType", 90);
		try {
			selenium.select("fundingDetail[0].adjustmentType", "index=0");
			selenium.type("fundingDetail[0].transactionAmount", "100");
			selenium.type("fundingDetail[0].transactionDate", "09/09/2009");
		} catch (Exception e) {
			logger.info("Option \"Actual\" is not available for commitments.");
		}
		selenium.click("//input[@onclick='addFundingDetail(1)']"); //add a disbursement (actual)
		SeleniumTestUtil.waitForElement(selenium,"fundingDetail[1].adjustmentType", 90);
		try {
			selenium.select("fundingDetail[1].adjustmentType", "index=0");
			selenium.type("fundingDetail[1].transactionAmount", "200");
			selenium.type("fundingDetail[1].transactionDate", "09/09/2009");
		} catch (Exception e) {
			logger.info("Option \"Actual\" is not available for disbursement.");
		}
		
		selenium.click("//input[@onclick=\"return addFunding()\"]");
		if (alertSum) {
			selenium.close();
			selenium.selectWindow("null");
		} else {
			selenium.selectWindow("null");
			selenium.waitForPageToLoad("50000");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
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
			if (selenium.getAttribute("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Public View")) {
				if (enbabledPublicView) {
					selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=1");
				} else {
					selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=0");
				}
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		cnt = 2;
		done = false;
		while (!done) {
			if (selenium.getAttribute("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Alert if sum of disbursments is bigger than sum of commitments")) {
				if (alertSum) {
					selenium.select("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/select", "index=1");
				} else {
					selenium.select("//table[@id='funding']/tbody/tr["+cnt+"]/td[2]/select", "index=0");
				}
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		cnt = 2;
		done = false;
		while (!done) {
			if (selenium.getAttribute("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Change Range Default End Value")) {
				selenium.select("//table[@id='date']/tbody/tr["+cnt+"]/td[2]/select", "value="+endYear);
				done = true;
			}
			cnt++;
			if (cnt==20) {
				done = true;
			}
		}
		selenium.click("saveAll");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Global Settings Test Finished Successfully");
	}
}

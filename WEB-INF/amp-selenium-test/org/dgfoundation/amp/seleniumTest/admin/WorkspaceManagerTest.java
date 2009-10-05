package org.dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class WorkspaceManagerTest  extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(WorkspaceManagerTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*chrome");
	}
	public static void testWorkspaceManager(Selenium selenium) throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String wsName ="Test Workspace Manager " + testTime;
		
		selenium.open("/");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/aim/workspaceManager.do~page=1\")]")) {
			selenium.click("//a[contains(@href, \"/aim/workspaceManager.do~page=1\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/createWorkspace.do~dest=admin\")]");
			selenium.waitForPageToLoad("30000");
			selenium.type("teamName", wsName);
			selenium.select("workspaceType", "index=2");
			selenium.click("//input[@onclick=\"update('add')\"]");
			selenium.waitForPageToLoad("30000");
			String tId = selenium.getAttribute("link="+wsName+"@href");
			tId = tId.substring(tId.indexOf("tId=")+4, tId.indexOf("~event"));
			selenium.click("//a[contains(@href, '/aim/teamActivities.do~id="+tId+"')]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, '/aim/assignActivity.do~id="+tId+"')]");
			selenium.waitForPageToLoad("30000");
			selenium.click("selectedActivities");
			String aId = selenium.getAttribute("selectedActivities@value");
			selenium.click("//input[@type=\"submit\"]");
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.getAttribute("selActivities@value").equals(aId));
			selenium.click("selActivities");
			selenium.click("//input[@onclick=\"return confirmDelete()\"]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, \"/aim/workspaceManager.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[contains(@href, '/aim/deleteWorkspace.do~tId="+tId+"~event=delete')]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
		} else {
			logger.error("Module \"Workspace Manager\" is not available");
		}
		
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Workspace Manager Test Finished Successfully");
		
	}
}
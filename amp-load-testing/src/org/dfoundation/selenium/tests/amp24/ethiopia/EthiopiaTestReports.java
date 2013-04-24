package org.dfoundation.selenium.tests.amp24.ethiopia;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

public class EthiopiaTestReports extends EthiopiaTest {

	public EthiopiaTestReports() {
		super();
	}
	

	@Test
	public void EthiopiaTestReports() throws Exception {
		selenium.open("/");
		selenium.click("id=show_login_pop");
		selenium.type("id=j_username", "anebebe@mofed.gov");
		selenium.type("id=j_password", "abc");
		selenium.click("css=input.buttonx_sm_lgn");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UN");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Reports");
		selenium.click("link=All Reports");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=p[title=\"exec agency monthly88\"]");
		selenium.waitForPopUp("null", "15000");
		selenium.selectPopUp("null");
//		String[] windows = selenium.getAllWindowTitles();
//		for (int i = 0; i < windows.length; i++) {
//			System.out.println(windows[i]);
//		}
		selenium.click("link=Change Filters");
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isVisible("name=indexString")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		
		selenium.click("css=button[type=\"button\"]");
		selenium.click("xpath=(//button[@type='button'])[6]");
		selenium.click("xpath=(//button[@type='button'])[12]");
		selenium.click("xpath=(//button[@type='button'])[17]");
		selenium.click("xpath=(//button[@type='button'])[21]");
		selenium.click("xpath=(//button[@type='button'])[28]");
		selenium.click("//div[@id='tabview_container']/ul/li[3]/a/div");
		selenium.click("xpath=(//input[@name='selectedSectors'])[11]");
		selenium.click("id=filterPickerSubmitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=img[alt=\"Export to Excel\"]");
		selenium.click("css=input.dr-menu.buton");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}

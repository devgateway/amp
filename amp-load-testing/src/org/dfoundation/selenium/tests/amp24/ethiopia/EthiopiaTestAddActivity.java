package org.dfoundation.selenium.tests.amp24.ethiopia;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class EthiopiaTestAddActivity extends EthiopiaTest {
	
	@Test
	public void EthiopiaTest1RC() throws Exception {
		selenium.open("/");
		selenium.click("id=show_login_pop");
		selenium.type("id=j_username", "anebebe@mofed.gov");
		selenium.type("id=j_password", "abc");
		selenium.click("css=input.buttonx_sm_lgn");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UN");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Desktop");
		selenium.click("link=Add Activity");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=id5", selenium.getEval("\"Test Selenium \"+Math.floor(Math.random()*11111);"));
		selenium.select("id=id53", "label=Ongoing");
		selenium.type("id=id123", "objectives comments");
		selenium.keyPress("id=id123", "\\13");
		selenium.select("id=id55", "label=On Budget");
		selenium.click("id=id129");
		selenium.type("id=id129", "purpose comment");
		selenium.keyPress("id=id129", "\\13");
		selenium.type("id=id12e", "results comments");
		selenium.keyPress("id=id12e", "\\13");
		Thread.sleep(1000);
		selenium.click("id=qItem416374697669747920496e7465726e616c20494473");
		selenium.click("id=yui-gen0-button");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isVisible("id=id67")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("xpath=id('id67')/x:div[1]/x:div[2]/x:ul/x:li[1]");
		Thread.sleep(1000);
		selenium.click("id=qItem506c616e6e696e67");
		selenium.click("id=id6cIcon");
		selenium.click("id=id6cDpJs_cell2");
		selenium.click("id=id76Icon");
		selenium.click("css=#id76DpJs_cell8 > a.selector");
		selenium.click("id=id6dIcon");
		selenium.click("id=id6dDpJs_cell11");
		selenium.click("id=id6bIcon");
		selenium.click("id=id6bDpJs_cell15");
		selenium.click("id=id6fIcon");
		selenium.click("css=#id6fDpJs_cell11 > a.selector");
		selenium.click("id=id6aIcon");
		selenium.click("css=#id6aDpJs_cell17 > a.selector");
		selenium.click("id=id70Icon");
		selenium.click("css=#id70DpJs_cell12 > a.selector");
		selenium.click("id=id152");
		selenium.type("id=id152", "comment completion date");
		selenium.keyPress("id=id152", "\\13");
		selenium.click("id=id159");
		selenium.type("id=id159", "comment disbursements date");
		selenium.keyPress("id=id159", "\\13");
		Thread.sleep(1000);
		selenium.click("id=qItem4c6f636174696f6e");
		selenium.select("id=id79", "label=Regional");
		selenium.select("id=id7a", "label=Region");
		selenium.click("id=yui-gen1-button");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isVisible("id=id82")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("xpath=id('id82')/x:div[1]/x:div[2]/x:ul/x:li[1]");
		Thread.sleep(1000);
		selenium.click("id=qItem50726f6772616d");
		selenium.click("id=yui-gen2-button");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isVisible("id=id8b")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("xpath=id('id8b')/x:div[1]/x:div[2]/x:ul/x:li[2]");
		Thread.sleep(1000);
		selenium.click("id=qItem536563746f7273");
		selenium.click("id=yui-gen5-button");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isVisible("id=ida7")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("xpath=id('ida7')/x:div[1]/x:div[2]/x:ul/x:li[11]");
		Thread.sleep(1000);
		selenium.click("name=saveAndSubmit:fieldButton");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
	

}

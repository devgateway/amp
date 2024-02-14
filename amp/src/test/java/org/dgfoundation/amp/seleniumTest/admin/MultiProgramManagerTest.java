package org.dgfoundation.amp.seleniumTest.admin;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class MultiProgramManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(MultiProgramManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testMultiProgramManager(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String progName ="Program Test " + testTime;
        String progName1 ="Program Test " + testTime + " Sub 1";
        String progName2 ="Program Test " + testTime + " Sub 2";
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (SeleniumFeaturesConfiguration.getModuleState("National Planning Dashboard")){
            if (selenium.isElementPresent("//a[contains(@href, \"/aim/themeManager.do~view=multiprogram\")]")) {
                selenium.click("//a[contains(@href, \"/aim/themeManager.do~view=multiprogram\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("addBtn");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.type("programName", progName);
                selenium.type("programCode", "PTS");
                selenium.select("programTypeCategValId", "index=1");
                selenium.click("addBtn");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("30000");
                String pId = selenium.getAttribute("link="+progName+"@href");
                pId = pId.substring(pId.indexOf("ram(")+4, pId.indexOf(")"));
                selenium.click("//a[contains(@href, \"javascript:addSubProgram('5','"+pId+"','0','"+progName+"')\")]");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.type("programName", progName1);
                selenium.type("programCode", "PTS1");
                selenium.select("programTypeCategValId", "index=1");
                selenium.click("addBtn");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("30000");
                String pId1 = selenium.getAttribute("link="+progName1+"@href");
                pId1 = pId1.substring(pId1.indexOf("ram(")+4, pId1.indexOf(")"));
                selenium.click("//a[contains(@href, \"javascript:addSubProgram('5','"+pId1+"','1','"+progName1+"')\")]");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.type("programName", progName2);
                selenium.type("programCode", "PTS2");
                selenium.select("programTypeCategValId", "index=1");
                selenium.click("addBtn");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/programConfigurationPage.do\")]");
                selenium.waitForPageToLoad("30000");
                String progIndex = selenium.getSelectedIndex("settingsList[0].defaultHierarchyId");
                selenium.select("settingsList[0].defaultHierarchyId", "label="+progName);
                selenium.click("save");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                selenium.waitForPageToLoad("30000");
                
                selenium.type("j_username", "UATtl@amp.org");
                selenium.type("j_password", "abc");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("link=UAT Team Workspace");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
                selenium.waitForPageToLoad("120000");
                if (selenium.isElementPresent("//a[@href='javascript:gotoStep(2)']")) {
                    selenium.click("//a[@href='javascript:gotoStep(2)']");
                    selenium.waitForPageToLoad("50000");
                    if (selenium.isElementPresent("//input[@onclick=\"addProgram(1);\"]")) {
                        selenium.click("//input[@onclick=\"addProgram(1);\"]");
                        Thread.sleep(10000);
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                        selenium.select("//td[@id='slo1']/select", "label="+progName1);
                        Thread.sleep(10000);
                        selenium.select("//td[@id='slo2']/select", "label="+progName2);
                        Thread.sleep(2000);
                        selenium.click("submitButton");
                        selenium.selectWindow("null");
                        selenium.waitForPageToLoad("30000");
                    }
                }
                selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                selenium.waitForPageToLoad("30000");
                
                selenium.type("j_username", "admin@amp.org");
                selenium.type("j_password", "admin");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/programConfigurationPage.do\")]");
                selenium.waitForPageToLoad("30000");
                selenium.select("settingsList[0].defaultHierarchyId", "index="+progIndex);
                selenium.click("save");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/themeManager.do~view=multiprogram\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/themeManager.do~event=delete~themeId="+pId+"\")]");
                selenium.getConfirmation();
                Thread.sleep(10000);
                if (selenium.isElementPresent("link="+progName)) {
                    logger.error("Program wasn't deleted");
                    //selenium.logAssertion"assertTrue", "Program wasn't deleted", "condition=false");
                }
            } else {
                logger.error("Module \"National Planning Dashboard\" is active in Feature Manager but is not available.");
                //selenium.logAssertion"assertTrue", "Module \"National Planning Dashboard\" is active in Feature Manager but is not available.", "condition=false");
            }
        } else {
            logger.info("Module \"National Planning Dashboard\" is not available.");
           //selenium.logComment("Module \"National Planning Dashboard\" is not available.");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Multi Program Manager Test Finished Successfully");
       //selenium.logComment("Multi Program Manager Test Finished Successfully");
    }
}


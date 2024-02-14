package org.dgfoundation.amp.seleniumTest.admin;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.apache.log4j.Logger;


import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class SectorManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(SectorManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testSectorManager(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String schemeName = "Selenium Scheme " + testTime;
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/getSectorSchemes.do\")]")) {
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/updateSectorSchemes.do~dest=admin~event=addscheme\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("secSchemeName", schemeName);
            selenium.type("secSchemeCode", "SSH");
            selenium.click("addBtn");
            selenium.waitForPageToLoad("30000");
            String scId = selenium.getAttribute("link="+schemeName+"@href");
            scId = scId.substring(scId.indexOf("eId=")+4);
            selenium.click("link=" + schemeName);
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/addSector.do~parent=scheme~ampSecSchemeId="+scId+"\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("sectorName", "Selenium Sector");
            selenium.type("sectorCodeOfficial", "SST1");
            selenium.click("addBtn");
            selenium.waitForPageToLoad("30000");
            String s1Id = selenium.getAttribute("link=Selenium Sector@href");
            s1Id = s1Id.substring(s1Id.indexOf("Id=")+3);
            selenium.click("link=Selenium Sector");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/addSector.do~parent=sector~ampSecSchemeId="+s1Id+"\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("sectorName", "Selenium SubSector");
            selenium.type("sectorCodeOfficial", "SST2");
            selenium.click("addBtn");
            selenium.waitForPageToLoad("30000");
            String s2Id = selenium.getAttribute("link=Selenium SubSector@href");
            s2Id = s2Id.substring(s2Id.indexOf("Id=")+3);
            selenium.click("link=Selenium SubSector");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/addSector.do~level=three~parent=sector3~ampSecSchemeId="+s2Id+"\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("sectorName", "Selenium SubSubSector");
            selenium.type("sectorCodeOfficial", "SST3");
            selenium.click("addBtn");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorClassConfig.do\")]");
            selenium.waitForPageToLoad("30000");
            String classification = "";
            int classId = 0;
            for (int i = 1; i < 20; i++) {
                selenium.click("//a[contains(@href, \"/aim/updateSectorClassConfig.do~event=edit~id="+i+"\")]");
                selenium.waitForPageToLoad("30000");
                if (selenium.getText("configName").equalsIgnoreCase("Primary")) {
                    classification = selenium.getSelectedIndex("sectorClassId");
                    classId = i;
                    selenium.select("sectorClassId", schemeName);
                    selenium.click("//input[@onclick=\"saveClicked()\"]");
                    selenium.waitForPageToLoad("30000");
                    break;
                }
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "UATtl@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"javascript:addActivity()\")]")) {
                selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
                selenium.waitForPageToLoad("120000");
                if (selenium.isElementPresent("//a[@href='javascript:gotoStep(2)']")) {
                    selenium.click("//a[@href='javascript:gotoStep(2)']");
                    selenium.waitForPageToLoad("50000");
                    if (SeleniumFeaturesConfiguration.getFeatureState("Sectors")){
                        //Add Primary Sector
                        if (SeleniumFeaturesConfiguration.getFieldState("Primary Sector")){
                            if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addSectors(false,1);']")) {
                                selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,1);']");
                                SeleniumTestUtil.waitForElement(selenium,"sector", 90);
                                selenium.select("sector", "label=Selenium Sector");
                                Thread.sleep(3000);
                                selenium.select("subsectorLevel1", "label=Selenium SubSector");
                                Thread.sleep(3000);
                                selenium.select("subsectorLevel2", "label=Selenium SubSubSector");
                                selenium.click("addButton");
                                selenium.waitForPageToLoad("30000");
                            } else {
                                logger.error("Field \"Primary Sector\" is active in Feature Manager but is not available.");
                                //selenium.logAssertion"assertTrue", "Field \"Primary Sector\" is active in Feature Manager but is not available.", "condition=false");
                            }
                        } else {
                            logger.info("Field \"Primary Sector\" is not available.");
                           //selenium.logComment("Field \"Primary Sector\" is not available.");
                        }
                    } else {
                        logger.info("Feature \"Sectors\" is not available.");
                       //selenium.logComment("Feature \"Sectors\" is not available.");
                    }
                }           
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorClassConfig.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/updateSectorClassConfig.do~event=edit~id="+classId+"\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("sectorClassId", "index="+classification);
            selenium.click("//input[@onclick=\"saveClicked()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+schemeName);
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Selenium Sector");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Selenium SubSector");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return onDelete()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+schemeName);
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Selenium Sector");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return onDelete()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+schemeName);
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick=\"return onDelete()\"]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/getSectorSchemes.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, '/aim/updateSectorSchemes.do~event=deleteScheme~ampSecSchemeId="+scId+"')]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("link="+schemeName)) {
                logger.error("Scheme wasn't deleted");
                //selenium.logAssertion"assertTrue", "Scheme wasn't deleted", "condition=false");
            }
        } else {
            logger.error("Module \"Sector Manager\" is not available.");
            //selenium.logAssertion"assertTrue", "Module \"Sector Manager\" is not available.", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Sector Manager Test Finished Successfully");
       //selenium.logComment("Sector Manager Test Finished Successfully");
    }
}


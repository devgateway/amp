package org.dgfoundation.amp.seleniumTest.admin;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class ComponentTypeManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(ComponentTypeManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testComponentTypeManager(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String typeName ="Test Component Manager " + testTime;
        boolean checkable = true;
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/updateComponentType.do\")]")) {
            selenium.click("//a[contains(@href, \"/aim/updateComponentType.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("submitButton");
            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "30000");
            Thread.sleep(10000);
            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Name")){
                if (selenium.isElementPresent("name")) {
                    selenium.type("name", typeName);
                } else {
                    checkable = false;
                    logger.error("Field \"Admin - Component Type Name\" is active in Feature Manager but is not available.");
                    //selenium.logAssertion"assertTrue", "Field \"Admin - Component Type Name\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Name\" is not available.");
               //selenium.logComment("Field \"Admin - Component Type Name\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Code")){
                if (selenium.isElementPresent("code")) {
                    selenium.type("code", "SCT");
                } else {
                    checkable = false;
                    logger.error("Field \"Admin - Component Type Code\" is active in Feature Manager but is not available.");
                    //selenium.logAssertion"assertTrue", "Field \"Admin - Component Type Code\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Code\" is not available.");
               //selenium.logComment("Field \"Admin - Component Type Code\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Save Button")){
                if (selenium.isElementPresent("addBtn")) {
                    selenium.click("addBtn");
                    selenium.selectWindow("null");
                    selenium.waitForPageToLoad("30000");
                } else {
                    checkable = false;
                    selenium.close();
                    selenium.selectWindow("null");
                    logger.error("Field \"Admin - Component Type Save Button\" is active in Feature Manager but is not available.");
                    //selenium.logAssertion"assertTrue", "Field \"Admin - Component Type Save Button\" is active in Feature Manager but is not available.", "condition=false");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Save Button\" is not available.");
               //selenium.logComment("Field \"Admin - Component Type Save Button\" is not available.");
            }
            if (checkable) {
                int tId = 0;
                for (int i = 500; i > 0; i--) {
                    if (selenium.isElementPresent("//a[contains(@href, 'javascript:editType("+i+")')]")) {
                        tId = i;
                        break;
                    }
                }
                selenium.click("//a[contains(@href, 'javascript:editType("+tId+")')]");
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "30000");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.type("name", typeName+" mod");
                selenium.click("addBtn");
                selenium.selectWindow("null");
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
                selenium.waitForPageToLoad("30000");
                if (SeleniumFeaturesConfiguration.getFeatureState("Components")){
                    if (selenium.isElementPresent("//a[@href='javascript:gotoStep(5)']")) {
                        selenium.click("//a[@href='javascript:gotoStep(5)']");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("//input[@onclick=\"addComponents()\"]");
                        Thread.sleep(5000);
                        if (SeleniumFeaturesConfiguration.getFeatureState("Admin - Component Type")){
                            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(5)']")) {
                                try {
                                    selenium.select("selectedType", typeName+" mod");
                                } catch (Exception e) {
                                    logger.error("Component type added is not available on Activity Form");
                                    //selenium.logAssertion"assertTrue", "Component type added is not available on Activity Form", "condition=false");
                                }
                            } else {
                                logger.error("Feature \"Admin - Component Type\" is active in Feature Manager but is not available.");
                                //selenium.logAssertion"assertTrue", "Feature \"Admin - Component Type\" is active in Feature Manager but is not available.", "condition=false");
                            }
                        } else {
                            logger.info("Feature \"Admin - Component Type\" is not available.");
                           //selenium.logComment("Feature \"Admin - Component Type\" is not available.");
                        }
                        selenium.type("newCompoenentName", "Selenium Component");
                        selenium.click("//div[@id='new']/div[3]");              
                    } else {
                        logger.error("Feature \"Components\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Feature \"Components\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Feature \"Components\" is not available.");
                   //selenium.logComment("Feature \"Components\" is not available.");
                }
                
                selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                selenium.waitForPageToLoad("30000");
                
                selenium.type("j_username", "admin@amp.org");
                selenium.type("j_password", "admin");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/updateComponentType.do\")]");
                selenium.waitForPageToLoad("30000");
                try {
                    selenium.click("//a[contains(@href, 'javascript:deleteType("+tId+");')]");
                    selenium.getConfirmation();
                    selenium.waitForPageToLoad("30000");
                    if (selenium.isTextPresent(typeName)) {
                        logger.error("Component Type wasn't deleted");
                        //selenium.logAssertion"assertTrue", "Component Type wasn't deleted", "condition=false");
                    }
                } catch (Exception e) {
                    logger.error("Component Type is not available to be deleted");
                    //selenium.logAssertion"assertTrue", "Component Type is not available to be deleted", "condition=false");
                }
            }           
        } else {
            logger.error("Component Type Manager is not available");
            //selenium.logAssertion"assertTrue", "Component Type Manager is not available", "condition=false");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Component Type Manager Test Finished Successfully");
       //selenium.logComment("Component Type Manager Test Finished Successfully");
    }
}


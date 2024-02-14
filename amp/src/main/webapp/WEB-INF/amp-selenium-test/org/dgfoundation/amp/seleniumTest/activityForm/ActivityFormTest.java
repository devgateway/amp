package org.dgfoundation.amp.seleniumTest.activityForm;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class ActivityFormTest extends SeleneseTestCase{
    
    private static Logger logger = Logger.getLogger(ActivityFormTest.class);
    public static final String[] FUNDING_DATES = {"01/02/2008","13/06/2008","25/10/2008","10/03/2009","20/07/2009"};
    public static final String[] ACTUAL_COMMITMENTS = {"25","10","13","12","27"};
    public static final String[] ACTUAL_DISBURSEMENT = {"17","7","5","10","13"};
    public static final String[] PLANNED_COMMITMENTS = {"30","12","13","16","18"};
    public static final String[] PLANNED_DISBURSEMENT = {"11","9","2","2","3"};
    public static final String TOTAL_ACTUAL_COMMITMENTS = "87";
    public static final String TOTAL_ACTUAL_DISBURSEMENT = "52";
    public static final String TOTAL_PLANNED_COMMITMENTS = "89";
    public static final String TOTAL_PLANNED_DISBURSEMENT = "27";
    public static final String TOTAL_ACTUAL_COMMITMENTS_2008 = "48";
    public static final String TOTAL_ACTUAL_DISBURSEMENT_2008 = "29";
    public static final String TOTAL_PLANNED_COMMITMENTS_2008 = "55";
    public static final String TOTAL_PLANNED_DISBURSEMENT_2008 = "22";
    public static final String TOTAL_ACTUAL_COMMITMENTS_2009 = "39";
    public static final String TOTAL_ACTUAL_DISBURSEMENT_2009 = "23";
    public static final String TOTAL_PLANNED_COMMITMENTS_2009 = "34";
    public static final String TOTAL_PLANNED_DISBURSEMENT_2009 = "5";
    
    
    
    public void setUp() throws Exception {
//      setUp("http://generic.ampdev.net", "*firefox");
//      setUp("http://drc.ampdev.net", "*firefox");
//      setUp("http://senegal.ampdev.net", "*firefox");
//      setUp("http://localhost:8080/", "*firefox");
        setUp("http://senegal.staging.ampdev.net/", "*chrome");
    }
    public static void testAddActivity(LoggingSelenium selenium) throws Exception {
        
        String testTime =  String.valueOf(System.currentTimeMillis());
        String activityName ="Activity for selenium test " + testTime;
        String user = "uattl@amp.org";
        String password = "abc";
        
        String primarySector = "";
        String primarySubSector = "";
        String secondarySector = "";
        String secondarySubSector = "";
        String NPOProgram = "";
        String NPOSubProgram = "";
        String primaryProgram = "";
        String primarySubProgram = "";
        String secondaryProgram = "";
        String secondarySubProgram = "";
        int fundingQty = 5;
        
        selenium.open("/");
        selenium.type("j_username", user);
        selenium.type("j_password", password);
        selenium.click("submitButton");
        selenium.waitForPageToLoad("50000");
        
        String version = selenium.getText("//div[@class=\"footerText\"]");
        version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);
        
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("50000");

        boolean addAvailable = false;
        if (selenium.isElementPresent("//a[contains(@href, \"javascript:addActivity()\")]")) {
            selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
            selenium.waitForPageToLoad("120000");
            addAvailable = true;
        } else {
            logger.error("Option \"Add Activity\" is not available.");
            //selenium.logAssertion"assertTrue", "Option \"Add Activity\" is not available.", "condition=false");
        }
        if (addAvailable) {
            //Identification
            if (SeleniumFeaturesConfiguration.getFeatureState("Identification")){
                if (SeleniumFeaturesConfiguration.getFieldState("Project Title")){
                    if (selenium.isElementPresent("identification.title")) {
                        selenium.type("identification.title", activityName);
                    } else {
                        logger.error("Field \"Project Title\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Project Title\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Project Title\" is not available.");
                   //selenium.logComment("Field \"Project Title\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Accession Instrument")){
                    if (selenium.isElementPresent("identification.accessionInstrument")) {
                        selenium.select("identification.accessionInstrument", "index=1");
                    } else {
                        logger.error("Field \"Accession Instrument\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Accession Instrument\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Accession Instrument\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Project Category")){
                    if (selenium.isElementPresent("identification.projectCategory")) {
                        selenium.select("identification.projectCategory", "index=1");
                    } else {
                        logger.error("Field \"Project Category\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Project Category\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Project Category\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Government Agreement Number")){
                    if (selenium.isElementPresent("identification.govAgreementNumber")) {
                        selenium.type("identification.govAgreementNumber", "12345");
                    } else {
                        logger.error("Field \"Government Agreement Number\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Government Agreement Number\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Government Agreement Number\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Budget Code Project ID")){
                    if (selenium.isElementPresent("myInput")) {
                        selenium.type("myInput", "11111");
                    } else {
                        logger.error("Field \"Budget Code Project ID\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Budget Code Project ID\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Budget Code Project ID\" is not available.");
                   //selenium.logComment("Field \"Budget Code Project ID\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("On/Off Budget")){
                    if (selenium.isElementPresent("budget")) {
                        selenium.click("budget");
                    } else {
                        logger.error("Field \"On/Off Budget\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"On/Off Budget\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Option \"Activity Budget\" is not available.");
                   //selenium.logComment("Option \"Activity Budget\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("A.C. Chapter")){
                    if (selenium.isElementPresent("identification.acChapter")) {
                        selenium.select("identification.acChapter", "index=1");
                    } else {
                        logger.error("Field \"A.C. Chapter\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"A.C. Chapter\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"A.C. Chapter\" is not available.");
                   //selenium.logComment("Field \"A.C. Chapter\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Government Approval Procedures")){
                    if (selenium.isElementPresent("identification.governmentApprovalProcedures")) {
                        selenium.click("identification.governmentApprovalProcedures");
                    } else {
                        logger.error("Field \"Government Approval Procedures\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Government Approval Procedures\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Government Approval Procedures\" is not available.");
                   //selenium.logComment("Field \"Government Approval Procedures\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Joint Criteria")){
                    if (selenium.isElementPresent("identification.jointCriteria")) {
                        selenium.click("identification.jointCriteria");
                    } else {
                        logger.error("Field \"Joint Criteria\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Joint Criteria\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Joint Criteria\" is not available.");
                   //selenium.logComment("Field \"Joint Criteria\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Humanitarian Aid")){
                    if (selenium.isElementPresent("identification.humanitarianAid")) {
                        selenium.click("identification.humanitarianAid");
                    } else {
                        logger.error("Field \"Humanitarian Aid\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Humanitarian Aid\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Humanitarian Aid\" is not available.");
                   //selenium.logComment("Field \"Humanitarian Aid\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Cris Number")){
                    if (selenium.isElementPresent("identification.crisNumber")) {
                        selenium.type("identification.crisNumber", "2222");
                    } else {
                        logger.error("Field \"Cris Number\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Cris Number\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Cris Number\" is not available.");
                   //selenium.logComment("Field \"Cris Number\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Organizations and Project ID")){
                    if (selenium.isElementPresent("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitAfterSelectingOrg();~PARAM_COLLECTION_NAME=selectedOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]")) {
                        selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitAfterSelectingOrg();~PARAM_COLLECTION_NAME=selectedOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                        //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                        Thread.sleep(10000);
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
                       
                        SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                        selenium.click("//input[@onclick='return searchOrganization()']");
                        SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                        selenium.click("selOrganisations"); 
                        selenium.click("//input[@onclick='return selectOrganization()']");
                        selenium.selectWindow("null");
                        selenium.waitForPageToLoad("50000");
                    } else {
                        logger.error("Field \"Organizations and Project ID\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Organizations and Project ID\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Organizations and Project ID\" is not available.");
                   //selenium.logComment("Field \"Organizations and Project ID\" is not available.");
                }
            } else {
                logger.info("Feature \"Identification\" is not available.");
               //selenium.logComment("Feature \"Identification\" is not available.");
            }
            //Planning
            if (SeleniumFeaturesConfiguration.getFeatureState("Planning")){
                
                if (SeleniumFeaturesConfiguration.getFieldState("Line Ministry Rank")){
                    if (selenium.isElementPresent("planning.lineMinRank")) {
                        selenium.select("planning.lineMinRank", "index=1");
                    } else {
                        logger.error("Field \"Line Ministry Rank\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Line Ministry Rank\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Line Ministry Rank\" is not available.");
                   //selenium.logComment("Field \"Line Ministry Rank\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Ministry of Planning Rank")){
                    if (selenium.isElementPresent("planning.planMinRank")) {
                        selenium.select("planning.planMinRank", "index=1");
                    } else {
                        logger.error("Field \"Ministry of Planning Rank\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Ministry of Planning Rank\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Ministry of Planning Rank\" is not available.");
                   //selenium.logComment("Field \"Ministry of Planning Rank\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Proposed Approval Date")){
                    if (selenium.isElementPresent("originalAppDate")) {
                        selenium.type("originalAppDate","01/01/2009");
                    } else {
                        logger.error("Field \"Proposed Approval Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Proposed Approval Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Proposed Approval Date\" is not available.");
                   //selenium.logComment("Field \"Proposed Approval Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Actual Approval Date")){
                    if (selenium.isElementPresent("revisedAppDate")) {
                        selenium.type("revisedAppDate","02/01/2008");
                    } else {
                        logger.error("Field \"Actual Approval Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Actual Approval Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Actual Approval Date\" is not available.");
                   //selenium.logComment("Field \"Actual Approval Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Proposed Start Date")){
                    if (selenium.isElementPresent("originalStartDate")) {
                        selenium.type("originalStartDate","03/01/2008");
                    } else {
                        logger.error("Field \"Proposed Start Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Proposed Start Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Proposed Start Date\" is not available.");
                   //selenium.logComment("Field \"Proposed Start Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Actual Start Date")){
                    if (selenium.isElementPresent("revisedStartDate")) {
                        selenium.type("revisedStartDate","04/01/2008");
                    } else {
                        logger.error("Field \"Actual Start Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Actual Start Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Actual Start Date\" is not available.");
                   //selenium.logComment("Field \"Actual Start Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Final Date for Contracting")){
                    if (selenium.isElementPresent("contractingDate")) {
                        selenium.type("contractingDate","05/01/2008");
                    } else {
                        logger.error("Field \"Final Date for Contracting\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Final Date for Contracting\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Final Date for Contracting\" is not available.");
                   //selenium.logComment("Field \"Final Date for Contracting\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Final Date for Disbursements")){
                    if (selenium.isElementPresent("disbursementsDate")) {
                        selenium.type("disbursementsDate","06/01/2008");
                    } else {
                        logger.error("Field \"Final Date for Disbursements\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Final Date for Disbursements\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Final Date for Disbursements\" is not available.");
                   //selenium.logComment("Field \"Final Date for Disbursements\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Proposed Completion Date")){
                    if (selenium.isElementPresent("proposedCompDate")) {
                        selenium.type("proposedCompDate","07/01/2008");
                    } else {
                        logger.error("Field \"Proposed Completion Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Proposed Completion Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Proposed Completion Date\" is not available.");
                   //selenium.logComment("Field \"Proposed Completion Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Current Completion Date")){
                    if (selenium.isElementPresent("currentCompDate")) {
                        selenium.type("currentCompDate","08/01/2008");
                    } else {
                        logger.error("Field \"Current Completion Date\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Current Completion Date\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Current Completion Date\" is not available.");
                   //selenium.logComment("Field \"Current Completion Date\" is not available.");
                }
                if (SeleniumFeaturesConfiguration.getFieldState("Status")){
                    if (selenium.isElementPresent("planning.statusId")) {
                        selenium.select("planning.statusId", "index=1");
                        selenium.type("planning.statusReason", "N/A");
                    } else {
                        logger.error("Field \"Status\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Status\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Status\" is not available.");
                   //selenium.logComment("Field \"Status\" is not available.");
                }
            } else {
                logger.info("Feature \"Planning\" is not available.");
               //selenium.logComment("Feature \"Planning\" is not available.");
            }
        
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(2)']")) {
                selenium.click("//a[@href='javascript:gotoStep(2)']");
                selenium.waitForPageToLoad("50000");
                //Location
                if (SeleniumFeaturesConfiguration.getFeatureState("Location")){
                    if (SeleniumFeaturesConfiguration.getFieldState("Add Location")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='selectLocation()']")) {
                            selenium.select("location.levelId", "index=1");
                            selenium.waitForPageToLoad("50000");
                            selenium.select("location.implemLocationLevel", "index=1");
                            Thread.sleep(1000);
                            selenium.click("//input[@name='submitButton' and @onclick='selectLocation()']");
                            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                            Thread.sleep(10000);
                            selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
                           
                            SeleniumTestUtil.waitForElement(selenium,"loc_0", 90);
                            selenium.addSelection("loc_0", "index=0");
                            selenium.click("//input[@onclick=\"submitForm()\"]");
                            selenium.selectWindow("null");
                            selenium.waitForPageToLoad("50000");
                        } else {
                            logger.error("Field \"Add Location\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Add Location\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Add Location\" is not available.");
                       //selenium.logComment("Field \"Add Location\" is not available.");
                    }
                    if (SeleniumFeaturesConfiguration.getFieldState("Regional Percentage")){
                        selenium.type("selectedLocs[0].percent", "100");
                    } else {
                        logger.info("Field \"Regional Percentage\" is not available.");
                       //selenium.logComment("Field \"Regional Percentage\" is not available.");
                    }
                } else {
                    logger.info("Feature \"Location\" is not available.");
                   //selenium.logComment("Feature \"Location\" is not available.");
                }
                //Sectors
                if (SeleniumFeaturesConfiguration.getFeatureState("Sectors")){
                    //Add Primary Sector
                    if (SeleniumFeaturesConfiguration.getFieldState("Primary Sector")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addSectors(false,1);']")) {
                            selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,1);']");
                            SeleniumTestUtil.waitForElement(selenium,"sector", 90);
                            if (selenium.isElementPresent("sector")) {
                                selenium.select("sector", "index=1");
                                Thread.sleep(5000);
                                primarySector = selenium.getSelectedLabel("sector");
                                if (selenium.isElementPresent("subsectorLevel1")) {
                                    selenium.select("subsectorLevel1", "index=1");
                                    Thread.sleep(5000);
                                    primarySubSector = selenium.getSelectedLabel("subsectorLevel1");
                                } else {
                                    logger.info("Sub Sector no found for Primary Sector");
                                   //selenium.logComment("Sub Sector no found for Primary Sector");
                                }
                                selenium.click("addButton");
                                selenium.waitForPageToLoad("50000");    
                                if (selenium.isElementPresent("activitySectors[0].sectorPercentage")) {
                                    selenium.type("activitySectors[0].sectorPercentage", "100");
                                    logger.info("Primary Sector : " + primarySector);
                                    logger.info("Primary Sub Sector : " + primarySubSector);
                                } else {
                                    logger.info("Add Primary Sector Fail ");
                                   //selenium.logComment("Add Primary Sector Fail ");
                                }
                            } else {
                                logger.info("Sectors no found for Primary Sector");
                               //selenium.logComment("Sectors no found for Primary Sector");
                            }
                        } else {
                            logger.error("Field \"Primary Sector\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Primary Sector\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Primary Sector\" is not available.");
                       //selenium.logComment("Field \"Primary Sector\" is not available.");
                    }
                    //Add Secondary Sector
                    if (SeleniumFeaturesConfiguration.getFieldState("Secondary Sector")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addSectors(false,2);']")) {
                            selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,2);']");
                            SeleniumTestUtil.waitForElement(selenium,"sector", 90);
                            if (selenium.isElementPresent("sector")) {
                                selenium.select("sector", "index=1");
                                Thread.sleep(5000);
                                secondarySector = selenium.getSelectedLabel("sector");
                                if (selenium.isElementPresent("subsectorLevel1")) {
                                    selenium.select("subsectorLevel1", "index=1");
                                    Thread.sleep(5000);
                                    secondarySubSector = selenium.getSelectedLabel("subsectorLevel1");
                                } else {
                                    logger.info("Sub Sector no found for Secondary Sector");
                                   //selenium.logComment("Sub Sector no found for Secondary Sector");
                                }
                                selenium.click("addButton");
                                selenium.waitForPageToLoad("50000");
                                if (selenium.isElementPresent("activitySectors[1].sectorPercentage")) {
                                    selenium.type("activitySectors[1].sectorPercentage", "100");
                                    logger.info("Secondary Sector : " + secondarySector);
                                    logger.info("Secondary Sub Sector : " + secondarySector);
                                } else {
                                    logger.info("Add Secondary Sector Fail ");
                                   //selenium.logComment("Add Secondary Sector Fail ");
                                }
                            } else {
                                logger.info("Sectors no found for Secondary Sector");
                               //selenium.logComment("Sectors no found for Secondary Sector");
                            }
                        } else {
                            logger.error("Field \"Current Completion Date\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Current Completion Date\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Secondary Sector\" is not available.");
                       //selenium.logComment("Field \"Secondary Sector\" is not available.");
                    }
                } else {
                    logger.info("Feature \"Sectors\" is not available.");
                   //selenium.logComment("Feature \"Sectors\" is not available.");
                }
                //Programs
                if (SeleniumFeaturesConfiguration.getFeatureState("Program")){
                    //Add National Planning Objectives
                    if (SeleniumFeaturesConfiguration.getFieldState("Add Programs Button - National Plan Objective")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addProgram(1);']")) {
                            selenium.click("//input[@name='submitButton' and @onclick='addProgram(1);']");
                            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                            Thread.sleep(10000);
                            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                            SeleniumTestUtil.waitForElement(selenium,"programs.selPrograms", 90);
                            if (selenium.isElementPresent("programs.selPrograms")) {
                                selenium.select("programs.selPrograms", "index=1");
                                Thread.sleep(5000);
                                NPOProgram = selenium.getSelectedLabel("programs.selPrograms");                         
                                if (selenium.isElementPresent("//td[@id='slo2']/select")) {
                                    selenium.select("//td[@id='slo2']/select", "index=1");
                                    Thread.sleep(5000);
                                    NPOSubProgram = selenium.getSelectedLabel("//td[@id='slo2']/select");                       
                                } else {
                                    logger.info("Sub Programs no found for NPO");
                                   //selenium.logComment("Sub Programs no found for NPO");
                                }
                                selenium.click("submitButton");
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("50000");
                            } else {
                                logger.info("Programs no found for NPO");
                               //selenium.logComment("Programs no found for NPO");
                                if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addDefaultProgram()']")) {
                                    selenium.click("//input[@name='submitButton' and @onclick='addDefaultProgram()']");
                                    selenium.waitForPageToLoad("50000");
                                } 
                            }
                            if (selenium.isElementPresent("nationalPlanObjectivePrograms[0].programPercentage")) {
                                selenium.type("nationalPlanObjectivePrograms[0].programPercentage", "100");
                                logger.info("NPO Program : " + NPOProgram);
                                logger.info("NPO Sub Program : " + NPOSubProgram);
                            } else {
                                logger.info("National Planning Objectives weren't added.");
                               //selenium.logComment("National Planning Objectives weren't added.");
                            }
                        } else {
                            logger.error("Field \"Add Programs Button - National Plan Objective\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Add Programs Button - National Plan Objective\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Add Programs Button - National Plan Objective\" is not available.");
                       //selenium.logComment("Field \"Add Programs Button - National Plan Objective\" is not available.");
                    }
                    
                    //Add Primary Program
                    if (SeleniumFeaturesConfiguration.getFieldState("Add Programs Button - Primary Programs")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addProgram(2);']")) {
                            selenium.click("//input[@name='submitButton' and @onclick='addProgram(2);']");
                            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                            Thread.sleep(10000);
                            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                            SeleniumTestUtil.waitForElement(selenium,"programs.selPrograms", 90);
                            if (selenium.isElementPresent("programs.selPrograms")) {
                                selenium.select("programs.selPrograms", "index=1");
                                Thread.sleep(5000);
                                NPOProgram = selenium.getSelectedLabel("programs.selPrograms");
                                if (selenium.isElementPresent("//td[@id='slo2']/select")) {
                                    selenium.select("//td[@id='slo2']/select", "index=1");
                                    Thread.sleep(5000);
                                    primarySubProgram = selenium.getSelectedLabel("//td[@id='slo2']/select");                       
                                } else {
                                    logger.info("Sub Programs no found for Primary Program");
                                   //selenium.logComment("Sub Programs no found for Primary Program");
                                }
                                selenium.click("submitButton");
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("50000");
                            } else {
                                logger.info("Programs no found for Primary Program");
                               //selenium.logComment("Programs no found for Primary Program");
                                if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addDefaultProgram()']")) {
                                    selenium.click("//input[@name='submitButton' and @onclick='addDefaultProgram()']");
                                    selenium.waitForPageToLoad("50000");
                                } 
                            }
                            if (selenium.isElementPresent("primaryPrograms[0].programPercentage")) {
                                selenium.type("primaryPrograms[0].programPercentage", "100");
                                logger.info("Primary Program : " + primaryProgram);
                                logger.info("Primary Sub Program : " + primarySubProgram);
                            } else {
                                logger.info("Primary Program weren't added." );
                               //selenium.logComment("Primary Program weren't added.");
                            }
                        } else {
                            logger.error("Field \"Add Programs Button - Primary Programs\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Add Programs Button - Primary Programs\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Add Programs Button - Primary Programs\" is not available.");
                       //selenium.logComment("Field \"Add Programs Button - Primary Programs\" is not available.");
                    }
                    
                    //Add Secondary Program
                    if (SeleniumFeaturesConfiguration.getFieldState("Add Programs Button - Secondary Programs")){
                        if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addProgram(3);']")) {
                            selenium.click("//input[@name='submitButton' and @onclick='addProgram(3);']");
                            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                            Thread.sleep(10000);
                            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                            SeleniumTestUtil.waitForElement(selenium,"programs.selPrograms", 90);
                            if (selenium.isElementPresent("programs.selPrograms")) {
                                selenium.select("programs.selPrograms", "index=1");
                                Thread.sleep(5000);
                                NPOProgram = selenium.getSelectedLabel("programs.selPrograms");
                                if (selenium.isElementPresent("//td[@id='slo2']/select")) {
                                    selenium.select("//td[@id='slo2']/select", "index=1");
                                    Thread.sleep(5000);
                                    secondarySubProgram = selenium.getSelectedLabel("//td[@id='slo2']/select");                     
                                } else {
                                    logger.info("Sub Programs no found for Secondary Program");
                                   //selenium.logComment("Sub Programs no found for Secondary Program");
                                }
                                selenium.click("submitButton");
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("50000");
                            } else {
                                logger.info("Programs no found for Secondary Program");
                               //selenium.logComment("Programs no found for Secondary Program");
                                if (selenium.isElementPresent("//input[@name='submitButton' and @onclick='addDefaultProgram()']")) {
                                    selenium.click("//input[@name='submitButton' and @onclick='addDefaultProgram()']");
                                    selenium.waitForPageToLoad("50000");
                                } 
                            }
                            if (selenium.isElementPresent("secondaryPrograms[0].programPercentage")) {
                                selenium.type("secondaryPrograms[0].programPercentage", "100");
                                logger.info("Secondary Program : " + secondaryProgram);
                                logger.info("Secondary Sub Program : " + secondarySubProgram);
                            } else {
                                logger.info("Secondary Program weren't added.");
                               //selenium.logComment("Secondary Program weren't added.");
                            }
                        } else {
                            logger.error("Field \"Add Programs Button - Secondary Programs\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"Add Programs Button - Secondary Programs\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"Add Programs Button - Secondary Programs\" is not available.");
                       //selenium.logComment("Field \"Add Programs Button - Secondary Programs\" is not available.");
                    }
                    
                    if (SeleniumFeaturesConfiguration.getFieldState("NPD Program Description")){
                        if (selenium.isElementPresent("programs.programDescription")) {
                            selenium.type("programs.programDescription", "ProgramDescription");
                        } else {
                            logger.error("Field \"NPD Program Description\" is active in Feature Manager but is not available.");
                            //selenium.logAssertion"assertTrue", "Field \"NPD Program Description\" is active in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Field \"NPD Program Description\" is not available.");
                       //selenium.logComment("Field \"NPD Program Description\" is not available.");
                    }
                } else {
                    logger.info("Feature \"Program\" is not available.");
                   //selenium.logComment("Feature \"Program\" is not available.");
                }               
            } else {
                logger.info("Step 2 is not available on Activity Form");
               //selenium.logComment("Step 2 is not available on Activity Form");
            }
            
            //FUNDING
            boolean fundingAvailable = false;
            boolean allFundingOk = true;
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(3)']")) {
                selenium.click("//a[@href='javascript:gotoStep(3)']");
                selenium.waitForPageToLoad("50000");
                fundingAvailable = true;
            } else {
                logger.info("Step \"Funding\" is not available.");
               //selenium.logComment("Step \"Funding\" is not available.");
                allFundingOk = false;
            }
            if (SeleniumFeaturesConfiguration.getFeatureState("Funding Information")){
                /*if (SeleniumFeaturesConfiguration.getFieldState("Add Funding Button - Proposed Project Cost")){
                    if (selenium.isElementPresent("//input[@ onclick='addPropFunding()']")) {
                        selenium.click("//input[@ onclick='addPropFunding()']");
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]);            
                        SeleniumTestUtil.waitForElement(selenium,"funAmount", 90);
                        selenium.type("funAmount", "3000");
                        selenium.type("funDate", "01/02/2008");
                        selenium.click("//input[@onclick='addNewPropFunding();']");
                        selenium.selectWindow("null");
                        selenium.waitForPageToLoad("50000");
                    } else {
                        logger.error("Field \"Add Funding Button - Proposed Project Cost\" is active in Feature Manager but is not available.");
                    }
                } else {
                    logger.info("Field \"Add Funding Button - Proposed Project Cost\" is not available.");
                }*/
                if (SeleniumFeaturesConfiguration.getFieldState("Add Donor Organization")){
                    if (selenium.isElementPresent("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]")) {
                        selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                        //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                        Thread.sleep(10000);
                        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
                       
                        SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                        selenium.click("//input[@onclick='return searchOrganization()']");
                        SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                        selenium.click("selOrganisations"); 
                        selenium.click("//input[@onclick='return selectOrganization()']");
                        selenium.selectWindow("null");
                        selenium.waitForPageToLoad("50000");                    
                        String val = selenium.getAttribute("selFundingOrgs@value");
                        //Add Fundings
                        for (int i = 0; i < fundingQty; i++) {
                            selenium.click("//input[@onclick=\"addFunding('" + val + "')\"]"); 
                            //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                            Thread.sleep(10000);
                            selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
                            Thread.sleep(4000);
                            
                            //if (SeleniumFeaturesConfiguration.getFieldState("Type Of Assistance")){
                                selenium.select("funding.assistanceType", "index=1");
                            //} else {
                            //  logger.info("Field \"Type Of Assistance\" is not available.");
                            //}
                            if (SeleniumFeaturesConfiguration.getFieldState("Funding Organization Id")){
                                selenium.type("orgFundingId", "12345");
                            } else {
                                logger.info("Field \"Funding Organization Id\" is not available.");
                               //selenium.logComment("Field \"Funding Organization Id\" is not available.");
                            }
                            selenium.select("funding.modality", "index=1");
                            
                            if (SeleniumFeaturesConfiguration.getFieldState("Add Commitment Button")){
                                selenium.click("//input[@onclick='addFundingDetail(0)']"); //add a commitment (actual)
                                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[0].adjustmentType", 90);
                                if (selenium.isElementPresent("fundingDetail[0].adjustmentType")) {
                                    selenium.select("fundingDetail[0].adjustmentType", "value=1");
                                    selenium.type("fundingDetail[0].transactionAmount", ACTUAL_COMMITMENTS[i]);
                                    selenium.type("fundingDetail[0].transactionDate", FUNDING_DATES[i]);
                                } else {
                                    logger.info("Option \"Actual\" is not available for commitments.");
                                   //selenium.logComment("Option \"Actual\" is not available for commitments.");
                                    allFundingOk = false;
                                }
                                selenium.click("//input[@onclick='addFundingDetail(0)']"); //add a commitment (planned)
                                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[1].adjustmentType", 90);
                                if (selenium.isElementPresent("fundingDetail[1].adjustmentType")) {
                                    selenium.select("fundingDetail[1].adjustmentType", "value=0");
                                    selenium.type("fundingDetail[1].transactionAmount", PLANNED_COMMITMENTS[i]);
                                    selenium.type("fundingDetail[1].transactionDate", FUNDING_DATES[i]);
                                } else {
                                    logger.info("Option \"Planned\" is not available for commitments.");
                                   //selenium.logComment("Option \"Planned\" is not available for commitments.");
                                    allFundingOk = false;
                                }
                            } else {
                                logger.info("Field \"Add Commitment Button\" is not available.");
                               //selenium.logComment("Field \"Add Commitment Button\" is not available.");
                                allFundingOk = false;
                            }
                            
                            if (SeleniumFeaturesConfiguration.getFieldState("Add Disbursement Button")){
                                selenium.click("//input[@onclick='addFundingDetail(1)']"); //add a disbursement (actual)
                                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[2].adjustmentType", 90);
                                if (selenium.isElementPresent("fundingDetail[2].adjustmentType")) {
                                    selenium.select("fundingDetail[2].adjustmentType", "value=1");
                                    selenium.type("fundingDetail[2].transactionAmount", ACTUAL_DISBURSEMENT[i]);
                                    selenium.type("fundingDetail[2].transactionDate", FUNDING_DATES[i]);
                                } else {
                                    logger.info("Option \"Actual\" is not available for disbursement.");
                                   //selenium.logComment("Option \"Actual\" is not available for disbursement.");
                                    allFundingOk = false;
                                }
                                selenium.click("//input[@onclick='addFundingDetail(1)']"); //add a disbursement (planned)
                                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[3].adjustmentType", 90);
                                if (selenium.isElementPresent("fundingDetail[3].adjustmentType")) {
                                    selenium.select("fundingDetail[3].adjustmentType", "value=0");
                                    selenium.type("fundingDetail[3].transactionAmount", PLANNED_DISBURSEMENT[i]);
                                    selenium.type("fundingDetail[3].transactionDate", FUNDING_DATES[i]);
                                } else {
                                    logger.info("Option \"Planned\" is not available for disbursement.");
                                   //selenium.logComment("Option \"Planned\" is not available for disbursement.");
                                    allFundingOk = false;
                                }
                            } else {
                                logger.info("Field \"Add Disbursement Button\" is not available.");
                               //selenium.logComment("Field \"Add Disbursement Button\" is not available.");
                                allFundingOk = false;
                            }
                            selenium.click("//input[@onclick=\"return addFunding()\"]");
                            
                            selenium.selectWindow("null");
                            selenium.waitForPageToLoad("50000");
                        }
                        
                    } else {
                        logger.error("Field \"Add Donor Organization\" is active in Feature Manager but is not available.");
                        //selenium.logAssertion"assertTrue", "Field \"Add Donor Organization\" is active in Feature Manager but is not available.", "condition=false");
                    }
                } else {
                    logger.info("Field \"Add Donor Organization\" is not available.");
                   //selenium.logComment("Field \"Add Donor Organization\" is not available.");
                    allFundingOk = false;                   
                }
                
            } else {
                logger.info("Feature \"Funding Information\" is not available.");
               //selenium.logComment("Feature \"Funding Information\" is not available.");
            }
            selenium.click("//input[@onclick='saveClicked()']");
            selenium.waitForPageToLoad("50000");
            if (selenium.isElementPresent("//input[@onclick='saveClicked()']")) {
                logger.error("Save Activity Fail");
                //selenium.logAssertion"assertTrue", "Save Activity Fail", "condition=false");
            }           
        }
        
        //ADD TAB
        boolean addTabAvailable = false;
        if (selenium.isElementPresent("//a[contains(@href, "
                + "\"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=T\")]")) {
            selenium.click("//a[contains(@href, \"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=T\")]");
            selenium.waitForPageToLoad("50000");
            addTabAvailable = true;
        } else {
            logger.error("Option \"Add Tab\" is not available.");
            //selenium.logAssertion"assertTrue", "Option \"Add Tab\" is not available.", "condition=false");
        }
        
        if (addTabAvailable) {          
            selenium.click("//li[@id='columns_tab_label']/a/div");
            if (selenium.isElementPresent("fieldVis:4")) {
                selenium.click("fieldVis:4");
            } else {
                selenium.click("fieldVis:12");
                logger.error("Option \"Project Title\" is not available.");
                //selenium.logAssertion"assertTrue", "", "condition=false");
            }
            selenium.click("//button[@type='button' and @onclick=\"ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')\"]");
            selenium.click("//li[@id='measures_tab_label']/a/div");
            if (selenium.isElementPresent("//li[@id='measure_1']/input")) {
                selenium.click("//li[@id='measure_1']/input");
            } else {
                logger.info("Measure \"Actual Commitments\" is not available.");
               //selenium.logComment("Measure \"Actual Commitments\" is not available.");
            }
            if (selenium.isElementPresent("//li[@id='measure_2']/input")) {
                selenium.click("//li[@id='measure_2']/input");
            } else {
                logger.info("Measure \"Actual Disbursement\" is not available.");
               //selenium.logComment("Measure \"Actual Disbursement\" is not available.");
            }
            selenium.click("//button[@type='button' and @onclick=\"MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')\"]");
            selenium.click("step3_add_filters_button");
            SeleniumTestUtil.waitForElement(selenium, "indexString", 90);
            selenium.type("indexString", testTime);
            selenium.click("filterPickerSubmitButton");
            Thread.sleep(30000);
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.typeKeys("reportTitle", "Test Tab " + testTime);
            selenium.click("last_save_button");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@onclick='initializeTabManager()']");
            SeleniumTestUtil.waitForElement(selenium, "tabsId", 90);
            selenium.select("tabsId", "label=Test Tab " + testTime);
            selenium.click("tabManagerButton");
            SeleniumTestUtil.waitForElement(selenium, "//a[contains(@href, \"showDesktop.do\")]", 90);
            selenium.click("//a[contains(@href, \"showDesktop.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@id='Tab-Test Tab " + testTime + "']/div");
            Thread.sleep(30000);
            selenium.click("//a[@id='Tab-Test Tab " + testTime + "']/div");
            Thread.sleep(30000);
            
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[2]").equals(ActivityFormTest.TOTAL_ACTUAL_COMMITMENTS)){
                logger.error("Error on TOTAL_ACTUAL_COMMITMENTS shown on tab.");
                //selenium.logAssertion"assertTrue", "Error on TOTAL_ACTUAL_COMMITMENTS shown on tab.", "condition=false");
            } 
            if (!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[3]").equals(ActivityFormTest.TOTAL_ACTUAL_DISBURSEMENT)){
                logger.error("Error on TOTAL_ACTUAL_DISBURSEMENT shown on tab.");
                //selenium.logAssertion"assertTrue", "Error on TOTAL_ACTUAL_DISBURSEMENT shown on tab.", "condition=false");
            } 
            
            selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
            selenium.waitForPageToLoad("30000");
            //Do some validations
            
            if (!selenium.isTextPresent(primarySector)){
                logger.error("Error on Primary Sector shown.");
                //selenium.logAssertion"assertTrue", "Error on Primary Sector shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(primarySubSector)){
                logger.error("Error on Primary Sub Sector shown.");
                //selenium.logAssertion"assertTrue", "Error on Primary Sub Sector shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(secondarySector)){
                logger.error("Error on Secondary Sector shown.");
                //selenium.logAssertion"assertTrue", "Error on Secondary Sector shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(secondarySubSector)){
                logger.error("Error on Secondary Sub Sector shown.");
                //selenium.logAssertion"assertTrue", "Error on Secondary Sub Sector shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(NPOProgram)){
                logger.error("Error on NPO Program shown.");
                //selenium.logAssertion"assertTrue", "Error on NPO Program shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(NPOSubProgram)){
                logger.error("Error on NPO Sub Program shown.");
                //selenium.logAssertion"assertTrue", "Error on NPO Sub Program shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(primaryProgram)){
                logger.error("Error on Primary Program shown.");
                //selenium.logAssertion"assertTrue", "Error on Primary Program shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(primarySubProgram)){
                logger.error("Error on Primary Sub Program shown.");
                //selenium.logAssertion"assertTrue", "Error on Primary Sub Program shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(secondaryProgram)){
                logger.error("Error on Secondary Program shown.");
                //selenium.logAssertion"assertTrue", "Error on Secondary Program shown.", "condition=false");
            } 
            if (!selenium.isTextPresent(secondarySubProgram)){
                logger.error("Error on Secondary Sub Program shown.");
                //selenium.logAssertion"assertTrue", "Error on Secondary Sub Program shown.", "condition=false");
            } 
            
            /*selenium.click("//div[@id='tabs']/ul/li[2]/span/a/div");
            selenium.waitForPageToLoad("30000");
            
            for (int i = 0; i < fundingQty; i++) {
                int trCnt = 2 + i;
                assertTrue(selenium.getText("//table[@id='dataTable']/tbody/tr[" + trCnt + "]/td[3]").equals(ActivityFormTest.ACTUAL_COMMITMENTS[i]));
                assertTrue(selenium.getText("//table[@id='dataTable']/tbody/tr[" + trCnt + "]/td[4]").equals(ActivityFormTest.ACTUAL_DISBURSEMENT[i]));
                String undisbursedFunds = String.valueOf(Integer.parseInt(ActivityFormTest.ACTUAL_COMMITMENTS[i]) - Integer.parseInt(ActivityFormTest.ACTUAL_DISBURSEMENT[i]));
                assertTrue(selenium.getText("//table[@id='dataTable']/tbody/tr[" + trCnt + "]/td[5]").equals(undisbursedFunds));
            }
            selenium.click("//div[@id='tabs']/ul/li[1]/span/a/div");
            selenium.waitForPageToLoad("30000");
            */
            //selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
            if (SeleniumFeaturesConfiguration.getFeatureState("Edit Activity")){
                selenium.click("//img[@src=\"/repository/aim/images/tangopack_edit.png\"]");
                selenium.waitForPageToLoad("30000");
                selenium.type("identification.title", activityName + " modified");
                selenium.click("//input[@onclick='saveClicked()']");
                selenium.waitForPageToLoad("50000");
                selenium.click("//a[@id='Tab-Test Tab " + testTime + "']/div");
                Thread.sleep(30000);
                selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
                selenium.waitForPageToLoad("30000");
                //assertTrue(selenium.isTextPresent(activityName + " modified"));
                if (selenium.isTextPresent(activityName + " modified")) {
                    logger.info("Modification done Successfully");
                   //selenium.logComment("Modification done Successfully");
                } else {
                    logger.info("Modification error /" + activityName + " modified/");
                   //selenium.logComment("Modification error /" + activityName + " modified/");
                }
            } else {
                logger.info("Feature \"Edit Activity\" is not available.");
               //selenium.logComment("Feature \"Edit Activity\" is not available.");
            }
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Activity Form Test Finished Successfully");
       //selenium.logComment("Activity Form Test Finished Successfully");
    }
    
    /**
     * 
     * @return the name of the first activity for selenium test found
     */
    public static String getFirstActivityName (Selenium selenium){
        String ret = null;
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/j_spring_logout\")]")) {
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("50000");
        }
        
        selenium.open("/");
        String version = selenium.getText("//div[@class=\"footerText\"]");
        version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("50000");
        selenium.click("//a[contains(@href, \"/aim/activityManager.do\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("keyword", "Activity for selenium test");
        selenium.click("//input[@onclick=\"return searchActivity()\"]");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/activityManager.do~action=sort~sortByColumn=activityName\")]")){
            if (version.equals(SeleniumTestUtil.VERSION_BRANCH)) {
                ret = selenium.getText("//table[@cellpadding='3' and @bgcolor='#d7eafd']/tbody/tr[2]/td[2]");
            } else {
                ret = selenium.getText("//table[@id='dataTable']/tbody/tr[1]/td[2]");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            return ret;
        }else{
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            return null;
        }
    }
    
}

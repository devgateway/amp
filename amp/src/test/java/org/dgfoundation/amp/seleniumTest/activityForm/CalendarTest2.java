package org.dgfoundation.amp.seleniumTest.activityForm;
import java.util.Calendar;
import java.util.Date;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;


public class CalendarTest2 extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(CalendarTest2.class);
    
    public void setUp() throws Exception {
        setUp("http://generic.ampdev.net/", "*firefox");
    }
    public static void testCalendar(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String eventTitle = "Test Event" + testTime;
        
        selenium.open("/");
        selenium.type("j_username","uattl@amp.org");
        selenium.type("j_password","abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
 
        boolean eventSent = false;
        
        if (SeleniumFeaturesConfiguration.getModuleState("Calendar")){
            if (SeleniumFeaturesConfiguration.getFeatureState("Create Event")){
                if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new\")]")){
                    selenium.click("//a[contains(@href, \"/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new\")]");
                    selenium.waitForPageToLoad("30000");
                    selenium.type("eventTitle", eventTitle);
                    if (SeleniumFeaturesConfiguration.getFeatureState("Event Type")){
                        if (selenium.isElementPresent("selectedEventTypeId")) {
                            selenium.select("selectedEventTypeId", "index=1");
                        } else {
                            logger.error("Feature \"Event Type\" is enabled in Feature Manager but is not available.");
//                            //selenium.logAssertion"assertTrue", "Feature \"Event Type\" is enabled in Feature Manager but is not available.", "condition=false");
                        }
                    } else {
                        logger.info("Feature \"Event Type\" is not available.");
//                       //selenium.logComment("Feature \"Event Type\" is not available.");
                    }
                    Date date = new Date(System.currentTimeMillis());
                    selenium.type("selectedStartDate", date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900));
                    selenium.type("selectedEndDate",  date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900));
                    selenium.select("selectedStartHour", "index=11");
                    selenium.select("selectedEndHour", "index=12");
                    if (SeleniumFeaturesConfiguration.getFeatureState("Donors")){
                        if (SeleniumFeaturesConfiguration.getFieldState("Add Donor Button")){
                            if (selenium.isElementPresent("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitForm();~PARAM_COLLECTION_NAME=organizations~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]")) {
                                selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitForm();~PARAM_COLLECTION_NAME=organizations~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                                Thread.sleep(10000);
                                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                                selenium.click("//input[@onclick='return searchOrganization()']");
                                Thread.sleep(10000);
                                selenium.click("selOrganisations"); 
                                selenium.click("//input[@onclick='return selectOrganization()']");
                                //Thread.sleep(10000);
                                selenium.selectWindow("null");
                                selenium.waitForPageToLoad("30000");
                            } else {
                                logger.error("Field \"Add Donor Button\" is enabled in Feature Manager but is not available.");
//                                //selenium.logAssertion"assertTrue", "Field \"Add Donor Button\" is enabled in Feature Manager but is not available.", "condition=false");
                            }
                        } else {
                            logger.info("Field \"Add Donor Button\" is not available.");
//                           //selenium.logComment("Field \"Add Donor Button\" is not available.");
                        }
                    } else {
                        logger.info("Feature \"Donors\" is not available.");
//                       //selenium.logComment("Feature \"Donors\" is not available.");
                    }
                    //selenium.uncheck("privateEventCheckbox");
                    if (selenium.isChecked("privateEventCheckbox")) {
                        selenium.click("privateEventCheckbox");
                    }
                    selenium.addSelection("whoIsReceiver", "label=---UAT Team Workspace---");
                    selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
                    if (SeleniumFeaturesConfiguration.getFeatureState("Preview Event button")){
                        if (selenium.isElementPresent("//input[@onclick=\"return previewEvent();\"]")){
                            selenium.click("//input[@onclick=\"return previewEvent();\"]");
                            selenium.waitForPageToLoad("30000");
                            boolean attOk = false;
                            for (int i = 0; i < selenium.getSelectOptions("selreceivers").length; i++) {
                                if (selenium.getSelectOptions("selreceivers")[i].equalsIgnoreCase("---UAT Team Workspace---")) {
                                    attOk = true;
                                } else if (selenium.getSelectOptions("selreceivers")[i].equalsIgnoreCase("UATtl UATtl")){
                                    attOk = true;
                                } else if (selenium.getSelectOptions("selreceivers")[i].equalsIgnoreCase("UATtmc UATtmc")){
                                    attOk = true;
                                } else if (selenium.getSelectOptions("selreceivers")[i].equalsIgnoreCase("UATtm UATtm")){
                                    attOk = true;
                                } else {
                                    attOk = false;
                                }
                            }
                            if (!attOk) {
                                logger.error("Error on Attenders list on event preview");
//                                //selenium.logAssertion"assertTrue", "Error on Attenders list on event preview", "condition=false");
                            }                           
                            selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
                            selenium.waitForPageToLoad("30000");
                            if (selenium.isChecked("privateEventCheckbox")) {
                                selenium.click("privateEventCheckbox");
                            }
                            selenium.click("//input[@onclick=\"return sendEvent();\"]");
                            selenium.waitForPageToLoad("30000");
                            eventSent = true;
                        } else {
                            logger.error("Feature \"Preview Event button\" is enabled in Feature Manager but is not available.");
//                            //selenium.logAssertion"assertTrue", "Feature \"Preview Event button\" is enabled in Feature Manager but is not available.", "condition=false");
                            if (SeleniumFeaturesConfiguration.getFeatureState("Save and Send button")){
                                if (selenium.isElementPresent("//input[@onclick=\"return sendEvent();\"]")){
                                    if (selenium.isChecked("privateEventCheckbox")) {
                                        selenium.click("privateEventCheckbox");
                                    }
                                    selenium.click("//input[@onclick=\"return sendEvent();\"]");
                                    selenium.waitForPageToLoad("30000");
                                    eventSent = true;
                                } else {
                                    logger.error("Feature \"Save and Send button\" is enabled in Feature Manager but is not available.");
//                                    //selenium.logAssertion"assertTrue", "Feature \"Save and Send button\" is enabled in Feature Manager but is not available.", "condition=false");
                                }
                            } else {
                                logger.info("Feature \"Save and Send button\" is not available.");
//                               //selenium.logComment("Feature \"Save and Send button\" is not available.");
                            }
                        }
                    } else {
                        logger.info("Feature \"Preview Event button\" is not available.");
//                       //selenium.logComment("Feature \"Preview Event button\" is not available.");
                        if (SeleniumFeaturesConfiguration.getFeatureState("Save and Send button")){
                            if (selenium.isElementPresent("//input[@onclick=\"return sendEvent();\"]")){
                                if (selenium.isChecked("privateEventCheckbox")) {
                                    selenium.click("privateEventCheckbox");
                                }
                                selenium.click("//input[@onclick=\"return sendEvent();\"]");
                                selenium.waitForPageToLoad("30000");
                                eventSent = true;
                            } else {
                                logger.error("Feature \"Save and Send button\" is enabled in Feature Manager but is not available.");
//                                //selenium.logAssertion"assertTrue", "Feature \"Save and Send button\" is enabled in Feature Manager but is not available.", "condition=false");
                            }
                        } else {
                            logger.info("Feature \"Save and Send button\" is not available.");
//                           //selenium.logComment("Feature \"Save and Send button\" is not available.");
                        }
                    }
                    
                    if (eventSent) {
                        int eventId = 0;
                        for (int i = 1000; i > 0; i--) {
                            if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+i+"~method=preview~resetForm=true\")]")) {
                                eventId = i;
                                selenium.click("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+i+"~method=preview~resetForm=true\")]");
                                selenium.waitForPageToLoad("30000");
                                break;
                            }
                        }
                        selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
                        selenium.waitForPageToLoad("30000");
                        selenium.removeAllSelections("selreceivers");
                        selenium.addSelection("selreceivers", "label=UATtmc UATtmc");
                        selenium.click("//input[@onclick=\"MyremoveUserOrTeam()\"]");
                        selenium.click("//input[@onclick=\"return sendEvent();\"]");
                        selenium.waitForPageToLoad("30000");
                        eventId++;
                        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                        selenium.waitForPageToLoad("30000");
                        
                        selenium.type("j_username", "uattmc@amp.org");
                        selenium.type("j_password", "abc");
                        selenium.click("submitButton");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("link=UAT Team Workspace");
                        selenium.waitForPageToLoad("30000");
                        if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]")) {
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]");
                        }
                        selenium.waitForPageToLoad("30000");
                        if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+eventId+"~method=preview~resetForm=true\")]")) {
                            logger.error("Non Public Event is visible for an user who is not in the attender list.");
//                            //selenium.logAssertion"assertTrue", "Non Public Event is visible for an user who is not in the attender list.", "condition=false");
                        }
                        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                        selenium.waitForPageToLoad("30000");
                                                
                        selenium.type("j_username", "uattm@amp.org");
                        selenium.type("j_password", "abc");
                        selenium.click("submitButton");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("link=UAT Team Workspace");
                        selenium.waitForPageToLoad("30000");
                        if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]")) {
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]");
                        }
                        selenium.waitForPageToLoad("30000");
                        selenium.click("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+eventId+"~method=preview~resetForm=true\")]");
                        selenium.waitForPageToLoad("30000");
                        /*selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
                        selenium.waitForPageToLoad("30000");
                        selenium.check("privateEventCheckbox");
                        selenium.click("//input[@onclick=\"return sendEvent();\"]");
                        selenium.waitForPageToLoad("30000");
                        eventId++;*/
                        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                        selenium.waitForPageToLoad("30000");
                        
                        selenium.type("j_username", "uattl@amp.org");
                        selenium.type("j_password", "abc");
                        selenium.click("submitButton");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("link=UAT Team Workspace");
                        selenium.waitForPageToLoad("30000");
                        if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]")) {
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]");
                        }
                        selenium.waitForPageToLoad("30000");
                        selenium.click("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+eventId+"~method=preview~resetForm=true\")]");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
                        selenium.waitForPageToLoad("30000");
                        selenium.check("privateEventCheckbox");
                        selenium.click("//input[@onclick=\"return sendEvent();\"]");
                        selenium.waitForPageToLoad("30000");
                        eventId++;
                        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                        selenium.waitForPageToLoad("30000");
                                                
                        selenium.type("j_username", "uattm@amp.org");
                        selenium.type("j_password", "abc");
                        selenium.click("submitButton");
                        selenium.waitForPageToLoad("30000");
                        selenium.click("link=UAT Team Workspace");
                        selenium.waitForPageToLoad("30000");
                        if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]")) {
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=monthly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=weekly&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=daily&filterInUse=false\")]");
                        } else if (selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]")){
                            selenium.click("//a[contains(@href, \"/calendar/showCalendarView.do?view=none&filterInUse=false\")]");
                        }
                        selenium.waitForPageToLoad("30000");
                        if (!selenium.isElementPresent("//a[contains(@href, \"/calendar/showCalendarEvent.do~ampCalendarId="+eventId+"~method=preview~resetForm=true\")]")) {
                            logger.error("Public Event is not visible for an user who is not in the attender list.");
//                            //selenium.logAssertion"assertTrue", "Public Event is not visible for an user who is not in the attender list.", "condition=false");
                        }
                    }
                } else {
                    logger.error("Feature \"Create Event\" is enabled in Feature Manager but is not available.");
//                    //selenium.logAssertion"assertTrue", "Feature \"Create Event\" is enabled in Feature Manager but is not available.", "condition=false");
                }
            } else {
                logger.info("Feature \"Create Event\" is not available.");
//               //selenium.logComment("Feature \"Create Event\" is not available.");
            }
        } else {
            logger.info("Module \"Calendar\" is not available.");
//           //selenium.logComment("Module \"Calendar\" is not available.");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Calendar Test Finished Successfully");
//       //selenium.logComment("Calendar Test Finished Successfully");
    }   
}

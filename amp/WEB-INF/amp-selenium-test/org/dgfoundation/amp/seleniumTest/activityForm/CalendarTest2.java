package org.dgfoundation.amp.seleniumTest.activityForm;


import com.thoughtworks.selenium.SeleneseTestCase;


public class CalendarTest2 extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("http://generic.ampdev.net/", "*firefox");
	}
	public void testCalendar() throws Exception {
		String testTime =  String.valueOf(System.currentTimeMillis());
		String activityName ="Testing Calendar " + testTime;
		String user = "atl@amp.org";
		String password = "atl";
		String workspace = "M and E Testing Team";
		String eventTitle = "Test Event";
		System.out.println(activityName);		
				
		selenium.open("/");
		selenium.type("j_username",user);
		selenium.type("j_password",password);
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[@onclick=\"this.blur(); return sortTable('offTblBdy', 1, false);\"]")) {
			try {
				selenium.click("link=" + workspace);
				selenium.waitForPageToLoad("30000");
			} catch (Exception e) {
				//logger.error("Workspace not found.");
				System.out.println("Workspace not found.");
			}
		} 
		boolean addAvailable = false;
		try {
			selenium.click("//a[contains(@href, \"/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new\")]");
			selenium.waitForPageToLoad("30000");
			addAvailable = true;
		} catch (Exception e) {
			//logger.error("Option \"Add Event\" is not available.");
			System.out.println("Option \"Add Event\" is not available.");
		}
		if (addAvailable) {
		
		selenium.type("eventTitle", eventTitle);
		try {
			selenium.select("selectedCalendarTypeId", "index=0");
		} catch (Exception e) {
			//logger.info("Field \"Calendar Type\" is not available.");
			System.out.println("Field \"Calendar Type\" is not available.");
		}
		
		try {
			selenium.select("selectedEventTypeId", "index=3");
		} catch (Exception e) {
			//logger.info("Field \"Event Type\" is not available.");
			System.out.println("Field \"Event Type\" is not available.");
		}
		
		try {
			selenium.type("selectedStartDate", "08/06/2009");
		} catch (Exception e) {
			//logger.info("Cannot select Start date.");
			System.out.println("Cannot select Start date.");
		}
		try {
			selenium.type("selectedEndDate", "08/17/2009");
		} catch (Exception e) {
			//logger.info("Cannot select End date.");
			System.out.println("Cannot select End date.");
		}	
		try {
			selenium.select("selectedStartHour", "index=11");
		} catch (Exception e) {
			//logger.info("Cannot find \"Selected Start Hour\" drop down menu.");
			System.out.println("Cannot find \"Selected Start Hour\" drop down menu.");
		}
	
		try {
			selenium.select("selectedEndHour", "index=12");
		} catch (Exception e) {
			//logger.info("Cannot find \"Selected end Hour\" drop down menu.");
			System.out.println("Cannot find \"Selected end Hour\" drop down menu.");
		}
		try {
			selenium.addSelection("whoIsReceiver", "index=2");
		} catch (Exception e) {
			//logger.info("Cannot select the Attendee.");
			System.out.println("Cannot select the Attendee.");
		}
				
		try {
			selenium.click("//input[@value='Add >>']");
		} catch (Exception e) {
			//logger.info("Cannot find \"Add >>\"button.");
			System.out.println("Cannot find \"Add >>\"button.");
		}		
		try {
			selenium.addSelection("whoIsReceiver", "index=3");
		} catch (Exception e) {
			//logger.info("Field \"Cannot select the Attendee.");
			System.out.println("Cannot select the Attendee.");
		}
				
		try {
			selenium.click("//input[@value='Add >>']");
		} catch (Exception e) {
			//logger.info("Cannot find \"Add >>\"button.");
			System.out.println("Cannot find \"Add >>\"button.");
		}		
		
		try {
			selenium.addSelection("selreceivers", "index=2");
		} catch (Exception e) {
			//logger.info("Cannot find selected Attendee.");
			System.out.println("Cannot find selected Attendee.");
		}
				
		try {
			selenium.click("//input[@value='<<Remove']");
		} catch (Exception e) {
			//logger.info("Button \"<<Remove\" is not available.");
			System.out.println("Button \"<<Remove\" is not available.");
		}		
		try {
		selenium.click("//input[@onclick=\"javascript:selectOrg('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitForm();~PARAM_COLLECTION_NAME=organizations~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
		Thread.sleep(10000);
		selenium.click("//input[@onclick='return searchOrganization()']");
		Thread.sleep(10000);
		selenium.click("selOrganisations"); 
		selenium.click("//input[@onclick='return selectOrganization()']");
		Thread.sleep(10000);
	} catch (Exception e) {
		//logger.info("Option \"Add Organizations\" is not available.");
		System.out.println("Option \"Add Organizations\" is not available.");
	}
	selenium.waitForPageToLoad("30000");
	try {
		selenium.addSelection("selOrganizations", "index=0");
		Thread.sleep(10000);
	} catch (Exception e) {
		//logger.info("Cannot select Organization to remove.");
		System.out.println("Cannot select Organization to remove.");
	}
	
		try {
		selenium.click("//input[@onclick=\"return removeSelOrgs()\"]");
		Thread.sleep(10000);
		} catch (Exception e) {
		//logger.info("Cannot find \"Remove\" button and unable to remove organization.");
		System.out.println("Cannot find \"Remove\" button and unable to remove organization.");
	}
		selenium.waitForPageToLoad("30000");
	try {
		selenium.click("//input[@onclick=\"javascript:selectOrg('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=false~PARAM_CALLBACKFUNCTION_NAME=submitForm();~PARAM_COLLECTION_NAME=organizations~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
		Thread.sleep(10000);
		selenium.click("//input[@onclick='return searchOrganization()']");
		Thread.sleep(10000);
		selenium.click("selOrganisations"); 
		selenium.click("//input[@onclick='return selectOrganization()']");
		Thread.sleep(10000);
	} catch (Exception e) {
		//logger.info("Option \"Add Organizations\" is not available.");
		System.out.println("Option \"Add Organizations\" is not available.");
	}
	selenium.waitForPageToLoad("30000");
	try {
		selenium.click("//input[@onclick=\"return previewEvent();\"]");
	} catch (Exception e) {
		//logger.info("Cannot find \"Preview\" button.");
		System.out.println("Cannot find \"Preview\" button.");
	}		
	selenium.waitForPageToLoad("50000");
	Thread.sleep(10000);
	try {selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
		}catch (Exception e) {
			//logger.info("Cannot find \"Edit\" button.");
			System.out.println("Cannot find \"Edit\" button.");
		}	
	selenium.waitForPageToLoad("30000");
	try {selenium.click("//input[@onclick=\"return sendEvent();\"]");
	}catch (Exception e) {
		//logger.info("Cannot find \"Save and send\" button.");
		System.out.println("Cannot find \"Save and send\" button.");
	}	
	
	selenium.waitForPageToLoad("1000000");	
	try {
	selenium.click("//span[@title=\"Title:Test Event StartDate:Mon Jun 08 2009 11:00:00 GMT-0400 (Eastern Daylight Time)EndDate:Mon Aug 17 2009 00:00:00 GMT-0400 (Eastern Daylight Time)\"]");
	}catch (Exception e) {
	//logger.info("Cannot find event in calendar.");
	System.out.println("Cannot find event in calendar.");
		}
	
	selenium.waitForPageToLoad("50000");
	
	Thread.sleep(10000);
	try {selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = ''\"]");
		}catch (Exception e) {
			//logger.info("Cannot find the edit button.");
			System.out.println("Cannot find the edit button.");
		}	
	selenium.waitForPageToLoad("50000");
	
	selenium.type("eventTitle", "Test Event (edited)");
	selenium.type("guest", "a@b.com; am@dgfoundation.org");
	
	try {
		selenium.addSelection("whoIsReceiver", "index=5");
	} catch (Exception e) {
		//logger.info("Cannot select the attendee.");
		System.out.println("Cannot select the attendee.");
	}
			
	try {
		selenium.click("//input[@value='Add >>']");
	} catch (Exception e) {
		//logger.info("Field \"Cannot find Add>> button.");
		System.out.println("Cannot find Add>> button.");
	}
	selenium.waitForPageToLoad("30000");
	try {
		selenium.click("//input[@onclick=\"return previewEvent();\"]");
	} catch (Exception e) {
		//logger.info("Field \"Cannot find Preview button.");
		System.out.println("Cannot find Preview button.");
	}	
	selenium.waitForPageToLoad("30000");
	try {
		selenium.click("//input[@onclick=\"document.getElementById('hdnMethod').value = 'save'\"]");
	} catch (Exception e) {
		//logger.info("Cannot find Save button in preview screen.");
		System.out.println("Cannot find Save button in preview screen.");
	}	
	selenium.waitForPageToLoad("1000000");	
	try {
	selenium.click("//span[@title=\"Title:Test Event (edited) StartDate:Mon Jun 08 2009 11:00:00 GMT-0400 (Eastern Daylight Time)EndDate:Mon Aug 17 2009 00:00:00 GMT-0400 (Eastern Daylight Time)\"]");
	}catch (Exception e) {
	//logger.info("Cannot find edited event in calendar.");
	System.out.println("Cannot find edited event in calendar.");
		}
	
	selenium.waitForPageToLoad("30000");
	try {
		selenium.click("//input[@onclick=\"deleteEvent();\"]");
	} catch (Exception e) {
		//logger.info("Cannot delete the event.");
		System.out.println("Cannot delete event.");
	}
	selenium.waitForPageToLoad("30000");
	
	
	
		}
	}
}

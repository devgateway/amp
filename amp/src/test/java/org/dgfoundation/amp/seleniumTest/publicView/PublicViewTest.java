package org.dgfoundation.amp.seleniumTest.publicView;

import org.apache.log4j.Logger;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class PublicViewTest extends SeleneseTestCase {
        
    private static Logger logger = Logger.getLogger(PublicViewTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testPublicView(Selenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String tabName ="Test Tab Public View " + testTime;
        String reportName ="Test Report Public View " + testTime;
        String resourceName ="Test Resource Public View " + testTime;
        String enbabledPublicView = "";
    
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
                enbabledPublicView = selenium.getSelectedIndex("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select");
                selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=0");
                done = true;
            }
            cnt++;
            if (cnt==20) {
                done = true;
            }
        }
        selenium.click("saveAll");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Management Workspace");
        selenium.waitForPageToLoad("30000");
        
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
            selenium.check("publicReport");
            selenium.click("//li[@id='columns_tab_label']/a/div");
            if (selenium.isElementPresent("fieldVis:4")) {
                selenium.click("fieldVis:4");
            } else {
                selenium.click("fieldVis:12");
                logger.error("Option \"Project Title\" is not available.");
                //selenium.logAssertion"assertTrue", "Option \"Project Title\" is not available.", "condition=false");
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
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.typeKeys("reportTitle", tabName);
            selenium.click("last_save_button");
            selenium.waitForPageToLoad("30000");
        }
        
        boolean addReportAvailable = false;
        if (selenium.isElementPresent("//a[contains(@href, "
                + "\"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=R\")]")) {
            selenium.click("//a[contains(@href, \"/TEMPLATE/reampv2/build/index.html#/report_generator?profile=R\")]");
            selenium.waitForPageToLoad("360000");
            addReportAvailable = true;
        } else {
            logger.error("Option \"Add Report\" is not available.");
            //selenium.logAssertion"assertTrue", "Option \"Add Report\" is not available.", "condition=false");
        }
        if (addReportAvailable) {   
            selenium.check("publicReport");
            selenium.click("//input[@name='reportPeriod' and @value='Q']");
            selenium.click("//li[@id='columns_tab_label']/a/div");
            if (selenium.isElementPresent("fieldVis:4")) {
                selenium.click("fieldVis:4");
            } else {
                selenium.click("fieldVis:12");
                logger.error("Option \"Project Title\" is not available.");
                //selenium.logAssertion"assertTrue", "Option \"Project Title\" is not available.", "condition=false");
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
            selenium.click("//div[@id='measures_step_div']/div[1]/button[4]");
            selenium.typeKeys("reportTitle", reportName);
            selenium.click("last_save_button");
            selenium.waitForPageToLoad("50000");
        }
        String rId = "";
        boolean resourcesAvailable = false;
        if (selenium.isElementPresent("//a[contains(@href, \"/contentrepository/documentManager.do\")]")) {
            resourcesAvailable = true;
            selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button']");
            Thread.sleep(5000);
            selenium.click("//input[@name='webResource' and @value='true']");
            selenium.type("docTitle", resourceName);
            selenium.type("webLink", "http://docs.ampdev.net/"+testTime);
            selenium.click("//input[@onclick=\"return validateAddDocumentLocal()\"]");
            selenium.waitForPageToLoad("30000");
            rId = selenium.getAttribute("//a[@onclick=\"window.open('http://docs.ampdev.net/"+testTime+"')\" and @style=\"cursor: pointer; text-decoration: underline; color: blue;\"]@id");
            rId = rId.substring(1);
            if (selenium.isElementPresent("//a[@id='Pub"+rId+"']/img")) {
                selenium.click("//a[@id='Pub"+rId+"']/img");
                selenium.waitForPageToLoad("30000");
            } else {
                if (selenium.isElementPresent("//a[@id='Priv"+rId+"']/img")) {
                    logger.info("Resource is already public");
                   //selenium.logComment("Resource is already public");
                }
            }
        } else {
            logger.info("Option 'Resources' is not available.");
           //selenium.logComment("Option 'Resources' is not available.");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
        selenium.click("//a[contains(@href, \"/aim/reportsPublicView.do\")]");
        selenium.waitForPageToLoad("30000");
        if (addTabAvailable) {
            if (!selenium.isElementPresent("//a[@id='Tab-" + tabName + "']/div")) {
                logger.error("Public tab is not shown in Public View");
                //selenium.logAssertion"assertTrue", "Public tab is not shown in Public View", "condition=false");
            }
        }
        selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
        Thread.sleep(15000);
        if (resourcesAvailable) {
            if (!selenium.isTextPresent(resourceName)) {
                logger.error("Public resource is not shown on Public View");
                //selenium.logAssertion"assertTrue", "Public resource is not shown on Public View", "condition=false");
            }
        }
        selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=false\")]");
        selenium.waitForPageToLoad("30000");
        if (addReportAvailable) {
            if (!selenium.isElementPresent("link="+reportName)) {
                logger.error("Public Report is not shown on Public View");
                //selenium.logAssertion"assertTrue", "Public Report is not shown on Public View", "condition=false");
            }
        }
        selenium.click("//a[contains(@href, \"/aim\")]");
        selenium.waitForPageToLoad("30000");
        
        //Restore all things as before the test.
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/GlobalSettings.do\")]");
        selenium.waitForPageToLoad("30000");
        cnt = 2;
        done = false;
        if (enbabledPublicView.equals("1")) {
            while (!done) {
                if (selenium.getAttribute("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/input[2]@value").equalsIgnoreCase("Public View")) {
                    selenium.select("//table[@id='general']/tbody/tr["+cnt+"]/td[2]/select", "index=1");
                    done = true;
                }
                cnt++;
                if (cnt==20) {
                    done = true;
                }
            }
        }
        selenium.click("saveAll");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Management Workspace");
        selenium.waitForPageToLoad("30000");
        if (addTabAvailable && addReportAvailable) {
            selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=false\")]");
            selenium.waitForPageToLoad("30000");
            done = false;
            cnt = 0;
            while (!done){
                if (selenium.isElementPresent("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]")) {
                    String repname = selenium.getText("//a[contains(@href, \"/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=" + cnt + "\")]");
                    if (repname.equals(reportName)) {
                        selenium.click("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=0~rid=" + cnt + "~event=edit\")]");
                        selenium.getConfirmation();
                        done = true;
                        selenium.waitForPageToLoad("30000");                    
                    }
                } 
                cnt++;
                if (cnt==10000) {
                    done = true;
                    logger.error("Delete report error.");
                    //selenium.logAssertion"assertTrue", "Delete report error.", "condition=false");
                }
            }
            cnt-=2;
            selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=1~rid=" + cnt + "~event=edit\")]")) {
                selenium.click("//a[contains(@href, \"/aim/deleteAllReports.do~isTab=1~rid=" + cnt + "~event=edit\")]");
                selenium.getConfirmation();
                selenium.waitForPageToLoad("30000");    
            } else {
                logger.error("Delete tab error.");
                //selenium.logAssertion"assertTrue", "Delete tab error.", "condition=false");
            }
        }
        
        if (resourcesAvailable) {
            selenium.click("//a[contains(@href, \"/contentrepository/documentManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[@id='a"+rId+"']/img");
            selenium.getConfirmation();
            Thread.sleep(5000);
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Public View Test Finished Successfully");
       //selenium.logComment("Public View Test Finished Successfully");
    }
    
}


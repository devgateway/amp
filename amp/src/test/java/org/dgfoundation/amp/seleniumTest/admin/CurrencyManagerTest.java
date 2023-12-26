package dgfoundation.amp.seleniumTest.admin;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class CurrencyManagerTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(CurrencyManagerTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testCurrencyManager(LoggingSelenium selenium) throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String currencyName = "Currency Test Selenium";
        String currencyCode = "STC";
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/aim/currencyManager.do\")]")) {
            selenium.click("//a[contains(@href, \"/aim/currencyManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("numRecords", "200");
            selenium.click("//input[@onclick=\"submit()\"]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"javascript:deleteCurrency('"+currencyCode+"')\")]")) {
                selenium.click("//a[contains(@href, \"javascript:deleteCurrency('"+currencyCode+"')\")]");
                selenium.getConfirmation();
                selenium.waitForPageToLoad("30000");            
            }
            selenium.click("//a[contains(@href, \"javascript:addNewCurrency()\")]");
            Thread.sleep(10000);
            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
            selenium.type("currencyCode", currencyCode);
            selenium.type("currencyName", currencyName);
            selenium.select("lstCountry", "index=1");
            selenium.click("//input[@onclick=\"return saveCurrency()\"]");
            selenium.selectWindow("null");
            selenium.waitForPageToLoad("30000");
            selenium.type("numRecords", "200");
            selenium.click("//input[@onclick=\"submit()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("link="+currencyCode);
            Thread.sleep(10000);
            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
            selenium.type("currencyName", currencyName + " mod");
            selenium.click("//input[@onclick=\"return saveCurrency()\"]");
            selenium.selectWindow("null");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=" + currencyName + " mod");
            Thread.sleep(10000);
            selenium.selectWindow(selenium.getAllWindowTitles()[1]);
            selenium.type("currencyName", currencyName);
            selenium.click("//input[@onclick=\"return saveCurrency()\"]");
            selenium.selectWindow("null");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/admin.do\")]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"/aim/showCurrencyRates.do~clean=true~timePeriod=1\")]")) {
                selenium.click("//a[contains(@href, \"/aim/showCurrencyRates.do~clean=true~timePeriod=1\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"javascript:addExchangeRate()\")]");
                Thread.sleep(5000);
                selenium.select("updateCRateCode", "label="+currencyCode);
                selenium.type("updateCRateDate", "09/09/2009");
                selenium.type("updateCRateAmount", "5");
                selenium.click("//input[@onclick=\"saveRate()\"]");
                selenium.waitForPageToLoad("30000");
                selenium.type("filterByDateFrom","09/09/2009");
                selenium.type("numResultsPerPage", "200");
                selenium.click("//input[@type=\"submit\"]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"javascript:editExchangeRate('09/09/2009','"+currencyCode+"')\")]");
                Thread.sleep(5000);
                selenium.type("updateCRateAmount", "6");
                selenium.click("//input[@onclick=\"saveRate()\"]");
                selenium.waitForPageToLoad("30000");
            } else {
                logger.error("Currency Rate Manager is not available");
                selenium.logComment("Currency Rate Manager is not available");
            }
            
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
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(3)']")) {
                selenium.click("//a[@href='javascript:gotoStep(3)']");
                selenium.waitForPageToLoad("50000");
                selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(5000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);        
                SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                selenium.click("//input[@onclick='return searchOrganization()']");
                SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                selenium.click("selOrganisations"); 
                selenium.click("//input[@onclick='return selectOrganization()']");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("50000");
                String val = selenium.getAttribute("selFundingOrgs@value");
                selenium.click("//input[@onclick=\"addFunding('" + val + "')\"]"); 
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.click("//input[@onclick='addFundingDetail(0)']");
                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[0].currencyCode", 90);
                try {
                    selenium.select("fundingDetail[0].currencyCode", currencyName);
                } catch (Exception e) {
                    logger.error("Currency added is not available on Activity Form");
                    selenium.logComment("Currency added is not available on Activity Form");
                }
                selenium.click("//input[@onclick=\"closeWindow()\"]");
                selenium.selectWindow("null");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
                    
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/currencyManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("numRecords", "200");
            selenium.click("//input[@onclick=\"submit()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"javascript:makeInactive('"+currencyCode+"')\")]");
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
            if (selenium.isElementPresent("//a[@href='javascript:gotoStep(3)']")) {
                selenium.click("//a[@href='javascript:gotoStep(3)']");
                selenium.waitForPageToLoad("50000");
                selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(5000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);        
                SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
                selenium.click("//input[@onclick='return searchOrganization()']");
                SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
                selenium.click("selOrganisations"); 
                selenium.click("//input[@onclick='return selectOrganization()']");
                selenium.selectWindow("null");
                selenium.waitForPageToLoad("50000");
                String val = selenium.getAttribute("selFundingOrgs@value");
                selenium.click("//input[@onclick=\"addFunding('" + val + "')\"]"); 
                //selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
                Thread.sleep(10000);
                selenium.selectWindow(selenium.getAllWindowTitles()[1]);
                selenium.click("//input[@onclick='addFundingDetail(0)']");
                SeleniumTestUtil.waitForElement(selenium,"fundingDetail[0].currencyCode", 90);
                boolean inList = false;
                String[] list = selenium.getSelectOptions("fundingDetail[0].currencyCode");
                for (int i = 0; i < list.length; i++) {
                    if (list[i].equals(currencyName)) {
                        inList = true;
                    }
                }
                if (inList) {
                    logger.error("Inactive Currency is available on Activity Form");
                    selenium.logComment("Inactive Currency is available on Activity Form");
                }
                selenium.click("//input[@onclick=\"closeWindow()\"]");
                selenium.selectWindow("null");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/aim/currencyManager.do\")]");
            selenium.waitForPageToLoad("30000");
            selenium.select("filterByCurrency", "index=2");
            selenium.click("//input[@onclick=\"applyFilter()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.type("numRecords", "200");
            selenium.click("//input[@onclick=\"submit()\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"javascript:deleteCurrency('"+currencyCode+"')\")]");
            selenium.getConfirmation();
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("link="+currencyCode)) {
                logger.error("Currency wasn't deleted");
                selenium.logComment("Currency wasn't deleted");
            }
        } else {
            logger.error("Currency Manager is not available");
            selenium.logComment("Currency Manager is not available");
        }
        
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Currency Manager Test Finished Successfully");
        selenium.logComment("Currency Manager Test Finished Successfully");
    }
}

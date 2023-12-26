package dgfoundation.amp.seleniumTest.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class MondrianTest extends SeleneseTestCase{
    
    private static Logger logger = Logger.getLogger(MondrianTest.class);
    
    public void setUp() throws Exception {
//      setUp("http://malawi.ampdev.net/", "*chrome");
        setUp("http://senegal.staging.ampdev.net/", "*chrome");
    }
    public static void testMondrian(LoggingSelenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "uattl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("50000");
        
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("50000");
        if (SeleniumFeaturesConfiguration.getModuleState("Multi-dimensional Reports")){
            if (selenium.isElementPresent("//a[contains(@href, \"/mondrian/mainreports.do\")]")) {
                selenium.click("//a[contains(@href, \"/mondrian/mainreports.do\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/mondrian/showreport.do?id=1&pagename=query\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("toolbar01.cubeNaviButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("//table[@id=\"navi01\"]/tbody/tr[2]/td[1]/div/a");
                selenium.waitForPageToLoad("30000");
                selenium.check("//table[@id='navi01']/tbody/tr[2]/td/div/input[1]");
                selenium.check("//table[@id='navi01']/tbody/tr[3]/td/div/input[1]");
                selenium.check("//table[@id='navi01']/tbody/tr[4]/td/div/input[1]");
                String measure1 = selenium.getText("//table[@id='navi01']/tbody/tr[2]/td/div");
                String measure2 = selenium.getText("//table[@id='navi01']/tbody/tr[3]/td/div");
                String measure3 = selenium.getText("//table[@id='navi01']/tbody/tr[4]/td/div");
                selenium.click("navi01.membernav.ok");
                selenium.waitForPageToLoad("30000");
                selenium.click("//table[@id=\"navi01\"]/tbody/tr[4]/td[1]/div/a");
                selenium.waitForPageToLoad("30000");
                selenium.click("//table[@id='navi01']/tbody/tr[2]/td/div/input[1]");
                selenium.click("//table[@id='navi01']/tbody/tr[2]/td/div/input[3]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//table[@id='navi01']/tbody/tr[3]/td/div/input[3]");
                selenium.waitForPageToLoad("30000");
                int sectors = 0;
                String[] sector = null;
                if (selenium.isElementPresent("//table[@id='navi01']/tbody/tr[4]/td/div/input[1]")) {
                    selenium.click("//table[@id='navi01']/tbody/tr[4]/td/div/input[1]");
                    sector[0] = selenium.getText("//table[@id='navi01']/tbody/tr[4]/td/div");
                    sectors++;
                }
                if (selenium.isElementPresent("//table[@id='navi01']/tbody/tr[5]/td/div/input[1]")) {
                    selenium.click("//table[@id='navi01']/tbody/tr[5]/td/div/input[1]");
                    sector[1] = selenium.getText("//table[@id='navi01']/tbody/tr[5]/td/div");
                    sectors++;
                }
                if (selenium.isElementPresent("//table[@id='navi01']/tbody/tr[6]/td/div/input[1]")) {
                    selenium.click("//table[@id='navi01']/tbody/tr[6]/td/div/input[1]");
                    sector[2] = selenium.getText("//table[@id='navi01']/tbody/tr[6]/td/div");
                    sectors++;
                }
                selenium.click("navi01.membernav.ok");
                selenium.waitForPageToLoad("30000");
                selenium.click("navi01.hiernav.ok");
                selenium.waitForPageToLoad("30000");
                selenium.click("toolbar01.chartPropertiesButton01");
                selenium.waitForPageToLoad("30000");
                selenium.type("chartform01.204", "1990");
                selenium.type("chartform01.205", "1990");
                selenium.click("chartform01.214");
                selenium.waitForPageToLoad("30000");
                selenium.click("toolbar01.chartButton01");
                selenium.waitForPageToLoad("30000");
                if (!selenium.getText("//table[@id='table01']/tbody/tr[2]/th[3]").equals(measure1)) {
                    logger.error("Error on measure");
                    selenium.logAssertion("assertTrue", "Error on measure", "condition=false");
                }
                if (!selenium.getText("//table[@id='table01']/tbody/tr[2]/th[4]").equals(measure2)) {
                    logger.error("Error on measure");
                    selenium.logAssertion("assertTrue", "Error on measure", "condition=false");
                }
                if (!selenium.getText("//table[@id='table01']/tbody/tr[2]/th[5]").equals(measure3)) {
                    logger.error("Error on measure");
                    selenium.logAssertion("assertTrue", "Error on measure", "condition=false");
                }
                for (int i = 0; i < sectors; i++) {
                    if (!selenium.getText("//table[@id='table01']/tbody/tr[" +(i+3)+ "]/th[1]").equals(sector[i])) {
                        logger.error("Error on sector");
                        selenium.logAssertion("assertTrue", "Error on sector", "condition=false");
                    }
                }
                if (!selenium.isElementPresent("//img[@width='1990' and @height='1990']")) {
                    logger.error("Error on graphic settings");
                    selenium.logAssertion("assertTrue", "Error on graphic settings", "condition=false");
                }
            } else {
                logger.error("Module \"Multi-dimensional Reports\" is active in Feature Manager but is not available.");
                selenium.logAssertion("assertTrue", "Module \"Multi-dimensional Reports\" is active in Feature Manager but is not available.", "condition=false");
            }
        } else {
            logger.info("Module \"Multi-dimensional Reports\" is not available.");
            selenium.logComment("Module \"Multi-dimensional Reports\" is not available.");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Mondrian Test Finished Successfully");
        selenium.logComment("Mondrian Test Finished Successfully");
    }

}

package dgfoundation.amp.seleniumTest;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class SeleniumFeaturesConfiguration extends SeleneseTestBase {
    
    private static Logger logger = Logger.getLogger(SeleniumFeaturesConfiguration.class);
    
    private static Hashtable<String, Boolean> moduleVisibility = new Hashtable<String, Boolean>();
    private static Hashtable<String, Boolean> featureVisibility = new Hashtable<String, Boolean>();
    private static Hashtable<String, Boolean> fieldVisibility = new Hashtable<String, Boolean>();

    private static Hashtable<String, String> moduleIds = new Hashtable<String, String>();
    private static Hashtable<String, String> featureIds = new Hashtable<String, String>();
    private static Hashtable<String, String> fieldIds = new Hashtable<String, String>();

    public static void getConfigurationFromFM(LoggingSelenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "admin@amp.org");
        selenium.type("j_password", "admin");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/aim/visibilityManager.do\")]");
        selenium.waitForPageToLoad("30000");
        boolean done = false;
        int cnt = 0;
        while (!done) {
            if (selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]") && !selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=delete\")]")) {
                selenium.click("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]");
                selenium.waitForPageToLoad("30000");
                done = true;
            }
            cnt++;
        }
        int modulesCounter = 0;
        int featuresCounter = 0;
        int fieldsCounter = 0;
        for (int i = 0; i < 1000; i++) {
            if (selenium.isElementPresent("moduleVis:"+i)) {
                moduleVisibility.put(selenium.getText("module:"+i).toUpperCase(), selenium.isChecked("moduleVis:"+i));
                moduleIds.put(selenium.getText("module:"+i), String.valueOf(i));
                modulesCounter++;
            }
            if (selenium.isElementPresent("featureVis:"+i)) {
                featureVisibility.put(selenium.getText("feature:"+i).toUpperCase(), selenium.isChecked("featureVis:"+i));
                featureIds.put(selenium.getText("feature:"+i), String.valueOf(i));
                featuresCounter++;
            }
            if (selenium.isElementPresent("fieldVis:"+i)) {
                fieldVisibility.put(selenium.getText("field:"+i).toUpperCase(), selenium.isChecked("fieldVis:"+i));
                fieldIds.put(selenium.getText("field:"+i), String.valueOf(i));
                fieldsCounter++;
            }           
        }
        logger.info("Modules found: " + modulesCounter);
        selenium.logComment("Modules found: " + modulesCounter);
        logger.info("Features found: " + featuresCounter);
        selenium.logComment("Features found: " + featuresCounter);
        logger.info("Fields found: " + fieldsCounter);
        selenium.logComment("Fields found: " + fieldsCounter);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Get Features Configuration Finished Successfully");
        selenium.logComment("Get Features Configuration Finished Successfully");
    }
    
    public static boolean getModuleState(String key) throws Exception {
        if (moduleVisibility.containsKey(key.toUpperCase())) {
            return moduleVisibility.get(key.toUpperCase());
        } else {
            return false;
        }
    }
    
    public static boolean getFeatureState(String key) throws Exception {
        if (featureVisibility.containsKey(key.toUpperCase())) {
            return featureVisibility.get(key.toUpperCase());
        } else {
            return false;
        }
    }
    
    public static boolean getFieldState(String key) throws Exception {
        if (fieldVisibility.containsKey(key.toUpperCase())) {
            return fieldVisibility.get(key.toUpperCase());
        } else {
            return false;
        }
    }
    
    public static String getModuleId(String key) throws Exception {
        if (moduleIds.containsKey(key.toUpperCase())) {
            return moduleIds.get(key.toUpperCase());
        } else {
            return null;
        }
    }
    
    public static String getFeatureId(String key) throws Exception {
        if (featureIds.containsKey(key.toUpperCase())) {
            return featureIds.get(key.toUpperCase());
        } else {
            return null;
        }
    }
    
    public static String getFieldId(String key) throws Exception {
        if (fieldIds.containsKey(key.toUpperCase())) {
            return fieldIds.get(key.toUpperCase());
        } else {
            return null;
        }
    }
    
}

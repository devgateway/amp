package org.dgfoundation.amp.seleniumTest.Messages;

import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingSelenium;

public class MessageTest extends SeleneseTestCase {
    
    private static Logger logger = Logger.getLogger(MessageTest.class);
    
    public void setUp() throws Exception {
        setUp("http://localhost:8080/", "*chrome");
    }
    public static void testMessages(LoggingSelenium selenium) throws Exception {
        selenium.open("/");
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]") && selenium.isElementPresent("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]")) {
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message to myself");
            selenium.type("descMax", "Test 1");
            selenium.addSelection("whoIsReceiver", "label=UATtl UATtl");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Message to myself")) {
                logger.error("Error sending message to myself");
                selenium.logAssertion("assertTrue", "Error sending message to myself", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message to team partner");
            selenium.type("descMax", "Test 2");
            selenium.addSelection("whoIsReceiver", "label=UATtm UATtm");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message to my team");
            selenium.type("descMax", "Test 3");
            selenium.addSelection("whoIsReceiver", "label=---UAT Team Workspace---");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Message to my team")) {
                logger.error("Error sending message to my team");
                selenium.logAssertion("assertTrue", "Error sending message to my team", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message to another team member");
            selenium.type("descMax", "Test 4");
            selenium.addSelection("whoIsReceiver", "label=---UAT Team Workspace - Computed---");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.addSelection("selreceivers", "label=UATtm UATtm");
            selenium.addSelection("selreceivers", "label=UATtl UATtl");
            selenium.click("//input[@onclick=\"MyremoveUserOrTeam()\"]");
            
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message to another team");
            selenium.type("descMax", "Test 5");
            selenium.addSelection("whoIsReceiver", "label=---UAT Team Workspace - Computed---");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Alert to myself");
            selenium.type("descMax", "Test 6");
            selenium.select("setAsAlert", "index=1");
            selenium.addSelection("whoIsReceiver", "label=UATtl UATtl");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Alert to my team");
            selenium.type("descMax", "Test 7");
            selenium.select("setAsAlert", "index=1");
            selenium.addSelection("whoIsReceiver", "label=---UAT Team Workspace---");
            selenium.click("//input[@onclick=\"MyaddUserOrTeam();\"]");
            selenium.click("//input[@onclick=\"save('send');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Alert to myself")) {
                logger.error("Error sending Alert to myself");
                selenium.logAssertion("assertTrue", "Error sending Alert to myself", "condition=false");
            }
            if (!selenium.isElementPresent("link=Test Alert to my team")) {
                logger.error("Error sending Alert to my team");
                selenium.logAssertion("assertTrue", "Error sending Alert to my team", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Alert saved");
            selenium.type("descMax", "Test 8");
            selenium.select("setAsAlert", "index=1");
            selenium.click("//input[@onclick=\"save('draft');\"]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=2\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isTextPresent("Test Alert saved")) {
                logger.error("Error on saved Alert");
                selenium.logAssertion("assertTrue", "Error on saved Alert", "condition=false");
            }
            
            selenium.click("//a[contains(@href, \"/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels\")]");
            selenium.waitForPageToLoad("30000");
            selenium.type("titleMax", "Test Message saved");
            selenium.type("descMax", "Test 9");
            selenium.click("//input[@onclick=\"save('draft');\"]");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]")) {
                selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");   
                selenium.waitForPageToLoad("30000");
            }
            Thread.sleep(1500);
            if (!selenium.isTextPresent("Test Message saved")) {
                logger.error("Error on saved Message");
                selenium.logAssertion("assertTrue", "Error on saved Message", "condition=false");
            }
            
            selenium.click("link=UAT Team Workspace - Computed");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Message to another team")) {
                logger.error("Error sending Message to another team");
                selenium.logAssertion("assertTrue", "Error sending Message to another team", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "uattm@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Message to my team")) {
                logger.error("Error sending Message to my team");
                selenium.logAssertion("assertTrue", "Error sending Message to my team", "condition=false");
            }
            if (!selenium.isElementPresent("link=Test Message to team partner")) {
                logger.error("Error sending Message to a team partner");
                selenium.logAssertion("assertTrue", "Error sending Message to a team partner", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Alert to my team")) {
                logger.error("Error sending Alert to my team");
                selenium.logAssertion("assertTrue", "Error sending Alert to my team", "condition=false");
            }
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            selenium.type("j_username", "uattmc@amp.org");
            selenium.type("j_password", "abc");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=UAT Team Workspace - Computed");
            selenium.waitForPageToLoad("30000");
            selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(1500);
            if (!selenium.isElementPresent("link=Test Message to another team member")) {
                logger.error("Error sending Message to another team member");
                selenium.logAssertion("assertTrue", "Error sending Message to another team member", "condition=false");
            }
            selenium.click("link=Test Message to another team member");
            SeleniumTestUtil.waitForElement(selenium, "//input[@onclick=\"closeWindow()\"]", 60);
            Thread.sleep(1500);
            if (!selenium.isTextPresent("Test 4")) {
                logger.error("Error in message body");
                selenium.logAssertion("assertTrue", "Error in message body", "condition=false");
            }
            selenium.click("//input[@onclick=\"closeWindow()\"]");
            String mId = selenium.getAttribute("link=Test Message to another team member@href");
            mId = mId.substring(mId.indexOf("ge('")+4, mId.indexOf("')"));
            selenium.click("//a[contains(@href, \"javascript:deleteMessage('"+mId+"')\")]");
            selenium.getConfirmation();
            Thread.sleep(5000);
            selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
            selenium.waitForPageToLoad("30000");
            
            deleteAllMessages(selenium);
            
            selenium.type("j_username", "admin@amp.org");
            selenium.type("j_password", "admin");
            selenium.click("submitButton");
            selenium.waitForPageToLoad("30000");
            if (selenium.isElementPresent("//a[contains(@href, \"/message/msgSettings.do~actionType=getSettings\")]")) {
                selenium.click("//a[contains(@href, \"/message/msgSettings.do~actionType=getSettings\")]");
                selenium.waitForPageToLoad("30000");
                selenium.type("msgStoragePerMsgTypeNew", "55");
                selenium.click("//input[@onclick=\"saveRecord('storage')\"]");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                selenium.waitForPageToLoad("30000");
                
                selenium.type("j_username", "UATtl@amp.org");
                selenium.type("j_password", "abc");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("link=UAT Team Workspace");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
                selenium.waitForPageToLoad("30000");
                selenium.click("show");
                Thread.sleep(1500);
                if (!selenium.getText("adimSettings").contains("55")) {
                    logger.error("Error in Messages Settings");
                    selenium.logAssertion("assertTrue", "Error in Messages Settings", "condition=false");
                }
                selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
                selenium.waitForPageToLoad("30000");
                
                selenium.type("j_username", "admin@amp.org");
                selenium.type("j_password", "admin");
                selenium.click("submitButton");
                selenium.waitForPageToLoad("30000");
                selenium.click("//a[contains(@href, \"/message/msgSettings.do~actionType=getSettings\")]");
                selenium.waitForPageToLoad("30000");
                selenium.type("msgStoragePerMsgTypeNew", "100");
                selenium.click("//input[@onclick=\"saveRecord('storage')\"]");
                selenium.waitForPageToLoad("30000");
            }
        } else {
            logger.info("Messages are not available");
            selenium.logComment("Messages are not available");
        }
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        logger.info("Messages Test Finished Successfully");
        selenium.logComment("Messages Test Finished Successfully");
    }
    
    private static void deleteAllMessages(Selenium selenium) throws InterruptedException {
        selenium.type("j_username", "UATtl@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("j_username", "UATtm@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        selenium.type("j_username", "UATtmc@amp.org");
        selenium.type("j_password", "abc");
        selenium.click("submitButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=UAT Team Workspace");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("link=UAT Team Workspace - Computed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=1\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=2\")]");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1500);
        deleteLoop(selenium);
        selenium.click("//a[contains(@href, \"/aim/j_spring_logout\")]");
        selenium.waitForPageToLoad("30000");
        
    }

    private static void deleteLoop(Selenium selenium) {
        while (selenium.isElementPresent("//img[@hspace=\"2\" and @src=\"/repository/message/view/images/trash_12.gif\"]")) {
            selenium.click("//img[@hspace=\"2\" and @src=\"/repository/message/view/images/trash_12.gif\"]");
            selenium.getConfirmation();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}

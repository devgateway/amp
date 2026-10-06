package org.dgfoundation.amp.seleniumTest.admin;

import org.dgfoundation.amp.seleniumTest.SeleniumFeaturesConfiguration;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComponentTypeManagerTest {

    private static final Logger logger = Logger.getLogger(ComponentTypeManagerTest.class);
    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    void setupClass() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // Update path as needed
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testComponentTypeManager() throws Exception {
        String testTime =  String.valueOf(System.currentTimeMillis());
        String typeName ="Test Component Manager " + testTime;
        boolean checkable = true;
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("j_username")).sendKeys("admin@amp.org");
        driver.findElement(By.id("j_password")).sendKeys("admin");
        driver.findElement(By.id("submitButton")).click();
        // Wait for the page to load
        Thread.sleep(30000);
        if (driver.findElements(By.xpath("//a[contains(@href, '/aim/updateComponentType.do')]")).size() > 0) {
            driver.findElement(By.xpath("//a[contains(@href, '/aim/updateComponentType.do')]")).click();
            // Wait for the page to load
            Thread.sleep(30000);
            driver.findElement(By.id("submitButton")).click();
            Thread.sleep(10000);
            driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Name")){
                if (driver.findElements(By.name("name")).size() > 0) {
                    driver.findElement(By.name("name")).sendKeys(typeName);
                } else {
                    checkable = false;
                    logger.error("Field \"Admin - Component Type Name\" is active in Feature Manager but is not available.");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Name\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Code")){
                if (driver.findElements(By.name("code")).size() > 0) {
                    driver.findElement(By.name("code")).sendKeys("SCT");
                } else {
                    checkable = false;
                    logger.error("Field \"Admin - Component Type Code\" is active in Feature Manager but is not available.");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Code\" is not available.");
            }
            if (SeleniumFeaturesConfiguration.getFieldState("Admin - Component Type Save Button")){
                if (driver.findElements(By.id("addBtn")).size() > 0) {
                    driver.findElement(By.id("addBtn")).click();
                    driver.switchTo().defaultContent();
                    Thread.sleep(30000);
                } else {
                    checkable = false;
                    driver.close();
                    driver.switchTo().defaultContent();
                    logger.error("Field \"Admin - Component Type Save Button\" is active in Feature Manager but is not available.");
                }
            } else {
                checkable = false;
                logger.info("Field \"Admin - Component Type Save Button\" is not available.");
            }
            if (checkable) {
                int tId = 0;
                for (int i = 500; i > 0; i--) {
                    if (driver.findElements(By.xpath("//a[contains(@href, 'javascript:editType("+i+")')]")).size() > 0) {
                        tId = i;
                        break;
                    }
                }
                driver.findElement(By.xpath("//a[contains(@href, 'javascript:editType("+tId+")')]")).click();
                Thread.sleep(10000);
                driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
                driver.findElement(By.name("name")).sendKeys(typeName+" mod");
                driver.findElement(By.id("addBtn")).click();
                driver.switchTo().defaultContent();
                Thread.sleep(30000);
                driver.findElement(By.xpath("//a[contains(@href, '/aim/j_spring_logout')]")).click();
                Thread.sleep(30000);

                driver.findElement(By.id("j_username")).sendKeys("UATtl@amp.org");
                driver.findElement(By.id("j_password")).sendKeys("abc");
                driver.findElement(By.id("submitButton")).click();
                Thread.sleep(30000);
                driver.findElement(By.linkText("UAT Team Workspace")).click();
                Thread.sleep(30000);
                driver.findElement(By.xpath("//a[contains(@href, \"javascript:addActivity()\")]")).click();
                Thread.sleep(30000);
                if (SeleniumFeaturesConfiguration.getFeatureState("Components")){
                    if (driver.findElements(By.xpath("//a[@href='javascript:gotoStep(5)']")).size() > 0) {
                        driver.findElement(By.xpath("//a[@href='javascript:gotoStep(5)']")).click();
                        Thread.sleep(30000);
                        driver.findElement(By.xpath("//input[@onclick=\"addComponents()\"]")).click();
                        Thread.sleep(5000);
                        if (SeleniumFeaturesConfiguration.getFeatureState("Admin - Component Type")){
                            if (driver.findElements(By.xpath("//a[@href='javascript:gotoStep(5)']")).size() > 0) {
                                try {
                                    driver.findElement(By.name("selectedType")).sendKeys(typeName+" mod");
                                } catch (Exception e) {
                                    logger.error("Component type added is not available on Activity Form");
                                }
                            } else {
                                logger.error("Feature \"Admin - Component Type\" is active in Feature Manager but is not available.");
                            }
                        } else {
                            logger.info("Feature \"Admin - Component Type\" is not available.");
                        }
                        driver.findElement(By.name("newCompoenentName")).sendKeys("Selenium Component");
                        driver.findElement(By.xpath("//div[@id='new']/div[3]")).click();
                    } else {
                        logger.error("Feature \"Components\" is active in Feature Manager but is not available.");
                    }
                } else {
                    logger.info("Feature \"Components\" is not available.");
                }

                driver.findElement(By.xpath("//a[contains(@href, '/aim/j_spring_logout')]")).click();
                Thread.sleep(30000);

                driver.findElement(By.id("j_username")).sendKeys("admin@amp.org");
                driver.findElement(By.id("j_password")).sendKeys("admin");
                driver.findElement(By.id("submitButton")).click();
                Thread.sleep(30000);
                driver.findElement(By.xpath("//a[contains(@href, '/aim/updateComponentType.do')]")).click();
                Thread.sleep(30000);
                try {
                    driver.findElement(By.xpath("//a[contains(@href, 'javascript:deleteType("+tId+");')]")).click();
                    driver.switchTo().alert().accept();
                    Thread.sleep(30000);
                    if (driver.getPageSource().contains(typeName)) {
                        logger.error("Component Type wasn't deleted");
                    }
                } catch (Exception e) {
                    logger.error("Component Type is not available to be deleted");
                }
            }
        } else {
            logger.error("Component Type Manager is not available");
        }

        driver.findElement(By.xpath("//a[contains(@href, '/aim/j_spring_logout')]")).click();
        Thread.sleep(30000);
        logger.info("Component Type Manager Test Finished Successfully");
    }
}

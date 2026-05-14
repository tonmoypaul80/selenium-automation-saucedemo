package com.saucedemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;


public class LoginTest {

    WebDriver driver;
    @BeforeMethod
    public void setUp() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
    
        // CI/CD environment detect করবে automatically
        String ciEnv = System.getenv("CI");
        if (ciEnv != null && ciEnv.equals("true")) {
        // GitHub Actions-এ headless mode
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

    }
    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC001 - Valid password দিয়ে login")
    public void validLoginTest() throws InterruptedException {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        
        Thread.sleep(2000); 
        driver.findElement(By.id("login-button")).click();

        Thread.sleep(2000); 
        // ভেরিফাই করছি যে আমরা প্রোডাক্ট পেজে পৌঁছেছি কিনা
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed!");
    }

    //TC002 - Invalid password দিয়ে login
    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC002 - Invalid password দিয়ে login")
    public void invalidLoginTest()throws InterruptedException{

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("invalid_secret_sauce");

        Thread.sleep(2000); 
        driver.findElement(By.id("login-button")).click();

        Thread.sleep(2000); 
        // ভেরিফাই করছি যে আমরা প্রোডাক্ট পেজে পৌঁছেছি কিনা
        String expectedUrl = "https://www.saucedemo.com";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Invalid password allowed user to log in!");

    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
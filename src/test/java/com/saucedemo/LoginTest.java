package com.saucedemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.time.Duration;



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
@Description("TC001 - Valid password login")
    public void validLoginTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
            .sendKeys("standard_user");

        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        Assert.assertTrue(
            driver.findElement(By.className("inventory_list")).isDisplayed(),
            "Login failed - Product page did not load!"
        );
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC002 - Invalid password login")
    public void invalidLoginTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
            .sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("invalid_secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Error message visible কিনা assert করো
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(
            driver.findElement(By.cssSelector("[data-test='error']")).isDisplayed(),
            "Error message not shown for invalid login!"
        );
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
         Thread.sleep(2000); // Only to see not for production
        if (driver != null) {
            driver.quit();
        }
    }
}
package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Unit test for simple App.
 */
public class LoginTest {
    private ChromeDriver driver;
    private String prefix = "https://digitalnizena.cz/rukovoditel/";

    @Before
    public void init() {
        //System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        //driver = new ChromeDriver();
        ChromeOptions cho = new ChromeOptions();
        cho.addArguments("headless");
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
       driver.close();
    }


    @Test
    public void validLogin() {
        //Given + When
        prihlasSe("rukovoditel","vse456ru");
        //Then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));
    }

    @Test
    public void invalidLogin() {
        //Given + When
        prihlasSe("admin","admin");
        //Then
        WebElement alert = driver.findElement(By.cssSelector(".alert"));
        Assert.assertTrue(!driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel"));
        Assert.assertTrue(alert.isDisplayed());
    }

    @Test
    public void userLogout() {
        //Given
        prihlasSe("rukovoditel","vse456ru");
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));

        //When
        driver.findElement(By.cssSelector(".username")).click();
        driver.findElement(By.cssSelector(".username")).click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='logoff']")));
        driver.findElement(By.cssSelector("a[href*='logoff']")).click();

        //Then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel"));
        Assert.assertTrue(!driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        WebElement nadpis = driver.findElement(By.cssSelector(".form-title"));
        Assert.assertTrue(nadpis.getText().equals("Login"));
    }

    public void prihlasSe(String jmeno,String heslo)
    {
        driver.get(prefix);
        WebElement searchInput = driver.findElement(By.name("username"));
        searchInput.sendKeys(jmeno);
        searchInput = driver.findElement(By.name("password"));
        searchInput.sendKeys(heslo);
        //searchInput.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".btn")).click();
    }
}

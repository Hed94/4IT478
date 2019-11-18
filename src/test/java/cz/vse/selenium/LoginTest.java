package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
    }

    @After
    public void tearDown() {
       driver.close();
    }


    @Test
    public void valid_login() {
        //Given + When
        prihlasSe("rukovoditel","vse456ru");
        //Then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));
    }

    @Test
    public void invalid_login() {
        //Given + When
        prihlasSe("admin","admin");
        //Then
        WebElement alert = driver.findElement(By.cssSelector(".alert"));
        Assert.assertTrue(!driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel"));
        Assert.assertTrue(alert.isDisplayed());
    }

    @Test
    public void user_logout() {
        //Given
        prihlasSe("rukovoditel","vse456ru");
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));

        //When
        WebElement logoff = driver.findElement(By.cssSelector(".username"));
        logoff.click();
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

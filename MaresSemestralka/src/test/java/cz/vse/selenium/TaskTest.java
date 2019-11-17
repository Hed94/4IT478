package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class TaskTest {
    private ChromeDriver driver;
    private String prefix = "https://digitalnizena.cz/rukovoditel/";

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();

    }

    @After
    public void tearDown() {
       driver.close();
    }


    

    // Metoda kteřá řeší přihlášení
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

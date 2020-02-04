package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PrihlasovaciTesty {
    private ChromeDriver driver;

    @Before
    public void init() {
        //System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        //driver = new ChromeDriver();
        ChromeOptions cho = new ChromeOptions();
        cho.addArguments("--headless");
        cho.addArguments("start-maximized");
        cho.addArguments("window-size=1200,1100");
        cho.addArguments("--disable-gpu");
        cho.addArguments("--disable-extensions");
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
       driver.close();
    }
    /**
     * Testování úspěšného scénáře přihlášení
     */
    @Test
    public void uspesnePrihlaseni() {
        //Given
        driver.get(TestovaciMetody.getAdresa());
        //When
        driver.findElement(By.name("username")).sendKeys("rukovoditel");
        driver.findElement(By.name("password")).sendKeys("vse456ru");
        driver.findElement(By.cssSelector(".btn")).click();
        //Then
        Assert.assertTrue(driver.getTitle().contains("Dashboard"));
    }
    /**
     * Testování neúspěšného scénáře přihlašení
     * zadány špatné přihlašovací údaje
     */
    @Test
    public void neuspesnePrihlaseni() {
        //Given
        driver.get(TestovaciMetody.getAdresa());
        //When
        driver.findElement(By.name("username")).sendKeys("kouba");
        driver.findElement(By.name("password")).sendKeys("heslo");
        driver.findElement(By.cssSelector(".btn")).click();
        //Then
        Assert.assertTrue(!driver.getTitle().contains("Dashboard"));
        Assert.assertTrue(driver.findElement(By.cssSelector(".alert")).isDisplayed());
    }
    /**
     * Testování odhlášení z webu
     */
    @Test
    public void odhlaseni() {
        //Given
        TestovaciMetody.prihlaseni("rukovoditel","vse456ru",driver);
        //When
        driver.findElement(By.cssSelector(".username")).click();
        driver.findElement(By.cssSelector(".username")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Logoff"))).click();
        //Then
        Assert.assertTrue(!driver.getTitle().contains("Dashboard"));
        Assert.assertTrue(driver.findElement(By.cssSelector(".form-title")).getText().equals("Login"));
    }
}

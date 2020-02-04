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
    public void validLogin() {
        //Given + When
        GeneralTestMethods.login("rukovoditel","vse456ru",driver);
        //Then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));
    }
    /**
     * Testování neúspěšného scénáře přihlašení
     * zadány špatné přihlašovací údaje
     */
    @Test
    public void invalidLogin() {
        //Given + When
        GeneralTestMethods.login("Donald","Trump",driver);
        //Then
        WebElement alert = driver.findElement(By.cssSelector(".alert"));
        Assert.assertTrue(!driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel"));
        Assert.assertTrue(alert.isDisplayed());
    }

    @Test
    public void userLogout() {
        //Given
        GeneralTestMethods.login("rukovoditel","vse456ru",driver);

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

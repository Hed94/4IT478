package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;


public class ProjektoveTesty {
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
     * Testování vytvoření nového projektu beze jména
     * Nesmí se vytvořit
     */
    @Test
    public void projektBezJmena() {
        //Given
        TestovaciMetody.prihlaseni("rukovoditel","vse456ru",driver);
        //When
        driver.findElement(By.cssSelector("li:nth-child(4) .title:nth-child(2)")).click();
        driver.findElement(By.className("btn-primary")).click();
        TestovaciMetody.cekejClassName(2,"btn-primary-modal-action",driver);
        driver.findElement(By.className("btn-primary-modal-action")).click();
        //Then
        Assert.assertTrue(driver.findElement(By.id("fields_158-error")).isDisplayed());
    }

    /**
     * Testování úspěšného scénáře vytvoření projektu
     */
    @Test
    public void uspesneVytvoreniProjektu() {
        //Given
        TestovaciMetody.prihlaseni("rukovoditel","vse456ru",driver);
        //When
        TestovaciMetody.novyProjekt("kouba",driver);
        //Then
        driver.findElement(By.cssSelector("li:nth-child(4) .title:nth-child(2)")).click();
        TestovaciMetody.cekejCssSelector(3,"[class='table table-striped table-bordered table-hover'] tr",driver);
        List<WebElement> tabulka = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        tabulka.remove(0);
        WebElement element = null;
        for (WebElement row : tabulka)
        {
            List<WebElement> bunka = row.findElements(By.tagName("td"));
            if (bunka.get(4).getText().equals("kouba"))
            {
                element = row;
                List<WebElement> buttony = row.findElements(By.tagName("a"));
                buttony.get(0).click();
                break;
            }
        }
        Assert.assertTrue(element != null);
        TestovaciMetody.cekejClassName(2,"btn-primary-modal-action",driver);
        driver.findElement(By.className("btn-primary-modal-action")).click();
        tabulka = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        Assert.assertTrue(!tabulka.contains(element));
    }
}

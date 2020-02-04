package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class TaskoveTesty {
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


    @Test
    public void jedenTask() throws ParseException {
        //Given
        TestovaciMetody.prihlaseni("rukovoditel","vse456ru",driver);
        TestovaciMetody.novyProjekt("Kouba",driver);
        //When
        driver.findElement(By.className("btn-primary")).click();
        TestovaciMetody.cekejID(3,"fields_168",driver);
        driver.findElement(By.id("fields_168")).sendKeys("Testovací task");
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.findElement(By.tagName("body")).sendKeys("Testovací popis tasku");
        driver.switchTo().defaultContent();
        driver.findElement(By.className("btn-primary-modal-action")).click();
        TestovaciMetody.cekejCssSelector(2,"[class='table table-striped table-bordered table-hover'] tr",driver);
        driver.findElement(By.cssSelector(".fa-info")).click();
        //Then
        TestovaciMetody.cekejCssSelector(2,"[class='table table-bordered table-hover table-item-details'] tr",driver);
        Assert.assertTrue(driver.findElement(By.className("caption")).getText().equals("Testovací task"));
        Assert.assertTrue(driver.findElement(By.className("fieldtype_textarea_wysiwyg")).getText().equals("Testovací popis tasku"));
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(driver.findElements(By.cssSelector(".form-group-165 > td")).get(0).getText().substring(0, 10));
        Assert.assertTrue(!driver.findElements(By.cssSelector(".form-group-165 > td")).contains(date.equals(new Date())));
        Assert.assertTrue(driver.findElements(By.cssSelector(".form-group-167 div")).get(0).getText().equals("Task"));
        Assert.assertTrue(driver.findElements(By.cssSelector(".form-group-169 div")).get(0).getText().equals("New"));
        Assert.assertTrue(driver.findElements(By.cssSelector(".form-group-170 div")).get(0).getText().equals("Medium"));
        driver.executeScript("window.history.go(-1)");
        TestovaciMetody.cekejCssSelector(2,".btn-xs > .fa-trash-o",driver);
        driver.findElements(By.cssSelector(".btn-xs > .fa-trash-o")).get(0).click();
        TestovaciMetody.cekejClassName(2,"btn-primary-modal-action",driver);
        driver.findElement(By.className("btn-primary-modal-action")).click();
    }

    @Test
    public void viceroTasku() {
        //Given
        TestovaciMetody.prihlaseni("rukovoditel","vse456ru",driver);
        TestovaciMetody.novyProjekt("Kouba",driver);
        int pocetTasku = 7;
        int pom = 0;
        //When
        while(pom < pocetTasku)
        {
            driver.findElement(By.className("btn-primary")).click();
            TestovaciMetody.cekejID(3,"fields_168",driver);
            driver.findElement(By.id("fields_168")).sendKeys("Testovací task");
            Select vyber = new Select(driver.findElement(By.id("fields_169")));
            vyber.selectByIndex(pom);
            driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
            driver.findElement(By.tagName("body")).sendKeys("Testovací popis tasku");
            driver.switchTo().defaultContent();
            driver.findElement(By.className("btn-primary-modal-action")).click();
            pom++;
        }
        //Then
        TestovaciMetody.cekejCssSelector(2,"[class='table table-striped table-bordered table-hover'] tr",driver);
        List<WebElement> elementy = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        elementy.remove(0);
        Assert.assertTrue(elementy.size() == 3);
        driver.findElement(By.className("filters-preview-condition-include")).click();
        TestovaciMetody.cekejCssSelector(2,"[class='chosen-choices'] a",driver);
        driver.findElements(By.cssSelector("[class='chosen-choices'] a")).get(1).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();
        TestovaciMetody.cekejCssSelector(2,"[class='table table-striped table-bordered table-hover'] tr",driver);
        elementy = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        elementy.remove(0);
        Assert.assertTrue(elementy.size() == 2);
        driver.findElement(By.cssSelector("a:nth-child(2) > .fa-trash-o")).click();
        TestovaciMetody.cekejCssSelector(2,"[class='table table-striped table-bordered table-hover'] tr",driver);
        Assert.assertTrue(driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")).size() == 8);
        driver.findElement(By.id("select_all_items")).click();
        driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
        driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
        TestovaciMetody.cekejLinkText(2,"Delete",driver);
        driver.findElement(By.linkText("Delete")).click();
        TestovaciMetody.cekejClassName(2,"btn-primary-modal-action",driver);
        driver.findElement(By.className("btn-primary-modal-action")).click();
    }
}

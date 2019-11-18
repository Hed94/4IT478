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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class TaskTest {
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
      // driver.close();
    }


    @Test
    public void taskCreated() throws ParseException {
        //Given
        prihlasSe("rukovoditel","vse456ru");
        vytvorProjekt("Trump2020");

        //When
        // Vytvoření tasku
        driver.findElement(By.className("btn-primary")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_168")));
        WebElement searchInput = driver.findElement(By.id("fields_168"));
        searchInput.sendKeys("Výběr peněz od sponzorů");
        // Vyplnění descriptového okna při tvoření tasku
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.findElement(By.tagName("body")).sendKeys("Kontaktovat všechny sponzory a vybrat od nich peníze na kampaň");
        driver.switchTo().defaultContent();
        driver.findElement(By.className("btn-primary-modal-action")).click();


        wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        List<WebElement> elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        List<WebElement> cells = elements.get(1).findElements(By.tagName("td"));
        List<WebElement> obsah = cells.get(1).findElements(By.tagName("a"));
        obsah.get(2).click();


        //Then
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-bordered table-hover table-item-details'] tr")));
        elements = driver.findElements(By.cssSelector("[class='table table-bordered table-hover table-item-details'] tr"));


        // Kontrola názvu - Výběr peněz od sponzorů
        WebElement nazev = driver.findElement(By.className("caption"));
        Assert.assertTrue(nazev.getText().equals("Výběr peněz od sponzorů"));

        // Kontrola desc - Kontaktovat všechny sponzory a vybrat od nich peníze na kampaň
        WebElement desc = driver.findElement(By.className("fieldtype_textarea_wysiwyg"));
        System.out.println(desc.getText());
        Assert.assertTrue(desc.getText().equals("Kontaktovat všechny sponzory a vybrat od nich peníze na kampaň"));

        // Kontrola datumu
        cells = elements.get(1).findElements(By.tagName("td"));
        Date datum =new SimpleDateFormat("MM/dd/yyyy").parse(cells.get(0).getText().substring(0, 10));
        Date sysdate = new Date();
        Assert.assertTrue(!elements.contains(datum.equals(sysdate)));

        // Kontrola typu - Task
        cells = elements.get(3).findElements(By.tagName("td"));
        obsah = cells.get(0).findElements(By.tagName("div"));
        Assert.assertTrue(obsah.get(0).getText().equals("Task"));

        // Kontrola status - New
        cells = elements.get(4).findElements(By.tagName("td"));
        obsah = cells.get(0).findElements(By.tagName("div"));
        Assert.assertTrue(obsah.get(0).getText().equals("New"));

        // Kontrola priority - Medium
        cells = elements.get(5).findElements(By.tagName("td"));
        obsah = cells.get(0).findElements(By.tagName("div"));
        Assert.assertTrue(obsah.get(0).getText().equals("Medium"));

        //Smazání tasku
        driver.executeScript("window.history.go(-1)");
        wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        cells = elements.get(1).findElements(By.tagName("td"));
        obsah = cells.get(1).findElements(By.tagName("a"));
        obsah.get(0).click();
        wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
        driver.findElement(By.className("btn-primary-modal-action")).click();
    }

    @Test
    public void sevenTasksCreated() {
        //Given
        prihlasSe("rukovoditel","vse456ru");
        vytvorProjekt("Trump2020");

        //When
        for(int i = 0;i<7;i++)
        {
            driver.findElement(By.className("btn-primary")).click();
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_168")));
            WebElement searchInput = driver.findElement(By.id("fields_168"));
            searchInput.sendKeys("Výběr peněz od sponzorů");
            Select select = new Select(driver.findElement(By.id("fields_169")));
            select.selectByIndex(i);
            driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
            driver.findElement(By.tagName("body")).sendKeys("Kontaktovat všechny sponzory a vybrat od nich peníze na kampaň");
            driver.switchTo().defaultContent();
            driver.findElement(By.className("btn-primary-modal-action")).click();
        }

        //Then
        // Kontrola že se zobrazují jen 3 tasky
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        List<WebElement> elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        Assert.assertTrue(elements.size() == 4); // 4 protože nadpis je taky řádek

        // Změna filtrů na - New / Waiting
        driver.findElement(By.className("filters-preview-condition-include")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='chosen-choices'] a")));
        List<WebElement> filtry = driver.findElements(By.cssSelector("[class='chosen-choices'] a"));
        filtry.get(1).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();

        // Kontrola že se zobrazují jen 2 tasky
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        Assert.assertTrue(elements.size() == 3); // 3 protože nadpis je taky řádek

        // Smazání všech filtrů
        driver.findElement(By.className("filters-preview-condition-include")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='chosen-choices'] a")));
        filtry = driver.findElements(By.cssSelector("[class='chosen-choices'] a"));
        filtry.get(1).click();
        filtry.get(0).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();

        // Kontrola že se zobrazuje všech 7 tasků
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        Assert.assertTrue(elements.size() == 8); // 8 protože nadpis je taky řádek

        // Vymazání všech tasků
        driver.findElement(By.id("select_all_items")).click();
        driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
        driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click(); // dvojklik
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Delete")));
        driver.findElement(By.linkText("Delete")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
        driver.findElement(By.className("btn-primary-modal-action")).click();
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

    // Metoda vytvoří projekt
    public void vytvorProjekt(String nazev)
    {
        driver.findElement(By.cssSelector(".fa-reorder")).click();
        driver.findElement(By.className("btn-primary")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
        driver.findElement(By.className("btn-primary-modal-action")).click();
        WebElement searchInput = driver.findElement(By.id("fields_158"));
        searchInput.sendKeys("Trump2020");
        Select select = new Select(driver.findElement(By.id("fields_156")));
        select.selectByIndex(1);
        driver.findElement(By.id("fields_159")).click();
        driver.findElement(By.cssSelector("td[class='active day']")).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();
    }
}

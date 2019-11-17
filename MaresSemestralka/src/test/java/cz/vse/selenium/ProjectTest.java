package cz.vse.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class ProjectTest {
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


    @Test
    public void project_not_created() throws InterruptedException{
        //Given
        prihlasSe("rukovoditel","vse456ru");
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));

        //When
        driver.findElement(By.cssSelector(".fa-reorder")).click();
        driver.findElement(By.className("btn-primary")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
        driver.findElement(By.className("btn-primary-modal-action")).click();

        //Then
        WebElement error = driver.findElement(By.id("fields_158-error"));
        Assert.assertTrue(error.isDisplayed());
    }

    @Test
    public void project_created_and_deleted() throws InterruptedException{
        //Given
        prihlasSe("rukovoditel","vse456ru");
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));

        //When
        // Uložení aktuálního počtu řádků a přechod do formuláře
        driver.findElement(By.cssSelector(".fa-reorder")).click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        List<WebElement> elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        int pocetPred = elements.size();
        driver.findElement(By.className("btn-primary")).click();

        // Vyplnění formuláže
        wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
        driver.findElement(By.className("btn-primary-modal-action")).click();
        WebElement searchInput = driver.findElement(By.id("fields_158"));
        searchInput.sendKeys("Trump2020");
        Select select = new Select(driver.findElement(By.id("fields_156")));
        select.selectByIndex(1);
        driver.findElement(By.id("fields_159")).click();
        driver.findElement(By.cssSelector("td[class='active day']")).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();


        //then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Tasks"));
        driver.findElement(By.cssSelector(".fa-reorder")).click();
        wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr")));
        elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        int pocetPo = elements.size();

        // Kontrola že předtím bylo řádků méně než je jich po přidání nového projektu
        Assert.assertTrue(pocetPred < pocetPo);

        //Pomocné vymazání nic neříkajícího prvního řádku tabulky
        elements.remove(0);
        // Deklarace kontrolní proměné
        WebElement radek = null;

        // Cyklus prochází všechny řádky a jejich políčka
        for (WebElement row : elements)
        {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(4).getText().equals("Trump2020"))
            {
                radek = row;
                List<WebElement> buttony = row.findElements(By.tagName("a"));
                buttony.get(0).click();
            }
        }
        // Kontrola že přidaný projekt existuje a je to ten muj
        Assert.assertTrue(radek != null);

        // Smazání a kontrola že už tam neni
        wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uniform-delete_confirm")));
        driver.findElement(By.id("delete_confirm")).click();
        driver.findElement(By.className("btn-primary-modal-action")).click();
        elements = driver.findElements(By.cssSelector("[class='table table-striped table-bordered table-hover'] tr"));
        Assert.assertTrue(!elements.contains(radek));
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

package ru.mail.maks825;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ProductComparatorByTitle implements Comparator<WebElement> {
    public int compare(WebElement p1, WebElement p2) {
        return (p1.getText()).compareTo(p2.getText());
    }
}

public class WileyTest {

    private static WebDriver driver;
    private static ArrayList<String> productsCopy = new ArrayList<String>();

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", ConfigProperties.getProperty("chromeDriver"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(ConfigProperties.getProperty("url"));
        driver.findElement(By.xpath("//button[text()='NO']")).click();
    }

    @Test
    public void step_1() {
        WebElement resources = driver.findElement(By.xpath("//a[text()='Resources']"));
        Assert.assertEquals("Resources", resources.getAttribute("textContent"));

        WebElement subjects = driver.findElement(By.xpath("//a[text()='Subjects']"));
        Assert.assertEquals("Subjects", subjects.getAttribute("textContent"));

        WebElement about = driver.findElement(By.xpath("//a[text()='About']"));
        Assert.assertEquals("About", about.getAttribute("textContent"));
    }

    @Test
    public void step_2() {
        List<WebElement> resources = driver.findElements(By.xpath("//div[@id='navigationNode_00000RS2']/*"));
        Assert.assertTrue(resources.size() == 10);

        List<String> resourceTitle = new ArrayList<String>();
        Collections.addAll(resourceTitle, "Students", "Instructors", "Researchers", "Professionals",
                "Librarians", "Institutions", "Authors", "Resellers", "Corporations", "Societies");

        for (int i = 0; i < resources.size(); i++) {
            Assert.assertTrue(resourceTitle.contains(resources.get(i).getAttribute("textContent")
                    .replaceAll("[\\n\\t ]", "")));
        }
    }

    @Test
    public void step_3() {
        Actions builder = new Actions(driver);
        WebElement resource = driver.findElement(By.xpath("//a[text()='Resources']"));
        builder.moveToElement(resource);
        builder.moveToElement(driver.findElement(
                By.xpath("//div[@id='navigationNode_00000RS2']//a[text()='Students']")))
                .click().perform();

        Assert.assertEquals("https://www.wiley.com/en-ru/students", driver.getCurrentUrl());

        WebElement studentsHeader = driver.findElement(By.xpath("//p[@class='sg-title-h1'][text()='Students']"));
        Assert.assertEquals("Students", studentsHeader.getAttribute("textContent"));

        WebElement linkWPlus = driver.findElement(By.linkText("WileyPLUS"));
        Assert.assertEquals("WileyPLUS", linkWPlus.getAttribute("textContent"));
        Assert.assertEquals("http://wileyplus.wiley.com/", linkWPlus.getAttribute("href"));
    }

    @Test
    public void step_4() {
        Actions builder = new Actions(driver);
        WebElement subject = driver.findElement(By.xpath("//a[text()='Subjects']"));
        builder.moveToElement(subject);
        WebElement el = driver.findElement(By.xpath("//div[@id='navigationNode_00000RS5']//a[text()='E-L']"));
        builder.moveToElement(el);
        builder.moveToElement(driver.findElement(
                By.xpath("//ul[@class='dropdown-items']//a[text()='Education']")))
                .click().perform();

        WebElement educationHeader = driver.findElement(
                By.xpath("//div[@class='wiley-slogan']//span[text()='Education']"));
        Assert.assertEquals("Education", educationHeader.getAttribute("textContent"));

        List<WebElement> subjects = driver.findElements(By.xpath("//div[@class='side-panel']/ul/*"));
        Assert.assertTrue(subjects.size() == 13);

        List<String> subjectsElements = new ArrayList<String>();
        Collections.addAll(subjectsElements, "Information & Library Science",
                "Education & Public Policy", "K-12 General", "Higher Education General",
                "Vocational Technology", "Conflict Resolution & Mediation (School settings)",
                "Curriculum Tools- General", "Special Educational Needs", "Theory of Education",
                "Education Special Topics", "Educational Research & Statistics",
                "Literacy & Reading", "Classroom Management");

        for(int i = 0; i < subjectsElements.size(); i++){
            Assert.assertTrue(subjectsElements.contains(subjects.get(i).getAttribute("textContent")));
        }
    }

    @Test
    public void step_5() {
        driver.findElement(By.xpath("//div[@class='main-navigation-menu']/div[@class='yCmsContentSlot logo']")).click();
        Assert.assertEquals("https://www.wiley.com/en-ru", driver.getCurrentUrl());
    }

    @Test
    public void step_6() {
        driver.findElement(By.xpath("//button[text()='Search']")).click();
        Assert.assertEquals("https://www.wiley.com/en-ru", driver.getCurrentUrl());
    }

    @Test
    public void step_7() {
        driver.findElement(By.id("js-site-search-input")).sendKeys("Math");
       (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='ui-autocomplete ui-front main-navigation-search-autocomplete ui-menu ui-widget ui-widget-content']")));

        List<WebElement> mathWords =
                driver.findElements(
                        By.xpath("//form[@name='search_form_SearchBox']//div[@class='search-list']/div[@class='ui-menu-item']/a"));
        Assert.assertTrue(mathWords.size() == 4);

        ((JavascriptExecutor)driver).executeScript("document.getElementById('ui-id-2').style.display='flex'");
        for(WebElement word : mathWords) {
            Assert.assertTrue(word.getText().startsWith("Math"));
        }

        WebElement headerContent = driver.findElement(By.xpath("//form[@name='search_form_SearchBox']//header/h5"));
        Assert.assertEquals("Related Content", headerContent.getAttribute("textContent"));

        List<WebElement> products = driver.findElements(By.xpath("//div[@class='related-content-products']/section"));
        Assert.assertTrue(products.size() == 4);

        for(WebElement product : products) {
            ((JavascriptExecutor)driver).executeScript("document.getElementById('ui-id-2').style.display='flex'");
            Assert.assertTrue(product.getAttribute("textContent").contains("Math"));
        }
    }

    @Test
    public void step_8() {
        driver.findElement(By.xpath("//button[text()='Search']")).click();

        List<WebElement> products = driver.findElements(By.className("product-item "));
        Assert.assertTrue(products.size() == 10);

        for(WebElement product : products) {
            Assert.assertTrue(product.findElement(By.className("product-title")).getText().contains("Math"));

            List<WebElement> containsButton = product.findElements(
                    By.xpath("//button[@class='small-button add-to-cart-button js-add-to-cart']"));

            Assert.assertTrue(containsButton.size() > 0);
        }

        for(int i = 0; i < products.size(); i++) {
            productsCopy.add(products.get(i).findElement(By.className("product-title")).getText());
        }
    }

    @Test
    public void step_9() {
        driver.findElement(By.id("js-site-search-input")).sendKeys("Math");
        driver.findElement(By.xpath("//button[text()='Search']")).click();

        List<WebElement> products = driver.findElements(By.className("product-item "));
        Assert.assertTrue(products.size() == productsCopy.size());

        ProductComparatorByTitle byTitle = new ProductComparatorByTitle();
        Collections.sort(products, byTitle);
        Collections.sort(productsCopy);

        for(int i = 0; i < products.size(); i++) {
            Assert.assertEquals(products.get(i).findElement(By.className("product-title")).getText(),
                    productsCopy.get(i));
        }
    }

    @AfterClass
    public static void endTest() {
        driver.quit();
    }
}

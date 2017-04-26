package myprojects.automation.assignment4;

import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private By catalogLink = By.cssSelector("#subtab-AdminCatalog");
    private By productLink = By.cssSelector("#subtab-AdminProducts");
    private By add = By.cssSelector("#page-header-desc-configuration-add");
    private By mainDiv = By.cssSelector("#main-div");

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void login(String login, String password) {
        driver.navigate().to(Properties.getBaseAdminUrl());
        driver.findElement(By.cssSelector("#email")).sendKeys(login);
        driver.findElement(By.cssSelector("#passwd")).sendKeys(password);
        driver.findElement(By.cssSelector("[name='submitLogin']")).click();
        waitForContentLoad(By.id("main"));
    }

    public void createProduct(ProductData newProduct) {
        waitForContentLoad(this.catalogLink);
        WebElement catalogLink = driver.findElement(this.catalogLink);
        WebElement productLink = driver.findElement(this.productLink);
        Actions actions = new Actions(driver);
        actions.moveToElement(catalogLink).click(productLink).build().perform();
        waitForContentLoad(mainDiv);
        driver.findElement(add).click();
        waitForContentLoad(mainDiv);
        WebElement name = driver.findElement(By.cssSelector("#form_step1_name_1"));
        name.sendKeys(newProduct.getName());
        driver.findElement(By.cssSelector("#tab_step3")).click();
        WebElement qty = driver.findElement(By.cssSelector("#form_step3_qty_0"));
        qty.sendKeys("\b");
        qty.sendKeys(newProduct.getQty().toString());
        driver.findElement(By.cssSelector("#tab_step2")).click();
        WebElement price = driver.findElement(By.cssSelector("#form_step2_price_ttc"));
        price.sendKeys("\b");
        price.sendKeys(newProduct.getPrice());
        driver.findElement(By.className("switch-input")).click();
        waitForContentLoad(By.cssSelector("#growls"));
        driver.findElement(By.className("growl-close")).click();
        waitForContentLoad(By.cssSelector("#submit"));
        driver.findElement(By.cssSelector("#submit")).click();
        waitForContentLoad(By.cssSelector("#growls"));
        driver.findElement(By.className("growl-close")).click();
    }

    public void checkProduct(ProductData newProduct) {
        driver.navigate().to(Properties.getBaseUrl());
        waitForContentLoad(By.cssSelector("#wrapper"));
        driver.findElement(By.cssSelector("#content > section > a")).click();
        List<WebElement> listPages = driver.findElement(By.cssSelector(".page-list.clearfix.text-xs-center"))
                .findElements(By.cssSelector("a.js-search-link"));

        WebElement checkProduct = null;

        for (int i = 1; i < listPages.size() - 1; i++) {
            listPages = driver.findElement(By.cssSelector(".page-list.clearfix.text-xs-center"))
                    .findElements(By.cssSelector("a.js-search-link"));
            List<WebElement> elements = driver.findElements(By.cssSelector(".h3.product-title"));

            for (WebElement c : elements) {
                if (c.getText().equals(newProduct.getName())) {
                    checkProduct = c;
                    break;
                }
            }

            if (checkProduct == null) {
                listPages.get(i + 1).click();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        checkProduct.click();
        waitForContentLoad(By.cssSelector(".container"));
    }

    private void waitForContentLoad(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}

package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    private static ProductData productData;

    @DataProvider(name = "login")
    public static Object[][] login() {
        return new Object[][]{
                {"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"},
        };
    }

    @Test(dataProvider = "login")
    public void createNewProduct(String login, String password) {
        actions.login(login, password);
        Assert.assertTrue(driver.findElement(By.className("page-title")).getText().contains("Пульт"));
        productData = ProductData.generate();
        actions.createProduct(productData);
    }

    @Test(dependsOnMethods = "createNewProduct")
    public void checkNewProduct(){
        actions.checkProduct(productData);
        Assert.assertTrue(driver.findElement(By.cssSelector(".h1")).getText().contains(productData.getName().toUpperCase()));
        Assert.assertTrue(driver.findElement(By.cssSelector(".current-price")).getText().contains(productData.getPrice()));
        Assert.assertTrue(driver.findElement(By.cssSelector(".product-quantities")).getText().contains(productData.getQty().toString()));
    }
}

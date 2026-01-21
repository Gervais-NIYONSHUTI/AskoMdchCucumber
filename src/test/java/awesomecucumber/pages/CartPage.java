package awesomecucumber.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage{
    @FindBy(css = "td[class=\"product-name\"]") private WebElement productNameField;
    @FindBy(css = "input[type='number']") private WebElement productQuantityField;
    @FindBy(css = ".checkout-button") private WebElement checkoutBtn;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName(){
        return wait.until(ExpectedConditions.visibilityOf(productNameField)).getText();
    }
    public int getQuantity(){
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOf(productQuantityField)).getAttribute("value"));
    }

    public void checkout(){
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
    }
}

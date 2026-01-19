package steps;

import awesomecucumber.factory.DriverFactory;
import awesomecucumber.pages.CartPage;
import awesomecucumber.pages.CheckoutPage;
import awesomecucumber.pages.StorePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;

public class MyStepDefinition {
    private WebDriver driver;
    private final String websiteUrl = "https://askomdch.com/store";
    private WebDriverWait wait;

    @Given("I'm on the Store Page")
    public void i_m_on_the_store_page() {
        driver = DriverFactory.getDriver();
        new StorePage().load("https://askomdch.com/store");
    }

    @When("I add a {string} to the Cart")
    public void i_add_a_to_the_cart(String productName) {
        new StorePage().addToCart(1, 2);
    }

    @Then("I see {int} {string} in the Cart")
    public void i_see_in_the_cart(int quantity, String productName) {
        CartPage cartPage = new CartPage(driver);
        Assert.assertEquals(productName, cartPage.getProductName());
        Assert.assertEquals(quantity, cartPage.getQuantity());
    }

    @Given("I'm a guest customer")
    public void i_m_a_guest_customer() {
        driver = DriverFactory.getDriver();
        new StorePage().load("https://askomdch.com/store");
    }

    @Given("I'm have a product in the cart")
    public void i_m_have_a_product_in_the_cart() {
        new StorePage().addToCart(1,3);
    }

    @Given("I'm on the Checkout Page")
    public void i_m_on_the_checkout_page() {
        new CartPage(driver).checkout();
    }

    @When("I provide billing details")
    public void i_m_provide_billing_details(List<Map<String, String>> billingDetails) {
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.setBillingDetails(
                billingDetails.getFirst().get("firstname"),
                billingDetails.getFirst().get("lastname"),
                billingDetails.getFirst().get("address_line1"),
                billingDetails.getFirst().get("city"),
                billingDetails.getFirst().get("state"),
                billingDetails.getFirst().get("zip"),
                billingDetails.getFirst().get("email")
        );
    }

    @When("I place an Order")
    public void i_place_an_order() throws InterruptedException {
        new CheckoutPage(driver).placeOrder();
    }

//    @When("I place an Order")
//    public void i_place_an_order() throws InterruptedException {
//        By placeOrderButton = By.id("place_order");
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement element = wait.until(ExpectedConditions.refreshed(
//                ExpectedConditions.elementToBeClickable(placeOrderButton)
//        ));
//        try {
//            element.click();
//        } catch (ElementClickInterceptedException e) {
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//        }
//        Thread.sleep(5000);
//
//    }

    @Then("the order should be placed successfully")
    public void the_order_should_be_placed_successfully() {
        String expectedResult = "Thank you. Your order has been received.";
        Assert.assertEquals(expectedResult, new CheckoutPage(driver).getNotice());
    }
}

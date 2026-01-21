package awesomecucumber.pages;

import awesomecucumber.factory.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class StorePage extends BasePage {

    @FindBy(className = "woocommerce-products-header__title")
    private WebElement storeHeading;
    @FindBy(id = "woocommerce-product-search-field-0")
    private WebElement storeSearchInput;
    @FindBy(css = "li h2")
    private List<WebElement> storeList;
    @FindBy(css = "button[type='submit']")
    private List<WebElement> storeSearchAndFilterButton;
    @FindBy(className = "woocommerce-no-products-found")
    private WebElement invalidSearchResponse;
    @FindBy(className = "ui-slider-handle")
    private List<WebElement> sliderSelector;
    @FindBy(id = "product_cat")
    private WebElement categorySelect;
    @FindBy(className = "orderby")
    private WebElement sortingSelector;
    @FindBy(className = "from")
    private WebElement fromPrice;
    @FindBy(className = "to")
    private WebElement toPrice;
    @FindBy(css = ".astra-shop-summary-wrap a.button")
    private List<WebElement> addToCartSelector;
    @FindBy(className = "added_to_cart")
    private WebElement viewCart;
    @FindBy(className = "ast-loop-product__link")
    private List<WebElement> viewProduct;
    @FindBy(tagName = "bdi")
    private List<WebElement> storeListPrice;
    @FindBy(css = "del bdi")
    private List<WebElement> storeDel;
    @FindBy(className = "woocommerce-loop-product__title")
    private List<WebElement> storeProductNames;
    private String noProductFoundMessage;

    public StorePage() {
        super(DriverFactory.getDriver());
    }

    public void loadStorePage() {
        load("https://askomdch.com/store");
    }

    public String getStoreHeader() {
        return wait.until(ExpectedConditions.visibilityOf(storeHeading)).getText();
    }

    public boolean areProductsDisplayed() {
        return !storeList.isEmpty();
    }

    public boolean search(String searchText) {
        storeSearchInput.clear();
        storeSearchInput.sendKeys(searchText);
        storeSearchAndFilterButton.get(0).click();
        wait.withTimeout(Duration.ofSeconds(5));

        if (!driver.findElements(By.className("product_title")).isEmpty()) {
            return true;
        }

        if (!storeList.isEmpty()) {
            return storeList.stream()
                    .anyMatch(val -> val.getText().toLowerCase().contains(searchText.toLowerCase()));
        } else {
            noProductFoundMessage = wait.until(ExpectedConditions.visibilityOf(invalidSearchResponse)).getText();
            return false;
        }
    }

    public String getNoProductFoundMessage() {
        return noProductFoundMessage;
    }

    public boolean filterByPrice(int startingPrice, int endingPrice) {
        while (Integer.parseInt(fromPrice.getText().replace("$", "")) < startingPrice) {
            sliderSelector.get(0).sendKeys(Keys.ARROW_RIGHT);
        }

        while (Integer.parseInt(toPrice.getText().replace("$", "")) > endingPrice) {
            sliderSelector.get(1).sendKeys(Keys.ARROW_LEFT);
        }

        storeSearchAndFilterButton.get(1).click();
        wait.withTimeout(Duration.ofSeconds(5));

        List<WebElement> currentPrices = storeListPrice.stream()
                .filter(val -> !storeDel.contains(val))
                .toList();

        return currentPrices.stream()
                .anyMatch(val -> {
                    try {
                        double price = Double.parseDouble(val.getText().replace("$", ""));
                        return price >= startingPrice && price <= endingPrice;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
    }

    public void filterByCategory(String categoryName) {
        Select category = new Select(categorySelect);
        categorySelect.sendKeys(Keys.ENTER);
        category.selectByContainsVisibleText(categoryName);
        wait.withTimeout(Duration.ofSeconds(3));
    }

    public boolean isCategoryFiltered(String categoryName) {
        String header = getStoreHeader();
        return header.toLowerCase().contains(categoryName.toLowerCase());
    }

    public void sorting(String selectorName) {
        Select sortSelect = new Select(sortingSelector);
        sortingSelector.sendKeys(Keys.ENTER);
        sortSelect.selectByVisibleText(selectorName);
        wait.withTimeout(Duration.ofSeconds(3));
    }

    public boolean isSortedByPriceAscending() {
        List<WebElement> currentPrices = storeListPrice.stream()
                .filter(val -> !storeDel.contains(val))
                .toList();

        List<Double> prices = currentPrices.stream()
                .map(val -> {
                    try {
                        return Double.parseDouble(val.getText().replace("$", ""));
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .toList();

        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public CartPage addToCart(int index, int frequency) {
        for (int i = 0; i < frequency; i++) {
            WebElement addButton = addToCartSelector.get(index);
            addButton.click();
            wait.until(ExpectedConditions.attributeContains(addButton, "class", "added"));
        }

        wait.until(ExpectedConditions.visibilityOf(viewCart));
        viewCart.click();
        return new CartPage(driver);
    }

    public ProductDetailPage clickOnAProduct(int index) {
        String product = viewProduct.get(index).getText();
        viewProduct.get(index).click();
        return new ProductDetailPage(product);
    }

    public int getProductIndexByName(String productName) {
        for (int i = 0; i < storeProductNames.size(); i++) {
            if (storeProductNames.get(i).getText().equalsIgnoreCase(productName)) {
                return i;
            }
        }
        return -1;
    }
}
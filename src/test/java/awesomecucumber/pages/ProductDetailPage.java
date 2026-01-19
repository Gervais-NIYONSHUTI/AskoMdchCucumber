package awesomecucumber.pages;

import awesomecucumber.factory.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetailPage extends BasePage {

    @FindBy(className = "product_title")
    private WebElement productTitle;
    @FindBy(className = "price")
    private WebElement productPrice;
    @FindBy(css = ".woocommerce-product-details__short-description")
    private WebElement productDescription;
    @FindBy(name = "quantity")
    private WebElement quantityField;
    @FindBy(name = "add-to-cart")
    private WebElement addToCartButton;
    @FindBy(className = "woocommerce-message")
    private WebElement successMessage;
    @FindBy(className = "additional_information_tab")
    private WebElement additionalInformationTab;
    @FindBy(className = "reviews_tab")
    private WebElement reviewsTab;
    @FindBy(className = "description_tab")
    private WebElement descriptionTab;
    @FindBy(name = "comment")
    private WebElement commentField;
    @FindBy(name = "author")
    private WebElement authorField;
    @FindBy(name = "email")
    private WebElement emailField;
    @FindBy(id = "wp-comment-cookies-consent")
    private WebElement saveInfoCheckbox;
    @FindBy(id = "submit")
    private WebElement submitButton;
    @FindBy(css = ".description p")
    private WebElement commentDescription;
    @FindBy(css = ".wp-die-message p")
    private WebElement errorMessage;

    private final String expectedProductName;

    public ProductDetailPage(String productName) {
        super(DriverFactory.getDriver());
        this.expectedProductName = productName;
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public boolean productNameCheck() {
        return wait.until(ExpectedConditions.visibilityOf(productTitle)).getText().equals(expectedProductName);
    }

    public boolean isProductTitleDisplayed() {
        return productTitle.isDisplayed();
    }

    public boolean isProductPriceDisplayed() {
        return productPrice.isDisplayed();
    }

    public boolean isDescriptionDisplayed() {
        return productDescription.isDisplayed();
    }

    public boolean isQuantityFieldDisplayed() {
        return quantityField.isDisplayed();
    }

    public boolean isAddToCartButtonDisplayed() {
        return addToCartButton.isDisplayed();
    }

    public boolean addSelectedProductToCart(int quantity) {
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        addToCartButton.click();

        String returnMessage = wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
        returnMessage = returnMessage.substring(returnMessage.indexOf("T") + 2);

        if (quantity > 1) {
            return returnMessage.contains("“" + expectedProductName + "” have been added to your cart.");
        } else {
            return returnMessage.contains("“" + expectedProductName + "” has been added to your cart.");
        }
    }

    public boolean additionalInformation() {
        additionalInformationTab.click();
        return additionalInformationTab.getAttribute("class").contains("active");
    }

    public boolean review() {
        reviewsTab.click();
        return reviewsTab.getAttribute("class").contains("active");
    }

    public boolean description() {
        descriptionTab.click();
        return descriptionTab.getAttribute("class").contains("active");
    }

    public boolean clickTab(String tabName) {
        switch (tabName.toLowerCase()) {
            case "description":
                return description();
            case "additional information":
                return additionalInformation();
            case "reviews":
                return review();
            default:
                throw new IllegalArgumentException("Unknown tab: " + tabName);
        }
    }

    public void reviewComment(String comment) {
        commentField.clear();
        commentField.sendKeys(comment);
    }

    public void name(String authorName) {
        if (!driver.findElements(By.name("author")).isEmpty()) {
            authorField.clear();
            authorField.sendKeys(authorName);
        }
    }

    public void email(String email) {
        if (!driver.findElements(By.name("email")).isEmpty()) {
            emailField.clear();
            emailField.sendKeys(email);
        }
    }

    public void starRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            driver.findElement(By.className("star-" + rating)).click();
            wait.withTimeout(Duration.ofSeconds(10)).until(ExpectedConditions.attributeContains(By.className("stars"), "class", "selected"));
        }
    }

    public void saveNameAndEmail() {
        if (saveInfoCheckbox.isDisplayed() && !saveInfoCheckbox.isSelected()) {
            saveInfoCheckbox.click();
        }
    }

    public void setSaveInfo(boolean shouldSave) {
        if (saveInfoCheckbox.isDisplayed()) {
            if (shouldSave && !saveInfoCheckbox.isSelected()) {
                saveInfoCheckbox.click();
            } else if (!shouldSave && saveInfoCheckbox.isSelected()) {
                saveInfoCheckbox.click();
            }
        }
    }

    public String submitComment() {
        submitButton.click();
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            customWait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (TimeoutException e0) {
            try {
                customWait.until(ExpectedConditions.visibilityOf(commentDescription));
                return commentDescription.getText();
            } catch (TimeoutException e) {
                try {
                    customWait.until(ExpectedConditions.visibilityOf(errorMessage));
                    return errorMessage.getText();
                } catch (TimeoutException e2) {
                    return "Timeout waiting for review submission response";
                }
            }
        }
    }

    public String submitReviewWithDetails(int rating, String comment, String author, String email, boolean saveInfo) {
        review();
        if (rating > 0) {
            starRating(rating);
        }
        if (comment != null && !comment.isEmpty()) {
            reviewComment(comment);
        }
        if (author != null && !author.isEmpty()) {
            name(author);
        }
        if (email != null && !email.isEmpty()) {
            email(email);
        }
        if (saveInfo) {
            saveNameAndEmail();
        }
        return submitComment();
    }

    public boolean containsErrorMessage(String expectedError) {
        try {
            String errorText = errorMessage.getText();
            return errorText.toLowerCase().contains(expectedError.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean containsSuccessMessage(String expectedSuccess) {
        try {
            String successText = successMessage.getText();
            return successText.toLowerCase().contains(expectedSuccess.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }
}
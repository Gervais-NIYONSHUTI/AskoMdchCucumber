package steps;

import awesomecucumber.pages.ProductDetailPage;
import awesomecucumber.pages.StorePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class ProductDetailStepDefinitions {

    private ProductDetailPage productDetailPage;
    private StorePage storePage;
    private String submissionResult;

    @Given("I am viewing a product details page")
    public void i_am_viewing_a_product_details_page() {
        storePage = new StorePage();
        storePage.loadStorePage();
        productDetailPage = storePage.clickOnAProduct(0);
        Assert.assertTrue(productDetailPage.productNameCheck());
    }

    @Then("I should see the product title")
    public void i_should_see_the_product_title() {
        Assert.assertTrue(productDetailPage.isProductTitleDisplayed());
    }

    @Then("I should see the product price")
    public void i_should_see_the_product_price() {
        Assert.assertTrue(productDetailPage.isProductPriceDisplayed());
    }

    @Then("I should see product description")
    public void i_should_see_product_description() {
        Assert.assertTrue(productDetailPage.isDescriptionDisplayed());
    }

    @Then("I should see quantity selector")
    public void i_should_see_quantity_selector() {
        Assert.assertTrue(productDetailPage.isQuantityFieldDisplayed());
    }

    @Then("I should see add to cart button")
    public void i_should_see_add_to_cart_button() {
        Assert.assertTrue(productDetailPage.isAddToCartButtonDisplayed());
    }

    @When("I add the following quantities to cart:")
    public void i_add_the_following_quantities_to_cart(List<String> quantities) {
        for (String quantityStr : quantities) {
            int quantity = Integer.parseInt(quantityStr);
            boolean addToCartResult = productDetailPage.addSelectedProductToCart(quantity);
            Assert.assertTrue(addToCartResult);
        }
    }

    @When("I click on the following tabs:")
    public void i_click_on_the_following_tabs(List<String> tabs) {
        for (String tabName : tabs) {
            boolean tabResult = productDetailPage.clickTab(tabName);
            Assert.assertTrue(tabResult);
        }
    }

    @When("I submit reviews with the following details:")
    public void i_submit_reviews_with_the_following_details(DataTable dataTable) {
        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : reviews) {
            int rating = Integer.parseInt(row.get("rating"));
            String comment = row.get("comment");
            String name = row.get("name");
            String email = row.get("email");

            submissionResult = productDetailPage.submitReviewWithDetails(rating, comment, name, email, false);
            Assert.assertNotEquals(submissionResult, "Timeout waiting for review submission response");
        }
    }

    @When("I submit a review with save info:")
    public void i_submit_a_review_with_save_info(DataTable dataTable) {
        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : reviews) {
            int rating = Integer.parseInt(row.get("rating"));
            String comment = row.get("comment");
            String name = row.get("name");
            String email = row.get("email");
            boolean saveInfo = Boolean.parseBoolean(row.get("save_info"));

            submissionResult = productDetailPage.submitReviewWithDetails(rating, comment, name, email, saveInfo);
            Assert.assertNotEquals(submissionResult, "Timeout waiting for review submission response");
        }
    }

    @When("I submit invalid reviews:")
    public void i_submit_invalid_reviews(DataTable dataTable) {
        List<Map<String, String>> invalidReviews = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : invalidReviews) {
            String ratingStr = row.get("rating");
            int rating = ratingStr.isEmpty() ? 0 : Integer.parseInt(ratingStr);
            String comment = row.get("comment");
            String name = row.get("name");
            String email = row.get("email");
            String expectedError = row.get("expected_error");

            submissionResult = productDetailPage.submitReviewWithDetails(rating, comment, name, email, false);
            boolean containsErrorResult = productDetailPage.containsErrorMessage(expectedError);
            Assert.assertTrue(submissionResult.toLowerCase().contains(expectedError.toLowerCase()) || containsErrorResult);
        }
    }

    @When("I submit a review with rating {int} and comment {string}")
    public void i_submit_a_review_with_rating_and_comment(int rating, String comment) {
        submissionResult = productDetailPage.submitReviewWithDetails(rating, comment, "Test User", "test@example.com", false);
    }

    @Then("I should see review submitted successfully")
    public void i_should_see_review_submitted_successfully() {
        Assert.assertNotEquals(submissionResult, "Timeout waiting for review submission response");
        boolean containsSuccessResult = productDetailPage.containsSuccessMessage("success");
        Assert.assertTrue(!submissionResult.toLowerCase().contains("error") || containsSuccessResult);
    }

    @Then("I should see {string} error")
    public void i_should_see_error(String expectedError) {
        boolean containsErrorResult = productDetailPage.containsErrorMessage(expectedError);
        Assert.assertTrue(submissionResult.toLowerCase().contains(expectedError.toLowerCase()) || containsErrorResult);
    }
}
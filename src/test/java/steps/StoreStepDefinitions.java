package steps;

import awesomecucumber.pages.CartPage;
import awesomecucumber.pages.ProductDetailPage;
import awesomecucumber.pages.StorePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class StoreStepDefinitions {
    private StorePage storePage;
    private CartPage cartPage;
    private ProductDetailPage productDetailPage;
    private boolean searchResult;
    private boolean priceRangeResult;
    private String priceRangeResultMessage;
    private boolean filterCategoryResult;
    private String filterCategoryMessage;
    private boolean sortCategoryResult;
    private boolean isCorrectPage;

    @Given("I am on the store page")
    public void i_am_on_the_store_page() {
        storePage = new StorePage();
        storePage.loadStorePage();
    }

    @Then("I should see the store page header")
    public void i_should_see_the_store_page_header() {
        String header = storePage.getStoreHeader();
        Assert.assertNotNull(header);
        Assert.assertFalse(header.isEmpty());
    }

    @Then("I should see products displayed")
    public void i_should_see_products_displayed() {
        Assert.assertTrue(storePage.areProductsDisplayed());
    }

    @When("I search for the following products:")
    public void i_search_for_the_following_products(List<String> searchData) {
        for (String searchTerm : searchData) {
            searchResult = storePage.search(searchTerm);
            Assert.assertTrue(searchResult);
        }
    }

    @When("I search for invalid products:")
    public void i_search_for_invalid_products(List<String> searchData) {
        for (String searchTerm : searchData) {
            searchResult = storePage.search(searchTerm);
            Assert.assertFalse(searchResult);
        }
    }

    @Then("I should see corresponding products in search results")
    public void i_should_see_corresponding_products_in_search_results() {
        Assert.assertTrue(searchResult);
    }

    @Then("I should see {string} message")
    public void i_should_see_message(String expectedMessage) {
        String actualMessage = storePage.getNoProductFoundMessage();
        Assert.assertNotNull(actualMessage);
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

    @When("I filter products by the following price ranges:")
    public void i_filter_products_by_the_following_price_ranges(DataTable dataTable) {
        List<Map<String, String>> priceRanges = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : priceRanges) {
            int minPrice = Integer.parseInt(row.get("min_price"));
            int maxPrice = Integer.parseInt(row.get("max_price"));
            priceRangeResult = storePage.filterByPrice(minPrice, maxPrice);
            priceRangeResultMessage = "Should find products in price range " + minPrice + " to " + maxPrice;
            Assert.assertTrue(priceRangeResult, priceRangeResultMessage);
            storePage.loadStorePage();
        }
    }

    @When("I filter by the following categories:")
    public void i_filter_by_the_following_categories(DataTable dataTable) {
        List<Map<String, String>> categories = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : categories) {
            String categoryName = row.get("category");
            storePage.filterByCategory(categoryName);
            filterCategoryResult = storePage.isCategoryFiltered(categoryName);
            filterCategoryMessage = "Should see only " + categoryName + " products";
            Assert.assertTrue(filterCategoryResult, filterCategoryMessage);
            storePage.loadStorePage();
        }
    }

    @When("I sort products using these options:")
    public void i_sort_products_using_these_options(DataTable dataTable) {
        List<Map<String, String>> sortOptions = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : sortOptions) {
            String sortOption = row.get("sort_option");
            storePage.sorting(sortOption);
            if (sortOption.equals("Price: low to high")) {
                sortCategoryResult = storePage.isSortedByPriceAscending();
                Assert.assertTrue(sortCategoryResult);
            }
            storePage.loadStorePage();
        }
    }

    @When("I add the following products to cart:")
    public void i_add_the_following_products_to_cart(DataTable dataTable) {
        List<Map<String, String>> products = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : products) {
            String productName = row.get("product_name");
            int quantity = Integer.parseInt(row.get("quantity"));
            int productIndex = storePage.getProductIndexByName(productName);
            Assert.assertTrue(productIndex >= 0);
            cartPage = storePage.addToCart(productIndex, quantity);
            storePage.loadStorePage();
        }
    }

    @When("I click on the following products:")
    public void i_click_on_the_following_products(DataTable dataTable) {
        List<Map<String, String>> products = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : products) {
            int productIndex = Integer.parseInt(row.get("product_index"));
            productDetailPage = storePage.clickOnAProduct(productIndex);
            Assert.assertNotNull(productDetailPage);
            isCorrectPage = productDetailPage.productNameCheck();
            Assert.assertTrue(isCorrectPage);
            productDetailPage.navigateBack();
        }
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        searchResult = storePage.search(searchTerm);
    }

    @Then("I should see products containing {string}")
    public void i_should_see_products_containing(String expectedProduct) {
        Assert.assertTrue(searchResult);
    }
}
Feature: Store Page Product Browsing and Cart Operations
  As a customer
  I want to browse, search, and add products in the store
  So that I can find and purchase items I want

  Background:
    Given I am on the store page

  @store @smoke
  Scenario: Verify store page loads correctly
    Then I should see the store page header
    And I should see products displayed

  @search
  Scenario: Search for different products
    When I search for the following products:
      | Shoes    |
      | Jeans    |
      | Bracelet |
    Then I should see corresponding products in search results

  @search
  Scenario: Search returns no results for invalid products
    When I search for invalid products:
      | XYZ123            |
      | InvalidProduct999 |
      | Test123456        |
    Then I should see "No products were found matching your selection" message

  @filter
  Scenario: Filter products by different price ranges
    When I filter products by the following price ranges:
      | min_price | max_price |
      | 10        | 50        |
      | 50        | 100       |
      | 100       | 150       |

  @filter
  Scenario: Filter products by different categories
    When I filter by the following categories:
      | category    |
      | Accessories |
      | Men         |
      | Women       |

  @sort
  Scenario: Sort products using different options
    When I sort products using these options:
      | sort_option            |
      | Default sorting        |
      | Sort by popularity     |
      | Sort by average rating |
      | Sort by latest         |
      | Price: low to high     |
      | Price: high to low     |

  @cart @smoke
  Scenario: Add different products to cart
    When I add the following products to cart:
      | product_name     | quantity |
      | Blue Shoes       | 1        |
      | Basic Blue Jeans | 2        |
      | Anchor Bracelet  | 3        |

  @product
  Scenario: View details of different products
    When I click on the following products:
      | product_index |
      | 0             |
      | 1             |
      | 2             |
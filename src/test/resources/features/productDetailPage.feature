Feature: Product Details Page
  As a customer
  I want to view product details and submit reviews
  So that I can make informed purchase decisions

  Background:
    Given I am viewing a product details page

  @product @smoke
  Scenario: Verify product details page elements
    Then I should see the product title
    And I should see the product price
    And I should see product description
    And I should see quantity selector
    And I should see add to cart button

  @cart
  Scenario: Add different quantities of product to cart
    When I add the following quantities to cart:
      | 1 |
      | 2 |
      | 5 |

  @tabs
  Scenario: Navigate through product page tabs
    When I click on the following tabs:
      | Description            |
      | Additional Information |
      | Reviews                |

  @review
  Scenario: Submit product reviews with different ratings
    When I submit reviews with the following details:
      | rating | comment                              | name        | email             |
      | 5      | Excellent product! Highly recommend. | John Doe    | john@example.com  |
      | 4      | Good quality, fast delivery.         | Jane Smith  | jane@example.com  |
      | 3      | Average product, meets expectations. | Bob Johnson | bob@example.com   |
      | 2      | Could be better.                     | Alice Brown | alice@example.com |
      | 1      | Poor quality, not recommended.       | Tom Wilson  | tom@example.com   |

  @review
  Scenario: Submit review with save info option
    When I submit a review with save info:
      | rating | comment         | name         | email               | save_info |
      | 5      | Will buy again! | Regular User | regular@example.com | true      |
      | 4      | Good value      | Test User    | test@example.com    | false     |

  @review
  Scenario: Submit invalid reviews
    When I submit invalid reviews:
      | rating | comment | name | email            | expected_error          |
      |        | Good    | John | john@example.com | Rating is required      |
      | 3      |         | Jane | jane@example.com | Comment is required     |
      | 4      | Test    |      | test@example.com | Name is required        |
      | 5      | Great   | Bob  | invalid-email    | Valid email is required |

  @review @single
  Scenario: Submit a single review
    When I submit a review with rating 5 and comment "Excellent product!"
    Then I should see review submitted successfully

  @review @error
  Scenario: Submit review with missing rating
    When I submit a review with rating 0 and comment "Missing rating test"
    Then I should see "Rating is required" error
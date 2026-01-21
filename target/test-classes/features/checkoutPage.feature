Feature: Place an Order

  Scenario: Using default Payment option
    Given I'm a guest customer
    And I'm have a product in the cart
    And I'm on the Checkout Page
    When I provide billing details
      | firstname  | lastname | country | address_line1     | city  | state | zip   | email            |
      | NIYONSHUTI | Tester   | US      | 6300 Spring Creek | Plano | Texas | 74243 | kabaye@gmail.com |
    And I place an Order
    Then the order should be placed successfully
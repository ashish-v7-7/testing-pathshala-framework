Feature: Shopping cart
  As a logged-in customer
  I want to add and remove products
  So that my cart holds exactly what I want to buy

  Background:
    Given I am logged in as "standard_user"

  @smoke @regression
  Scenario: Add one product to the cart
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1

  @regression
  Scenario: Add two products and see them in the cart
    When I add "Sauce Labs Backpack" to the cart
    And I add "Sauce Labs Bike Light" to the cart
    And I open the cart
    Then the cart should contain 2 items

  @regression
  Scenario: Remove a product from the cart
    When I add "Sauce Labs Bolt T-Shirt" to the cart
    And I remove "Sauce Labs Bolt T-Shirt" from the cart
    Then the cart badge should show 0

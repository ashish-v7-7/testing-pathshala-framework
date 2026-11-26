Feature: Checkout
  As a logged-in customer
  I want to check out my cart
  So that my order is placed

  Background:
    Given I am logged in as "standard_user"
    And I add "Sauce Labs Backpack" to the cart
    And I open the cart

  @smoke @regression
  Scenario: Complete a checkout
    When I check out as "Priya" "Sharma" with postal code "110001"
    Then I should see the order confirmation "Thank you for your order!"

  @regression
  Scenario: Postal code is required
    When I check out as "Priya" "Sharma" with postal code ""
    Then I should see the checkout error "Error: Postal Code is required"

Feature: Login
  As a SauceDemo customer
  I want to log in with my username and password
  So that I can shop for products

  @smoke @regression
  Scenario: Standard user logs in successfully
    Given I am on the login page
    When I log in as "standard_user" with password "secret_sauce"
    Then I should see the products page

  @regression
  Scenario Outline: Login result for user "<username>"
    Given I am on the login page
    When I log in as "<username>" with password "<password>"
    Then I should see the message "<message>"

    Examples:
      | username        | password     | message                                                                   |
      | standard_user   | secret_sauce | Products                                                                  |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                       |
      | wrong_user      | wrong_pass   | Epic sadface: Username and password do not match any user in this service |

  @failing-demo
  Scenario: Intentional failure - wrong page title expected
    Given I am on the login page
    When I log in as "standard_user" with password "secret_sauce"
    Then the page title should be "Welcome to Swag Store"

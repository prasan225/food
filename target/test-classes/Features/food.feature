Feature: Online Food Delivery Management System API Testing
  As a user
  I want to interact with the Online Food Delivery Management System
  So that I can perform CRUD operations on users, restaurants, orders, and foods

  Background:
    Given the server is running

  Scenario: User Registration
    When I send a POST request to "/users/register" with the following body:
      | username | password | email              |
      | user6    | pass6    | user6@example.com  |
    Then the response status should be 201
    And the response should contain the user's details

  Scenario: User Login
    When I send a POST request to "/users/login" with the following body:
      | username | password |
      | user6    | pass6    |
    Then the response status should be 200
    And the response should contain the message "Login successful"

  Scenario: Get All Users
    When I send a GET request to "/users"
    Then the response status should be 200
    And the response should contain a list of users

  Scenario: Create a Restaurant
    When I send a POST request to "/restaurants" with the following body:
      | name         | address     | cuisineType | contactInfo |
      | Restaurant6  | Address6    | Cuisine6    | Contact6    |
    Then the response status should be 201
    And the response should contain the restaurant details

  Scenario: Search Food by Name
    When I send a GET request to "/foods/search?name=Food1"
    Then the response status should be 200
    And the response should contain food details with name "Food1"

  Scenario: Filter Food by Cuisine
    When I send a GET request to "/foods/filter?cuisine=Cuisine2"
    Then the response status should be 200
    And the response should contain food details with cuisineType "Cuisine2"


  Scenario: Place a new order
    Given I send a POST request to "/orders" with the order details
      | userId  | restaurantId | foodItems    | totalPrice |
      | 20      | 11          | Dosa, Idly   | 90.0       |
    Then the response status should be 201
    Then I should receive a JSON response with the new order details


  Scenario: Get an order by ID
    Given the server is running
    When I send a GET request to "/orders" with order ID 5
    Then the response status should be 200
    And the response should contain the order with userId 5, restaurantId 5, totalPrice 60.0
    And the response should include the food items
      | Food9 |
      | Food10|
      
      
   Scenario: Get restaurant details by ID
    When I send a GET request to "/restaurants/11"
    Then the response status should be 200
    And the response should contain the restaurant details with ID 11
    
    
    Scenario: Successfully update user profile
    When I send a PUT request to "/users/18" with the username "newUsername" and email "newEmail@example.com"
    Then the response status should be 200
    And the response should contain updated username "newUsername" and email "newEmail@example.com"

  Scenario: Delete user account
    When I send a DELETE request to "/users/23"
    Then the response status should be 204
    And the user account should no longer exist
package stepdefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;



public class foodstep {
	
	private Response response;
    private static final String BASE_URL = "http://localhost:3000";


	@Given("the server is running")
    public void the_server_i_running() {
        RestAssured.baseURI = BASE_URL;
    }
	
	@When("I send a POST request to {string} with the following body:")
    public void i_send_a_post_request_to_with_the_following_body(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        // Convert DataTable to a list of maps
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        // Loop through each row of data and send the request
        for (Map<String, String> row : data) {
            response = given()
                        .contentType(ContentType.JSON)
                        .body(row)  // Use each row as the body
                        .when()
                        .post(endpoint);
        }
    }
	
	
	@Then("the response status should be {int}")
    public void the_response_status_should_be(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }
	
    @Then("the response should contain the user's details")
    public void the_response_should_contain_the_user_s_details() {
        assertNotNull(response.getBody().jsonPath().getString("username"));
        assertNotNull(response.getBody().jsonPath().getString("email"));
    }

    @Then("the response should contain the message {string}")
    public void the_response_should_contain_the_message(String message) {
        assertEquals(message, response.getBody().jsonPath().getString("message"));
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = given().when().get(endpoint);
    }

    
    @Then("the response should contain a list of users")
    public void the_response_should_contain_a_list_of_users() {
        assertTrue(response.getBody().jsonPath().getList("$").size() > 0);
    }

    @Then("the response should contain the restaurant details")
    public void the_response_should_contain_the_restaurant_details() {
        assertNotNull(response.getBody().jsonPath().getString("name"));
        assertNotNull(response.getBody().jsonPath().getString("cuisineType"));
    }
    
    @Then("the response should contain food details with name {string}")
    public void the_response_should_contain_food_details_with_name(String expectedFoodName) {
        // Extract the list of food items from the response body
        List<String> foodNames = response.jsonPath().getList("name");

        // Assert that the expected food name exists in the list of food names
        assertTrue("The expected food name was not found!", foodNames.contains(expectedFoodName));
    }
    
   

    @Then("the response should contain food details with cuisineType {string}")
    public void the_response_should_contain_food_details_with_cuisine_type(String expectedCuisineType) {
        // Write code here that turns the phrase above into concrete actions
    	List<String> foodNames = response.jsonPath().getList("cuisineType");

    	assertTrue("The expected food name was not found!", foodNames.contains(expectedCuisineType));
    }


    @Given("I send a POST request to {string} with the order details")
    public void i_send_a_post_request_to_with_the_order_details(String url, DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        String requestBody = "{\"userId\":" + rows.get(1).get(0) + ",\"restaurantId\":" + rows.get(1).get(1) + ",\"foodItems\":[\"" + rows.get(1).get(2).replace(", ", "\", \"") + "\"],\"totalPrice\":" + rows.get(1).get(3) + "}";
//        response = RestAssured.post(url).body(requestBody.toString());
        
//        List<List<String>> rows = dataTable.asLists(String.class);
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("userId", rows.get(0).get(0));
//        requestBody.put("restaurantId", rows.get(0).get(1));
//        requestBody.put("foodItems", rows.get(0).get(2).split(","));
//        requestBody.put("totalPrice", rows.get(0).get(3));
        System.out.println(requestBody);

        response = RestAssured.given().body(requestBody.toString()).post(url);
    }
    

    @Then("I should receive a JSON response with the new order details")
    public void i_should_receive_a_json_response_with_the_new_order_details() {
    	System.out.println(response.getBody().prettyPrint());
        assertNotNull(response.getBody().jsonPath().getString("id"));
    }
    
    
    

    @When("I send a GET request to {string} with order ID {int}")
    public void i_send_a_get_request_to_with_order_id(String endpoint, int orderId) {
        response = given()
                .when()
                .get(endpoint + "/" + orderId);
    }

  

    @Then("the response should contain the order with userId {int}, restaurantId {int}, totalPrice {double}")
    public void the_response_should_contain_the_order(int userId, int restaurantId, double totalPrice) {
        assertEquals(userId, (int) response.jsonPath().getInt("userId"));
        assertEquals(restaurantId, (int) response.jsonPath().getInt("restaurantId"));
        assertEquals(totalPrice, response.jsonPath().getDouble("totalPrice"), 0.01);
    }

    @Then("the response should include the food items")
    public void the_response_should_include_the_food_items(io.cucumber.datatable.DataTable expectedFoodItemsTable) {
        List<String> expectedFoodItems = expectedFoodItemsTable.asList(String.class);
        List<String> actualFoodItems = response.jsonPath().getList("foodItems");

        assertEquals(expectedFoodItems, actualFoodItems);
    }


    @Then("the response should contain the restaurant details with ID {int}")
    public void the_response_should_contain_the_restaurant_details_with_id(int restaurantId) {
        assertEquals(restaurantId, response.jsonPath().getInt("id"));
    }

    @When("I send a PUT request to {string} with the username {string} and email {string}")
    public void i_send_a_put_request_to_with_the_username_and_email(String endpoint, String username, String email) {
        String requestBody = "{ \"username\": \"" + username + "\", \"email\": \"" + email + "\" }";
        
        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(endpoint);
    }

   

    @Then("the response should contain updated username {string} and email {string}")
    public void the_response_should_contain_updated_username_and_email(String expectedUsername, String expectedEmail) {
        assertEquals(expectedUsername, response.jsonPath().getString("username"));
        assertEquals(expectedEmail, response.jsonPath().getString("email"));
    }

  

    // DELETE request to delete user account
    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        response = when().delete(endpoint);
    }

    @Then("the user account should no longer exist")
    public void the_user_account_should_no_longer_exist() {
        // Assuming 204 No Content means the account was successfully deleted.
        assertEquals(204, response.getStatusCode());
    }


    
    
}
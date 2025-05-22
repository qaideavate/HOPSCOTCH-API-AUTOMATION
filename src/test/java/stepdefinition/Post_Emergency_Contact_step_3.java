package stepdefinition;

import Utils.ConfigReader;
import Utils.GlobalTokenStore;
import com.github.javafaker.Faker;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Post_Emergency_Contact_step_3 {

    String baseURL;
    String parentToken;
    int parentId;
    int childId;
    JSONObject payload;
    Response response;

    Faker faker = new Faker();

    @Given("The base URL is {string}")
    public void the_base_url_is(String baseURL)
    {
        this.baseURL = ConfigReader.getProperty("baseURL");
    }

    @Given("I have a valid Parent token")
    public void i_have_a_valid_parent_token() 
    {
        this.parentToken = GlobalTokenStore.getToken("Parent");
    }

    @Given("I have a valid Child ID")
    public void i_have_a_valid_child_id() 
    {
    	 Post_Child_Information_Step1 childRegistration = new Post_Child_Information_Step1();

    	    // Execute the registration flow to ensure a child is created
    	    childRegistration
    	        .i_have_a_valid_parent_token()
    	        .the_base_url()
    	        .i_prepare_the_child_registration_payload_with_valid_data()
    	        .i_send_a_post_request_to()
    	        .the_child_registration_response_should_store_child_id();

    	    // Now fetch the registered childId from GlobalTokenStore
    	    this.childId = Integer.parseInt(GlobalTokenStore.getChildId());
    }
    
    @Given("I have a valid Parent ID")
    public void i_have_a_valid_parent_id() 
    {
       
    }

   

    @Given("I prepare the emergency contact request payload with valid data")
    public void i_prepare_the_emergency_contact_request_payload_with_valid_data() {
        JSONObject contact = new JSONObject();
        contact.put("lastName", faker.name().lastName());
        contact.put("firstName", faker.name().firstName());
        contact.put("email", faker.internet().emailAddress());
        contact.put("cellPhone", "1111111111");
        contact.put("streetAddress", faker.address().streetAddress());
        contact.put("apt", "");
        contact.put("city", faker.address().city());
        contact.put("zipCode", faker.address().zipCode());
        contact.put("sameAddressAsChild", true);
        contact.put("relationship", "mother");
        contact.put("parentId", parentId);
        contact.put("middleName", "");
        contact.put("workPhone", "");
        contact.put("altPhone", "");
        contact.put("contactId", 0);

        JSONArray contactsArray = new JSONArray();
        contactsArray.put(contact);

        payload = new JSONObject();
        payload.put("contacts", contactsArray);
        payload.put("childId", childId);
    }

    @When("I send a POST request to endpoint {string}")
    public void i_send_a_post_request_to_endpoint(String endpoint) {
        response = RestAssured
                .given()
                .baseUri(baseURL)
                .basePath(endpoint)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + parentToken)
                .body(payload.toString())
                .post();
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals("Unexpected status code", expectedStatusCode, response.getStatusCode());
    }

    @Then("The response message should be {string}")
    public void the_response_message_should_be(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals("Unexpected response message", expectedMessage, actualMessage);
    }

    @Then("The success field should be true")
    public void the_success_field_should_be_true() {
        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue("Expected success to be true", success);
    }

    @Then("The response should contain a non-empty list of contactIds")
    public void the_response_should_contain_a_non_empty_list_of_contact_ids() {
        List<Integer> contactIds = response.jsonPath().getList("contactIds.contactIds");
        Assert.assertNotNull("contactIds list is null", contactIds);
        Assert.assertFalse("contactIds list is empty", contactIds.isEmpty());
        Assert.assertTrue("contactIds should contain positive integers", contactIds.get(0) > 0);
    }
}

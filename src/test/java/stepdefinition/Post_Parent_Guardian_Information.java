package stepdefinition;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Faker;

import Utils.APIUtils;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import io.cucumber.java.en.*;
import io.restassured.config.Config;
import io.restassured.response.Response;

public class Post_Parent_Guardian_Information 
{
	private String ParentToken;
	private int ChildId;
	private Map<String, String> headers;
	private String requestBody;
	private Response res;
	private ExtentTest test;
	
	/*
	 * @Given("I have a valid parent token") public void
	 * i_have_a_valid_parent_token() { ParentToken =
	 * Utils.GlobalTokenStore.getToken("parent"); }
	 */

	@Given("I have a valid child ID")
	public void i_have_a_valid_child_id(int childid)
	{
		Get_children_List_Stepdefinition GCLS = new Get_children_List_Stepdefinition();
		childid = GCLS.i_send_a_get_request_of_children_with_enrollments_API_with_the_parent_token();
		this.ChildId = childid;
		Assert.assertTrue("Child ID should be greater than 0", ChildId > 0);
	}

	@Given("I prepare the parent registration body with all valid fields")
	public void i_prepare_the_parent_registration_body_with_all_valid_fields(String body) 
	{
		  this.requestBody = body.replace("{{childID}}", String.valueOf(ChildId));
	}

	@When("I send a POST request to parent guardian endpoint {string}")
	public void i_send_a_post_request_to_parent_guardian_endpoint(String endpoint) 
	{
	   this.headers = ConfigReader.getHeadersFromConfig("header"); 
	   this.test = Extent_Report_Manager.getTest();
       String baseURL = ConfigReader.getProperty("baseURL");
       String contentType = headers.getOrDefault("Content-Type", "application/json");
       this.headers.put("Accept-Encoding", "gzip, deflate");
       endpoint = ConfigReader.getProperty("ParentRegisterEndpoint");
       
	   APIUtils.logRequestHeaders(test, headers);
       APIUtils.logRequestBody(test, requestBody);

        	res = given()
               .baseUri(baseURL)
               .headers(headers)
               .header("Authorization", "Bearer " + ParentToken)
               .contentType(contentType)
               .body(requestBody)
               .when()
               .post(endpoint)
               .then()
               .log().all()
               .extract().response();
	}

	/*
	 * @Then("the response status code should be {int}") public void
	 * the_response_status_code_should_be(Integer expectedCode) {
	 * BaseMethods.validateStatusCode(res, expectedCode, test ); }
	 */

	@Then("The response message should be for parent guardian {string}")
	public void thenTheResponseMessageShouldBeForParentGuardian(String message)
	{
		Assert.assertEquals(message, res.jsonPath().getString("message"));
	}

	@Then("the returned parentId should be a {int}-digit positive number")
	public void the_returned_parent_id_should_be_a_digit_positive_number(Integer int1) 
	{
		int parentId = res.jsonPath().getInt("data[0].parentId");
		Assert.assertTrue("ParentId should be between 100 and 999", parentId >= 100 && parentId <= 999);
	}

	@Then("the success message should be true")
	public void the_success_message_should_be_true()
	{
		 Assert.assertTrue(res.jsonPath().getBoolean("success"));
	}

	@And("I prepare the parent registration body with {string} set to {string}")
	public void i_prepare_the_parent_registration_body_with_set_to(String field, String value) 
	{
		Faker faker = new Faker();
	    Map<String, Object> parent = new HashMap<>();
	    
	    parent.put("lastName", faker.name().lastName());
	    parent.put("firstName", faker.name().firstName());
	    parent.put("email", faker.internet().emailAddress());
	    parent.put("cellPhone", faker.phoneNumber().subscriberNumber(10));
	    parent.put("streetAddress", faker.address().streetAddress());
	    parent.put("apt", faker.address().buildingNumber());
	    parent.put("city", faker.address().city());
	    parent.put("zipCode", faker.address().zipCode().substring(0, 5));
	    parent.put("sameAddressAsChild", true);
	    parent.put("relationship", "mother");
	    parent.put("parentId", 0);
	    parent.put("middleName", faker.name().nameWithMiddle().split(" ")[1]);
	    parent.put("workPhone", faker.phoneNumber().subscriberNumber(10));
	    parent.put("altPhone", faker.phoneNumber().subscriberNumber(10));

	        // Modify the targeted field
	        if (value.equalsIgnoreCase("null")) 
	        {
	            parent.put(field, null);
	        } else 
	        {
	            parent.put(field, value);
	        }

	        String jsonBody = "{ \"parents\": [ " + APIUtils.mapToJson(parent) + " ], \"childId\": " + ChildId + " }";
	        this.requestBody = jsonBody;
	}

	@Then("The field error should be for parent guardian {string}")
	public void thenTheFieldErrorShouldBeForParentGuardian(String fieldPath)
	{
		 String errorPath = res.jsonPath().getString("error.field");
	     Assert.assertEquals(fieldPath, errorPath);
	}

	@Then("the success message should be {string}.")
	public void the_success_message_should_be(String successFlag) 
	{
		 boolean actualSuccess = res.jsonPath().getBoolean("success");
	     boolean expectedSuccess = Boolean.parseBoolean(successFlag);
	     Assert.assertEquals(expectedSuccess, actualSuccess);
	}
}

package stepdefinition;
 
import Utils.*;
import io.cucumber.java.en.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;
import java.io.File;
import java.util.*;

public class Post_Child_Information_Step1 
{
    public Response childres, Parentres, lastres;
    private String parentToken;
    private String requestBody;
    private ExtentTest test;
    private Map<String, String> headers;
    private String endpoint;
    private Map<String, Object> childInfo;

    Map<String, Object> parent;
    List<Map<String, Object>> parents;
    String childId;
    String ParentId;
    Payload Pl = new Payload();

    // Constructor
    	public Post_Child_Information_Step1() 
    	{
        this.test = Extent_Report_Manager.getTest();
        this.headers = ConfigReader.getHeadersFromConfig("header");
        test.info("Initialized Post_Child_Information_Step1 with baseURL: " + Endpoints.baseURL + ", endpoint: " + Endpoints.CHILD_REGISTER);
    	}
 
	    @Given("I have a valid parent token")
	    public String i_have_a_valid_parent_token()
	    {
	        this.parentToken = GlobalTokenStore.getToken("parent");
	        test.info("Retrieved Parent Token: " + parentToken);
	        test.info("Decoded JWT: " + BaseMethods.decodeJWT(parentToken));
			return parentToken;
	    }

	    @When("I send a POST request to child endpoint")
	    public void i_send_a_post_request_to() 
	    {   String payload = Pl.child_registration_payload();
	        APIUtils.logRequestHeaders(test, headers);
	        APIUtils.logRequestBody(test, payload);
	        test.info("Sending POST request to: " + Endpoints.baseURL + Endpoints.CHILD_REGISTER);
	       
	  childres = given()
	            .baseUri(Endpoints.baseURL)
	            .headers(headers)
	            .header("Authorization", "Bearer " + parentToken)
	            .body(payload)
	            .when()
	            .post(Endpoints.CHILD_REGISTER)
	            .then()
	            .extract().response();
	  		lastres = childres;

	        test.info("Response Received: " + childres.getBody().asString());
	        APIUtils.logResponseToExtent(childres, test);
	    }

	    @Then("the child registration response should store childId")
	    public void the_child_registration_response_should_store_child_id()
	    {
	        this.childId = childres.jsonPath().getString("childId");
	        Assert.assertNotNull("childId is null in the response", childId);
	        Assert.assertFalse("childId is empty", childId.trim().isEmpty());
	        
	        GlobalTokenStore.setChildId(childId);  // ✅ Store globally
	        
	        // ✅ Save to config.properties file
	        Map<String, String> properties = new HashMap<>();
	        properties.put("childId", childId);
	        ConfigReader.writeMultipleProperties(properties);
	        
	        test.pass("childId successfully stored: " + childId);
	        System.out.println("Stored childId: " + childId);
	    }
	    
	    @Then("I should receive a 200 OK response")
	    public void i_should_receive_a_200_ok_response() 
	    {
	        int statusCode = lastres.getStatusCode();
	        test.info("Validating status code: " + statusCode);
	        Assert.assertEquals(200, statusCode);
	    }

	    
	    @When("I send a POST request to Parent endpoint")
	    public void i_send_a_post_request_to_parent_endpoint()
	    {
	    	String childId = GlobalTokenStore.getChildId(); // ✅ Fetch here, after it's stored
	    	String payload = Pl.parent_registration_payload(childId); // ✅ Pass it in
	    	APIUtils.logRequestHeaders(test, headers);
	    	APIUtils.logRequestBody(test, payload);
	    	test.info("Sending POST request to endpoint: " + Endpoints.PARENT_REGISTER);

	    	Parentres = given()
	    	            .baseUri(Endpoints.baseURL)
	    	            .headers(headers)
	    	            .header("Authorization", "Bearer " + parentToken)
	    	            .body(payload)
	    	            .when()
	    	            .post(Endpoints.PARENT_REGISTER)
	    	            .then()
	    	            .extract().response();
	    	lastres = Parentres;

	    	test.info("Received response: " + Parentres.asString());
	    	APIUtils.logResponseToExtent(Parentres, test);
	    }

		@Then("the response should contain the parentId")
		public void the_response_should_contain_the_parent_id()
		{
		   this.ParentId= Parentres.jsonPath().getString("parentIds.parentIds[0]");
		   GlobalTokenStore.setParentId(ParentId);  			// ✅ Store globally
		   
		   test.pass("parentId successfully stored: " + ParentId);
		    System.out.println("Stored parentId: " + ParentId); 
		}	
			
		
		@When("I send a POST request to Emergency Contact endpoint")
		public void i_send_a_post_request_to_emergency_contact_endpoint() 
		{
			String childId = GlobalTokenStore.getChildId();
			String parentId = GlobalTokenStore.getParentId();
			String payload = Pl.emergency_contact_payload(childId, parentId);
	    	APIUtils.logRequestHeaders(test, headers);
	    	APIUtils.logRequestBody(test, payload);
			      lastres = given()
			            .baseUri(Endpoints.baseURL)
			            .headers(headers)
			            .header("Authorization", "Bearer " + parentToken)
			            .body(payload)
			            .when()
			            .post(Endpoints.Emergency_Contact)
			            .then()
		                .extract().response(); // ✅ Make sure to extract the response
		}

		@Then("the response should contain the emergency contact ID\\(s)")
		public void the_response_should_contain_the_emergency_contact_id_s() 
		{
			 int contactId = lastres.jsonPath().getInt("contactIds.contactIds[0]");
			 Assert.assertTrue("Emergency contact ID should be positive", contactId > 0);
			 test.pass("Emergency Contact ID successfully retrieved: " + contactId);
			 System.out.println("Emergency Contact ID: " + contactId);
		}

		
		@When("I send a POST request to Pickup Contact endpoint")
		public void i_send_a_post_request_to_pickup_contact_endpoint() 
		{
		    String childId = GlobalTokenStore.getChildId();
		    String parentId = GlobalTokenStore.getParentId();
		    String payload = Pl.add_Pickup_payload(childId, parentId); // Pass both IDs

		    APIUtils.logRequestHeaders(test, headers);
		    APIUtils.logRequestBody(test, payload);

		    lastres = given()
		            .baseUri(Endpoints.baseURL)
		            .headers(headers)
		            .header("Authorization", "Bearer " + parentToken)
		            .body(payload)
		            .when()
		            .post(Endpoints.Add_pickup)
		            .then()
	                .extract().response(); // ✅ Make sure to extract the response;
		}

		@Then("the response should contain the pickup contact ID\\(s)")
		public void the_response_should_contain_the_pickup_contact_id_s()
		{
			System.out.println("Pickup Contact API Response: " + lastres.asPrettyString());
		    List<Integer> pickupIds = lastres.jsonPath().getList("contactIds.pickupIds", Integer.class);
		    Assert.assertTrue("Pickup contact ID should be a positive number", pickupIds.get(0) > 0);
		}
 
		//5.
		@When("I send a POST request to Health Document Upload endpoint")
		public void i_send_a_post_request_to_health_document_upload_endpoint() {
		    String childId = GlobalTokenStore.getChildId();  // e.g. "1819"
		    File file = new File("src/test/resources/files/Screenshot_2.png");

		    headers.remove("Content-Type");  // remove any Content-Type header you might have set

		    APIUtils.logRequestHeaders(test, headers);
		    test.info("Sending POST request to Health Document Upload endpoint with childId: " + childId);

		    lastres = given()
		            .baseUri(Endpoints.baseURL) // Should be https://dev-api.hopscotchconnect.com
		            .headers(headers)
		            .header("Authorization", "Bearer " + parentToken)
		            .multiPart("childId", childId)  // sends childId as text field
		            .multiPart("clsForm", file)     // sends file
		            .log().all()
		            .when()
		            .post(Endpoints.Add_Health_document_sub)
		            .then()
		            .log().all()
		            .extract()
		            .response();

		    test.info("Received response: " + lastres.asString());
		    APIUtils.logResponseToExtent(lastres, test);
		}

		
		@Then("the response should confirm document upload success")
		public void the_response_should_confirm_document_upload_success() 
		{	Assert.assertEquals("Expected 200 OK response", 200, lastres.getStatusCode());
		    boolean success = lastres.jsonPath().getBoolean("success");
		    String message = lastres.jsonPath().getString("message");

		    Assert.assertTrue("Document upload should be successful", success);
		    Assert.assertTrue("Message should indicate success", message != null && message.toLowerCase().contains("success"));
		    test.pass("Health document upload confirmed successfully with message: " + message);
		}
 
		
		@When("I send a Post request to Health Document Delete endpoint")
		public void i_send_a_Post_request_to_health_document_delete_endpoint() 
		{	 childId = GlobalTokenStore.getChildId();
			 String requestBody = Pl.child_id_payload(childId);

		    APIUtils.logRequestHeaders(test, headers);
		    APIUtils.logRequestBody(test, requestBody);
		    test.info("Sending DELETE request to endpoint: " + Endpoints.Delete_Document_sub);

		    lastres = given()
		                .baseUri(Endpoints.baseURL)
		                .headers(headers)
		                .header("Authorization", "Bearer " + parentToken)
		                .body(requestBody)
		            .when()
		                .post(Endpoints.Delete_Document_sub);

		    test.info("Received response: " + lastres.asString());
		    APIUtils.logResponseToExtent(lastres, test);
		}

		@Then("the document should be removed successfully")
		public void the_document_should_be_removed_successfully() 
		{
		    lastres.then()
		           .statusCode(200)
		           .body("success", equalTo(true))
		           .body("message", equalTo("Health info document deleted"));
		}

		
		@When("I send a POST request to Health Info endpoint")
		public void i_send_a_post_request_to_health_info_endpoint() 
		{	String childId = GlobalTokenStore.getChildId();
		    String payload = Pl.health_info_payload(childId);
		    APIUtils.logRequestHeaders(test, headers);
		    APIUtils.logRequestBody(test, payload);  // Use the correct variable name (was: requestBody)

		    test.info("Sending POST request to endpoint: " + Endpoints.Health);

		    lastres = given()
		                .baseUri(Endpoints.baseURL)
		                .headers(headers)
		                .header("Authorization", "Bearer " + parentToken)
		                .body(payload)
		              .when()
		                .post(Endpoints.Health);

		    test.info("Received response: " + lastres.asString());
		    APIUtils.logResponseToExtent(lastres, test);
		}

		@Then("the response should contain the healthInfoId")
		public void the_response_should_contain_the_health_info_id() {
		    lastres.then()
		           .statusCode(200)
		           .body("success", equalTo(true))
		           .body("message", equalTo("Health info saved successfully."))
		           .body("healthInfoId", notNullValue());
		}

		
		@When("I send a POST request to Consent endpoint")
		public void i_send_a_post_request_to_consent_endpoint()
		{	String childId = GlobalTokenStore.getChildId();
	        String payload = Pl.consent_payload(childId);
			APIUtils.logRequestHeaders(test, headers);
	        APIUtils.logRequestBody(test, payload);
			test.info("Sending POST request to endpoint: " + endpoint);
			lastres = given()
			            .baseUri(Endpoints.baseURL)
			            .headers(headers)
			            .header("Authorization", "Bearer " + parentToken)
			            .body(payload)
			            .when()
			            .post(Endpoints.Add_Consent);
			
			 test.info("Received response: " + lastres.asString());
			 APIUtils.logResponseToExtent(lastres, test);
		}
		
		@Then("the response should contain the consentId")
		public void the_response_should_contain_the_consent_id() 
		{
		    lastres.then()
		           .statusCode(200)
		           .body("success", equalTo(true))
		           .body("message", equalTo("Consent saved successfully."))
		           .body("consentId", notNullValue());
		}
				
				
		@When("I send a PUT request to Final Submission endpoint")
		public void i_send_a_put_request_to_final_submission_endpoint()
		{	 childId = GlobalTokenStore.getChildId();
			 String payload = Pl.child_id_payload(childId);
			APIUtils.logRequestHeaders(test, headers);
	        APIUtils.logRequestBody(test, payload);
			test.info("Sending POST request to endpoint: " + Endpoints.Submit_Child);
			lastres = given()
			            .baseUri(Endpoints.baseURL)
			            .headers(headers)
			            .header("Authorization", "Bearer " + parentToken)
			            .body(payload)
			            .when()
			            .post(Endpoints.Submit_Child);
			
			 test.info("Received response: " + lastres.asString());
			 APIUtils.logResponseToExtent(lastres, test);
		}
		
		
		@Then("the child registration status should be updated to COMPLETE")
		public void the_child_registration_status_should_be_updated_to_complete() 
		{
		    lastres.then()
		           .statusCode(200)
		           .body("success", equalTo(true))
		           .body("message", equalTo("Child status updated successfully."))
		           .body("childId", notNullValue());
		}
		
		@Then("The response message should be for Parent guardian {string}")
		public void the_response_message_should_be_for_parent_guardian(String expectedMessage )
		{
			String actualMessage;
			if(lastres.getStatusCode()==200)
			{	
				 actualMessage = lastres.jsonPath().getString("message");
		    }
			else
			{
				 actualMessage = lastres.jsonPath().getString("errors[0].message");
			}
			test.info("Asserting response message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
	        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
		}
		
		@Then("the returned parentId should be a positive number")
		public void the_returned_parent_id_should_be_a_positive_number()
		{
			 int parentId = lastres.jsonPath().getInt("parentIds.parentIds[0]");
			 test.info("Validating parentId is positive. Received: " + parentId);
			 Assert.assertTrue("Expected childId to be a positive number, but got: " + parentId, parentId > 0);
		}
		
		@Then("the success message should be {string}")
		public void the_success_message_should_be(String expectedsuccess )
		{
			String actualSuccess = lastres.jsonPath().getString("success");
	        test.info("Validating success flag. Expected: " + expectedsuccess + ", Actual: " + actualSuccess);
		    Assert.assertEquals("Expected success to be true, but was false",expectedsuccess , actualSuccess);
		}
		
	 @Then("the error field path should be {string}")
      public void validateErrorFieldPath(String expectedFieldPath)
		    {
		        if (!"N/A".equalsIgnoreCase(expectedFieldPath))
		        {
		            String actualField = lastres.jsonPath().getString("errors[0].field");
		            test.info("Validating error field path. Expected: " + expectedFieldPath + ", Actual: " + actualField);
		            Assert.assertEquals(expectedFieldPath, actualField);
		        }
		    }
	
	    @Then("The response message should be for child {string}")
	    public void then_The_Response_Message_Should_Be_For_Child(String expectedMessage)
	    {	
	    	String actualMessage = lastres.jsonPath().getString("message");
	    	test.info("Validating response message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
	        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
	    }
 
	   @When("I update child {string} with {string}")
	    public void updateField(String field, String value)
	    {
	    	test.info("Updating field '" + field + "' with value '" + value + "'");
	        childInfo.put(field, value);
	
	        JSONObject json = new JSONObject(childInfo);
	        JSONObject root = new JSONObject();
	        root.put("childInfo", json);
	        requestBody = root.toString();
	        test.info("Updated payload: " + requestBody);
	    }
   
	    @Then("the response message should be {string} and {string}")
	    public void verifyResponseMessage(String expectedMessage, String expectedFieldPath)
	    {
		    String actualMessage;
			    if (lastres.getStatusCode() == 200)
			    {
			    	then_The_Response_Message_Should_Be_For_Child(expectedMessage);
		
			        // Additional validation for childId
			    	the_returned_child_id_should_be_a_positive_number();
			    }
			    else
			    {
			        actualMessage = lastres.jsonPath().getString("errors[0].message");
			        String  actualFieldPath=lastres.jsonPath().get("errors[0].field");
			        test.info("Error received: " + actualMessage + " at field: " + actualFieldPath);
			        Assert.assertEquals("Expected error message mismatch", expectedMessage, actualMessage);
			        Assert.assertEquals("Expected error message mismatch", expectedFieldPath, actualFieldPath);
			    }
	      }
	    
	    @Then("the returned childId should be a positive number")
	    public void the_returned_child_id_should_be_a_positive_number()
	    {
	    	 int childId = lastres.jsonPath().getInt("childId");
	    	 test.info("Validating childId is positive. Received childId: " + childId);
	    	 Assert.assertTrue("Expected childId to be a positive number, but got: " + childId, childId > 0);
	    	 GlobalTokenStore.setChildId(String.valueOf(childId));
	    }
	    
	    @When("I update parent {string} with {string}")
		public void ParentupdateField(String field, String value)
			    {
				    parent.put(field, value);
				    test.info("Updated field [" + field + "] with value: " + value);
 
				    Map<String, Object> payload = new HashMap<>();
				    payload.put("parents", parents);
				    payload.put("childId", childId);
 
				    JSONObject root = new JSONObject(payload);
				    requestBody = root.toString();
				    test.info("Updated request payload: " + requestBody);
			    }
}
 
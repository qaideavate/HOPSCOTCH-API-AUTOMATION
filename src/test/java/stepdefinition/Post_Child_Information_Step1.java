package stepdefinition;

import Utils.*;
import io.cucumber.java.en.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.*;

public class Post_Child_Information_Step1 
{
    public Response res;
    private String parentToken;
    private String requestBody;
    private ExtentTest test;
    private String baseURL;
    private Map<String, String> headers;
    private String endpoint;
    private Map<String, Object> childInfo;
    private Map<String, Object> payload;
    // Constructor
    	public Post_Child_Information_Step1() 
    	{
        this.test = Extent_Report_Manager.getTest();
        this.baseURL = ConfigReader.getProperty("baseURL");
        this.headers = ConfigReader.getHeadersFromConfig("header");
        this.endpoint = Endpoints.CHILD_REGISTER;
        test.info("Initialized Post_Child_Information_Step1 with baseURL: " + baseURL + ", endpoint: " + endpoint);
    	}

	    @Given("I have a valid parent token")
	    public String  i_have_a_valid_parent_token()
	    {
	        parentToken = GlobalTokenStore.getToken("parent");
	        System.out.println(parentToken);
	        test.info("Retrieved Parent Token: " + parentToken);
	        return parentToken;
	        
	    }
    
	    @Given("The base URL")
	    public  Post_Child_Information_Step1 the_base_url() 
	    {
	        ConfigReader.getProperty("baseURL");
	        test.info("Using Base URL: " + baseURL);
	        return this;
	    }

	    @Given("I prepare the child registration payload with valid data")
	    public Post_Child_Information_Step1 i_prepare_the_child_registration_payload_with_valid_data() 
	    {
	    	childInfo = new HashMap<>();
	        childInfo.put("childId", "");
	        childInfo.put("lastName", "AsadApi");
	        childInfo.put("middleName", "");
	        childInfo.put("firstName", "AsadApi");
	        childInfo.put("nickname", "");
	        childInfo.put("birthdate", "2012-08-15");
	        childInfo.put("gender", "Boy");
	        childInfo.put("streetAddress", "830 Southeast Ireland Street");
	        childInfo.put("city", "Oak Harbor");
	        childInfo.put("zipCode", "98277");
	        childInfo.put("apt", "");
	
	        payload = new HashMap<>();
	        payload.put("childInfo", childInfo);
	        
	        JSONObject json = new JSONObject(childInfo);
	        JSONObject root = new JSONObject();
	        root.put("childInfo", json);
	        requestBody = root.toString();
	        test.info("Prepared child registration payload: " + requestBody);
	        return this;
	    }

	    @When("I send a POST request to child endpoint")
	    public Post_Child_Information_Step1 i_send_a_post_request_to() 
	    {
	    	APIUtils.logRequestHeaders(test, headers);
	        APIUtils.logRequestBody(test, requestBody);
	    	test.info("Sending POST request to: " + endpoint);
	        res = given()
	            .baseUri(baseURL)
	            .headers(headers)
	            .header("Authorization", "Bearer " + parentToken)
	            .body(requestBody)
	            .when()
	            .post(endpoint);
	       
	       test.info("Response Received: " + res.getBody().asString());
	       APIUtils.logResponseToExtent(res, test);
	       GlobalTokenStore.setChildId(res.jsonPath().getString("childId"));
	       return this;
	    }

	    @Then("the child registration response status code should be {int}")
	    public void the_child_registration_status_code_should_be(int Statuscode)
	    {
	    	test.info("Validating Status Code. Expected: " + Statuscode + ", Actual: " + res.getStatusCode());
	        BaseMethods.validateStatusCode(res, Statuscode, test);
	    }

	    @Then("The response message should be for child {string}")
	    public void then_The_Response_Message_Should_Be_For_Child(String expectedMessage) 
	    {	
	    	String actualMessage = res.jsonPath().getString("message");
	    	test.info("Validating response message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
	        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
	    }
    
	    @Then("the returned childId should be a positive number")
	    public void the_returned_child_id_should_be_a_positive_number()
	    {
	    	 int childId = res.jsonPath().getInt("childId");
	    	 test.info("Validating childId is positive. Received childId: " + childId);
	    	 Assert.assertTrue("Expected childId to be a positive number, but got: " + childId, childId > 0);
	    	 GlobalTokenStore.setChildId(String.valueOf(childId));
	    	
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
			    if (res.getStatusCode() == 200) 
			    {
			    	then_The_Response_Message_Should_Be_For_Child(expectedMessage);
		
			        // Additional validation for childId
			    	the_returned_child_id_should_be_a_positive_number();
			    } 
			    else 
			    {
			        actualMessage = res.jsonPath().getString("errors[0].message");
			        String  actualFieldPath=res.jsonPath().get("errors[0].field");
			        test.info("Error received: " + actualMessage + " at field: " + actualFieldPath);
			        Assert.assertEquals("Expected error message mismatch", expectedMessage, actualMessage);
			        Assert.assertEquals("Expected error message mismatch", expectedFieldPath, actualFieldPath);
			    }
	      }
	    
	    @Then("the child registration response should store childId")
	    public Post_Child_Information_Step1 the_child_registration_response_should_store_child_id() 
	    {
	    	String childId = res.jsonPath().getString("childId");
	        test.info("Storing returned childId: " + childId);
	    	GlobalTokenStore.setChildId(res.jsonPath().getString("childId"));
		        return this;
	    }
}

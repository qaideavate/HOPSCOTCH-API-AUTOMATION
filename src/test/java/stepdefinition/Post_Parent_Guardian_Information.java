package stepdefinition;

import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class Post_Parent_Guardian_Information 
{
    public Response res;
    private String parentToken;
    private String requestBody;
    private ExtentTest test;
    private String baseURL;
    private Map<String, String> headers;
    private String endpoint;
    Map<String, Object> parent;
    List<Map<String, Object>> parents;
    String childId;

    	// Constructor
    	public Post_Parent_Guardian_Information ()
    	{
        this.test = Extent_Report_Manager.getTest();
        this.baseURL = ConfigReader.getProperty("baseURL");
        this.headers = ConfigReader.getHeadersFromConfig("header");
        this.endpoint = Endpoints.PARENT_REGISTER;
    	}

    	 	@Given("I have a parent token")
    	    public String i_have_a_parent_token()
    	    {
				  if(parentToken==null)
				  { 
					this.parentToken = GlobalTokenStore.getToken("parent");																
				    test.info("Fetched parent token from GlobalTokenStore.");
				  }
				  else
				  {		
					  Post_Child_Information_Step1 TOKEN =new Post_Child_Information_Step1();
				  	  this.parentToken= TOKEN.i_have_a_valid_parent_token();
				      test.info("Fetched parent token using fallback method.");
				  }
				  test.info("Decoded JWT: " + BaseMethods.decodeJWT(parentToken));
				  return parentToken;
    	    }
    	 	
			@Given("I have a valid child ID")
			public String i_have_a_valid_child_id() 
			{ 
				GlobalTokenStore gts = new GlobalTokenStore();
				if(childId==null)
				{
				this.childId = GlobalTokenStore.getChildId();												
			    System.out.println("   childId:  " + childId);
			    test.info("Fetched existing child ID: " + childId);
				}
				else
				{	this.childId = gts.generateChildId(); 												
			    	test.info("Generated new child ID: " + childId);
				 	
					}
				return childId;
			}
			
			@Given("I prepare the parent registration payload with valid data")
			public void i_prepare_the_parent_registration_payload_with_valid_data() 
			{
			   // Create a parent info map
			    parent = new HashMap<>();
			    
			    parent.put("lastName", "tes");
			    parent.put("firstName", "TestPa");
			    parent.put("email", "pankaj@yopmail.com");
			    parent.put("cellPhone", "1111111111");
			    parent.put("streetAddress", "830 Southeast Ireland Street");
			    parent.put("apt", "");
			    parent.put("city", "Oak Harbor");
			    parent.put("zipCode", "98277");
			    parent.put("sameAddressAsChild", true);
			    parent.put("relationship", "mother");
			    parent.put("parentId", 0);
			    parent.put("middleName", "");
			    parent.put("workPhone", "");
			    parent.put("altPhone", "");

			    // Create list of parents
			    parents = new ArrayList<>();
			    parents.add(parent);
			   
			    // Create the full payload
			    Map<String, Object> requestMap = new HashMap<>();
			    requestMap.put("parents", parents);
			    requestMap.put("childId", childId); // You can replace this dynamically

			    // Convert to JSON
			    JSONObject root = new JSONObject(requestMap);
			    requestBody = root.toString();  // Save this string to send in request
			    test.info("Prepared parent registration payload: " + requestBody);
			}

			@When("I send a POST request to Parent endpoint")
			public void i_send_a_post_request_to_parent_endpoint()
			{
				APIUtils.logRequestHeaders(test, headers);
		        APIUtils.logRequestBody(test, requestBody);
				test.info("Sending POST request to endpoint: " + endpoint);
				 res = given()
				            .baseUri(baseURL)
				            .headers(headers)
				            .header("Authorization", "Bearer " + parentToken)
				            .body(requestBody)
				            .when()
				            .post(endpoint);
				 
				 test.info("Received response: " + res.asString());
				 APIUtils.logResponseToExtent(res, test);
			    
			}
			
			@When("I update parent {string} with {string}")
			public void updateField(String field, String value) 
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
			
			@Then("the Parent registration response status code should be {int}")
			public void the_parent_resgistration_response_status_code_should_be(Integer Statuscode) 
			{
				test.info("Validating response status code. Expected: " + Statuscode + ", Actual: " + res.getStatusCode());
				BaseMethods.validateStatusCode(res, Statuscode, test);
			}
			
			@Then("The response message should be for Parent guardian {string}")
			public void the_response_message_should_be_for_parent_guardian(String expectedMessage ) 
			{ 
				String actualMessage;
				if(res.getStatusCode()==200)
				{	
					 actualMessage = res.jsonPath().getString("message");
			    }
				else
				{ 
					 actualMessage = res.jsonPath().getString("errors[0].message");
				}
				test.info("Asserting response message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
		        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
			}
			
			@Then("the returned parentId should be a positive number")
			public void the_returned_parent_id_should_be_a_positive_number()
			{ 
				 int parentId = res.jsonPath().getInt("parentIds.parentIds[0]");
				 test.info("Validating parentId is positive. Received: " + parentId);
				 Assert.assertTrue("Expected childId to be a positive number, but got: " + parentId, parentId > 0);
			}
			
			@Then("the success message should be {string}")
			public void the_success_message_should_be(String expectedsuccess ) 
			{
				String actualSuccess = res.jsonPath().getString("success");
		        test.info("Validating success flag. Expected: " + expectedsuccess + ", Actual: " + actualSuccess);
			    Assert.assertEquals("Expected success to be true, but was false",expectedsuccess , actualSuccess);
			}
			
		 @Then("the error field path should be {string}")
	      public void validateErrorFieldPath(String expectedFieldPath)
			    {
			        if (!"N/A".equalsIgnoreCase(expectedFieldPath))
			        {
			            String actualField = res.jsonPath().getString("errors[0].field");
			            test.info("Validating error field path. Expected: " + expectedFieldPath + ", Actual: " + actualField);
			            Assert.assertEquals(expectedFieldPath, actualField);
			        }
			    }	   
}
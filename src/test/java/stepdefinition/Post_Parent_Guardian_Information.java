package stepdefinition;

import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Faker;

import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import Utils.GlobalTokenStore;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class Post_Parent_Guardian_Information 
{
    public Response res;
    private String parentToken;
    private String baseUrl;
    private String requestBody;
    private ExtentTest test;
    private String baseURL;
    private Map<String, String> headers;
    private String endpoint;
    private Map<String, Object> parentInfo;
    private Map<String, Object> payload;
    String childId;

    	// Constructor
    	public Post_Parent_Guardian_Information ()
    	{
        this.test = Extent_Report_Manager.getTest();
        this.baseURL = ConfigReader.getProperty("baseURL");
        this.headers = ConfigReader.getHeadersFromConfig("header");
        this.endpoint = ConfigReader.getProperty("ParentRegisterEndpoint");
    	}

    	 	@Given("I have a parent token")
    	    public String i_have_a_parent_token()
    	    {
				  Post_Child_Information_Step1 TOKEN =new Post_Child_Information_Step1();
				  this.parentToken= TOKEN.i_have_a_valid_parent_token();
				  System.out.println(parentToken);
				  System.out.println("Decoded JWT: " + BaseMethods.decodeJWT(parentToken));
				  return parentToken;
    	    }
    	 	
			@Given("I have a valid child ID")
			public String i_have_a_valid_child_id() 
			{ 
				GlobalTokenStore gts = new GlobalTokenStore();
				this.childId = gts.generateChildId(); 
			    System.out.println("   childId:  " + childId);
				return childId;
			}
			
			@Given("I prepare the parent registration payload with valid data")
			public void i_prepare_the_parent_registration_payload_with_valid_data() 
			{
			    // Create a parent info map
			    Map<String, Object> parent = new HashMap<>();
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
			    List<Map<String, Object>> parents = new ArrayList<>();
			    parents.add(parent);
			   
			    // Create the full payload
			    Map<String, Object> requestMap = new HashMap<>();
			    requestMap.put("parents", parents);
			    requestMap.put("childId", childId); // You can replace this dynamically

			    // Convert to JSON
			    JSONObject root = new JSONObject(requestMap);
			    requestBody = root.toString();  // Save this string to send in request
			}

			@When("I send a POST request to Parent endpoint")
			public void i_send_a_post_request_to_parent_endpoint()
			{
				 res = given()
				            .baseUri(baseURL)
				            .headers(headers)
				            .header("Authorization", "Bearer " + parentToken)
				            .body(requestBody)
				            .when()
				            .post(endpoint);
				 
				 System.out.println("Decoded JWT: " + BaseMethods.decodeJWT(parentToken));

			}
			
			@Then("the Parent registration response status code should be {int}")
			public void the_parent_resgistration_response_status_code_should_be(Integer Statuscode) 
			{
				BaseMethods.validateStatusCode(res, Statuscode, test);
			}
			
			@Then("The response message should be for Parent guardian {string}")
			public void the_response_message_should_be_for_parent_guardian(String expectedMessage ) 
			{
				String actualMessage = res.jsonPath().getString("message");
		        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
			}
			
			@Then("the returned parentId should be a {int}-digit positive number")
			public void the_returned_parent_id_should_be_a_digit_positive_number(Integer digits)
			{
				int parentId = res.jsonPath().getInt("parentIds.parentIds[0]");
				 int lowerBound = (int) Math.pow(10, digits - 1);
				 int upperBound = (int) Math.pow(10, digits) - 1;
				 Assert.assertTrue("Expected parentId to be a " + digits + "-digit positive number, but got: " + parentId, parentId >= lowerBound && parentId <= upperBound);
			}
			
			@Then("the success message should be {string}.")
			public void the_success_message_should_be(String expectedsuccess ) 
			{
				String actualSuccess = res.jsonPath().getString("success");
			    Assert.assertEquals("Expected success to be true, but was false",expectedsuccess , actualSuccess);
			}
			
			
			@Then("the response message should be {string},{string} and  {string}")
			public void the_response_message_should_be_and(String expectedMessage, String expectedsuccess, String expectedFieldPath) 
			{
			   String actualmessage = res.jsonPath().getString("errors[1].message");
			   Assert.assertEquals("Message should be: "+ actualmessage, expectedMessage, actualmessage);
			   
			   the_success_message_should_be(expectedsuccess);
			   
			   String actualFieldPath=res.jsonPath().getString("errors[0].field");
			   Assert.assertEquals("Message should be :" +actualFieldPath, expectedFieldPath, actualFieldPath);
			   
			}
}
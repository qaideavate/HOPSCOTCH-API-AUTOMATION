package stepdefinition;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import Utils.GlobalTokenStore;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.List;
import java.util.Map;

public class Post_Child_Information_Step1 
{
    public Response res;
    private String parentToken ;
    private String baseUrl ;
    private String requestBody;
	private ExtentTest test;
	private String baseURL;
	private Map<String, String> headers;
	private String endpoint;

	//constructor
    public Post_Child_Information_Step1()
    {
    	 this.test = Extent_Report_Manager.getTest();
         this.baseURL = ConfigReader.getProperty("baseURL");
         this.headers = ConfigReader.getHeadersFromConfig("header");
        // this.headers.put("Accept-Encoding", "gzip, deflate");
         this.endpoint = ConfigReader.getProperty("ChildRegisterEndpoint");
    }
    
    //action class
    @Given("I have a valid parent token")
    public void i_have_a_valid_parent_token() 
    {
    	parentToken = GlobalTokenStore.getToken("parent");
    }

    @Given("The base URL")
    public void the_base_url() 
    {
    	baseUrl = ConfigReader.getProperty("baseURL");
    }
    
    @Given("I provide the child register with the following details:")
    public void i_provide_the_child_register_with_the_following_details(DataTable dataTable) 
    {
    		List<Map<String, String>> childDetails = dataTable.asMaps(String.class, String.class);
    	    Map<String, String> child = childDetails.get(0);

    	    JSONObject json = new JSONObject();
    	    json.put("childId", child.get("childId")!= null ? child.get("childId") : "");
    	    json.put("lastName", child.get("lastName"));
    	    json.put("middleName", child.get("middleName") != null ? child.get("middleName") : "");
    	    json.put("firstName", child.get("firstName"));
    	    json.put("nickname", child.get("nickname")!=null?child.get("nickname") : " ");
    	    
    	    String birthdate = BaseMethods.formatBirthdate(child.get("birthdate"));
    	    json.put("birthdate", birthdate);
    	    json.put("gender", child.get("gender"));
    	    json.put("streetAddress", child.get("streetAddress"));
    	    json.put("city", child.get("city"));
    	    json.put("zipCode", child.get("zipCode"));
    	    json.put("apt", child.get("apt")!=null?child.get("apt") : " ");

    	    JSONObject root = new JSONObject();
    	    root.put("childInfo", json);

    	    requestBody = root.toString(); // ✅ Ensure this is not null
    }
    
    @When("I send a POST request to child endpoint")
    public void i_send_a_post_request_to()
    {
    	res = given()
    		.baseUri(baseURL)
    		.headers(headers)
            .header("Authorization", "Bearer " + parentToken)
            .body(requestBody)
            .when()
            .post(endpoint)
    		.then()
            .log().all()
            .extract().response();
    }
    
    @Then("the child registration response status code should be {int}")
    public void the_child_registration_status_code_should_be(int expectedStatusCode)
    {
        BaseMethods.validateStatusCode(res, expectedStatusCode, test);
    }

    @Then("The response message should be for child {string}")
    public void then_The_Response_Message_Should_Be_For_Child(String message) 
    {
        Assert.assertEquals("Expected response message to be " + message, message, res.jsonPath().getString("message"));
    }

    @Then("the returned childId should be a 3-digit positive number")
    public void then_The_Returned_ChildId_Should_Be_A_3Digit_PositiveNumber() 
    {
        int childId = res.jsonPath().getInt("childId");
        Assert.assertTrue("Expected childId to be a 3-digit positive number", childId > 99 && childId < 1000);
    }

    // Scenario Outline: Validate child registration request body field validations
    @Given("I provide the child register with the following details from outline::")
    public void i_provide_the_child_register_with_the_following_details_from_outline(DataTable dataTable)
    {
    	List<Map<String, String>> childDetails = dataTable.asMaps(String.class, String.class);
	    Map<String, String> child = childDetails.get(0);

	    JSONObject json = new JSONObject();
	    json.put("childId", child.get("childId")!= null ? child.get("childId") : "");
	    json.put("lastName", child.get("lastName"));
	    json.put("middleName", child.get("middleName") != null ? child.get("middleName") : "");
	    json.put("firstName", child.get("firstName"));
	    json.put("nickname", child.get("nickname")!=null?child.get("nickname") : " ");
	    
	    String birthdate = BaseMethods.formatBirthdate(child.get("birthdate"));
	    json.put("birthdate", birthdate);
	    json.put("gender", child.get("gender"));
	    json.put("streetAddress", child.get("streetAddress"));
	    json.put("city", child.get("city"));
	    json.put("zipCode", child.get("zipCode"));
	    json.put("apt", child.get("apt")!=null?child.get("apt") : " ");

	    JSONObject root = new JSONObject();
	    root.put("childInfo", json);

	    requestBody = root.toString(); // ✅ Ensure this is not null
	    
	    res = given()
	    		.baseUri(baseURL)
	    		.headers(headers)
	            .header("Authorization", "Bearer " + parentToken)
	            .body(requestBody)
	            .when()
	            .post(endpoint)
	    		.then()
	            .log().all()
	            .extract().response();
    }
    
    @Then("The field error should be for child {string}")
    public void thenTheFieldErrorShouldBeForChild(String fieldError)
    {
    	 String actualErrorMessage = res.jsonPath().getString("errors[0].message");
       Assert.assertTrue("Expected field error to be " + fieldError, actualErrorMessage.contains(fieldError));
    }
}

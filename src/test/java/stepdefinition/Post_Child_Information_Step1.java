package stepdefinition;

import io.cucumber.java.en.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import Utils.GlobalTokenStore;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.*;

public class Post_Child_Information_Step1 {
    public Response res;
    private String parentToken;
    private String baseUrl;
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
        this.endpoint = ConfigReader.getProperty("ChildRegisterEndpoint");
    	}

    @Given("I have a valid parent token")
    public String  i_have_a_valid_parent_token()
    {
        parentToken = GlobalTokenStore.getToken("parent");
        System.out.println(parentToken);
        return parentToken;
    }
    
    @Given("The base URL")
    public  Post_Child_Information_Step1 the_base_url() 
    {
        baseUrl = ConfigReader.getProperty("baseURL");
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
        return this;
    }

    @When("I send a POST request to child endpoint")
    public Post_Child_Information_Step1 i_send_a_post_request_to() 
    {
        res = given()
            .baseUri(baseURL)
            .headers(headers)
            .header("Authorization", "Bearer " + parentToken)
            .body(requestBody)
            .when()
            .post(endpoint);
       
        GlobalTokenStore.setChildId(res.jsonPath().getString("childId"));
        return this;
    }

    @Then("the child registration response status code should be {int}")
    public void the_child_registration_status_code_should_be(int Statuscode)
    {
        BaseMethods.validateStatusCode(res, Statuscode, test);
    }

    @Then("The response message should be for child {string}")
    public void then_The_Response_Message_Should_Be_For_Child(String expectedMessage) 
    {	String actualMessage = res.jsonPath().getString("message");
        Assert.assertEquals("Expected response message to be " + expectedMessage, expectedMessage, actualMessage);
    }
    
    @Then("the returned childId should be a 3-digit positive number")
    public void then_The_Returned_ChildId_Should_Be_A_3Digit_PositiveNumber()
    {
        int childId = res.jsonPath().getInt("childId");
        Assert.assertTrue("Expected childId to be a 3-digit positive number", childId > 99 && childId < 1000);
    }

    @When("I update {string} with {string}")
    public void updateField(String field, String value) 
    {
        childInfo.put(field, value);

        JSONObject json = new JSONObject(childInfo);
        JSONObject root = new JSONObject();
        root.put("childInfo", json);
        requestBody = root.toString();
    }
    
    @Then("the response message should be {string} and {string}")
    public void verifyResponseMessage(String expectedMessage, String expectedFieldPath) 
    {
	    String actualMessage;
	    if (res.getStatusCode() == 200) 
	    {
	    	then_The_Response_Message_Should_Be_For_Child(expectedMessage);

	        // Additional validation for childId
	        then_The_Returned_ChildId_Should_Be_A_3Digit_PositiveNumber();
	    } 
	    else 
	    {
	        actualMessage = res.jsonPath().getString("errors[0].message");
	        String  actualFieldPath=res.jsonPath().get("errors[0].field");
	        Assert.assertEquals("Expected error message mismatch", expectedMessage, actualMessage);
	        Assert.assertEquals("Expected error message mismatch", expectedFieldPath, actualFieldPath);
	        
	    }
	}
    
    @Then("the child registration response should store childId")
    public void storeChildIdFromResponse()
    {
        int childId = res.jsonPath().getInt("childId");
        GlobalTokenStore.setChildId(String.valueOf(childId));
        System.out.println("✅ Stored childId: " + childId);
    }
}

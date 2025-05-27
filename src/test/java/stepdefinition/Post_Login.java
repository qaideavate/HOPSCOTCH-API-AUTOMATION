package stepdefinition;
import static io.restassured.RestAssured.*;
import java.util.HashMap;
import java.util.Map;

import Utils.*;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class Post_Login
{
	private Response loginRes;
    private String email;
    private String password;
    private String endpoint;
    private String baseURL = ConfigReader.getProperty("baseURL");
    private ExtentTest test = Extent_Report_Manager.getTest();
    private Map<String, String> headers = ConfigReader.getHeadersFromConfig("header");
    private String contentType="application/json";
    public static Map<String, String> userTokens = new HashMap<>();
  


    // Step for sending POST request to the login endpoint
    @When("The {string} sends a POST request to the login endpoint")
    public void the_sends_a_post_request_to_the_login_endpoint(String userType)
    { 
    	test.info("Preparing login request for: " + userType);
        HashMap<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", this.email);
        loginPayload.put("password", this.password);

        APIUtils.logRequestHeaders(test, headers);
        APIUtils.logRequestBody(test, loginPayload);
        test.info("Sending POST request to: " + endpoint);
        loginRes = given()
                .baseUri(baseURL)
                .contentType(contentType)
                .body(loginPayload)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
        
        test.info("POST request completed. Status Code: " + loginRes.getStatusCode());
        APIUtils.logResponseToExtent(loginRes, test);
        
        // Store access token if login is successful
        if (loginRes.getStatusCode() == 201 && loginRes.jsonPath().get("accessToken") != null)
        {
            String logintoken = loginRes.jsonPath().getString("accessToken");
            test.info("Decoded JWT: " + BaseMethods.decodeJWT(logintoken));
            test.info(userType + " token retrieved and stored successfully.");
        }
    }
    
    // Step for checking the status code
    @Then("The response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) 
    {
    	int actualCode = loginRes.getStatusCode();
    	test.info("Validating response status code: Expected = " + expectedStatusCode + ", Actual = " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedStatusCode.intValue(), actualCode);
    }

    // Step for checking the response message
    @Then("The response message should be {string}")
    public void the_response_message_should_be(String expectedMessage) 
    {
    	  String actualMessage = loginRes.jsonPath().getString("message");
    	  test.info("Validating response message: Expected = " + expectedMessage + ", Actual = " + actualMessage);
          Assert.assertEquals("Unexpected response message", expectedMessage, actualMessage);
    }


    // Handling empty or missing email/password in login request
    @Given("The {string} provides email {string} and password {string}")
    public void the_provides_email_and_password(String userType, String email, String password)
    {
    	 this.email = (email == null || email.isEmpty()) ? "" : email;
         this.password = (password == null || password.isEmpty()) ? "" : password;
         this.endpoint = userType.equalsIgnoreCase("Provider") ? Endpoints.PROVIDER_LOGIN : Endpoints.PARENT_LOGIN;
         this.contentType = headers.getOrDefault("Content-Type", "application/json");
        
         test.info("Login attempt for " + userType);
         test.info("Using email: " + (this.email.isEmpty() ? "EMPTY" : this.email));
         test.info("Using password: " + (this.password.isEmpty() ? "EMPTY" : "******"));
    }
    
    
    // Step for handling missing fields (Scenario Outline)
    @Then("The response error should be {string}")
    public void the_response_error_should_be(String expectedError)
    {
        String actualErrorMessage = null;
        test.info("Full Response Body: " + loginRes.asString());

        // Check for field-level errors
        if (loginRes.jsonPath().get("errors") != null) 
        {
            Map<String, String> errors = loginRes.jsonPath().getMap("errors");

            // Combine all field errors with " | "
            actualErrorMessage = String.join(" | ", errors.values());
        } 
        // Else check top-level 'error' and 'message'
	        else if (loginRes.jsonPath().get("error") != null && loginRes.jsonPath().get("message") != null) 
	        {
	            actualErrorMessage = loginRes.jsonPath().getString("message") + " | " + loginRes.jsonPath().getString("error");
	        } 
		        else if (loginRes.jsonPath().get("error") != null)
		        {
		            actualErrorMessage = loginRes.jsonPath().getString("error");
		        } 
			        else if (loginRes.jsonPath().get("message") != null) 
			        {
			            actualErrorMessage = loginRes.jsonPath().getString("message");
			        }
        
        String normalizedActual = actualErrorMessage.replace(" | ", ",");
      
        
        test.info("Validating error message: Expected = " + expectedError + ", Actual = " + normalizedActual);
        Assert.assertEquals("Unexpected error message", expectedError, normalizedActual);
    }
    
    // Step for validating the access token
    @Then("The response should contain a valid access token")
    public void the_response_should_contain_a_valid_access_token() 
    {
    	 String token = loginRes.jsonPath().getString("accessToken");
    	 test.info("Checking for presence and length of access token...");
         Assert.assertNotNull("Access token should not be null", token);
         Assert.assertTrue("Access token should be of reasonable length", token.length() > 100); 
         test.info("Access token validation passed. Token length: " + token.length());
    }
}

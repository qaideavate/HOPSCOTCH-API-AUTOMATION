package stepdefinition;

import static io.restassured.RestAssured.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import Utils.APIUtils;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class LoginStepdefinition 
{
	private Response res;
    private String contentType;
    private String email;
    private String password;
    private String baseURL;
    private ExtentTest test;
    private Map<String, String> headers;
  
    public LoginStepdefinition()
    {
    	 this.test = Extent_Report_Manager.getTest();
         this.baseURL = ConfigReader.getProperty("baseURL");
         this.headers = ConfigReader.getHeadersFromConfig("header");
         this.contentType = headers.getOrDefault("Content-Type", "application/json");
    }
    
    @Given("I set the header param request content type as {string}")
    public void i_set_the_header_param_request_content_type_as(String contentType)
    {
        headers.put("Content-Type", contentType);
        this.contentType = contentType;
    }
    
    // Step for providing valid email login credentials
    @Given("The user provides valid email login credentials")
    public void the_user_provides_valid_email_login_credentials() 
    {
    	test = Extent_Report_Manager.getTest();
        email = ConfigReader.getProperty("email");
        password = ConfigReader.getProperty("password");
  
        Assert.assertNotNull("Email is not set in config", this.email);
        Assert.assertNotNull("Password is not set in config", this.password);
        Assert.assertNotNull("Content-Type is not set", this.contentType);
    }
    
    // Handling empty or missing email/password in login request
    @Given("The user provides email {string} and password {string}")
    public void the_user_provides_email_and_password(String email, String password) 
    {
    	 this.email = (email == null || email.isEmpty()) ? "" : email;
         this.password = (password == null || password.isEmpty()) ? "" : password;
         this.contentType = headers.getOrDefault("Content-Type", "application/json");

        test.info("Using email: " + (this.email.isEmpty() ? "EMPTY" : this.email));
        test.info("Using password: " + (this.password.isEmpty() ? "EMPTY" : "******"));
    }

    // Step for sending POST request to the login endpoint
    @When("The user sends a POST request to the provider login endpoint")
    public void the_user_sends_a_post_request_to_the_provider_login_endpoint()
    {
        HashMap<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", this.email);
        loginPayload.put("password",this.password);
       
        APIUtils.logRequestHeaders(test, headers);
        APIUtils.logRequestBody(test, loginPayload);
        
        res = given()
        		.baseUri(baseURL)
                .contentType(contentType)
                .body(loginPayload)
                .log().all()
            .when()
                .post("/auth/provider-login")
            .then()
                .log().all()
                .extract().response();

        
        APIUtils.logResponseToExtent(res, test);
    }

    // Step for checking the status code
    @Then("The response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) 
    {
    	int actualCode = res.getStatusCode();
        test.info("Asserting status code. Expected: " + expectedStatusCode + ", Actual: " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedStatusCode.intValue(), actualCode);
    }

    // Step for checking the response message
    @Then("The response message should be {string}")
    public void the_response_message_should_be(String expectedMessage) 
    {
    	  String actualMessage = res.jsonPath().getString("message");
          test.info("Asserting message. Expected: " + expectedMessage + ", Actual: " + actualMessage);
          Assert.assertEquals("Unexpected response message", expectedMessage, actualMessage);
    }

    // Step for handling missing fields (Scenario Outline)
    @Then("The response error should be {string}")
    public void the_response_error_should_be(String expectedError)
    {
        String actualErrorMessage = null;

        // Log full response
        test.info("Full Response: " + res.asString());

        // Check for field-level errors
        if (res.jsonPath().get("errors") != null) 
        {
            Map<String, String> errors = res.jsonPath().getMap("errors");

            // Combine all field errors with " | "
            actualErrorMessage = String.join(" | ", errors.values());
        } 
        // Else check top-level 'error' and 'message'
	        else if (res.jsonPath().get("error") != null && res.jsonPath().get("message") != null) 
	        {
	            actualErrorMessage = res.jsonPath().getString("message") + " | " + res.jsonPath().getString("error");
	        } 
		        else if (res.jsonPath().get("error") != null)
		        {
		            actualErrorMessage = res.jsonPath().getString("error");
		        } 
			        else if (res.jsonPath().get("message") != null) 
			        {
			            actualErrorMessage = res.jsonPath().getString("message");
			        }
        
        String normalizedActual = actualErrorMessage.replace(" | ", ",");
      
        
        test.info("Asserting error. Expected: " +expectedError + ", Actual: " + normalizedActual);
        Assert.assertEquals("Unexpected error message", expectedError, normalizedActual);
    }
    
    // Step for validating the access token
    @Then("The response should contain a valid access token")
    public void the_response_should_contain_a_valid_access_token() 
    {
    	 String token = res.jsonPath().getString("accessToken");
         test.info("Validating access token presence and length");
         Assert.assertNotNull("Access token should not be null", token);
         Assert.assertTrue("Access token should be of reasonable length", token.length() > 100);
    }

}

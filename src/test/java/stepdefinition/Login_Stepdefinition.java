package stepdefinition;

import static io.restassured.RestAssured.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import Utils.APIUtils;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import Utils.GlobalTokenStore;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class Login_Stepdefinition 
{
	private Response res;
    private String contentType;
    private String email;
    private String password;
    private String endpoint;
    private String baseURL;
    private ExtentTest test;
    private Map<String, String> headers;
    public static Map<String, String> userTokens = new HashMap<>();
  
    public Login_Stepdefinition()
    {
    	 this.test = Extent_Report_Manager.getTest();
         this.baseURL = ConfigReader.getProperty("baseURL");
         this.headers = ConfigReader.getHeadersFromConfig("header");
         this.contentType = headers.getOrDefault("Content-Type", "application/json");
         this.headers.put("Accept-Encoding", "gzip, deflate");
         test.info("Initialized Login_Stepdefinition with baseURL: " + baseURL);
    }
    
    @Given("I set the header param request content type as {string}")
    public void i_set_the_header_param_request_content_type_as(String contentType)
    {
        headers.put("Content-Type", contentType);
        this.contentType = contentType;
        test.info("Set request Content-Type to: " + contentType);
    }
    
    // Step for providing valid email login credentials
    @Given("The {string} provides valid email login credentials")
    public void the_user_provides_valid_email_login_credentials(String userType)
    {
    	 if (userType.equalsIgnoreCase("Parent")) 
        {
            email = ConfigReader.getProperty("Parent_email");
            password = ConfigReader.getProperty("Parent_password");
        } 
        else if (userType.equalsIgnoreCase("Provider"))
        {
            email = ConfigReader.getProperty("Provider_email");
            password = ConfigReader.getProperty("Provider_password");
        } 
        else 
        {
            Assert.fail("Unsupported user type: " + userType);
        }
        test.info(userType + " login using email: " + email);
        Assert.assertNotNull("Email is not set in config", this.email);
        Assert.assertNotNull("Password is not set in config", this.password);
        Assert.assertNotNull("Content-Type is not set", this.contentType);
    }
    
    // Handling empty or missing email/password in login request
    @Given("The {string} provides email {string} and password {string}")
    public void the_provides_email_and_password(String userType, String email, String password)
    {
    	 this.email = (email == null || email.isEmpty()) ? "" : email;
         this.password = (password == null || password.isEmpty()) ? "" : password;
         this.endpoint = userType.equalsIgnoreCase("Provider") ? "/auth/provider-login" : "/auth/login";
         this.contentType = headers.getOrDefault("Content-Type", "application/json");
        
         test.info("Login attempt for " + userType);
        test.info("Using email: " + (this.email.isEmpty() ? "EMPTY" : this.email));
        test.info("Using password: " + (this.password.isEmpty() ? "EMPTY" : "******"));
    }

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
        res = given()
                .baseUri(baseURL)
                .headers(headers)
                .contentType(contentType)
                .body(loginPayload)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        
        test.info("POST request completed. Status Code: " + res.getStatusCode());
        APIUtils.logResponseToExtent(res, test);
        
        // Store access token if login is successful
        if (res.getStatusCode() == 201 && res.jsonPath().get("accessToken") != null)
        {
            String token = res.jsonPath().getString("accessToken");
            GlobalTokenStore.setToken(userType, token);
            test.info(userType + " token retrieved and stored successfully.");
        }
    }
    
    // Step for checking the status code
    @Then("The response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) 
    {
    	int actualCode = res.getStatusCode();
    	  test.info("Validating response status code: Expected = " + expectedStatusCode + ", Actual = " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedStatusCode.intValue(), actualCode);
    }

    // Step for checking the response message
    @Then("The response message should be {string}")
    public void the_response_message_should_be(String expectedMessage) 
    {
    	  String actualMessage = res.jsonPath().getString("message");
    	  test.info("Validating response message: Expected = " + expectedMessage + ", Actual = " + actualMessage);
          Assert.assertEquals("Unexpected response message", expectedMessage, actualMessage);
    }

    // Step for handling missing fields (Scenario Outline)
    @Then("The response error should be {string}")
    public void the_response_error_should_be(String expectedError)
    {
        String actualErrorMessage = null;
        test.info("Full Response Body: " + res.asString());

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
      
        
        test.info("Validating error message: Expected = " + expectedError + ", Actual = " + normalizedActual);
        Assert.assertEquals("Unexpected error message", expectedError, normalizedActual);
    }
    
    // Step for validating the access token
    @Then("The response should contain a valid access token")
    public void the_response_should_contain_a_valid_access_token() 
    {
    	 String token = res.jsonPath().getString("accessToken");
    	 test.info("Checking for presence and length of access token...");
         Assert.assertNotNull("Access token should not be null", token);
         Assert.assertTrue("Access token should be of reasonable length", token.length() > 100); 
         test.info("Access token validation passed. Token length: " + token.length());
    }
}

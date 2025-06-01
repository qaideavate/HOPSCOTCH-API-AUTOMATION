package stepdefinition;

import org.junit.Assert;
import io.restassured.builder.ResponseBuilder;
import Utils.APIUtils;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import com.aventstack.extentreports.ExtentTest;
import Utils.Endpoints;
import java.util.Map;
public class Get_children_List_API
{
	private Response res ;
	private ExtentTest test = Extent_Report_Manager.getTest();
	private Map<String, String> headers =ConfigReader.getHeadersFromConfig("header");

	@When("I send a GET request of children-with-enrollments API")
	public void i_send_a_get_request_of_children_with_enrollments_api() 
	{  
		    String parentToken = ConfigReader.getProperty("ParentToken");
	        if (parentToken == null || parentToken.trim().isEmpty()) 
	        {
	            BaseMethods.parentLogin();  
	            parentToken = ConfigReader.getProperty("ParentToken");
	        }
	        APIUtils.logRequestHeaders(test, headers);

	        test.info("Sending GET request to: " + Endpoints.baseURL + Endpoints.GET_CHILDREN);
	        test.info("Using Authorization token for parent.");
	        
	        // Sending GET request
	        res = given()
	                .baseUri(Endpoints.baseURL)
	                .headers(headers)
	                .contentType("application/json; charset=utf-8")
	                .header("Authorization", "Bearer " + parentToken)
	                .when()
	                .get(Endpoints.GET_CHILDREN);
	             
	        // Log response to extent
	        APIUtils.logResponseToExtent(res, test);
	        String contentType = res.getHeader("Content-Type");
	        test.info("Content-Type: " + contentType);
	    }

		@Then("the child list response status code should be {int}") 
		  public void the_child_list_status_code_should_be(Integer expectedStatusCode) 
		  {
			   test.info("Validating status code: expected " + expectedStatusCode);
			   BaseMethods.validateStatusCode(res, expectedStatusCode, test);
		   }
	  
		@Then("the response body should match the predefined JSON schema {string}")
		public void the_response_body_should_match_the_predefined_json_schema(String schemaFileName)
		{
		  test.info("Validating response against schema: schema/" + schemaFileName); 
		  String cleanedJson = APIUtils.getJsonBodyIfValid(res);   // common logic reuse

	      // Rebuild response with cleaned body
		  Response cleanedResponse = new ResponseBuilder().clone(res).setBody(cleanedJson).build();

     	  // Schema validation
		 cleanedResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/" + schemaFileName));
		}
		    
		  @Then("the response status message should be {string}") 
		  public void the_response_status_message_should_be(String expectedStatusMessage) 
		  { 		  
			  String actualStatusLine = res.getStatusLine();
			  test.info("Validating status message. Expected: " + expectedStatusMessage + ", Actual: " + actualStatusLine + " ");
		      Assert.assertNotNull("Status field is missing in response", actualStatusLine);
		      Assert.assertEquals("Status message mismatch", expectedStatusMessage, actualStatusLine);
		  }


	 
}
 	

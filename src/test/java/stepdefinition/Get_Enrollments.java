package stepdefinition;

import static io.restassured.RestAssured.given;
import java.util.Map;

import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.en.*;
import io.restassured.builder.ResponseBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class Get_Enrollments
{
	private Response res ;
	private String baseURL ;
	private String getEnrolled ;
	private ExtentTest test;
	private Map<String, String> headers;
	
	public Get_Enrollments()
	{
		 this.test = Extent_Report_Manager.getTest();
		 this.headers = ConfigReader.getHeadersFromConfig("header");
		
		 getEnrolled = Endpoints.GET_ENROLLMENT;
	}
	
	@When("I send a GET request of Get Enrollments API")
	public void i_send_a_get_request_of_get_enrollments_api() 
	{
		 String parentToken = GlobalTokenStore.getToken("parent");
	     APIUtils.logRequestHeaders(test, headers);
	     
	        test.info("Sending GET request to: " + Endpoints.baseURL + getEnrolled);
	        test.info("Using Authorization token for parent.");
	        
	        // Sending GET request
	        res = given()
	                .baseUri(Endpoints.baseURL)
	                .headers(headers)
	                 // .contentType("application/json; charset=utf-8")
	                .header("Authorization", "Bearer " + parentToken)
	                .when()
	                .get(getEnrolled)
	                .then()
	                .extract().response();
	        
	        // Log response to extent
	        APIUtils.logResponseToExtent(res, test);
	        String contentType = res.getHeader("Content-Type");
	        test.info("Content-Type: " + contentType);
	        System.out.println(res.asPrettyString());
	}

	@Then("the Enrolled child-list response status code should be {int}")
	public void the_enrolled_child_list_response_status_code_should_be(Integer expectedStatusCode) 
	{
		 test.info("Validating status code: expected " + expectedStatusCode);
		  BaseMethods.validateStatusCode(res, expectedStatusCode, test); 
	}

	@Then("the Enrolled list response body should match the predefined JSON schema {string}")
	public void the_enrolled_list_response_body_should_match_the_predefined_json_schema(String schemaFileName)
	{
		test.info("Validating response against schema: schema/" + schemaFileName); 
		  String cleanedJson = APIUtils.getJsonBodyIfValid(res); // common logic reuse

	      // Rebuild response with cleaned body
		  Response cleanedResponse = new ResponseBuilder().clone(res).setBody(cleanedJson).build();

   	  // Schema validation
		 cleanedResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/" + schemaFileName));
	}

}

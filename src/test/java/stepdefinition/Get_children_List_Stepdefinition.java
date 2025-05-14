package stepdefinition;

import org.junit.Assert;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseBuilder;
import io.restassured.config.DecoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.parsing.Parser;
import Utils.APIUtils;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import Utils.GlobalTokenStore;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import com.aventstack.extentreports.ExtentTest;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import io.cucumber.datatable.*;
public class Get_children_List_Stepdefinition
{
	private Response res ;
	private String baseURL ;
	private String getchildren;
	private ExtentTest test;
	private Map<String, String> headers;
	
	public Get_children_List_Stepdefinition()
	{
		 this.test = Extent_Report_Manager.getTest();
		 this.headers = ConfigReader.getHeadersFromConfig("header");
		 baseURL = ConfigReader.getProperty("baseURL");
		 getchildren = ConfigReader.getProperty("getChildren");
	}
	
	
	@When("I send a GET request of children-with-enrollments API")
	public void i_send_a_get_request_of_children_with_enrollments_api() 
	{
	        String parentToken = GlobalTokenStore.getToken("parent");
	        APIUtils.logRequestHeaders(test, headers);

	        test.info("Sending GET request to: " + baseURL + getchildren);
	        test.info("Using Authorization token for parent.");
	        
	        // Sending GET request
	        res = given()
	                .baseUri(baseURL)
	                .headers(headers)
	                .contentType("application/json; charset=utf-8")
	                .header("Authorization", "Bearer " + parentToken)
	                .when()
	                .get(getchildren)
	                .then()
	                .extract().response();
	        
	        // Log response to extent
	        APIUtils.logResponseToExtent(res, test);
	        String contentType = res.getHeader("Content-Type");
	        test.info("Content-Type: " + contentType);
	        System.out.println(res.asPrettyString());
	    }

		@Then("the response body should match the predefined JSON schema {string}")
		public void the_response_body_should_match_the_predefined_json_schema(String schemaFileName)
		{
		  test.info("Validating response against schema: schema/" + schemaFileName); 
		  String cleanedJson = APIUtils.getJsonBodyIfValid(res); // common logic reuse

	      // Rebuild response with cleaned body
		  Response cleanedResponse = new ResponseBuilder().clone(res).setBody(cleanedJson).build();

     	  // Schema validation
		 cleanedResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/" + schemaFileName));
		}
		
	  @Then("the child list response status code should be {int}") 
	  public void the_child_list_status_code_should_be(Integer expectedStatusCode) 
	  {
		  test.info("Validating status code: expected " + expectedStatusCode);
		  BaseMethods.validateStatusCode(res, expectedStatusCode, test); 
	   }
	  
	  @Then("all children should have the same {string}")
	  public void all_children_should_have_the_same(String user_id) {
	      String expectedUserId = GlobalTokenStore.getUserId(); // Fetch expected user ID

	      // Parse the response once
	      Object parsedResponse = APIUtils.parseJson(res);
	      
	      // Ensure the response is a List of Maps (children)
	      if (parsedResponse instanceof List) {
	          List<Map<String, Object>> children = (List<Map<String, Object>>) parsedResponse;
	          
	          // Loop through each child and assert that 'user_id' matches the expected user_id
	          for (Map<String, Object> child : children) {
	              Object actualUserId = child.get(user_id); // Get the 'user_id' value
	              
	              // Log the actual user ID for debugging
	              System.out.println("Actual User ID: " + actualUserId);
	              test.info("Child user_id: " + actualUserId);
	              
	              // Assert that all children have the same user_id
	              Assert.assertEquals("Mismatch in value for key '" + user_id + "'. Expected: " + expectedUserId + ", but found: " + actualUserId, expectedUserId, actualUserId);
	          }
	      } else {
	          throw new RuntimeException("Expected response to be a list of children, but received: " + parsedResponse.getClass().getSimpleName());
	      }
	  }

	  
	  
	  @Then("each child object should contain the following fields:") 
	  public void each_child_object_should_contain_the_following_fields(DataTable dataTable) 
	  { 
		  List<Map<String, Object>> children = APIUtils.parseJson(res);
	      List<String> expectedFields = dataTable.asList();
	      test.info("Validating presence of fields in each child object: " + expectedFields);

	        for (Map<String, Object> child : children) 
	        {
	            for (String field : expectedFields) 
	            {
	                test.info("Checking for field: " + field);
	                Assert.assertTrue("Field '" + field + "' is missing in child object: " + child, child.containsKey(field));
	            }
	        }
	  }
	  
	  
	  @Then("for each child, the following fields should not be null or empty:")
	  public void for_each_child_the_following_fields_should_not_be_null_or_empty(DataTable dataTable)
	  {
		  List<Map<String, Object>> children = APIUtils.parseJson(res);
		  List<String> fields = dataTable.asList();
	        test.info("Validating non-null and non-empty fields for each child: " + fields);
	        
		    for (Map<String, Object> child : children)
		    {
		        for (String field : fields) 
		        {
		            Object value = child.get(field);
		            test.info("Checking field '" + field + "' value: " + value);
		            Assert.assertNotNull("Field '" + field + "' is null in child: " + child, value);
		            Assert.assertTrue("Field '" + field + "' is empty in child: " + child, !value.toString().trim().isEmpty());
		        }
	        }
	    }

	  
	  @Then("for each child, the {string} should be equal to {string}") 
	  public void for_each_child_the_should_be_equal_to(String id, String child_info_id) 
	  {
		  List<Map<String, Object>> children = APIUtils.parseJson(res);
		  test.info("Validating each child's '" + id + "' equals '" + child_info_id + "'");
		  
	        for (Map<String, Object> child : children) {
	            Assert.assertEquals("Values of '" + id + "' and '" + child_info_id + "' do not match.", child.get(id), child.get(child_info_id));
	        }
	  }


	  @Then("the {string} field should be one of the following:") 
	  public void the_field_should_be_one_of_the_following(String field, DataTable dataTable) 
	  { 
		  List<String> validValues = dataTable.asList();
	      List<Map<String, Object>> children = APIUtils.parseJson(res);
	        
	        test.info("Validating field '" + field + "' contains one of: " + validValues);
	        for (Map<String, Object> child : children) 
	        {
	        	 Assert.assertTrue("Invalid value for '" + field + "' in child: " + child, validValues.contains(child.get(field).toString()));
	        }
	  }


	  @Then("the {string} should be a positive number for each child") 
	  public void the_should_be_a_positive_number_for_each_child(String enrollment_count) 
	  { 
		  List<Map<String, Object>> children = APIUtils.parseJson(res);
		  test.info("Validating '" + enrollment_count + "' is a positive number for each child");
		  
	      for (Map<String, Object> child : children) 
	      {
	    	  int value = Integer.parseInt(child.get(enrollment_count).toString());
	          test.info("Value of '" + enrollment_count + "': " + value);
	      Assert.assertTrue("Field " + enrollment_count + " should be a positive number", Integer.parseInt(child.get(enrollment_count).toString()) > 0);
	      }
	  }


	  @Then("the response status message should be {string}") 
	  public void the_response_status_message_should_be(String expectedStatusMessage) 
	  { 
		  
		  String actualStatusLine = res.getStatusLine();
		  test.info("Validating status message. Expected: " + expectedStatusMessage + ", Actual: " + actualStatusLine + " ");
	      Assert.assertNotNull("Status field is missing in response", actualStatusLine);
	      Assert.assertEquals("Status message mismatch", expectedStatusMessage, actualStatusLine);
	  }


	  @Then("the Reponse time should be less than {double} seconds.") 
	  public void the_reponse_time_should_be_less_than_seconds(Double seconds) 
	  { 
		  long responseTime = res.getTime();
		  test.info("Validating response time < " + seconds + " seconds. Actual time: " + (responseTime / 1000.0) + " seconds");
	      Assert.assertTrue("Expected response time < " + seconds + " seconds, but was " + (responseTime / 1000.0) + " seconds", responseTime < (seconds * 1000));
	  }

}
 	

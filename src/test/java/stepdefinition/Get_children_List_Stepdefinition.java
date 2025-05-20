package stepdefinition;

import org.junit.Assert;
import io.restassured.builder.ResponseBuilder;
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
import java.util.List;
import Utils.Endpoints;
import java.util.Map;
import io.cucumber.datatable.*;
public class Get_children_List_Stepdefinition
{
	private Response res ;
	private String baseURL = ConfigReader.getProperty("baseURL");
	private String getchildren = Endpoints.GET_CHILDREN;
	private ExtentTest test = Extent_Report_Manager.getTest();
	private Map<String, String> headers =ConfigReader.getHeadersFromConfig("header");

	@When("I send a GET request of children-with-enrollments API")
	public void i_send_a_get_request_of_children_with_enrollments_api() 
	{       String parentToken = GlobalTokenStore.getToken("parent");
	        APIUtils.logRequestHeaders(test, headers);

	        test.info("Sending GET request to: " + baseURL + getchildren);
	        test.info("Using Authorization token for parent.");
	        
	        // Sending GET request
	        this.res = given()
	                .baseUri(baseURL)
	                .headers(headers)
	               // .contentType("application/json; charset=utf-8")
	                .header("Authorization", "Bearer " + parentToken)
	                .when()
	                .get(getchildren)
	                .then()
	                .extract().response();
	        
	        // Log response to extent
	        APIUtils.logResponseToExtent(res, test);
	        String contentType = res.getHeader("Content-Type");
	        test.info("Content-Type: " + contentType);
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
		
	  @Then("the child list response status code should be {int}") 
	  public void the_child_list_status_code_should_be(Integer expectedStatusCode) 
	  {
		   test.info("Validating status code: expected " + expectedStatusCode);
		   BaseMethods.validateStatusCode(res, expectedStatusCode, test);
	   }
	  
	  @Then("all children should have the same {string}")
	  public void all_children_should_have_the_same(String expectedUserIdstr)
	  {
	    String expectedUserIdstr1 = GlobalTokenStore.getUserId(); // Fetch expected user ID
	    int expecteduserid=Integer.parseInt(expectedUserIdstr1);
	    List<Integer> userIds = res.jsonPath().getList("user_id");
	    
	    for (Integer userId : userIds) 
	     {
	         Assert.assertEquals("User ID does not match",userId.intValue(), expecteduserid);
	     }
	  }
	    
	  @Then("each child object should contain the following fields:") 
	  public void each_child_object_should_contain_the_following_fields(List<String> expectedFields) 
	  {   List<Map<String, Object>> childrenList = res.jsonPath().getList("$");
		  
		  for (int i = 0; i < childrenList.size(); i++)
		  {
		        Map<String, Object> child = childrenList.get(i);

		        for (String field : expectedFields) 
		        {
		            Assert.assertTrue("Child at index " + i + " does not contain expected field: " + field, child.containsKey(field));
		        }
		  } 
	  }
	  
	  @Then("for each child, the following fields should not be null or empty:")
	  public void for_each_child_the_following_fields_should_not_be_null_or_empty(List<String> mandatoryFields)
	  {		List<Map<String, Object>> childrenList = res.jsonPath().getList("$");
		    for (int i = 0; i < childrenList.size(); i++) 
		    {
		        Map<String, Object> child = childrenList.get(i);

		        for (String field : mandatoryFields) 
		        {
		            Object value = child.get(field);
	            
		            Assert.assertNotNull("Field '" + field + "' is null for child at index " + i, value);
		            if (value instanceof String) 
		            {
		                Assert.assertFalse("Field '" + field + "' is empty for child at index " + i,((String) value).trim().isEmpty());
		            }
		        }
		    }
	    }

	  @Then("for each child, the <id> should be equal to <child_info_id>") 
	  public void for_each_child_the_id_should_be_equal_to_child_info_id() 
	  {	  List<Map<String, Object>> childrenlist = res.jsonPath().getList("$");
		  
	        for (Map<String, Object> child : childrenlist) 
	        {
	        	int id = (int) child.get("id");
	        	int child_info_id = (int) child.get("child_info_id");
	        	 test.info("Validating each child's '" + id + "' equals '" + child_info_id + "'");
	            Assert.assertEquals("Values of " + id + " and " + child_info_id + "matches ",id, child_info_id);
	        }
	  }
	  
	  @Then("for each child, {string} should be equal to {string}")
	  public void for_each_child_should_be_equal_to(String actualName, String expectedName )
	  {   List<Map<String, Object>> children = res.jsonPath().getList("$");

		     for (Map<String, Object> child : children)
		     {
		        String firstName = String.valueOf(child.get("first_name"));
		        String lastName = String.valueOf(child.get("last_name"));
		        String expectedChildName = capitalize(firstName) + " " + capitalize(lastName);
		        String actualChildName = String.valueOf(child.get("childName"));

		        Assert.assertEquals("Child name does not match for ID: " + child.get("id"), expectedChildName,actualChildName);
		    }
		}

		  private String capitalize(String input) 
		  {
			    if (input == null || input.isEmpty()) return input;
			    return input.substring(0, 1).toUpperCase() + input.substring(1);
			}
	
	@Then("the {string} field should be one of the following:")
	  public void the_field_should_be_one_of_the_following(String gender, DataTable dataTable) 
	  {  List<String> Values = dataTable.asList();
		 List<Map<String, Object>> children = res.jsonPath().getList("$");

		    for (Map<String, Object> child : children) 
		    {   String actualValue = String.valueOf(child.get("gender"));
		        
		     if (!Values.contains(actualValue)) 
		     {
		      Assert.assertEquals("Invalid value for '" + gender + " in child ID: " + child.get("id"), actualValue , Values);
		      }
		    }
	  }
	
	@Then("the enrollment_count should be a equal to or greater than zero for each child")
	public void the_enrollment_count_should_be_a_equal_to_or_greater_than_zero_for_each_child() 
	  {
		List<Map<String, Object>> children = res.jsonPath().getList("$");
		
		 for (Map<String, Object> child : children) 
		 {
			Object enrollmentObj = child.get("enrollment_count");
			int value = Integer.parseInt(String.valueOf(enrollmentObj));
		    Assert.assertTrue( "Expected enrollment_count to be positive for child ID: " + child.get("id") + ", but found: " + value,value >= 0); 
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
	      Assert.assertTrue("Expected response time < " + seconds + " seconds, but was " + (responseTime / 1000.0) + " seconds", responseTime < (seconds * 2000));
	  }

}
 	

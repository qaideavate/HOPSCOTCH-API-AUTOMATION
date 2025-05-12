package stepdefinition;
import org.junit.Assert;

import Utils.APIUtils;
import Utils.BaseMethods;
import Utils.ConfigReader;
import Utils.Extent_Report_Manager;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import com.aventstack.extentreports.ExtentTest;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Get_children_List_Stepdefinition
{
	private Response res ;
	private String parentToken;
	private String baseurl ;
	private String getchildren;
	private ExtentTest test;
	private Map<String, String> headers;
	public static Map<String, String> userTokens = new HashMap<>();
	
	@Given("I log in with valid parent credentials")
	public void i_log_in_with_valid_parent_credentials() 
	{
	        test = Extent_Report_Manager.getTest();
	        String email = ConfigReader.getProperty("Parent_email");
	        String password = ConfigReader.getProperty("Parent_password");
	        String baseURL = ConfigReader.getProperty("ParentbaseloginUrl");
	        Map<String, String> headers = ConfigReader.getHeadersFromConfig("header");
	      

	        parentToken = BaseMethods.loginAndGetToken(email, password, baseURL,headers, "Parent", test); 
	        Assert.assertNotNull("Parent token should have been stored after login", parentToken);
	        System.out.println(parentToken);
	}

	@When("I send a GET request of children-with-enrollments API with the ParentToken")
	public int i_send_a_get_request_of_children_with_enrollments_API_with_the_parent_token() 
	{
		 this.headers = ConfigReader.getHeadersFromConfig("header");
		 APIUtils.logRequestHeaders(test, headers);
		 baseurl =ConfigReader.getProperty("baseURL");
		 getchildren=ConfigReader.getProperty("getChildren");
		   	  res = given()
		            .baseUri(baseurl)
		            .headers(headers)
		            .header("Authorization", "Bearer " + parentToken)
		            .when()
		            .get(getchildren)
		            .then()
		            .extract().response();
		     	
		 APIUtils.logResponseToExtent(res, test);
		 return res.jsonPath().getInt("childId");
	}
	
	/*
	 * @Then("the response body should match the predefined JSON schema {string}")
	 * public void the_response_body_should_match_the_predefined_json_schema(String
	 * schemaFileName) { res.then().assertThat() .body(matchesJsonSchemaInClasspath(
	 * "schema/children_with_enrollments_schema.json")); }
	 * 
	 * @Then("the response status code should be {int}") public void
	 * the_response_status_code_should_be(Integer expectedStatusCode) {
	 * BaseMethods.validateStatusCode(res, expectedStatusCode, test); }
	 * 
	 * @Then("all children should have the same {string}") public void
	 * all_children_should_have_the_same(String user_id) { List<Map<String, Object>>
	 * children = res.jsonPath().getList("$"); Object expectedValue =
	 * children.get(0).get(user_id);
	 * 
	 * for (Map<String, Object> child : children) { Object actualValue =
	 * child.get(user_id); Assert.assertEquals("Mismatch in value for key '" +
	 * user_id + "'. Expected: " + expectedValue + ", but found: " + actualValue,
	 * expectedValue, actualValue); } }
	 * 
	 * @Then("each child in the response should have the following fields:") public
	 * void each_child_in_the_response_should_have_the_following_fields(io.cucumber.
	 * datatable.DataTable dataTable) { // Write code here that turns the phrase
	 * above into concrete actions // For automatic transformation, change DataTable
	 * to one of // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or // Map<K,
	 * List<V>>. E,K,V must be a String, Integer, Float, // Double, Byte, Short,
	 * Long, BigInteger or BigDecimal. // // For other transformations you can
	 * register a DataTableType. throw new io.cucumber.java.PendingException(); }
	 * 
	 * @Then("for each child, the fields {string}, {string}, {string}, {string}, {string}, {string}, and {string} should not be null or empty"
	 * ) public void
	 * for_each_child_the_fields_and_should_not_be_null_or_empty(String string,
	 * String string2, String string3, String string4, String string5, String
	 * string6, String string7) { // Write code here that turns the phrase above
	 * into concrete actions throw new io.cucumber.java.PendingException(); }
	 * 
	 * @Then("for each child, the {string} should be equal to {string}") public void
	 * for_each_child_the_should_be_equal_to(String string, String string2) { //
	 * Write code here that turns the phrase above into concrete actions throw new
	 * io.cucumber.java.PendingException(); }
	 * 
	 * @Then("the {string} field should be one of the following:") public void
	 * the_field_should_be_one_of_the_following(String string,
	 * io.cucumber.datatable.DataTable dataTable) { // Write code here that turns
	 * the phrase above into concrete actions // For automatic transformation,
	 * change DataTable to one of // E, List<E>, List<List<E>>, List<Map<K,V>>,
	 * Map<K,V> or // Map<K, List<V>>. E,K,V must be a String, Integer, Float, //
	 * Double, Byte, Short, Long, BigInteger or BigDecimal. // // For other
	 * transformations you can register a DataTableType. throw new
	 * io.cucumber.java.PendingException(); }
	 * 
	 * @Then("the {string} should be a positive number for each child") public void
	 * the_should_be_a_positive_number_for_each_child(String string) { // Write code
	 * here that turns the phrase above into concrete actions throw new
	 * io.cucumber.java.PendingException(); }
	 * 
	 * @Then("the response status message should be {string}") public void
	 * the_response_status_message_should_be(String string) { // Write code here
	 * that turns the phrase above into concrete actions throw new
	 * io.cucumber.java.PendingException(); }
	 * 
	 * @Then("the Reponse time should be less than {double} seconds.") public void
	 * the_reponse_time_should_be_less_than_seconds(Double double1) { // Write code
	 * here that turns the phrase above into concrete actions throw new
	 * io.cucumber.java.PendingException(); }
	 */
}
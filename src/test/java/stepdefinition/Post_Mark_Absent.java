package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.*;
import static io.restassured.RestAssured.*;
public class Post_Mark_Absent 
{
    private String baseUrl;
    private String providerToken;
    private Map<String, String> headers;
    private Response response;
    private Map<String, Object> requestBody;
    private ExtentTest test;
    Payload Pl = new Payload();
    public Post_Mark_Absent() 
    {
        this.test = Extent_Report_Manager.getTest();
        this.providerToken = GlobalTokenStore.getToken("provider");
        this.headers = ConfigReader.getHeadersFromConfig("header");
    }

	@When("When I send a POST request to providers/enrollments/mark-absent")
	public void I_send_Post_Request_To_MarkAbsent(int enrollmentId)
	    {
			String endpoint = "/providers/enrollments/mark-absent";
			requestBody = Pl.MarkingAbsence(enrollmentId);
			test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL +" "+Endpoints.mark_absent);
			test.log(Status.INFO, "Prepared request body: " + requestBody.toString());
			APIUtils.logRequestHeaders(test, headers);

	        response = given()
	                .baseUri(baseUrl)
	                .headers(headers)
	                .body(requestBody)
	                .when()
	                .post(endpoint);

	        APIUtils.logResponseToExtent(response, test);
	        System.out.println("API Response:\n" + response.asPrettyString());
	    }

	    @Then("the response status code should be {int}")
	    public void theResponseStatusCodeShouldBe(int expectedCode)
		{
	        int actualCode = response.getStatusCode();
	        Assert.assertEquals("Unexpected status code!", expectedCode, actualCode);
	        test.pass("Validated status code: " + actualCode);
	    }

	    @Then("the response body should contain {string} as true")
	    public void theResponseBodyShouldContainKeyAsTrue(String key)
		{
	        boolean value = response.jsonPath().getBoolean(key);
	        Assert.assertTrue("Expected " + key + " to be true", value);
	        test.pass("Key " + key + " is true in the response");
	    }

	    @Then("the response body should contain the keyvalue {string}")
	    public void theResponseBodyShouldContainKeyvalue(String key)
		{
	        Object value = response.jsonPath().get(key);
	        Assert.assertNotNull("Expected response to contain key: " + key, value);
	        test.pass("Response contains key: " + key + " with value: " + value);
	    }

}
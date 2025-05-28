package stepdefinition;

import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class Put_Enrollment_Status
{
    String childId ;
    Response res;
    Map<String, String> requestBody;
    private Map<String, String> headers;
    String endpoint;
    private ExtentTest test;
    private String contentType="application/json";

    public Put_Enrollment_Status()
    {
        this.headers = ConfigReader.getHeadersFromConfig("header");
        this.test = Extent_Report_Manager.getTest();
    }

    @When("I send a PUT request to endpoint with {string} and status {string}")
    public void i_send_a_put_request_to_endpoint_with_and_status(String childId, String status) 
    {	
    	 requestBody = new HashMap<>();
    	 requestBody.put("enrollment_status", status);
    	
        String providerToken =GlobalTokenStore.getToken("provider");
        
        APIUtils.logRequestHeaders(test, headers);
        APIUtils.logRequestBody(test, requestBody);
        
        test.info("Sending POST request to endpoint: " + Endpoints.CHANGE_ENROLLMERNT_STATUS);
        System.out.println(requestBody);
        res = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .contentType(contentType)
                .header("Authorization", "Bearer " + providerToken)
                .body(requestBody)
                .when()
                .put(Endpoints.CHANGE_ENROLLMERNT_STATUS + childId);

        test.info("Received response: " + res.asString());
        System.out.println(res.asPrettyString());
        APIUtils.logResponseToExtent(res, test);
    }

    @Then("the update Enrollment status code  should be {int}")
    public void the_update_Enrollment_status_code_should_be(Integer Statuscode)
    {
        test.info("Validating response status code. Expected: " + Statuscode + ", Actual: " + res.getStatusCode());
        BaseMethods.validateStatusCode(res, Statuscode, test);
    }

    @And("the response body should contain:")
    public void theResponseBodyShouldContain(DataTable dataTable)
    {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows)
        {
            for (Map.Entry<String, String> entry : row.entrySet())
            {
                String expectedKey = entry.getKey();
                String expectedValue = entry.getValue();

                String actualValue = res.jsonPath().getString(expectedKey);
                Assert.assertEquals("Mismatch for key: " + expectedKey, expectedValue, actualValue);
            }
        }

    }
}

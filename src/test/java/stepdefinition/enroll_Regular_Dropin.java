package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class enroll_Regular_Dropin
{
    private String parentToken;
    private Map<String, String> headers;
    private Map<String, Object> requestBody;
    private Response response;
    private ExtentTest test = Extent_Report_Manager.getTest();
    Payload Pl = new Payload();

    public enroll_Regular_Dropin()
    {
        parentToken = GlobalTokenStore.getToken("parent");
        headers = ConfigReader.getHeadersFromConfig("header");
    }

    @When("I send a POST request to enroll-Dropin child API with valid body")
    public void i_send_a_post_request_to_enroll_dropin_child_api_with_valid_body()
    {
        int providerId = Integer.parseInt(ConfigReader.getProperty("provider_id"));
        int classroomId = Integer.parseInt(ConfigReader.getProperty("classroom_id"));
        int childId = Integer.parseInt(ConfigReader.getProperty("CHILDID1"));
        String startDate = ConfigReader.getProperty("start_date");
        requestBody = Pl.getEnrollDropinChildPayload(providerId, classroomId, childId, startDate);

        test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL + Endpoints.enroll_dropin);
        test.log(Status.INFO, "Request body: " + requestBody);

        response = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + parentToken)
                .body(requestBody)
                .when()
                .post(Endpoints.enroll_dropin + "?t=" + System.currentTimeMillis());

        APIUtils.logResponseToExtent(response, test);
        System.out.println(response.prettyPrint());
    }

    @Then("the response status code for Enrollchild should be {int}")
    public void the_response_status_code_for_Enrollchild_should_be(Integer expectedStatusCode) 
    {
        Assert.assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
        test.pass("Validated status code: " + response.getStatusCode());
    }

    @Then("the response body should contain {string} as {string}")
    public void the_response_body_should_contain_key_as_value(String key, String value) 
    {
        Assert.assertEquals(value, response.jsonPath().getString(key));
        test.pass("Response contains " + key + " as " + value);
    }
    
    @When("I send a POST request to enroll-regular child API with valid body")
    public void i_send_a_post_request_to_enroll_regular_child_api_with_valid_body() 
    {
    	int providerId = Integer.parseInt(ConfigReader.getProperty("provider_id"));
        int classroomId = Integer.parseInt(ConfigReader.getProperty("classroom_id"));
        int childId = Integer.parseInt(ConfigReader.getProperty("CHILDID2"));
        String startDate = ConfigReader.getProperty("start_date");
        requestBody = Pl.getEnrollRegularChildPayload(providerId, classroomId, childId, startDate);

        test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL +Endpoints.enroll_regular);
        test.log(Status.INFO, "Request body: " + requestBody);

        response = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + parentToken)
                .body(requestBody)
                .when()
                .post(Endpoints.enroll_regular + "?t=" + System.currentTimeMillis());

        APIUtils.logResponseToExtent(response, test);
        System.out.println(response.prettyPrint());
    }

}
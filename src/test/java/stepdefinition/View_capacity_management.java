package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import org.junit.Assert;
import static io.restassured.RestAssured.*;
import java.util.Map;

public class View_capacity_management
{
    private final String endpoint = Endpoints.PROVIDER_CAPACITY;
    private Response res;
    private ExtentTest test = Extent_Report_Manager.getTest();
    private Map<String, String> headers = ConfigReader.getHeadersFromConfig("header");

    @When("I send a GET request to view classroom capacity at classroom endpoint with classroom Id")
    public void i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id()
    {
        String ProviderToken = GlobalTokenStore.getToken("provider");
        int classroomId = Integer.parseInt(ConfigReader.getProperty("classroomId"));
        res = given()
                .baseUri(Endpoints.baseURL)
                .pathParam("classroomId", classroomId)
                .header("Authorization", "Bearer " + ProviderToken)
                .when()
                .get(endpoint + "/{classroomId}")
                .then()
                .extract()
                .response();
        System.out.println(res.prettyPrint());
    }

    @Then("the response status code for classroom should be {int}")
    public void the_response_status_code_for_classroom_should_be(Integer expectedStatusCode)
    {
        BaseMethods.validateStatusCode(res, expectedStatusCode, test);
        Assert.assertEquals(expectedStatusCode.intValue(), res.getStatusCode());
    }
}

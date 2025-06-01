package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import org.junit.Assert;
import static io.restassured.RestAssured.*;

public class Get_capacity_management
{
    private Response res;
    private ExtentTest test = Extent_Report_Manager.getTest();

    @When("I send a GET request to view classroom capacity at classroom endpoint with classroom Id")
    public void i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id()
    {
    	String providerToken = ConfigReader.getProperty("ProviderToken");
        if (providerToken == null ||providerToken.trim().isEmpty()) 
        {
            BaseMethods.parentLogin();  
            providerToken = ConfigReader.getProperty("ProviderToken");
        }
        int classroomId = Integer.parseInt(ConfigReader.getProperty("classroomId"));
        res = given()
                .baseUri(Endpoints.baseURL)
                .pathParam("classroomId", classroomId)
                .header("Authorization", "Bearer " + providerToken)
                .when()
                .get(Endpoints.PROVIDER_CAPACITY + "/{classroomId}")
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

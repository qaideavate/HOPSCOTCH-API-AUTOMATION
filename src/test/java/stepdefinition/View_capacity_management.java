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
    
    
    @When("I send a GET request to the endpoint with path parameter {int}")
    public void i_Send_A_GET_Request_To_The_Endpoint_With_Path_Parameter(int classroomId)
    {
        String ProviderToken=GlobalTokenStore.getToken("provider");
              res = given()
              .baseUri(Endpoints.baseURL)
              .pathParam("classroomId", classroomId)
              .header("Authorization", "Bearer " + ProviderToken)
               .when()
               .get(endpoint + "/{classroomId}");
                System.out.println(res.prettyPrint());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode)
    {
        BaseMethods.validateStatusCode(res, expectedStatusCode, test);
        Assert.assertEquals(expectedStatusCode.intValue(), res.getStatusCode());
    }

    @And("the classroom should contain a schedule_capacity object")
    public void the_Classroom_Should_Contain_A_Schedule_capacity_Object()
    {
        Assert.assertNotNull("schedule_capacity is missing", res.jsonPath().get("[0].schedule_capacity"));
    }

    @Then("the classroom object should have the following fields:")
    public void the_Classroom_Object_Should_Have_The_Following_Fields(Map<String, String> expectedFields)
    {
        for (Map.Entry<String, String> entry : expectedFields.entrySet())
        {
            String actual =  res.jsonPath().getString("[0]." + entry.getKey());
            Assert.assertEquals(entry.getKey() + " mismatch", entry.getValue(), actual);
        }
    }

    @And("the full_time_enrollment should be {int}")
    public void theFull_time_enrollment_Should_Be(Integer expectedValue)
    {
        Integer actualValue = res.jsonPath().getInt("[0].schedule_capacity.full_time_enrollment");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Then("the dropin_capacity should contain today and tomorrow dates")
    public void the_dropin_capacity_should_contain_today_and_tomorrow_dates()
    {
        Assert.assertNotNull(res.jsonPath().get("[0].schedule_capacity.dropin_capacity.today"));
        Assert.assertNotNull(res.jsonPath().get("[0].schedule_capacity.dropin_capacity.tomorrow"));
    }

    @Then("the today dropin capacity should have:")
    public void the_today_dropin_capacity_should_have(Map<String, String> expectedTodayFields)
    {
        for (Map.Entry<String, String> entry : expectedTodayFields.entrySet())
        {
            String actual = res.jsonPath().getString("[0].schedule_capacity.dropin_capacity.today." + entry.getKey());
            Assert.assertEquals(entry.getKey() + " mismatch", entry.getValue(), actual);
        }
    }

    @Then("the tomorrow dropin capacity should have:")
    public void the_tomorrow_dropin_capacity_should_have(Map<String, String> expectedTomorrowFields)
    {
        for (Map.Entry<String, String> entry : expectedTomorrowFields.entrySet())
        {
            String actual = res.jsonPath().getString("[0].schedule_capacity.dropin_capacity.tomorrow." + entry.getKey());
            Assert.assertEquals(entry.getKey() + " mismatch", entry.getValue(), actual);
        }
    }

    @Then("the classroom object in response should have {string} equal to {int}")
    public void the_classroom_object_in_response_should_have_equal_to(String id, Integer expectedValue)
    {
        long actualValue = res.jsonPath().getLong("[0]." + id);
        Assert.assertEquals(id + " mismatch", expectedValue.longValue(), actualValue);
    }
}

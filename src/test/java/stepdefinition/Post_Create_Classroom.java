package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class Post_Create_Classroom
{
    String Baseurl;
    String ProviderToken;
    private Map<String, Object> payload;
    private Response res;
    private ExtentTest test;
    Faker faker = new Faker();
    private Map<String, String> headers;
    String todayDate = LocalDate.now().toString();

    public Post_Create_Classroom()
    {
        this.test = Extent_Report_Manager.getTest();
        this.Baseurl= ConfigReader.getProperty("baseURL");
        this.ProviderToken = GlobalTokenStore.getToken("provider");
        this.headers = ConfigReader.getHeadersFromConfig("header");
        test.info("Decoded JWT: " + BaseMethods.decodeJWT(ProviderToken));
    }

    @Given("I prepare a valid request body for creating a classroom")
    public void iPrepareAValidRequestBodyForCreatingClassroom()
    {   // Get today's date in ISO format (yyyy-MM-dd)
        String todayDate = LocalDate.now().toString();

        payload = new HashMap<>();
        payload.put("name", "class " + faker.lorem().word());
        payload.put("min_age", 15);
        payload.put("max_age", 30);
        payload.put("start_date", todayDate);
        payload.put("license_capacity", 7);
        payload.put("capacity", 4);
        payload.put("enrollment_cutoff_window", "7");
        payload.put("fulltime_tuition", 10);
        payload.put("fulltime_tuition_cadence", "weekly");
        payload.put("fulltime_tuition_status", true);
        payload.put("mornings_tuition_status", false);
        payload.put("afternoons_tuition_status", false);
        payload.put("mwf_tuition_status", false);
        payload.put("tuth_tuition_status", false);
        payload.put("dropin_tution_status", false);
        payload.put("keywords", "");

        List<Map<String, Integer>> allocations = new ArrayList<>();
        allocations.add(new HashMap<String, Integer>() {{ put("position_id", 1); put("staff_id", 12); }});
        allocations.add(new HashMap<String, Integer>() {{ put("position_id", 2); put("staff_id", 14); }});
        allocations.add(new HashMap<String, Integer>() {{ put("position_id", 3); put("staff_id", 13); }});
        allocations.add(new HashMap<String, Integer>() {{ put("position_id", 4); put("staff_id", 15); }});
        payload.put("allocations", allocations);

        test.log(Status.INFO, "Prepared payload: " + payload.toString());
    }

    @When("I send a POST request to the {string} endpoint")
    public void iSendPostRequest(String endpoint)
    {    test.log(Status.INFO, "Sending POST request to: " + endpoint);
         APIUtils.logRequestHeaders(test, headers);
         APIUtils.logRequestBody(test, payload.toString());
          res = given()
                .baseUri(Baseurl)
                .headers(headers)
                .header("Authorization", "Bearer " + ProviderToken)
                .body(payload)
                .when()
                .post(endpoint);

        test.log(Status.INFO, "Request Payload: " + payload.toString());
        APIUtils.logResponseToExtent(res, test);
        test.log(Status.INFO, "Response: " + res.asString());
        System.out.println(res);
    }

    @Then("the response status code on Creating Classroom  should be {int}")
    public void theResponseStatusCodeOnCreatingClassroomShouldBe(int expectedStatusCode)
    {
        test.info("Validating Status Code. Expected: " + expectedStatusCode + ", Actual: " + res.getStatusCode());
        BaseMethods.validateStatusCode(res, expectedStatusCode, test);
    }

    @Then("the response body should contain {string} as true")
    public void responseBodyShouldContainSuccessAsTrue(String key)
    {
        boolean value = res.jsonPath().getBoolean(key);
        Assert.assertTrue(" Expected " + key + "to be true", value);
        test.pass("Key " + key + " is true in response");
    }

    @Then("the response body should contain the key {string}")
    public void responseBodyShouldContainKey(String key)
    {
        Object value = res.jsonPath().get(key);
        Assert.assertNotNull("Expected response to contain key: " + key, value);
        test.pass("Response contains key: " + key + " with value: " + value);
    }

    @Given("I prepare a request body with the following values:")
    public void iPrepareRequestBodyWithDataTable(DataTable dataTable)
    {
        Map<String, String> inputData = dataTable.asMap(String.class, String.class);
        payload = new HashMap<>();
        // Use values from the data table or generate defaults using Faker
        payload.put("name", inputData.getOrDefault("name", "Program_" + faker.lorem().word()));
        payload.put("min_age", parseOrDefault(inputData.get("min_age"), 15));
        payload.put("max_age", parseOrDefault(inputData.get("max_age"), 30));
        payload.put("start_date", inputData.getOrDefault("start_date", todayDate));
        payload.put("license_capacity", parseOrDefault(inputData.get("license_capacity"), 7));
        payload.put("capacity", parseOrDefault(inputData.get("capacity"), 4));
        payload.put("enrollment_cutoff_window", inputData.getOrDefault("enrollment_cutoff_window", "7"));
        payload.put("fulltime_tuition", parseOrDefault(inputData.get("fulltime_tuition"), 10));
        payload.put("fulltime_tuition_cadence", inputData.getOrDefault("fulltime_tuition_cadence", "weekly"));
        payload.put("fulltime_tuition_status", parseBooleanOrDefault(inputData.get("fulltime_tuition_status"), true));
        payload.put("mornings_tuition_status", parseBooleanOrDefault(inputData.get("mornings_tuition_status"), false));
        payload.put("afternoons_tuition_status", parseBooleanOrDefault(inputData.get("afternoons_tuition_status"), false));
        payload.put("mwf_tuition_status", parseBooleanOrDefault(inputData.get("mwf_tuition_status"), false));
        payload.put("tuth_tuition_status", parseBooleanOrDefault(inputData.get("tuth_tuition_status"), false));
        payload.put("dropin_tution_status", parseBooleanOrDefault(inputData.get("dropin_tution_status"), false));
        payload.put("keywords", inputData.getOrDefault("keywords", ""));

        List<Map<String, Integer>> allocations = new ArrayList<>();

       // Generate 4 allocation entries using Faker
        for (int i = 1; i <= 4; i++)
        {
            Map<String, Integer> allocation = new HashMap<>();
            allocation.put("position_id", i); // You can randomize this too if needed
            allocation.put("staff_id", faker.number().numberBetween(10, 20)); // Random staff_id between 10 and 19
            allocations.add(allocation);
        }
        payload.put("allocations", allocations);

        test.log(Status.INFO, "Prepared payload from DataTable: " + payload.toString());
    }
    // Helper method to parse integer or return default
    private int parseOrDefault(String value, int defaultValue)
    {   try
        {
            return value == null || value.isEmpty() ? defaultValue : Integer.parseInt(value);
        }   catch (NumberFormatException e)
        {    test.warning("Invalid integer for value: '" + value + "', defaulting to: " + defaultValue);
            return defaultValue;
        }
    }

    // Helper method to parse boolean or return default
    private boolean parseBooleanOrDefault(String value, boolean defaultValue)
    {
        if (value == null || value.isEmpty()) return defaultValue;
        return Boolean.parseBoolean(value);
    }
}


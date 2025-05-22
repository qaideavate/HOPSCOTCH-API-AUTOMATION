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
import java.util.Arrays;
import java.util.Collections;
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

        payload = new HashMap<>();
        payload.put("name", "Test class " + faker.number().randomNumber());
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
        payload.put("dropin_tuition_status", false);
        payload.put("keywords", "");

        payload.put("allocations", generateUniqueAllocations());

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
        System.out.println(res.asPrettyString());
        System.out.println(payload.toString());
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
        payload.put("name", resolveName(inputData.get("name")));
        payload.put("min_age", parseOrFaker(inputData.get("min_age"), 1, 50));
        payload.put("max_age", parseOrFaker(inputData.get("max_age"), 51, 100));
        payload.put("start_date", inputData.getOrDefault("start_date", todayDate));
        payload.put("license_capacity", parseOrFaker(inputData.get("license_capacity"), 1, 10));
        payload.put("capacity", parseOrFaker(inputData.get("capacity"), 11, 50));
        payload.put("enrollment_cutoff_window", parseOrFaker(inputData.get("enrollment_cutoff_window"), 7, 56));
        payload.put("fulltime_tuition", parseOrFaker(inputData.get("fulltime_tuition"), 12, 25));
        payload.put("fulltime_tuition_cadence", inputData.getOrDefault("fulltime_tuition_cadence", "weekly"));
        payload.put("fulltime_tuition_status", parseBooleanOrFaker(inputData.get("fulltime_tuition_status")));
        payload.put("mornings_tuition_status", parseBooleanOrFaker(inputData.get("mornings_tuition_status")));
        payload.put("afternoons_tuition_status", parseBooleanOrFaker(inputData.get("afternoons_tuition_status")));
        payload.put("mwf_tuition_status", parseBooleanOrFaker(inputData.get("mwf_tuition_status")));
        payload.put("tuth_tuition_status", parseBooleanOrFaker(inputData.get("tuth_tuition_status")));
        payload.put("dropin_tuition_status", parseBooleanOrFaker(inputData.get("dropin_tution_status")));
        payload.put("keywords", inputData.getOrDefault("keywords", ""));
        
        payload.put("allocations", generateUniqueAllocations());
        test.log(Status.INFO, "Prepared payload from DataTable: " + payload.toString());
    }
    // Helper method to parse integer or return default
     private int parseOrFaker(String value, int min, int max) 
     {
        try {
            return (value == null || value.trim().isEmpty()) ? faker.number().numberBetween(min, max): Integer.parseInt(value.trim());
        } 
        catch (NumberFormatException e)
        {
            return faker.number().numberBetween(min, max);
        }
    }

     private boolean parseBooleanOrFaker(String value) 
     {
    	    if (value == null || value.trim().isEmpty()) {
    	        return faker.bool().bool(); // Random true or false
    	    }
    	    return Boolean.parseBoolean(value);
    	}
    
    private String resolveName(String nameInput) 
    {
        if (nameInput == null || nameInput.trim().isEmpty() || nameInput.equalsIgnoreCase("[empty]")) {
            return "Test class " + faker.number().randomNumber();
        }
        return nameInput;
    }
    
    private List<Map<String, Integer>> generateUniqueAllocations()
    {
        List<Integer> staffIds = new ArrayList<>(Arrays.asList(12, 13, 14, 15));
        Collections.shuffle(staffIds);
        List<Map<String, Integer>> allocations = new ArrayList<>();
        for (int i = 0; i < staffIds.size(); i++) {
            Map<String, Integer> allocation = new HashMap<>();
            allocation.put("position_id", i + 1);
            allocation.put("staff_id", staffIds.get(i));
            allocations.add(allocation);
        }
        return allocations;
    }
}


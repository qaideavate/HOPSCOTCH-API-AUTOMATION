package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class Post_Create_Classroom
{
    String Baseurl;
    String ProviderToken;
    private Response res;
    private ExtentTest test;
    Faker faker = new Faker();
    private Map<String, String> headers;
    String todayDate = LocalDate.now().toString();
    Payload Pl = new Payload();

    public Post_Create_Classroom()
    {
        this.test = Extent_Report_Manager.getTest();
        this.ProviderToken = ConfigReader.getProperty("ProviderToken");
        if (this.ProviderToken == null || this.ProviderToken.trim().isEmpty()) 
        {
            BaseMethods.providerLogin();  
            ConfigReader.waitAndReloadConfig(3000);
            this.ProviderToken = ConfigReader.getProperty("ProviderToken");
        }
        this.headers = ConfigReader.getHeadersFromConfig("header");
        test.info("Decoded JWT: " + BaseMethods.decodeJWT(ProviderToken));
    }

    @When("I send a POST request to create classroom at classroom endpoint")
    public void i_send_a_post_request_to_create_classroom_at_classroom_endpoint()
    {   String payload = Pl.classroom_payload(); 
    	
    	test.log(Status.INFO, "Sending POST request to: " + Endpoints.create_classroom);
         APIUtils.logRequestHeaders(test, headers);
         APIUtils.logRequestBody(test, payload.toString());
         System.out.println(payload);
          res = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + ProviderToken)
                .body(payload)
                .when()
                .post(Endpoints.create_classroom);

        test.log(Status.INFO, "Request Payload: " + payload.toString());
        APIUtils.logResponseToExtent(res, test);
        System.out.println(res.asPrettyString());
    }
    

    @Then("the response status code on Creating Classroom  should be {int}")
    public void theResponseStatusCodeOnCreatingClassroomShouldBe(int expectedStatusCode)
    {
        test.info("Validating Status Code. Expected: " + expectedStatusCode + ", Actual: " + res.getStatusCode());
        BaseMethods.validateStatusCode(res, expectedStatusCode, test);
    }

    @Then("the response body for classroom should contain {string} as true")
    public void responseBodyforclassroomShouldContainSuccessAsTrue(String key)
    {
        boolean value = res.jsonPath().getBoolean(key);
        Assert.assertTrue(" Expected " + key + "to be true", value);
        test.pass("Key " + key + " is true in response");
    }

    @Then("the response body should contain the key {string}")
    public void responseBodyShouldContainKey(String key)
    {
        Object classroomId = res.jsonPath().getString("classroomId");
        Assert.assertNotNull("Expected response to contain key: " + key, classroomId);
        test.pass("Response contains key: " + key + " with value: " + classroomId);
        
        if ("classroomId".equals(key))
        {
            Map<String, String> properties = new HashMap<>();
            properties.put("classroomId", String.valueOf(classroomId));
            ConfigReader.writeMultipleProperties(properties);
        }
    }
}


package stepdefinition;
import Utils.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class enroll_Regular_Dropin
{
    private String parentToken;
    private Map<String, String> headers;
    private Response response;
    private ExtentTest test = Extent_Report_Manager.getTest();
    Payload Pl = new Payload();

    public enroll_Regular_Dropin()
    {
        parentToken = GlobalTokenStore.getToken("parent");
        headers = ConfigReader.getHeadersFromConfig("header");
    }

    @When("I send a POST request to enroll-Dropin child {string} API with valid body")
    public void i_send_a_post_request_to_enroll_dropin_child_api_with_valid_body(String childIdKey)
    {
    	ConfigReader.waitAndReloadConfig(3000);
    	String providerIdStr = ConfigReader.getProperty("provider_id");
        String classroomIdStr = ConfigReader.getProperty("classroomId");
        String childIdStr = ConfigReader.getProperty(childIdKey);
        String startDate = ConfigReader.getProperty("Today");

        if (providerIdStr == null || classroomIdStr == null || childIdStr == null || startDate == null) {
            throw new RuntimeException("❌ Missing config values: provider_id=" + providerIdStr +
                    ", classroomId=" + classroomIdStr + ", " + childIdKey + "=" + childIdStr + ", start_date=" + startDate);
        }

        int providerId = Integer.parseInt(providerIdStr.trim());
        int classroomId = Integer.parseInt(classroomIdStr.trim());
        int childId = Integer.parseInt(childIdStr.trim());

        System.out.println("✅ Dropping child with ID: " + childId);
        System.out.println("classroomid :" +classroomId);

        Map<String, Object> requestBody = Pl.getEnrollDropinChildPayload(providerId, classroomId, childId, startDate);

        test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL + Endpoints.enroll_dropin);
        test.log(Status.INFO, "Request body: " + requestBody);
        System.out.println("payload :" +requestBody);
        System.out.println(requestBody.toString());
        response = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + parentToken)
                .body(requestBody)
                .when()
                .post(Endpoints.enroll_dropin + "?t=" + System.currentTimeMillis());
       
        System.out.println("Response: " + response.asString());
        boolean isSuccess = false;
        try {
            isSuccess = response.jsonPath().getBoolean("success");
        } catch (Exception e) {
            System.out.println("⚠️ Unable to parse success field: " + e.getMessage());
        }

        if (isSuccess) {
            Integer enrollmentId = response.jsonPath().get("data");

            if (enrollmentId != null) {
                Map<String, String> enrollmentMap = new HashMap<>();
                enrollmentMap.put("enrollmentId_" + childIdKey, String.valueOf(enrollmentId));
                ConfigReader.writeMultipleProperties(enrollmentMap);
                System.out.println("✅ Enrollment ID saved: " + enrollmentId);
            } else {
                System.out.println("❌ Enrollment ID is missing in success response.");
            }
        } else {
            String err = response.jsonPath().getString("error");
            System.out.println("❌ API call failed. Error: " + err);
            throw new RuntimeException("Drop-in enrollment failed. Error from API: " + err);
        }
        
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
    
    //regular Enroll child
    @When("I send a POST request to enroll-regular child {string} API with valid body {string}")
    public void i_send_a_post_request_to_enroll_regular_child_api_with_valid_body(String childIdKey, String dateKey)
    {
        String providerIdStr = ConfigReader.getProperty("provider_id");
        String classroomIdStr = ConfigReader.getProperty("classroomId");
        String childIdStr = ConfigReader.getProperty(childIdKey);
        String startDate = ConfigReader.getProperty(dateKey);

        if (providerIdStr == null || classroomIdStr == null || childIdStr == null || startDate == null) {
            throw new RuntimeException("❌ Missing config values: provider_id=" + providerIdStr +
                    ", classroomId=" + classroomIdStr + ", " + childIdKey + "=" + childIdStr + ", start_date=" + startDate);
        }

        int providerId = Integer.parseInt(providerIdStr.trim());
        int classroomId = Integer.parseInt(classroomIdStr.trim());
        int childId = Integer.parseInt(childIdStr.trim());

        System.out.println("✅ Enrolling child with ID: " + childId);
        System.out.println("classroomid :" +classroomId);

        Map<String, Object> requestBody = Pl.getEnrollRegularChildPayload(providerId, classroomId, childId, startDate);

        test.log(Status.INFO, "Sending POST request to: " + Endpoints.baseURL + Endpoints.enroll_regular);
        test.log(Status.INFO, "Request body: " + requestBody);
        System.out.println("payload :" +requestBody);
        System.out.println(requestBody.toString());
        response = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + parentToken)
                .body(requestBody)
                .when()
                .post(Endpoints.enroll_regular + "?t=" + System.currentTimeMillis());
        
        System.out.println("Response: " + response.asString());
        Integer enrollmentId = response.jsonPath().get("childEnrollmentId");
        
        if (enrollmentId != null) {
            Map<String, String> enrollmentMap = new HashMap<>();
            enrollmentMap.put("EnrollmentId_" + childIdKey, String.valueOf(enrollmentId));
            ConfigReader.writeMultipleProperties(enrollmentMap);

            System.out.println("✅ Enrollment ID saved for " + childIdKey + ": " + enrollmentId);
        } else {
            System.out.println("❌ 'childEnrollmentId' is missing or null in the response for " + childIdKey);
            // Optionally fail the test or throw an exception
            throw new AssertionError("'childEnrollmentId' not found in response");
        }

        System.out.println("✅ Enrollment ID saved for " + childIdKey + ": " + enrollmentId);
        
        APIUtils.logResponseToExtent(response, test);
        System.out.println(response.prettyPrint());
    }
}
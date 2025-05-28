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

    @When("I send a POST request to enroll-Dropin child API with valid body")
    public void i_send_a_post_request_to_enroll_dropin_child_api_with_valid_body()
    {
    	 ConfigReader.reloadProperties();
    	 String providerIdStr = ConfigReader.getProperty("provider_id");
         String classroomIdStr = ConfigReader.getProperty("classroomId");
         String childIdStr = ConfigReader.getProperty("CHILDID3");
         String startDate = ConfigReader.getProperty("start_date");

         // Validate all required fields
         if (providerIdStr == null || classroomIdStr == null || childIdStr == null || startDate == null) 
         {
             throw new RuntimeException("❌ Missing one or more required config values: "
                 + "provider_id=" + providerIdStr + ", classroomId=" + classroomIdStr 
                 + ", CHILDID3=" + childIdStr + ", start_date=" + startDate);
         }

         // Parse safely
         int providerId = Integer.parseInt(providerIdStr.trim());
         int classroomId = Integer.parseInt(classroomIdStr.trim());
         int childId = Integer.parseInt(childIdStr.trim());

         // Log values
         System.out.println("providerId = " + providerId);
         System.out.println("classroomId = " + classroomId);
         System.out.println("childId = " + childId);
         System.out.println("startDate = " + startDate);

         Map<String, Object> requestBody = Pl.getEnrollDropinChildPayload(providerId, classroomId, childId, startDate);

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
    
    //regular Enroll child
    @When("I send a POST request to enroll-regular child {string} API with valid body")
    public void i_send_a_post_request_to_enroll_regular_child_api_with_valid_body(String childIdKey)
    {
        String providerIdStr = ConfigReader.getProperty("provider_id");
        String classroomIdStr = ConfigReader.getProperty("classroomId");
        String childIdStr = ConfigReader.getProperty(childIdKey);
        String startDate = ConfigReader.getProperty("start_date");

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

        response = given()
                .baseUri(Endpoints.baseURL)
                .headers(headers)
                .header("Authorization", "Bearer " + parentToken)
                .body(requestBody)
                .when()
                .post(Endpoints.enroll_regular + "?t=" + System.currentTimeMillis());
        
        int enrollmentId = response.jsonPath().getInt("childEnrollmentId");
        Map<String, String> enrollmentMap = new HashMap<>();
        enrollmentMap.put("enrollmentId_" + childIdKey, String.valueOf(enrollmentId));
        ConfigReader.writeMultipleProperties(enrollmentMap);

        System.out.println("✅ Enrollment ID saved for " + childIdKey + ": " + enrollmentId);
        
        APIUtils.logResponseToExtent(response, test);
        System.out.println(response.prettyPrint());
    }
}
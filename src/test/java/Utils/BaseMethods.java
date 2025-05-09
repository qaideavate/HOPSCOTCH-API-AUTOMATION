package Utils;

import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class BaseMethods 
{
    public static void validateStatusCode(Response res, int expectedCode, ExtentTest test)
    {
        int actualCode = res.getStatusCode();
        test.info("Asserting status code. Expected: " + expectedCode + ", Actual: " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedCode, actualCode);
    }

    
    public static void validateStatusCodeNotEqual(Response res, int unexpectedCode, ExtentTest test) 
    {
        int actualCode = res.getStatusCode();
        test.info("Asserting status code is NOT: " + unexpectedCode + ", Actual: " + actualCode);
        Assert.assertNotEquals("Unexpected status code", unexpectedCode, actualCode);
    }
    
    
    public static String loginAndGetToken(String email, String password, String baseURL, Map<String, String> headers, String userType, ExtentTest test) 
    {
            HashMap<String, String> loginPayload = new HashMap<>();
            loginPayload.put("email", email);
            loginPayload.put("password", password);

            Response res = given()
                    .baseUri(baseURL)
                    .headers(headers)
                    .contentType("application/json")
                    .body(loginPayload)
                    .log().all()
                    .when()
                    .post(userType.equalsIgnoreCase("Parent") ? "/auth/login" : "/auth/provider-login")
                    .then()
                    .log().all()
                    .extract().response();

            APIUtils.logResponseToExtent(res, test);

            if (res.getStatusCode() == 201 && res.jsonPath().get("accessToken") != null) 
            {
                String token = res.jsonPath().getString("accessToken");
                GlobalTokenStore.userTokens.put(userType.toLowerCase(), token);  // <<-- YOU NEED A GLOBAL CLASS FOR TOKENS
                return token;
            } 
            else 
            {
                test.fail("Login failed. Status Code: " + res.getStatusCode() + " | Response: " + res.asString());
                return null;
            }
     }
}
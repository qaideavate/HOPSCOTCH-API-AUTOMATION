package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import stepdefinition.Post_Child_Information_Step1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static io.restassured.RestAssured.*;

public class GlobalTokenStore 
{
    private static final Map<String, String> userTokens = new ConcurrentHashMap<>();
    private static final Map<String, String> userIds = new ConcurrentHashMap<>();
    private static String childId;

	    public static String getToken(String userType)
	    {
	        String key = userType.toLowerCase();
	        return userTokens.computeIfAbsent(key, GlobalTokenStore::generateTokenForUser);
	    }

	    private static String generateTokenForUser(String userType) 
	    {
	        Map<String, String> payload = new ConcurrentHashMap<>();
	        String loginURL;
	
	        switch (userType.toLowerCase()) 
	        {
	            case "parent":
	                loginURL = "https://dev-api.hopscotchconnect.com/api/auth/login";
	                payload.put("email", ConfigReader.getProperty("parent.email"));
	                payload.put("password", ConfigReader.getProperty("parent.password"));
	                break;
	            case "provider":
	                loginURL = "https://dev-api.hopscotchconnect.com/api/auth/provider-login";
	                payload.put("email", ConfigReader.getProperty("provider.email"));
	                payload.put("password", ConfigReader.getProperty("provider.password"));
	                break;
	            default:
	                throw new IllegalArgumentException("❌ Unknown user type: " + userType);
	        }
	
	        Response res = given()
	                .contentType(ContentType.JSON)
	                .body(payload)
	                .post(loginURL)
	                .then()
	                .extract().response();
	
	        if (res.getStatusCode() == 201) 
	        {
	            String userId = res.jsonPath().getString("user");
	            userIds.put(userType.toLowerCase(), userId);
	            return res.jsonPath().getString("accessToken");
	        }
	        else {
	            throw new RuntimeException("❌ Failed to get token. Status: " + res.getStatusCode() + " | Response: " + res.asString());
	        }
	    }

	
	    public static void setToken(String userType, String token) 
	    {
	        userTokens.put(userType.toLowerCase(), token);
	    }
	
	    public static String getUserId(String userType) 
	    {
	        return userIds.get(userType.toLowerCase());
	    }
	    
	    public static String createChildAndGetId() {
	        Post_Child_Information_Step1 step = new Post_Child_Information_Step1();
	        step.i_have_a_valid_parent_token();
	        step.the_base_url()
	            .i_prepare_the_child_registration_payload_with_valid_data()
	            .i_send_a_post_request_to();
	        return getChildId();
	    }

	    public static void setChildId(String id) 
	    {
	        childId = id;
	    }

	    public static String getChildId() 
	    {
	        return childId;
	    }
	    
}

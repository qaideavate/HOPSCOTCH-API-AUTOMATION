package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static io.restassured.RestAssured.*;

public class GlobalTokenStore 
{
    private static final Map<String, String> userTokens = new ConcurrentHashMap<>();
    private static final Map<String, String> userIds = new ConcurrentHashMap<>();
    private static final Map<String, String> store = new ConcurrentHashMap<>();

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
	                loginURL = Endpoints.baseURL + Endpoints.PARENT_LOGIN;
	                payload.put("email", Endpoints.parent_email);
	                payload.put("password", Endpoints.parent_password);
	                break;
	            case "provider":
	                loginURL = Endpoints.baseURL + Endpoints.PROVIDER_LOGIN;
	                payload.put("email", Endpoints.provider_email);
	                payload.put("password", Endpoints.provider_password);
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
	    
	    // ✅ Specific setter for childId
	    public static void setChildId(String childId) 
	    {
	        store.put("childId", childId);
	    }

	    // ✅ Getter for childId
	    public static String getChildId()
	    {
	        return store.get("childId");
	    }

	    // ✅ Specific setter for parentId
	    public static void setParentId(String parentId)
	    {
	        store.put("parentId", parentId);
	    }

	    // ✅ Getter for parentId
	    public static String getParentId()
	    {
	        return store.get("parentId");
	    }
	    
}

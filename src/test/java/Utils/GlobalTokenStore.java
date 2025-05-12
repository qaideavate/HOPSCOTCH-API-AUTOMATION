package Utils;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GlobalTokenStore
{
    public static final Map<String, String> userTokens = new HashMap<>();
    private static String userId;
    
    public static String getToken(String userType) 
    {  String key = userType.toLowerCase();
        if (!userTokens.containsKey(key)) {
            String token = generateTokenForUser(key);
            if (token != null) 
            {  userTokens.put(key, token);		   } 
            else 
            {  	throw new RuntimeException("❌ Failed to generate token for user type: " + key);			 }
      }
        return userTokens.get(key);
    }
    
    public static void setToken(String userType, String token) 
    {	userTokens.put(userType.toLowerCase(), token);	 }
    
    private static String generateTokenForUser(String userType) 
    {
        Map<String, String> payload = new HashMap<>();
        String loginURL;
        
        if ("parent".equalsIgnoreCase(userType)) 
        {	loginURL = "https://dev-api.hopscotchconnect.com/api/auth/login";
            payload.put("email", "pankaj@yopmail.com");
            payload.put("password", "Test@12345");
        } 
        else if ("provider".equalsIgnoreCase(userType))
        {	loginURL = "https://dev-api.hopscotchconnect.com/api/auth/provider-login";
            payload.put("email", "pankaj.patidar@mindruby.com");
            payload.put("password", "Test@12345");
        } 
        else 
        {	throw new IllegalArgumentException("❌ Unknown user type: " + userType);   }

	     Response res = given()
	                    .contentType(ContentType.JSON)
	                    .body(payload)
	                    .post(loginURL)
	                    .then()
	                    .extract()
	                    .response();
	            
	            userId = res.jsonPath().getString("user");
	            System.out.println("User ID: " + userId);
	
	            if (res.getStatusCode() == 201 && res.getContentType().contains("application/json")) 
	            {	 return res.jsonPath().getString("accessToken");		   
	               } 
	            else 
	            {	throw new RuntimeException("❌ Failed to generate token. Status Code: " + res.getStatusCode() + ", Response: " + res.asString());	
	              }
     }
	public static String getUserId() 
		{	return userId; 				}
}

package Utils;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import stepdefinition.Post_Child_Information_Step1;

public class GlobalTokenStore 
{
    public static final Map<String, String> userTokens = new HashMap<>();
    private static String userId;
    private static String childId;
    
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
	            {	 return null;	
	              }
     }
   
    public static String getToken(String userType) 
    {  String key = userType.toLowerCase();
        if (!userTokens.containsKey(key))
        {
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
    
    
    public static String getUserId() 
    {	return userId; 				}
    	
    
    public String generateChildId()  
     {
    	Post_Child_Information_Step1 PCIS = new Post_Child_Information_Step1();
    	 PCIS.i_have_a_valid_parent_token();
    	 PCIS.the_base_url().i_prepare_the_child_registration_payload_with_valid_data().i_send_a_post_request_to();
    	 
    	 return getChildId();
     }

    public static String getChildId()
    {
        return childId;
    }
    public static void setChildId(String id)
    {
        childId = id;
    }
	
}

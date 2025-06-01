package Utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import stepdefinition.Post_Login;

public class BaseMethods 
{
	static Map<String, String> tokens = new HashMap<>();
	static Post_Login login = new Post_Login();
	
    public static void validateStatusCode(Response res, int expectedCode, ExtentTest test)
    {
        int actualCode = res.getStatusCode();
        test.info("Asserting status code. Expected: " + expectedCode + ", Actual: " + actualCode);
        Assert.assertEquals("Unexpected status code", expectedCode, actualCode);
    }
    
	public static String formatBirthdate(String dateString) 
	{
	    if (dateString != null && !dateString.trim().isEmpty())
	    {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");
	            Date parsedDate = inputFormat.parse(dateString.trim());
	            return outputFormat.format(parsedDate);
	        	}
	        catch (Exception e) 
	        {
	            throw new RuntimeException("Birthdate format is invalid: " + dateString, e);
	        }
	    } 
	    else 
	    {
	        throw new IllegalArgumentException("birthdate field is missing or empty");
	    }
	 }
	public static String decodeJWT(String jwt) 
	{
	    String[] split = jwt.split("\\.");
	    return new String(Base64.getUrlDecoder().decode(split[1]));
	}
	
	public static void providerLogin()
	{ 	 
		 login.the_provides_email_and_password("Provider", Endpoints.provider_email, Endpoints.provider_password);
		 tokens.put("ProviderToken", login.the_sends_a_post_request_to_the_login_endpoint("Provider"));
		 ConfigReader.writeMultipleProperties(tokens);
	}
	
	public static  void parentLogin()
	{
		 login.the_provides_email_and_password("Parent", Endpoints.parent_email, Endpoints.parent_password);
		 tokens.put("ParentToken", login.the_sends_a_post_request_to_the_login_endpoint("Parent"));
		 ConfigReader.writeMultipleProperties(tokens);
	}
	
	
	
	
	
	

}
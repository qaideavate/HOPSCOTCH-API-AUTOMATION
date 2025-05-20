package Utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;

public class BaseMethods 
{
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

}
package Utils;

import org.junit.Assert;
import org.junit.Before;

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
}

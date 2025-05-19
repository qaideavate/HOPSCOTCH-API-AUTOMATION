package Utils;
import java.util.Map;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.http.Header;

public class APIUtils 
{
	public static void logResponseToExtent(Response response, ExtentTest test) {
	    test.info("Status Code: " + response.getStatusCode());
	    test.info("Response Body:\n<pre>" + response.getBody().asPrettyString() + "</pre>");

	    StringBuilder sb = new StringBuilder();
	    sb.append("<pre>");
	    for (Header header : response.getHeaders()) {
	        sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
	    }
	    sb.append("</pre>");
	    test.info("Response Headers:\n" + sb.toString());
	}
	
	public static void logRequestHeaders(ExtentTest test, Map<String, String> headers)
	{
		if (headers == null || headers.isEmpty())
		{
            test.info("Request Headers: <pre>No headers provided</pre>");
            return;
        }
	    StringBuilder sb = new StringBuilder();
	    sb.append("<pre>");

	    // Loop through all headers
	    for (Map.Entry<String, String> entry : headers.entrySet()) {
	        sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	    }

	    sb.append("</pre>");
	    test.info("Request Headers:\n" + sb.toString());
	}
	
    public static void logRequestBody(ExtentTest test, Map<String, String> requestBody)
    {
        test.info("Request Body:\n<pre>" + requestBody + "</pre>");
    }
    
    public static void logRequestBody(ExtentTest test, String requestBody)
    {
        test.info("Request Body:\n<pre>" + requestBody + "</pre>");
    }
  
    public static String getJsonBodyIfValid(Response res) 
    {
        String contentType = res.getHeader("Content-Type");
        String rawBody = res.getBody().asString().trim();

        // Check if response is JSON and starts with valid JSON tokens
        if (contentType != null && contentType.contains("application/json") && (rawBody.startsWith("{") || rawBody.startsWith("["))) 
        {
            // Remove control characters except newlines, tabs
            return rawBody.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        } 
        else 
        {
            throw new RuntimeException("Response is not valid JSON:\n" + rawBody);
        }
    }
}
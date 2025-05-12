package Utils;
import java.util.Map;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static String mapToJson(Map<String, Object> map)
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }
}
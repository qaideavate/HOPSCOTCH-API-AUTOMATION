package Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
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
        try 
        {
            return mapper.writeValueAsString(map);
        } 
        catch (JsonProcessingException e) 
        {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
      
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
	
    public static Object parseJson(Response res) 
    {
        String validJson = getJsonBodyIfValid(res);
        ObjectMapper mapper = new ObjectMapper();
        try {
            // If the JSON starts with '{', it is an object (Map)
            if (validJson.startsWith("{")) 
            {
                return mapper.readValue(validJson, Map.class); // Use Map.class for parsing the JSON into a Map
            } 
            // If the JSON starts with '[', it is an array (List of Map)
            else if (validJson.startsWith("["))
            {
                return mapper.readValue(validJson, List.class); // Use List.class for parsing into a List
            }
            else 
            {
                throw new RuntimeException("Invalid JSON format");
            }
        } catch (IOException e) 
        {
            throw new RuntimeException("JSON parsing failed", e);
        }
    }
	
}
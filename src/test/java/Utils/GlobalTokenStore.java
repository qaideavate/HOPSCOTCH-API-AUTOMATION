package Utils;

import java.util.HashMap;
import java.util.Map;
public class GlobalTokenStore
{
	    public static Map<String, String> userTokens = new HashMap<>();

	    public static String getToken(String userType) 
	    {
	        return userTokens.get(userType.toLowerCase());
	    }

	    public static void setToken(String userType, String token) 
	    {
	        userTokens.put(userType.toLowerCase(), token);
	    }
	
}
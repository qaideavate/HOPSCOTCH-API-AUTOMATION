package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader 
{
    private static Properties prop;

    static {
        try {
            String path = "C:\\Users\\Jitender.Kumar\\BDD Workspace\\restassured\\BDDApiAutomation\\src\\test\\resources\\config.properties";
            FileInputStream fis = new FileInputStream(path);
            prop = new Properties();
            prop.load(fis);
            fis.close();
        	} 
        	catch (IOException e) 
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file.");
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    // Get all properties starting with a specific prefix as a map
    public static Map<String, String> getHeadersFromConfig(String prefix) {
        Map<String, String> headers = new HashMap<>();
        for (String name : prop.stringPropertyNames()) {
            if (name.startsWith(prefix + ".")) {
                String headerKey = name.substring(prefix.length() + 1);
                headers.put(headerKey, prop.getProperty(name));
            }
        }
        return headers;
    }
    
 // Optional: Get all properties as a Map (useful for debugging)
    public static Map<String, String> getAllProperties() {
        Map<String, String> allProps = new HashMap<>();
        for (String name : prop.stringPropertyNames()) {
            allProps.put(name, prop.getProperty(name));
        }
        return allProps;
    }

}
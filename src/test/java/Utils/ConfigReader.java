package Utils;
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
 
public class ConfigReader 
{
    private static Properties prop;
    static String path =  ".//src/test/resources/config.properties";
    static {
        try {
        	String path =  ".//src/test/resources/config.properties";
        	//String path = System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties";
            //String path = "C:\\Users\\Jitender.Kumar\\BDD Workspace\\restassured\\BDDApiAutomation\\src\\test\\resources\\config.properties";
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
 
    
    
    public static void reloadProperties() {
        try (FileInputStream fis = new FileInputStream(path)) 
        {
            prop.clear(); // Clear old values
            prop.load(fis);
            System.out.println("🔄 Config file reloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to reload config file", e);
        }
    }

// WRITE: Save multiple key-value pairs at once
    public static void writeMultipleProperties(Map<String, String> propertiesToWrite) 
    {
        try {
            // Load existing properties
            FileInputStream fis = new FileInputStream(path);
            Properties existingProps = new Properties();
            existingProps.load(fis);
            fis.close();
 
            // Add all new properties
            for (Map.Entry<String, String> entry : propertiesToWrite.entrySet()) {
                existingProps.setProperty(entry.getKey(), entry.getValue());
            }
 
            // Save back to file
            FileOutputStream fos = new FileOutputStream(path);
            existingProps.store(fos, null);
            fos.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to write multiple properties to config.properties file.");
        }
    }
}
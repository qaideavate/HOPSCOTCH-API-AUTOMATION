package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Extent_Report_Manager
{
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ExtentTest test;
    private static String reportPath;  // Make this static to access it in flushReports

    // Initialize the report
    public static void initReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd--HH.mm.ss").format(new Date());
            reportPath = System.getProperty("user.dir") + "/test-output/API_Test_Report-" + timestamp + ".html"; // Dynamic path

            sparkReporter = new ExtentSparkReporter(reportPath);

            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("API Automation Test Report");
            sparkReporter.config().setReportName("REST Assured + Cucumber Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Project", "API Testing");
            extent.setSystemInfo("Tester", "Jitendra Kumar");
            extent.setSystemInfo("Environment", "Dev");
            extent.setSystemInfo("Tool", "Postman");
            
            System.out.println("Extent report initialized at: " + reportPath);
        }
    }
    
    public static String getReportPath()
    {
        return reportPath;
    }

    // Open the report automatically after test execution
    public static void openReportAutomatically() {
        if (reportPath != null) {
            File reportFile = new File(reportPath); // Use the dynamically generated report path
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(reportFile.toURI()); // Opens the report in default browser
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   //  Flush the report and open it automatically
    public static void flushReports() {
        if (extent != null) {
            extent.flush();  // Ensure report is written
            openReportAutomatically();  // Open report after flushing
        }
    }

    // Get the current test instance
    public static ExtentTest getTest() {
        return test;
    }

    // Set a custom test instance (if required)
    public static void setTest(ExtentTest testRef) {
        test = testRef;
    }

    // Create a new test
    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }
}

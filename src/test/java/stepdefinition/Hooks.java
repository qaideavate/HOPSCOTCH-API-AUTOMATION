package stepdefinition;
import io.cucumber.java.*;

	import com.aventstack.extentreports.ExtentTest;

import Utils.Extent_Report_Manager;

	public class Hooks
	{

	    private static boolean isInitialized = false;

	    @Before
	    public void beforeScenario(Scenario scenario)
	    {
	        // Simulate @BeforeAll
	        if (!isInitialized) {
	            Extent_Report_Manager.initReports();
	            isInitialized = true;
	        }

	        ExtentTest test = Extent_Report_Manager.createTest(scenario.getName());
	        Extent_Report_Manager.setTest(test);
	        test.info("Starting Scenario: " + scenario.getName());
	        
	    }

	    @After
	    public void afterScenario(Scenario scenario) 
	    {
	        ExtentTest test = Extent_Report_Manager.getTest();
	        if (scenario.isFailed()) {
	            test.fail("Scenario Failed: " + scenario.getName());
	        } else {
	            test.pass("Scenario Passed: " + scenario.getName());
	        }
	    }
	    
	    @AfterAll
	    public static void afterAllTests()
	    {
	        Extent_Report_Manager.flushReports();
	    }

	    // Simulate @AfterAll with JVM shutdown hook
	  /*  static {
	        try {
	            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	                try {
	                    Extent_Report_Manager.flushReports();
	                } catch (Exception e) {
	                    System.err.println("Failed to flush Extent reports during shutdown: " + e.getMessage());
	                    e.printStackTrace();
	                }
	            }));
	        } catch (IllegalStateException e) {
	            System.err.println("Shutdown already in progress, cannot add shutdown hook.");
	        }
	    }*/
	
	}



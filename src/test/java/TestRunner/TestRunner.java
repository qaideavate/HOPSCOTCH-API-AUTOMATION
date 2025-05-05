package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith (Cucumber.class)
@CucumberOptions
	(
		features ="classpath:feature/login.feature",
		glue={"stepdefinition"},
		dryRun = false,
		monochrome=true,
		plugin = {"pretty",
			    "html:target/cucumber-html-report",
			    "json:target/cucumber-report.json", 
			    }
		)

public class TestRunner 
{

}

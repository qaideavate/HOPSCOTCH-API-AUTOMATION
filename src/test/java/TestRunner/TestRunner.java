package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith (Cucumber.class)
@CucumberOptions
	(
		features ={"classpath:feature/Post_Child_Information_Step1.feature"},
		glue={"stepdefinition"},
		dryRun = false,
		monochrome=true,
		plugin = {"pretty",
			    "html:target/cucumber-html-report",
			    "json:target/cucumber-json.json", 
			    "junit:target/cucumber-xml.xml",
			    }
		)

public class TestRunner 
{

}

//"classpath:feature/login.feature",
//"classpath:feature/Get_children_List_API.feature"
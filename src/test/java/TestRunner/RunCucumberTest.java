package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith (Cucumber.class)
@CucumberOptions
	(
		features= {"classpath:feature/Put_Enrollment_Status.feature"},
	   // tags= "@runthis",
		/*features ={
				    "classpath:feature/Post_login.feature",                                   "classpath:feature/Post_Child_Information_Step1.feature",
				    "classpath:feature/Post_Parent_Guardian_Information.feature",              "classpath:feature/Get_children_List_API.feature",
				    "classpath:feature/Get_Enrollments.feature",                                 "classpath:feature/View_capacity_Management.feature"
				    "classpath:feature/Put_Enrollment_Status.feature"
				    },*/
		
		glue={"stepdefinition"},
		dryRun = false,
		monochrome=true,
		plugin = {"pretty",
			    "html:target/cucumber-html-report",
			    "json:target/cucumber.json", 
			    "junit:target/cucumber-xml.xml",
			    }
		)

public class RunCucumberTest 
{

}

//"classpath:feature/Post_login.feature",
//"classpath:feature/Get_children_List_API.feature"
//"classpath:feature/Post_Child_Information_Step1.feature",
//"classpath:feature/Post_Parent_Guardian_Information.feature"
//"classpath:feature/Get_Enrollments.feature"

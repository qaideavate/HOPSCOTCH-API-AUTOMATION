package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith (Cucumber.class)
@CucumberOptions
	(
	 //	features= {"classpath:feature/Post_Emergency_Contact_step_3.feature"},
	  //  tags= "@runthis",
		
	      features ={
		//		    "classpath:feature/Post_login.feature",                                 
	    //		     "classpath:feature/Get_children_List_API.feature",
		//		     "classpath:feature/Get_Enrollments.feature",
	 	//           "classpath:feature/Post_Child_Information_Step1.feature",                         
	 	 //           "classpath:feature/View_capacity_management.feature",
		//		    "classpath:feature/Put_Enrollment_Status.feature",						
	 	//          "classpath:feature/Post_Create_Classroom.feature"
	    			
				    },
	    
		
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

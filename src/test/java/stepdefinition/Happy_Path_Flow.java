package stepdefinition;

import java.util.HashMap;
import java.util.Map;

import Utils.ConfigReader;
import Utils.GlobalTokenStore;
import io.cucumber.java.en.*;

public class Happy_Path_Flow
{
	String ParentToken;
	String ProviderToken;
	@When("the {string} login via the POST login endpoint")
	public void the_login_via_the_post_login_endpoint(String userType ) 
	{
	   Post_Login login = new Post_Login();       
	   login.the_provides_email_and_password(userType, "pankaj.patidar@mindruby.com", "Test@12345");
	   this.ProviderToken = login.the_sends_a_post_request_to_the_login_endpoint(userType);
	   GlobalTokenStore.setToken("Provider", ProviderToken);
	}
	
	@When("the provider creates a new classroom using the POST classroom endpoint")
	public void the_provider_creates_a_new_classroom_using_the_post_classroom_endpoint() 
	{
		 Post_Create_Classroom classroom = new Post_Create_Classroom();
		 classroom.i_send_a_post_request_to_create_classroom_at_classroom_endpoint();
		 classroom.responseBodyShouldContainKey("classroomId");
	}
	
	@When("the provider retrieves the classroom capacity using the GET classroom endpoint with the classroom ID")
	public void the_provider_retrieves_the_classroom_capacity_using_the_get_classroom_endpoint_with_the_classroom_id() 
	{
		Get_capacity_management capacity = new Get_capacity_management();
		capacity.i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id();
	}
	
	
	@When("the {string} logs in via the POST login endpoint")
	public void the_logs_in_via_the_post_login_endpoint(String userType)
	{
		 Post_Login login = new Post_Login();
		 login.the_provides_email_and_password(userType, "pankaj@yopmail.com", "Test@12345");
		 this.ParentToken = login.the_sends_a_post_request_to_the_login_endpoint(userType); // ✅ Get token here
		 GlobalTokenStore.setToken("Parent", ParentToken);
	}
	
	@When("the parent creates {int} new children with valid information")
	public void the_parent_creates_new_children_with_valid_information(Integer numberOfChildren)
	{
	    Post_Child_Information_Step1 PCIS = new Post_Child_Information_Step1();
	    Map<String, String> allChildIds = new HashMap<>();
	    for (int i = 1; i <= numberOfChildren; i++)
	    {	PCIS.i_have_a_valid_parent_token();
	        String childId = PCIS.i_send_a_post_request_to();
	        if (childId == null || childId.isEmpty()) 
	        {
	            System.out.println("❌ Failed to create child #" + i);
	            continue;
	        }
	        allChildIds.put("CHILDID" + i, childId);

	        // Perform the rest of the child creation steps
	        PCIS.i_send_a_post_request_to_parent_endpoint();
	        PCIS.i_send_a_post_request_to_emergency_contact_endpoint();
	        PCIS.i_send_a_post_request_to_pickup_contact_endpoint();
	        PCIS.i_send_a_Post_request_to_health_document_delete_endpoint();
	        PCIS.i_send_a_post_request_to_health_info_endpoint();
	        PCIS.i_send_a_post_request_to_consent_endpoint();
	        PCIS.i_send_a_put_request_to_final_submission_endpoint();
	    }
	    if (!allChildIds.isEmpty()) 
	    {
	        ConfigReader.writeMultipleProperties(allChildIds);
	    }
	}
	
	@When("the parent enrolls {int} children for regular care in the classroom")
	public void the_parent_enrolls_children_for_regular_care_in_the_classroom(Integer int1) 
	{
	    
	}
	@Then("only {int} children should be enrolled successfully")
	public void only_children_should_be_enrolled_successfully(Integer int1) 
	{
	  
	}
	@Then("the remaining {int} child should be added to the waitlist due to capacity limits")
	public void the_remaining_child_should_be_added_to_the_waitlist_due_to_capacity_limits(Integer int1) 
	{
	    
	}



}


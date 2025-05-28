package stepdefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	        allChildIds.put("ChildId" + i, childId);

	        // Perform the rest of the child creation steps
	        PCIS.the_child_registration_response_should_store_child_id();
	        PCIS.i_send_a_post_request_to_parent_endpoint();
	        PCIS.the_response_should_contain_the_parent_id();
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
	public void the_parent_enrolls_children_for_regular_care_in_the_classroom(Integer numberOfChildren) 
	{
		 try {
		        // ⏳ Wait for 3 seconds (optional - adjust as needed)
		        System.out.println("⏳ Waiting for 3 seconds before reading config...");
		        Thread.sleep(3000); 
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		        throw new RuntimeException("Thread was interrupted", e);
		    }

		    // 🔄 Refresh the config file (force reload)
		    ConfigReader.reloadProperties();
	    
		enroll_Regular_Dropin regular = new enroll_Regular_Dropin();
	    String childrenToEnroll = ConfigReader.getProperty("ENROLL_CHILDREN");
	    List<String> selectedChildKeys = Arrays.asList(childrenToEnroll.split(","));

	    for (int i = 0; i < numberOfChildren && i < selectedChildKeys.size(); i++)
	    {
	        String childKey = selectedChildKeys.get(i).trim();
	        System.out.println("📌 Enrolling child with key: " + childKey);
	        regular.i_send_a_post_request_to_enroll_regular_child_api_with_valid_body(childKey);
	    }
	}


	@When("Approve the Enrolled {int} child.")
	public void approve_the_enrolled_child(Integer count) 
	{
		 try {
		        // ⏳ Wait for 3 seconds (optional - adjust as needed)
		        System.out.println("⏳ Waiting for 3 seconds before reading config...");
		        Thread.sleep(3000); 
		    } catch (InterruptedException e) 
		 {
		        Thread.currentThread().interrupt();
		        throw new RuntimeException("Thread was interrupted", e);
		    }
		    // 🔄 Refresh the config file (force reload)
		    ConfigReader.reloadProperties();
		  Put_Enrollment_Status statusUpdater = new Put_Enrollment_Status();
		  String enrollChildrenStr = ConfigReader.getProperty("enrollmentid");
		  String[] childKeys = enrollChildrenStr.split(",");
		   for (int i = 0; i < count; i++)
			 {
				String childKey = childKeys[i].trim();  // e.g., "ChildId4"
			    String childId = ConfigReader.getProperty(childKey); // e.g., "2009"

		    if (childId != null && !childId.isEmpty())
		    {  statusUpdater.i_send_a_put_request_to_endpoint_with_and_status(childId, "approved");
				        } 
		    else { System.out.println("⚠️ Child ID not found for key: " + childKey);
				   }
		 }
	}

	@When("the provider marks {int} enrolled child as absent for today and tomorrow using the enrollment ID")
	public void the_provider_marks_enrolled_child_as_absent_for_today_and_tomorrow_using_the_enrollment_id(Integer childIndex)
	{
		
	    // Construct the property key based on the index (e.g., ChildId1, ChildId2, etc.)
	    String enrollmentKey = "enrollmentId_ChildId3";
	    String enrollmentIdStr = ConfigReader.getProperty(enrollmentKey);
	    
	    if (enrollmentIdStr == null || enrollmentIdStr.isEmpty()) {
	        throw new RuntimeException("❌ Enrollment ID not found for key: " + enrollmentKey);
	    }

	    int enrollmentId = Integer.parseInt(enrollmentIdStr.trim());
	    System.out.println(enrollmentId);
	    Post_Mark_Absent absent = new Post_Mark_Absent();
	    absent.i_send_a_post_request_to_providers_enrollments_mark_absent_with(enrollmentId);
	}

	@When("the parent attempts to enroll a drop-in child on the same dates another child is absent")
	public void the_parent_attempts_to_enroll_a_drop_in_child_on_the_same_dates_another_child_is_absent()
	{
	   
	}

	@When("the provider graduates {int} enrolled child effective from today's date")
	public void the_provider_graduates_enrolled_child_effective_from_today_s_date(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("the parent enrolls the 4th child starting from the day after the graduation date")
	public void the_parent_enrolls_the_4th_child_starting_from_the_day_after_the_graduation_date() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the updated classroom capacity should be accurately reflected by the system")
	public void the_updated_classroom_capacity_should_be_accurately_reflected_by_the_system() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}





}


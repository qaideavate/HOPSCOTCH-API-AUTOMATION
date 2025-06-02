package stepdefinition;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Utils.BaseMethods;
import Utils.ConfigReader;
import io.cucumber.java.en.*;

public class Happy_Path_Flow
{
	@When("the Provider login via the POST login endpoint")
	public void both_login_via_the_post_login_endpoint() 
	{
	  BaseMethods.providerLogin();
	}
	
	@When("the Parent login via the POST login endpoint")
	public void the_login_via_the_post_login_endpoint()
	{
		 BaseMethods.parentLogin();
	}
	
	@When("the provider creates a new classroom using the POST classroom endpoint")
	public void the_provider_creates_a_new_classroom_using_the_post_classroom_endpoint() 
	{
		 ConfigReader.waitAndReloadConfig(3000);
		 Post_Create_Classroom classroom = new Post_Create_Classroom();
		 classroom.i_send_a_post_request_to_create_classroom_at_classroom_endpoint();
		 classroom.responseBodyShouldContainKey("classroomId");
		 ConfigReader.waitAndReloadConfig(3000);
	}
	
	@When("the provider retrieves the classroom capacity using the GET classroom endpoint with the classroom ID")
	public void the_provider_retrieves_the_classroom_capacity_using_the_get_classroom_endpoint_with_the_classroom_id() 
	{
		Get_capacity_management capacity = new Get_capacity_management();
		capacity.i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id();
	}

	@When("the parent creates new children with valid information")
	public void the_parent_creates_new_children_with_valid_information()
	{
		Post_Child_Information_Step1 PCIS = new Post_Child_Information_Step1();
		Map<String, String> allChildIds = new HashMap<>();

		int numberOfChildren = Integer.parseInt(ConfigReader.getProperty("number_of_children_to_create"));
		// Create the parent only once
		PCIS.i_have_a_valid_parent_token();

		for (int i = 1; i <= numberOfChildren; i++)
		{
			String childId = PCIS.i_send_a_post_request_to();
			if (childId == null || childId.isEmpty())
			{
				System.out.println("❌ Failed to create child #" + i);
				continue;
			}

			allChildIds.put("ChildId" + i, childId);

			// Perform the rest of the child creation steps
			PCIS.the_child_registration_response_should_store_child_id();
			PCIS.i_send_a_post_request_to_parent_endpoint(); // This might just link child to parent, not create a new one
			PCIS.the_response_should_contain_the_parent_id();
			PCIS.i_send_a_post_request_to_emergency_contact_endpoint();
			PCIS.i_send_a_post_request_to_pickup_contact_endpoint();
			PCIS.i_send_a_Post_request_to_health_document_delete_endpoint();
			PCIS.i_send_a_post_request_to_health_info_endpoint();
			PCIS.i_send_a_post_request_to_consent_endpoint();
			PCIS.i_send_a_put_request_to_final_submission_endpoint();
		}

		if (!allChildIds.isEmpty()) {
			ConfigReader.writeMultipleProperties(allChildIds);
		}
	}

	@When("the parent enrolls children for regular care in the classroom")
	public void the_parent_enrolls_children_for_regular_care_in_the_classroom()
	{
		ConfigReader.waitAndReloadConfig(3000);
		enroll_Regular_Dropin regular = new enroll_Regular_Dropin();
	    String childrenToEnroll = ConfigReader.getProperty("Enroll_Children");
	    
	    List<String> selectedChildKeys = Arrays.asList(childrenToEnroll.split(","));
	    for (int i = 0; i < selectedChildKeys.size(); i++)
	    {
	    	String Today = LocalDate.now().toString();
	        String childKey = selectedChildKeys.get(i).trim();
	        System.out.println("📌 Enrolling child with key: " + childKey);
	        regular.i_send_a_post_request_to_enroll_regular_child_api_with_valid_body(childKey, Today);
	    }
	}

	@When("Approve the Enrolled  child.")
	public void approve_the_enrolled_child()
	{
		 ConfigReader.waitAndReloadConfig(3000);
		  Put_Enrollment_Status statusUpdater = new Put_Enrollment_Status();
		  String enrollChildrenStr = ConfigReader.getProperty("Approve_EnrollmentId");
		  String[] childKeys = enrollChildrenStr.split(",");
		   for (int i = 0; i < childKeys.length; i++)
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

	@When("the provider marks  enrolled child as absent for today and tomorrow using the enrollment ID")
	public void the_provider_marks_enrolled_child_as_absent_for_today_and_tomorrow_using_the_enrollment_id()
	{
	    // Construct the property key based on the index (e.g., ChildId1, ChildId2, etc.)
		 String absentKeys = ConfigReader.getProperty("Absent");
		 if (absentKeys == null || absentKeys.isEmpty()) 
		 {
		        throw new RuntimeException("❌ No absent enrollment keys found in config.");
		    }

		    // 🧹 Clean and split the keys (e.g., EnrollmentId_ChildId1,EnrollmentId_ChildId2)
		    List<String> enrollmentKeys = Arrays.asList(absentKeys.split(","));

		    for (int i = 0; i < enrollmentKeys.size(); i++)
		    {
		        String key = enrollmentKeys.get(i).trim();
		        String idStr = ConfigReader.getProperty(key);

		        if (idStr == null || idStr.isEmpty()) 
		        {
		            throw new RuntimeException("❌ Enrollment ID not found for key: " + key);
		        }

		        int enrollmentId = Integer.parseInt(idStr.trim());
		        System.out.println("📌 Marking enrollment ID as absent: " + enrollmentId);

		        Post_Mark_Absent_Graduate absent = new Post_Mark_Absent_Graduate();
		        absent.i_send_a_post_request_to_providers_enrollments_mark_absent_with(enrollmentId);
		    }
	}

	@When("the parent attempts to drop-in  child on the same dates another child is absent")
	public void the_parent_attempts_to_enroll_a_drop_in_child_on_the_same_dates_another_child_is_absent()
	{
		 ConfigReader.waitAndReloadConfig(3000);
		enroll_Regular_Dropin regular = new enroll_Regular_Dropin();
	    String childrenToDrop = ConfigReader.getProperty("DropIn_Children");
	    List<String> selectedChildKeys = Arrays.asList(childrenToDrop.split(","));
	
	    for (int i = 0; i < selectedChildKeys.size(); i++)
	    {
        String childKey = selectedChildKeys.get(i).trim();
        System.out.println("📌 Enrolling child with key: " + childKey);
        regular.i_send_a_post_request_to_enroll_dropin_child_api_with_valid_body(childKey);
	    }
	}
	
	@When("Approve the Enrolled drop in  child")
	public void approve_the_enrolled__drop_in_child()
	{
		  ConfigReader.waitAndReloadConfig(3000);
		  Put_Enrollment_Status statusUpdater = new Put_Enrollment_Status();
		  String enrollChildrenStr = ConfigReader.getProperty("After_DropIn_Approve_EnrollmentId");
		  String[] childKeys = enrollChildrenStr.split(",");
		   for (int i = 0; i < childKeys.length; i++)
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

	@When("the provider graduates enrolled child effective from today's date")
	public void the_provider_graduates_enrolled_child_effective_from_today_s_date()
	{
		 String GraduateKeys = ConfigReader.getProperty("Graduate");
		 if (GraduateKeys == null || GraduateKeys.isEmpty()) 
		 {
		        throw new RuntimeException("❌ No Graduate Enrollment Keys  found in config.");
		    }

		    // 🧹 Clean and split the keys (e.g., EnrollmentId_ChildId1,EnrollmentId_ChildId2)
		    List<String> enrollmentKeys = Arrays.asList(GraduateKeys.split(","));

		    for (int i = 0; i < enrollmentKeys.size(); i++)
		    {
		        String key = enrollmentKeys.get(i).trim();
		        String idStr = ConfigReader.getProperty(key);

		        if (idStr == null || idStr.isEmpty()) 
		        {
		            throw new RuntimeException("❌ Enrollment ID not found for key: " + key);
		        }

		    int enrollmentId = Integer.parseInt(idStr.trim());
		    System.out.println("📌 Marking enrollment ID as Graduate: " +enrollmentId);
		    Post_Mark_Absent_Graduate Graduate = new Post_Mark_Absent_Graduate();
		    Graduate.i_send_a_post_request_to_providers_enrollments_mark_graduation_with(enrollmentId);
		    }
	}

	@When("the parent enrolls the  child starting from the day after the graduation date")
	public void the_parent_enrolls_the_child_starting_from_the_day_after_the_graduation_date()
	{
		    ConfigReader.waitAndReloadConfig(3000);
			enroll_Regular_Dropin regular = new enroll_Regular_Dropin();
		    String childrenToEnroll = ConfigReader.getProperty("After_Graduate_Enroll_Children");
		   
		    List<String> selectedChildKeys = Arrays.asList(childrenToEnroll.split(","));

		    for (int i = 0; i < selectedChildKeys.size(); i++)
		    {
		    	 LocalDate today = LocalDate.now();
		    	 LocalDate tomorrow = today.plusDays(1);
		    	 String tomorrowStr = tomorrow.toString();
		        String childKey = selectedChildKeys.get(i).trim();
		        System.out.println("📌 Enrolling child with key: " + childKey);
		        regular.i_send_a_post_request_to_enroll_regular_child_api_with_valid_body(childKey, tomorrowStr);
		    }
	}

	@When("Approve the Enrolled  child after the Graduation of Another Child.")
	public void approve_the_enrolled_child_after_the_graduation_of_another_child()
	{
		  ConfigReader.waitAndReloadConfig(3000);
		  Put_Enrollment_Status statusUpdater = new Put_Enrollment_Status();
		  String enrollChildrenStr = ConfigReader.getProperty("Graduate_Approve");
		  String[] childKeys = enrollChildrenStr.split(",");
		   for (int i = 0; i < childKeys.length; i++)
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

}


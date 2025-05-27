package Utils;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.google.gson.Gson;

import io.cucumber.java.en.Given;
 
public class Payload 
{
	String ParentId;
	String childId ;
	Faker faker = new Faker();
 
	public String child_registration_payload() 
	{
        Map<String, Object> childInfo = new HashMap<>();
        childInfo.put("childId", "");
        childInfo.put("lastName", "AsadApi");
        childInfo.put("middleName", "");
        childInfo.put("firstName", "AsadApi");
        childInfo.put("nickname", "");
        childInfo.put("birthdate", "2012-08-15");
        childInfo.put("gender", "Boy");
        childInfo.put("streetAddress", "830 Southeast Ireland Street");
        childInfo.put("city", "Oak Harbor");
        childInfo.put("zipCode", "98277");
        childInfo.put("apt", "");

        JSONObject json = new JSONObject(childInfo);
        JSONObject root = new JSONObject();
        root.put("childInfo", json);
        return root.toString();
    }

    // Parent registration payload
	public String parent_registration_payload(String childId)  
	{
	    HashMap<String, Object> parent = new HashMap<>();
	    parent.put("lastName", "tes");
	    parent.put("firstName", "TestPa");
	    parent.put("email", "pankaj@yopmail.com");
	    parent.put("cellPhone", "1111111111");
	    parent.put("streetAddress", "830 Southeast Ireland Street");
	    parent.put("apt", "");
	    parent.put("city", "Oak Harbor");
	    parent.put("zipCode", "98277");
	    parent.put("sameAddressAsChild", true);
	    parent.put("relationship", "mother");
	    parent.put("parentId", 0);
	    parent.put("middleName", "");
	    parent.put("workPhone", "");
	    parent.put("altPhone", "");

	    ArrayList<Object> parents = new ArrayList<>();
	    parents.add(parent);

	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("parents", parents);
	    requestMap.put("childId", childId); // ✅ Passed as parameter

	    JSONObject root = new JSONObject(requestMap);
	    return root.toString();
	}
		
	public String emergency_contact_payload(String childId, String parentId) 
	{
	    // Create a contact info map
	    HashMap<String, Object> contact = new HashMap<>();
	    contact.put("lastName", "sds");
	    contact.put("firstName", "PareA");
	    contact.put("email", "pankaj@yopmail.com");
	    contact.put("cellPhone", "1111111111");
	    contact.put("streetAddress", "830 Southeast Ireland Street");
	    contact.put("apt", "");
	    contact.put("city", "Oak Harbor");
	    contact.put("zipCode", "98277");
	    contact.put("sameAddressAsChild", true);
	    contact.put("relationship", "mother");
	    contact.put("parentId", parentId);          // ✅ Use passed parameter
	    contact.put("middleName", "");
	    contact.put("workPhone", "");
	    contact.put("altPhone", "");
	    contact.put("contactId", 0);

	    // Create list of contacts
	    ArrayList<HashMap<String, Object>> contacts = new ArrayList<>();
	    contacts.add(contact);

	    // Create the full payload
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("contacts", contacts);
	    requestMap.put("childId", childId);          // ✅ Use passed parameter

	    // Convert to JSON
	    JSONObject root = new JSONObject(requestMap);
	    return root.toString();                      // ✅ Clean return
	}
		
	public String add_Pickup_payload(String childId, String parentId) 
	{
	    // Create a contact info map
	    HashMap<String, Object> contact = new HashMap<>();
	    contact.put("lastName", "sds");
	    contact.put("firstName", "PareA");
	    contact.put("email", "pankaj@yopmail.com");
	    contact.put("cellPhone", "1111111111");
	    contact.put("streetAddress", "830 Southeast Ireland Street");
	    contact.put("apt", "");
	    contact.put("city", "Oak Harbor");
	    contact.put("zipCode", "98277");
	    contact.put("sameAddressAsChild", true);
	    contact.put("relationship", "mother");
	    contact.put("parentId", parentId);  // ✅ Use passed parameter
	    contact.put("middleName", "");
	    contact.put("workPhone", "");
	    contact.put("altPhone", "");
	    contact.put("contactId", 0);

	    // Create list of contacts
	    ArrayList<HashMap<String, Object>> contacts = new ArrayList<>();
	    contacts.add(contact);

	    // Create the full payload
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("contacts", contacts);
	    requestMap.put("childId", childId);  // ✅ Use passed parameter

	    // Convert to JSON
	    JSONObject root = new JSONObject(requestMap);
	    return root.toString();
	}
		
	public String health_info_payload(String childId)
	{
	    // Health information details
	    Map<String, Object> healthInfo = new HashMap<>();
	    healthInfo.put("medicalProvider", "test");
	    healthInfo.put("medicalPhone", "1111111111");
	    healthInfo.put("lastPhysicianVisit", "2025-04-30T18:30:00.000Z");
	    healthInfo.put("medicalAddress", "830 Southeast Ireland Street, Oak Harbor, US, 98277");
	    healthInfo.put("dentalProvider", "test");
	    healthInfo.put("dentalPhone", "1111111111");
	    healthInfo.put("lastDentistVisit", "2025-04-30T18:30:00.000Z");
	    healthInfo.put("dentalAddress", "830 Southeast Ireland Street, Oak Harbor, US, 98277");
	    healthInfo.put("healthConditions", "");
	    healthInfo.put("healthInsuranceProvider", "");
	    healthInfo.put("healthInsuranceGroupNumber", "");
	    healthInfo.put("healthInsuranceMemberNumber", "");
	    healthInfo.put("healthInsuranceEmployer", "");
	    healthInfo.put("allergies", "");
	    healthInfo.put("chronicIllnesses", "");
	    healthInfo.put("regularMedications", "");
	    healthInfo.put("cisForm", JSONObject.NULL); // null value in JSON
	    healthInfo.put("otherInfo", "");
	    healthInfo.put("regularSupervision", false);

	    // Complete payload with childId
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("healthInfo", healthInfo);
	    requestMap.put("childId", childId);

	    // Convert map to JSON string
	    return new JSONObject(requestMap).toString();
	}

		
	public String consent_payload(String childId) 
	{
	    // Create consent details map
	    Map<String, Object> consent = new HashMap<>();
	    consent.put("transportationSchoolPersonalVehicle", false);
	    consent.put("transportationSchoolPublicTransportation", true);
	    consent.put("transportationSchoolWalking", false);
	    consent.put("transportationFieldTripsPersonalVehicle", false);
	    consent.put("transportationFieldTripsPublicTransportation", false);
	    consent.put("transportationFieldTripsWalking", false);
	    consent.put("transportationOccasionalErrandsPersonalVehicle", true);
	    consent.put("transportationOccasionalErrandsPublicTransportation", false);
	    consent.put("transportationOccasionalErrandsWalking", false);
	    consent.put("transportationOther", "");
	    consent.put("transportationOtherPersonalVehicle", false);
	    consent.put("transportationOtherPublicTransportation", false);
	    consent.put("transportationOtherWalking", false);
	    consent.put("swimming", false);
	    consent.put("bathingAfterAccident", false);
	    consent.put("bathingOvernightCare", false);
	    consent.put("takePhotos", true);
	    consent.put("takeVideos", false);
	    consent.put("captureOnSurveillance", false);

	    // Create full payload map
	    Map<String, Object> requestMap = new HashMap<>();
	    requestMap.put("consent", consent);
	    requestMap.put("childId", childId); // Set passed childId here

	    // Convert to JSON
	    JSONObject root = new JSONObject(requestMap);
	    return root.toString();
	}

		
		public String child_id_payload(String childId) 
		{
		    Map<String, Object> requestMap = new HashMap<>();
		    requestMap.put("childId", childId);

		    JSONObject root = new JSONObject(requestMap);
		    return root.toString();
		}

		public String classroom_payload() 
		{
		    String todayDate = LocalDate.now().toString();
		    Map<String, Object> payload = new HashMap<>();
		    
		    payload.put("name", "Test class " + faker.number().randomNumber());
		    payload.put("min_age", 15);
		    payload.put("max_age", 30);
		    payload.put("start_date", todayDate);
		    payload.put("license_capacity", 7);
		    payload.put("capacity", 4);
		    payload.put("enrollment_cutoff_window", "7");
		    payload.put("fulltime_tuition", 10);
		    payload.put("fulltime_tuition_cadence", "weekly");
		    payload.put("fulltime_tuition_status", true);
		    payload.put("mornings_tuition_status", false);
		    payload.put("afternoons_tuition_status", false);
		    payload.put("mwf_tuition_status", false);
		    payload.put("tuth_tuition_status", false);
		    payload.put("dropin_tuition_status", false);
		    payload.put("keywords", "");

		    // Inline allocation generation
		    List<Integer> staffIds = new ArrayList<>(Arrays.asList(12, 13, 14, 15));
		    Collections.shuffle(staffIds);
		    List<Map<String, Integer>> allocations = new ArrayList<>();
		    for (int i = 0; i < staffIds.size(); i++) {
		        Map<String, Integer> allocation = new HashMap<>();
		        allocation.put("position_id", i + 1);
		        allocation.put("staff_id", staffIds.get(i));
		        allocations.add(allocation);
		    }
		    payload.put("allocations", allocations);

		    return new Gson().toJson(payload);
		}

		public Map<String, Object> MarkingAbsence(int enrollmentId)
		{
		    Map<String, Object> requestBody = new HashMap<>();

		    LocalDate today = LocalDate.now();
		    LocalDate tomorrow = today.plusDays(1);

		    requestBody.put("request_date", Arrays.asList(today.toString(), tomorrow.toString()));
		    requestBody.put("enrollment_id", enrollmentId);
		    requestBody.put("reason", "Test");
		    return requestBody;
		}

		public Map<String, Object> getEnrollChildPayload(int providerId, int classroomId, int childId, String startDate)
		{
			Map<String, Object> consent = new HashMap<>();
			consent.put("childId", childId);
			consent.put("providerId", providerId);
			consent.put("transportationSchoolPersonalVehicle", 1);
			consent.put("transportationSchoolPublicTransportation", false);
			consent.put("transportationSchoolWalking", false);
			consent.put("transportationFieldTripsPersonalVehicle", false);
			consent.put("transportationFieldTripsPublicTransportation", 1);
			consent.put("transportationFieldTripsWalking", false);
			consent.put("transportationOccasionalErrandsPersonalVehicle", false);
			consent.put("transportationOccasionalErrandsPublicTransportation", false);
			consent.put("transportationOccasionalErrandsWalking", 1);
			consent.put("transportationOther", "");
			consent.put("transportationOtherPersonalVehicle", false);
			consent.put("transportationOtherPublicTransportation", false);
			consent.put("transportationOtherWalking", false);
			consent.put("swimming", 1);
			consent.put("bathingAfterAccident", 1);
			consent.put("bathingOvernightCare", false);
			consent.put("takePhotos", false);
			consent.put("takeVideos", 1);
			consent.put("captureOnSurveillance", false);

			Map<String, Object> payload = new HashMap<>();
			payload.put("provider_id", providerId);
			payload.put("classroom_id", classroomId);
			payload.put("child", childId);
			payload.put("start_date", startDate);
			payload.put("tuition", "30.00");
			payload.put("schedule_type_id", 6);
			payload.put("tuition_cadence", "Not Applicable");
			payload.put("consent", consent);
			payload.put("isSubsidyEligible", false);

			return payload;
		}

}
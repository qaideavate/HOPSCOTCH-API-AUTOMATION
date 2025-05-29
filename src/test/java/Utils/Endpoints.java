package Utils;
 
public class Endpoints
    {
        public static final String PARENT_LOGIN="auth/login";
        public static final String PROVIDER_LOGIN="auth/provider-login";
        public static final String PARENT_REGISTER = "user/register-parent";
        public static final String CHILD_REGISTER = "user/register-child";
        public static final String GET_CHILDREN = "user/children-with-enrollments";
        public static final String GET_ENROLLMENT = "user/enrollments";
        public static final String PROVIDER_CAPACITY = "providers/capacity";
        public static final String CHANGE_ENROLLMERNT_STATUS = "providers/enrollments/";
        public static final String Emergency_Contact = "user/register-emergency-contact?t=1747901275337";
        public static final String Add_pickup = "user/add-pickup-contact?t=1747896099005";
        public static final String Add_Health_document_sub = "user/add-health-info-document";
        public static final String Delete_Document_sub ="user/delete-health-info-document?t=1747896099005";
        public static final String Health ="user/add-health-info?t=1747896099005";
        public static final String Add_Consent = "user/add-consent?t=1747896099005";
        public static final String Submit_Child= "user/update-child-status?t=1747896099005";
        public static final String create_classroom = "providers/programs";
        public static final String mark_absent ="providers/enrollments/mark-absent";
        public static final String enroll_dropin ="user/enroll-child";
        public static final String enroll_regular ="user/enroll-fulltime-child";
        
        
        
        public static final String baseURL="https://dev-api.hopscotchconnect.com/api/";
        public static final String parent_email="pankaj@yopmail.com";
        public static final String parent_password= "Test@12345";
 
        public static final String provider_email="pankaj.patidar@mindruby.com";
        public static final String provider_password= "Test@12345";
 
    }
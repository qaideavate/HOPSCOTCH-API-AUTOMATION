package stepdefinition;

import io.cucumber.java.en.*;
import classroom.*; // import your helper classes

public class ClassroomCapacitySteps {

    Post_Login postLogin = new Post_Login();
    Create_Classroom createClassroom = new Create_Classroom();
    View_capacity_management viewCapacity = new View_capacity_management();
    Create_Children createChildren = new Create_Children();
    Enroll_Children enrollChildren = new Enroll_Children();
    Mark_Absent markAbsent = new Mark_Absent();
    Dropin_Enrollment dropinEnrollment = new Dropin_Enrollment();
    Graduate_Child graduateChild = new Graduate_Child();

    @When("The {string} sends a POST request to the login endpoint")
    public void the_user_sends_a_post_request_to_the_login_endpoint(String userType) {
        postLogin.the_sends_a_post_request_to_the_login_endpoint(userType);
    }

    @When("I send a POST request to create classroom at classroom endpoint")
    public void i_send_a_post_request_to_create_classroom_at_classroom_endpoint() {
        createClassroom.i_send_a_post_request_to_create_classroom_at_classroom_endpoint();
    }

    @When("I send a GET request to view classroom capacity at classroom endpoint with classroom Id")
    public void i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id() {
        viewCapacity.i_send_a_get_request_to_view_classroom_capacity_at_classroom_endpoint_with_classroom_id();
    }

    @When("the user create a 4 new child with valid details\.")
    public void the_user_creates_four_children() {
        createChildren.create_4_children();
    }

    @When("the user enrolls 3 children for regular care in the classroom")
    public void the_user_enrolls_3_children_for_regular_care_in_the_classroom() {
        enrollChildren.enroll_3_regular_children();
    }

    @Then("only 2 children should be enrolled successfully")
    public void only_2_children_should_be_enrolled_successfully() {
        enrollChildren.validate_2_successful_enrollments();
    }

    @Then("1 child should be placed on the waitlist due to capacity")
    public void one_child_should_be_placed_on_waitlist() {
        enrollChildren.validate_1_waitlisted();
    }

    @When("the user marks 1 enrolled child as absent for today and tomorrow using enrollment ID")
    public void the_user_marks_child_as_absent() {
        markAbsent.mark_child_as_absent();
    }

    @When("the user tries to enroll a drop-in child for the same absent dates")
    public void the_user_tries_to_enroll_dropin_child() {
        dropinEnrollment.enroll_dropin_child_during_absence();
    }

    @When("the user graduates 1 enrolled child with today’s date")
    public void the_user_graduates_child() {
        graduateChild.graduate_child_today();
    }

    @When("the user enrolls the 4th new child starting from the next day after graduation")
    public void the_user_enrolls_4th_child_post_graduation() {
        enrollChildren.enroll_4th_child_after_graduation();
    }

    @Then("the system should reflect the updated classroom capacity")
    public void system_should_reflect_updated_capacity() {
        viewCapacity.validate_updated_classroom_capacity();
    }
}

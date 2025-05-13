package stepdefinition;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import com.yourprojectname.utils.TokenManager;



public class Post_Child_information_Steo1_StepDefinitions 
{

    private String parentToken;

	/*
	 * @Given("I have a valid parent token") public void
	 * i_have_a_valid_parent_token() { parentToken = TokenManager.getParentToken();
	 * }
	 */

    @And("The base URL is set dynamically")
    public void the_base_url_is_set_dynamically() {
        SerenityRest.useRelaxedHTTPSValidation();
        SerenityRest.baseURI = System.getProperty("base.url");
    }

    @And("I provide the child register \"<body>\" with all valid fields")
    public void i_provide_the_child_register_body_with_all_valid_fields(String body) {
        SerenityRest.given().header("Authorization", "Bearer " + parentToken)
                .contentType("application/json")
                .body(body);
    }

    @When("I send a POST request to \"/user/register-child\"")
    public void i_send_a_post_request_to_user_register_child() {
        SerenityRest.post("/user/register-child").then().log().all();
    }

	/*
	 * @Then("the response status code should be {int}") public void
	 * the_response_status_code_should_be(int statusCode) {
	 * SerenityRest.then().statusCode(statusCode); }
	 */

    @And("the response message should be \"{string}\"")
    public void the_response_message_should_be(String expectedMessage) {
        SerenityRest.then().body("message", equalTo(expectedMessage));
    }

    @And("the returned childId should be a 3-digit positive number")
    public void the_returned_childid_should_be_a_3_digit_positive_number() {
        SerenityRest.then().body("childInfo.childId", allOf(notNullValue(), greaterThan(99), lessThan(1000)));
    }

    @And("I provide the child register \"{string}\" set to \"{string}\" in the request body")
    public void i_provide_the_child_register_field_set_to_value(String field, String value) {
        String requestBody = String.format("{\"childInfo\": {\"%s\": \"%s\"}}", field, value);
        SerenityRest.given().header("Authorization", "Bearer " + parentToken)
                .contentType("application/json")
                .body(requestBody);
    }

    @And("the field error should be \"{string}\"")
    public void the_field_error_should_be(String fieldError) {
        SerenityRest.then().body("errors[0].message", equalTo(fieldError));
    }
}

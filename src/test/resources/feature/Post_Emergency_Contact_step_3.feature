Feature: Register Emergency Contact API

  Background:
    Given The base URL is "https://dev-api.hopscotchconnect.com/api/"
    And I have a valid Parent token
    And I have a valid Parent ID
    And I have a valid Child ID

  Scenario: Successfully register an emergency contact
    Given I prepare the emergency contact request payload with valid data
    When I send a POST request to endpoint "user/register-emergency-contact"
    Then the response status code should be 200
    And The response message should be "Emergency contact information saved successfully."
    And The success field should be true
    And The response should contain a non-empty list of contactIds.
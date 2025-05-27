Feature: Register Child API Test
Background: 
	 Given I have a valid parent token
 @runthis
  Scenario: Validate Child Registration with valid data
    When I send a POST request to child endpoint
    Then I should receive a 200 OK response
    Then the child registration response should store childId
 @runthis
  Scenario: Register a new parent linked to a child with valid data
    When I send a POST request to Parent endpoint
    Then I should receive a 200 OK response
    And the response should contain the parentId
 @runthis
  Scenario: Register emergency contact linked to the child
    When I send a POST request to Emergency Contact endpoint
    Then I should receive a 200 OK response
    And the response should contain the emergency contact ID(s)
 @runthis
  Scenario: Register pickup contact linked to the child
    When I send a POST request to Pickup Contact endpoint
    Then I should receive a 200 OK response
    And the response should contain the pickup contact ID(s)
 
  Scenario: Upload health document for the child
    When I send a POST request to Health Document Upload endpoint
    Then I should receive a 200 OK response
    And the response should confirm document upload success
 @runthis
  Scenario: Delete uploaded health document for the child
    Given I have a valid parent token
    When I send a Post request to Health Document Delete endpoint
    Then I should receive a 200 OK response
    And the document should be removed successfully
 @runthis
  Scenario: Submit health information for the child
    When I send a POST request to Health Info endpoint
    Then I should receive a 200 OK response
    And the response should contain the healthInfoId
 @runthis
  Scenario: Submit consent information for the child
    When I send a POST request to Consent endpoint
    Then I should receive a 200 OK response
    And the response should contain the consentId
 @runthis
  Scenario: Submit final child registration
    When I send a PUT request to Final Submission endpoint
    Then I should receive a 200 OK response
    And the child registration status should be updated to COMPLETE
 
 @runthis 
   Scenario: Validate Child Registration with valid data
    When I send a POST request to child endpoint
    Then I should receive a 200 OK response
   	Then The response message should be for child "Child information saved successfully."
    Then the returned childId should be a positive number
    Then the child registration response should store childId
    
 @runthis
   Scenario: Register a new parent linked to a child with valid data
    When I send a POST request to Parent endpoint
     Then I should receive a 200 OK response
    Then The response message should be for Parent guardian "Parent information saved successfully."
    Then the returned parentId should be a positive number
    Then the success message should be "true"
    
Scenario Outline: Validate Child registration with Empty/Invalid data : <field> --> <value>
  When I update child "<field>" with "<value>"
  When I send a POST request to child endpoint
  Then I should receive a <statuscode> OK response
  And the response message should be "<expectedMessage>" and "<expectedFieldPath>"
 
Examples:
| field          | value           | statuscode |expectedMessage                           |expectedFieldPath          |
| lastName       |                 | 400        |Last name is required.                    | childInfo.lastName        |
| childId        |                 | 200        |Child information saved successfully.     | 						   |
| lastName       |                 | 400        |Last name is required.                    | childInfo.lastName        |
| firstName      |                 | 400        |First name is required.                   | childInfo.firstName       |
| birthdate      |                 | 400        |Valid birthdate is required.              | childInfo.birthdate       |
| birthdate      | 2028-15-23      | 400        |Valid birthdate is required.              | childInfo.birthdate       |
| birthdate      | invalid-date    | 400        |Valid birthdate is required.              | childInfo.birthdate       |
| gender         |                 | 400        |Gender is required.                       | childInfo.gender          |
| gender         | Unknown         | 400        |Invalid gender: must be 'Boy' or 'Girl'   | childInfo.gender          |
| streetAddress  |                 | 400        |Street address is required.               | childInfo.streetAddress   |
| city           |                 | 400        |City is required.                         | childInfo.city            |
| zipCode        | abcde           | 400        |Valid US ZIP code is required.            | childInfo.zipCode         |
| zipCode        |                 | 400        |Valid US ZIP code is required.            | childInfo.zipCode         |
| zipCode        | 12345           | 200        |Child information saved successfully.     |                           |
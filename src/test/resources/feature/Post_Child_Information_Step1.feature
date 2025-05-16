Feature: Register Child API Test

  Background:
    Given I have a valid parent token
    And The base URL 
	 @runthis
   Scenario: Validate Child Registration with valid data
    Given I prepare the child registration payload with valid data
    When  I send a POST request to child endpoint
    Then the child registration response status code should be 200
   	Then The response message should be for child "Child information saved successfully."
    Then the returned childId should be a positive number
    Then the child registration response should store childId
    

Scenario Outline: Validate Child registration with Empty/Invalid data : <field> --> <value>
  Given I prepare the child registration payload with valid data
  When I update child "<field>" with "<value>"
  When I send a POST request to child endpoint
  Then the child registration response status code should be <statuscode>
  And the response message should be "<expectedMessage>" and "<expectedFieldPath>"

Examples:
| field          | value           | statuscode |expectedMessage                           |expectedFieldPath          |
| lastName       |                 | 400        |Last name is required.                    | childInfo.lastName        |
| childId        |                 | 200        |Child information saved successfully.     | 									         |
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
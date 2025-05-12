Feature: Register Child API Test

  Background:
    Given I have a valid parent token
    And The base URL is set dynamically.

  Scenario: Register a new child with valid fields
    And I provide the child register "<body>" with all valid fields
    When I send a POST request to "/user/register-child "
    Then the response status code should be 200
    And the response message should be "Child information saved successfully."
    And the returned childId should be a 3-digit positive number
   
     Examples:
      | body |
      | {
   				 "childInfo": {
									        "childId": "",
									        "lastName": "Test",
									        "middleName": "",
									        "firstName": "AsadApi",
									        "nickname": "",
									        "birthdate": "2025-05-01",
									        "gender": "Boy",
									        "streetAddress": "830 Southeast Ireland Street",
									        "city": "Oak Harbor",
									        "zipCode": "98277",
									        "apt": ""
  										  }
				} |
      

	  Scenario Outline: Validate child registration request body field validations
		And I provide the child register "" set to "" in the request body
		When I send a POST request to "/user/register-child"
		Then the response status code should be 
		And the response message should be ""
		And the field error should be ""

			Examples:
			
			| field          | value         | statusCode | message                          | fieldPath                       |
			| firstName      |               | 400        | First name is required           | childInfo.firstName             |
			| lastName       |               | 400        | Last name is required            | childInfo.lastName              |
			| birthdate      |               | 400        | Birthdate is required            | childInfo.birthdate             |
			| birthdate      | 2025-14-01    | 400        | Invalid date format              | childInfo.birthdate             |
			| gender         |               | 400        | Gender is required               | childInfo.gender                |
			| gender         | Unknown       | 400        | Invalid gender value             | childInfo.gender                |
			| streetAddress  |               | 400        | Street address is required       | childInfo.streetAddress         |
			| city           |               | 400        | City is required                 | childInfo.city                  |
			| zipCode        |               | 400        | Zip code is required             | childInfo.zipCode               |
			| zipCode        | ABCD          | 400        | Invalid zip code format          | childInfo.zipCode               |
			| childId        | -1            | 400        | Invalid child ID                 | childInfo.childId               |
			| apt            | (whitespace)  | 200        | Child information saved          | (No field error expected)       |
    
    
    

Feature: Login 

  Scenario: Login with valid email and password
    Given The user provides valid email login credentials
    When The user sends a POST request to the provider login endpoint
    Then The response status code should be 201
    And The response message should be "login_success"
    And The response should contain a valid access token
    
    
   Scenario Outline: Login with missing required fields
    Given I set the header param request content type as "application/json"
    And The user provides email "<email>" and password "<password>"
    When The user sends a POST request to the provider login endpoint
    Then The response status code should be <statusCode>
    And The response error for field "<field>" should be "<error_message>"

      Examples:
      | email                            | password   | statusCode | field    | error_message                   |
      |                               	 | Test@12345 | 422        | email    | Email is required               |
      | pankaj.patidar@mindruby.com      |        	  | 422        | password | Password is required            |
      |                              	   |       		  | 422        | email    | Please include a valid email    |
      |                                	 |       	    | 422        | password | Password is required            |
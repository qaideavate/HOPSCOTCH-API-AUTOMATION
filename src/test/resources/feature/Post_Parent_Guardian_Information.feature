Feature: Save Parent/Guardian information with child

  Background:
    Given I have a parent token
    And The base URL
    And I have a valid child ID

  Scenario: Register a new parent linked to a child with valid data
    Given I prepare the parent registration payload with valid data
    When I send a POST request to Parent endpoint 
    Then the Parent registration response status code should be 200
    Then The response message should be for Parent guardian "Parent Guardian Created Successfully"
    Then the returned parentId should be a 3-digit positive number
    Then the success message should be true
    

	  Scenario Outline: Validate Parent registration with missing or invalid field values
	  Given I prepare the parent registration payload with valid data.
	  When I update "<field>" with "<value>"
    When I send a POST request to Parent endpoint
    Then the Parent resgistration response status code should be <statuscode>
	  And the response message should be "<expectedMessage>","<expectedFieldPath>" and  "<success>"

    Examples:    
    | field             | value               | statusCode | message                           | fieldPath                    							 | success |
    | firstName         |                     | 400        | First name is required            | parentInfo[0].firstName    							   | false   |
Feature: Save Parent/Guardian information with child

  Background:
    Given I have a parent token
    And The base URL
    And I have a valid child ID

  Scenario: Register a new parent linked to a child with valid data
    Given I prepare the parent registration payload with valid data
    When I send a POST request to Parent endpoint 
    Then the Parent registration response status code should be 200
    Then The response message should be for Parent guardian "Parent information saved successfully."
    Then the returned parentId should be a positive number
    Then the success message should be "true"
    

   
	  Scenario Outline: Validate Parent registration with Empty/invalid values : <field> --><value>
	  Given I prepare the parent registration payload with valid data
	  When I update parent "<field>" with "<value>"
    When I send a POST request to Parent endpoint
    Then the Parent registration response status code should be <statuscode>
	  Then the success message should be "<status>"
	  Then The response message should be for Parent guardian "<message>"
	  Then the error field path should be "<fieldPath>" 

    Examples:    
    | field             | value               | statuscode |status| message                                                 | fieldPath                    		         |
    | firstName         |                     | 400        |false |First name is required.                                  | parentInfo[0].firstName  							   |
    | lastName          |                     | 400        |false |Last name is required.                                   | parentInfo[0].lastName   							   |
    | email             |                     | 400        |false |Valid email is required.                                 | parentInfo[0].email    							     |
    | cellPhone         |                     | 400        |false |Cell phone must be a valid 10-digit number.              | parentInfo[0].cellPhone    							 |
    | streetAddress     |                     | 400        |false |Parent/Guardian 1: Street address is required.           | parentInfo[0].streetAddress    					 |
    | apt               |                     | 200        |true  |Parent information saved successfully.                   |N/A                                        | 							                     
    | city              |                     | 400        |false |Parent/Guardian 1: City is required.                     |parentInfo[0].city    							     |
    | zipCode           |                     | 400        |false |Parent/Guardian 1: Valid US ZIP code is required.        |parentInfo[0].zipCode    							   |
    | sameAddressAsChild|   false             | 200        |true  |Parent information saved successfully.                   |N/A 							                         |
    | relationship      |                     | 400        |false |Relationship is required.                                |parentInfo[0].relationship    					 | 
    | parentId          |   A                 | 400        |false |parentId should be integer type                          |parentInfo[0].integer    							   | 
    | middleName        |                     | 200        |true  |Parent information saved successfully.                   |N/A  																     |
    | workPhone         |                     | 200        |true  | Parent information saved successfully.                  |N/A																       |
    | altPhone          |                     | 200        |true  | Parent information saved successfully.                  |N/A  																     |
 
    
    
    
   
Feature: Save Parent/Guardian information with child

  Background:
    Given I have a valid parent token
    And I have a valid child ID

  Scenario: Register a new parent linked to a child with valid fields
    And I provide the parent registration "<body>" with all valid fields
    When I send a POST request to "user/register-parent"
    Then the response status code should be 200
    And the response message should be "Parent information saved successfully."
    And the returned parentId should be a 3-digit positive number
    And the success message should be true
     Examples:
      | body |
      | {
          "parents": [
            {
              "lastName": "Smith",
              "firstName": "Anna",
              "email": "annasmith@yopmail.com",
              "cellPhone": "1234567890",
              "streetAddress": "123 Main Street",
              "apt": "Apt 2",
              "city": "Springfield",
              "zipCode": "62704",
              "sameAddressAsChild": true,
              "relationship": "mother",
              "parentId": 0,
              "middleName": "Marie",
              "workPhone": "9876543210",
              "altPhone": "1122334455"
            }
          ],
          "childId": {{childID}}
        } |
      

	  Scenario Outline: Validate parent registration request body field validations
	  And I prepare the parent registration body with "<field>" set to "<value>"
	  When I send a POST request to "user/register-parent"
	  Then the response status code should be <statusCode>
	  And the response message should be "<message>"
	  And the field error should be "<fieldPath>"
	  And the success message should be "<success>".

    Examples:    
    | field             | value               | statusCode | message                           | fieldPath                    							 | success |
    | firstName         |                     | 400        | First name is required            | parentInfo[0].firstName    							   | false   |
    | lastName          |                     | 400        | Last name is required             | parentInfo[0].lastName      							   | false   |
    | email             |                     | 400        | Email is required                 | parentInfo[0].email          							 | false   |
    | email             | wrongemail.com      | 400        | Invalid email format              | parentInfo[0].email           							 | false   |
    | cellPhone         |                     | 400        | Cell phone is required            | parentInfo[0].cellPhone      							 | false   |
    | cellPhone         | abc123              | 400        | Invalid phone number              | parentInfo[0].cellPhone      							 | false   |
    | streetAddress     |                     | 400        | Address is required               | parentInfo[0].streetAddress  							 | false   |
    | city              |                     | 400        | City is required                  | parentInfo[0].city           							 | false   |
    | zipCode           |                     | 400        | Zip code is required              | parentInfo[0].zipCode       							   | false   |
    | zipCode           | abcd                | 400        | Invalid zip code                  | parentInfo[0].zipCode        							 | false   |
    | sameAddressAsChild| null                | 400        | sameAddressAsChild is required    | parentInfo[0].sameAddressAsChild 					 | false   |
    | relationship      |                     | 400        | Relationship is required          | parentInfo[0].relationship    							 | false   |
    | relationship      | stranger            | 400        | Invalid relationship              | parentInfo[0].relationship  							   | false   |
    | childId           |                     | 401        | Child Error                       | Invalid Child Id             							 | false   |
    | parentId          | -1                  | 400        | Invalid parentId                  | parentInfo[0].parentId       							 | false   |
    | workPhone         | abcd                | 400        | Invalid workPhone format          | parentInfo[0].workPhone     								 | false   |
    | altPhone          | 123abc              | 400        | Invalid alternate phone format    | parentInfo[0].altPhone        							 | false   |
    
    
    
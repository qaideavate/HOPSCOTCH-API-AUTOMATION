Feature: Register Child API Test

  Background:
    Given I have a valid parent token
    And The base URL 

  Scenario: Register a new child with valid fields
    Given I provide the child register with the following details:
      | childId | lastName | middleName | firstName | nickname | birthdate   | gender | streetAddress           | city         | zipCode | apt |
      |123      |Test      |            |AsadApi    |          |2025-05-01   |Boy     |830 Southeast Ireland    |Oak Harbor    |98277    |     |
      
    
    When I send a POST request to child endpoint
    Then the child registration response status code should be 200
   	Then The response message should be for child "Child information saved successfully."
    And the returned childId should be a 3-digit positive number
    
      

	 Scenario Outline: Registering a child with invalid or missing fields
    Given I provide the child register with the following details from outline::
      | childId   | lastName 	 | middleName   | firstName   | nickname   | birthdate   | gender   | streetAddress   | city   | zipCode   | apt   |
      |"<childId>"|"<lastName>"|"<middleName>"|"<firstName>"|"<nickname>"| <birthdate> |"<gender>"|"<streetAddress>"|"<city>"|<zipCode>  |"<apt>"|
    Then the child registration response status code should be <statusCode>
    Then The response message should be for child "<message>"
    And the error fieldPath should be "<fieldPath>"

  Examples:
| childId   | lastName | middleName | firstName | nickname | birthdate     | gender | streetAddress | city     | zipCode | apt | statusCode | message                                 | fieldPath               |
|           | Smith    | Middle     | John      | Johnny   |2012-08-15     | Boy    | 123 Main St   | New York | 98277   |     | 200        | Child information saved successfully.   | childInfo.childId       |
| ABC123    |          | Middle     | John      | Johnny   |2012-08-15     | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | Last name is required.                  | childInfo.lastName      |
| ABC123    | Smith    |            | John      | Johnny   |2012-08-15     | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | Child information saved successfully.   | childInfo.firstName     |
| ABC123    | Smith    | Middle     |           | Johnny   |2012-08-15     | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | First name is required                  | childInfo.firstName     |
| ABC123    | Smith    | Middle     | John      | Johnny   |               | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | Birthdate is required                   | childInfo.birthdate     |
| ABC123    | Smith    | Middle     | John      |          |               | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | Birthdate is required                   | childInfo.birthdate     |
| ABC123    | Smith    | Middle     | John      | Johnny   |invalid-date   |        | 123 Main St   | New York | 98277   | 12A | 400        | Birthdate must be in YYYY-MM-DD format  | childInfo.birthdate     |
| ABC123    | Smith    | Middle     | John      | Johnny   |2025-12-01     | Boy    | 123 Main St   | New York | 98277   | 12A | 400        | Birthdate cannot be in the future       | childInfo.birthdate     |
| ABC123    | Smith    | Middle     | John      | Johnny   |2012-08-15     | Alien  | 123 Main St   | New York | 98277   | 12A | 400        | Invalid gender: must be 'Boy' or 'Girl' | childInfo.gender        |
| ABC123    | Smith    | Middle     | John      | Johnny   |2012-08-15     | Boy    |               | New York | 98277   | 12A | 400        | Street address is required              | childInfo.streetAddress |
| ABC123    | Smith    | Middle     | John      | Johnny   |2012-08-15     | Boy    | 123 Main St   |          | 98277   | 12A | 400        | City is required                        | childInfo.city          |
| ABC123    | Smith    | Middle     | John      | Johnny   |2012-08-15     | Boy    | 123 Main St   | New York | abcde   | 12A | 400        | Zip code must be numeric                | childInfo.zipCode       |

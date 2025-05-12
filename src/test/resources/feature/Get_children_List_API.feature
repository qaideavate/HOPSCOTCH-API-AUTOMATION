@Priority2
Feature: Validate children with enrollments API response

Background: Given I have a valid ParentToken
 
  Scenario: Validate response schema
    When I send a GET request of children-with-enrollments API
    Then the response status code should be 200 
    And  the response body should match the predefined JSON schema "children_with_enrollments_schema.json"
   
 
  Scenario: Validate all children have the same user_id
    When I send a GET request of children-with-enrollments API
    Then all children should have the same "user_id"
 
  Scenario: Validate presence of required fields in each child object
    When I send a GET request of children-with-enrollments API 
    Then each child object should contain the following fields:
      | id                |
      | user_id           |
      | first_name        |
      | last_name         |
      | birthdate         |
      | gender            |
      | street_address    |
      | city              |
      | zip_code          |
      | status            |
      | created_at        |
      | modified_at       |
      | childName         |
      | child_info_id     |
      | child_info_status |
      | enrollment_count  |
 
  Scenario: Validate mandatory fields are not null or empty
    When I send a GET request of children-with-enrollments API 
    Then for each child, the following fields should not be null or empty:
			 |first_name			| 
			 |last_name				| 
			 |birthdate				|
			 |street_address  |
			 | city						|
			 |zip_code				| 
			 |gender					|
 
  Scenario: Validate id matches child_info_id
    When I send a GET request of children-with-enrollments API 
    Then for each child, the "id" should be equal to "child_info_id"
 
  Scenario: Validate childName is a combination of first_name and last_name
    When I send a GET request of children-with-enrollments API 
    Then for each child, the "childName" should be equal to "first_name last_name"
 
  Scenario: Validate gender values 
    When I send a GET request of children-with-enrollments API
    Then the "gender" field should be one of the following:
      | Boy               |
      | Girl              |
      | Prefer not to say |
 
  Scenario: Validate enrollment_count is a positive integer
    When I send a GET request of children-with-enrollments API 
    Then the "enrollment_count" should be a positive number for each child
 
  Scenario: Validate status message is "ok"
    When I send a GET request of children-with-enrollments API 
    Then the response status message should be "HTTP/1.1 200 OK"
 
  Scenario: Validate the response time 
    When I send a GET request of children-with-enrollments API 
    Then the Reponse time should be less than 1.5 seconds.
 
 

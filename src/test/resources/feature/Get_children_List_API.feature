@Priority2
Feature: Validate children-with-enrollments API response

  Background:
    Given I log in with valid parent credentials

  Scenario: Get children with enrollments using valid bearer token
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then the response body should match the predefined JSON schema "children_with_enrollments_schema.json"
    Then the response status code should be 200

  Scenario: Validate all children have the same user_id
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then all children should have the same "user_id"

  Scenario: Validate required fields exist in each child object
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then each child in the response should have the following fields:
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
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then for each child, the fields "first_name", "last_name", "birthdate", "street_address", "city", "zip_code", and "gender" should not be null or empty

  Scenario: Validate id and child_info_id are the same
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then for each child, the "id" should be equal to "child_info_id"

  Scenario: Validate childName is the combination of first_name and last_name
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then for each child, the "childName" should be equal to "first_name last_name"

  Scenario: Validate gender values are within the allowed options
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then the "gender" field should be one of the following:
      | Boy               |
      | Girl              |
      | Prefer not to say |

  Scenario: Validate enrollment_count is a positive integer
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then the "enrollment_count" should be a positive number for each child

  Scenario: Validate status message is "ok"
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then the response status message should be "ok"

  Scenario: Validate the response time is not more than 1.5 seconds
    When I send a GET request of children-with-enrollments API with the ParentToken
    Then the Reponse time should be less than 1.5 seconds.

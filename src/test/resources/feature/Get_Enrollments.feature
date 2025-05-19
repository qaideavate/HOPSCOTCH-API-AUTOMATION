Feature: Validate children with enrollments API response

Background: Given I have a valid ParentToken
 @runthis
  Scenario: Validate response schema
    When I send a GET request of Get Enrollments API
    Then the Enrolled child-list response status code should be 200 
    And  the Enrolled list response body should match the predefined JSON schema "Get Enrollments.json"
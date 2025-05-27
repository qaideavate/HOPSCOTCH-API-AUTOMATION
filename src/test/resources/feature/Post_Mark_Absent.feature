 Feature: Mark child enrollment as absent
  
 Scenario: Successfully mark a child enrollment as absent
    Given I prepare a valid request body for marking absence
    When I send a POST request to providers/enrollments/mark-absent
    Then the response status code should be 200
    And the response body should contain "success" as true
    And the response body should contain the key "message"
    
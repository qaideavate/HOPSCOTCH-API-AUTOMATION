 Feature: Mark child enrollment as absent
  @runthis
 Scenario: Successfully mark a child enrollment as absent
  
    When I send a POST request to providers/enrollments/mark-absent with 516
    And the response body should contain "status" as true
    And the response body should contain the keyvalue "message"
    
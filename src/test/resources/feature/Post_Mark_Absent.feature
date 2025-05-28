 Feature: Mark child enrollment as absent
  @runthis
 Scenario: Successfully mark a child enrollment as absent
  
    When I send a POST request to providers/enrollments/mark-absent with 488
    And the response body should contain "success" as true
    And the response body should contain the keyvalue "message"
    
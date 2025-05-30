 Feature: Mark child enrollment as absent

 Scenario: Successfully mark a child enrollment as absent
  
    When I send a POST request to providers/enrollments/mark-absent with 516
    And the response body should contain "status" as true
    And the response body should contain the keyvalue "message"
    
    
  @runthis
 Scenario: Successfully mark a child enrollment as graduated
    When I send a POST request to providers/enrollments/mark-graduation with 608
    Then the response body should contain "status" as true
    And the response body should contain the keyvalue "message"
Feature: Classroom Capacity and Enrollment Management

@runthis
  Scenario: Initialize classroom and check initial capacity
    When both Parent and Provider login via the POST login endpoint
    And the provider creates a new classroom using the POST classroom endpoint
    And the provider retrieves the classroom capacity using the GET classroom endpoint with the classroom ID

@runthis
  Scenario: Enroll children with limited classroom capacity
    When the "Parent" login via the POST login endpoint
    And the parent creates 4 new children with valid information
    And the parent enrolls 2 children for regular care in the classroom

@runthis
 Scenario: Approve Enrolled child
  When the "Provider" login via the POST login endpoint
  And Approve the Enrolled 2 child.
  
@runthis
  Scenario: Mark an enrolled child as absent
    When the "Provider" login via the POST login endpoint
    When the provider marks 1 enrolled child as absent for today and tomorrow using the enrollment ID
    
@runthis
  Scenario: Enroll a drop-in child during an absence period
    When the "Parent" login via the POST login endpoint
    And the parent attempts to drop-in 1 child on the same dates another child is absent
    
@runthis
 Scenario: Approve Enrolled child
  When the "Provider" login via the POST login endpoint
  And Approve the Enrolled drop in 1 child.
  
@runthis
  Scenario: Graduate an enrolled child
    When the "Provider" login via the POST login endpoint
    And the provider graduates 1 enrolled child effective from today's date
    
@runthis
  Scenario: Enroll a new child after a graduation
    When the "Parent" login via the POST login endpoint
    And the parent enrolls the 1 child starting from the day after the graduation date

  Scenario: View updated classroom capacity
    When the "Provider" login via the POST login endpoint
    Then the updated classroom capacity should be accurately reflected by the system

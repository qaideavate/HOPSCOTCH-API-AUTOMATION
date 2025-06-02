Feature: Classroom Capacity and Enrollment Management

@runthis
  Scenario: Initialize classroom and check initial capacity
    When both Parent and Provider login via the POST login endpoint
    And the provider creates a new classroom using the POST classroom endpoint
    And the provider retrieves the classroom capacity using the GET classroom endpoint with the classroom ID

@runthis
  Scenario: Enroll children with limited classroom capacity
    When the "Parent" login via the POST login endpoint
    And  the parent creates new children with valid information
    And the parent enrolls children for regular care in the classroom

@runthis
 Scenario: Approve Enrolled child
  When the "Provider" login via the POST login endpoint
  And Approve the Enrolled  child.
  
@runthis
  Scenario: Mark an enrolled child as absent
    When the "Provider" login via the POST login endpoint
    When the provider marks  enrolled child as absent for today and tomorrow using the enrollment ID
    
@runthis
  Scenario: Enroll a drop-in child during an absence period
    When the "Parent" login via the POST login endpoint
    And the parent attempts to drop-in  child on the same dates another child is absent
    
@runthis
 Scenario: Approve Enrolled child
  When the "Provider" login via the POST login endpoint
  And Approve the Enrolled drop in  child
  
@runthis
  Scenario: Graduate an enrolled child
    When the "Provider" login via the POST login endpoint
    And the provider graduates enrolled child effective from today's date
    
@runthis
  Scenario: Enroll a new child after a graduation
    When the "Parent" login via the POST login endpoint
    And the parent enrolls the  child starting from the day after the graduation date
    
@runthis
 Scenario: Approve Enrolled child
  When the "Provider" login via the POST login endpoint
  And Approve the Enrolled  child after the Graduation of Another Child.

Feature: Classroom Capacity and Enrollment Management
@runthis
  Scenario: Initialize classroom and check initial capacity
    When the "Provider" login via the POST login endpoint
    And the provider creates a new classroom using the POST classroom endpoint
    And the provider retrieves the classroom capacity using the GET classroom endpoint with the classroom ID
@runthis
  Scenario: Enroll children with limited classroom capacity
    When the "Parent" logs in via the POST login endpoint
    And the parent creates 4 new children with valid information
    And the parent enrolls 3 children for regular care in the classroom
    Then only 2 children should be enrolled successfully
    And the remaining 1 child should be added to the waitlist due to capacity limits

  Scenario: Mark an enrolled child as absent
    When the "Provider" logs in via the POST login endpoint
    And the provider marks 1 enrolled child as absent for today and tomorrow using the enrollment ID

  Scenario: Enroll a drop-in child during an absence period
    When the "Parent" logs in via the POST login endpoint
    And the parent attempts to enroll a drop-in child on the same dates another child is absent

  Scenario: Graduate an enrolled child
    When the "Provider" logs in via the POST login endpoint
    And the provider graduates 1 enrolled child effective from today's date

  Scenario: Enroll a new child after a graduation
    When the "Parent" logs in via the POST login endpoint
    And the parent enrolls the 4th child starting from the day after the graduation date

  Scenario: View updated classroom capacity
    When the "Provider" logs in via the POST login endpoint
    Then the updated classroom capacity should be accurately reflected by the system

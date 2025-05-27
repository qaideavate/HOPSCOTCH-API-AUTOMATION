
Feature :Feature: Classroom Capacity and Enrollment Management

Scenario: Manage classroom capacity with regular, drop-in, absent, and graduated children
When The "Provider" sends a POST request to the login endpoint.
And I send a POST request to create classroom at classroom endpoint.
And I send a GET request to view classroom capacity at classroom endpoint with classroom Id.

Scenario: Regular enrollment with limited capacity
When The "Parent" sends a POST request to the login endpoint
And the user create a 4 new child with valid details.
And the user enrolls 3 children for regular care in the classroom
Then only 2 children should be enrolled successfully
And 1 child should be placed on the waitlist due to capacity

Scenario: Marking a child absent
When The "Provider" sends a POST request to the login endpoint
When the user marks 1 enrolled child as absent for today and tomorrow using enrollment ID

Scenario: Drop-in enrollment during absence
When The "Parent" sends a POST request to the login endpoint
And the user tries to enroll a drop-in child for the same absent dates

Scenario: Graduating a child
When The "Provider" sends a POST request to the login endpoint
When the user graduates 1 enrolled child with today’s date

Scenario: Enrolling a new child after graduation
When The "Parent" sends a POST request to the login endpoint
And the user enrolls the 4th new child starting from the next day after graduation

Scenario: Reflect updated classroom capacity
When The "Provider" sends a POST request to the login endpoint
Then the system should reflect the updated classroom capacity
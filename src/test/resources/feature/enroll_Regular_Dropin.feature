Feature: Enroll a child in a classroom

  Background: Given I have a valid ParentToken, providerId, classroom, child, and start date from config

  Scenario: Successfully enroll dropin child in a classroom
    When I send a POST request to enroll-Dropin child "childId" API with valid body
    Then the response status code for Enrollchild should be 200
    And the response body should contain "message" as "Enrollment successful"


@runthis
  Scenario: Successfully enroll regular child in a classroom
    When I send a POST request to enroll-regular child "childId" API with valid body
    Then the response status code for Enrollchild should be 201
    And the response body should contain "message" as "Enrollment successful"
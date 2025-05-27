Feature: Create a New Classroom

  Background: Given I have valid provider token
	
  Scenario: Successfully create a classroom using the provider token
    When I send a POST request to the endpoint
    Then the response status code on Creating Classroom  should be 200
    And the response body should contain "success" as true
    And the response body should contain the key "classroomId"

    
 
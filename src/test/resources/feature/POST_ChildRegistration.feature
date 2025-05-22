Feature: Register Child and Parent API Test

Background:
  Given The base URL is set

Scenario: Successfully Register a Child with valid information
  Given I have a valid Parent Token
  And I prepare the child registration payload with valid data
  When I send a POST request to child endpoint
  Then the child registration response status code should be 200
  And The response message should be for child "Child information saved successfully."
  And the returned childId should be a positive number
  And the child registration response should store childId

Scenario: Register a new parent linked to a child with valid information
  Given I have a valid child ID
  And I have a valid parent token
  And I prepare the parent registration payload with valid data
  When I send a POST request to Parent endpoint
  Then the Parent registration response status code should be 200
  And The response message should be for Parent guardian "Parent information saved successfully."
  And the returned parentId should be a positive number
  And the success message should be "true"

Scenario: Emergency
  Given I have a valid child ID
  And I have a valid parent token
  And I prepare the parent registration payload with valid data
  When I send a POST request to Parent endpoint
  Then the Parent registration response status code should be 200
  And The response message should be for Parent guardian "Parent information saved successfully."
  And the returned parentId should be a positive number
  And the success message should be "true"
  
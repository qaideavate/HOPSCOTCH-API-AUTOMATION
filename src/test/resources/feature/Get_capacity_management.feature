Feature: Provider Capacity API
Background : Given I have a valid ProviderToken and baseurl

  Scenario: Verify the provider capacity API returns correct data for classroom ID
    When I send a GET request to view classroom capacity at classroom endpoint with classroom Id
    Then the response status code for classroom should be 200

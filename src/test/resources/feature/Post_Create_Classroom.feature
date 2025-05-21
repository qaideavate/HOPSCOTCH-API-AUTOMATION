Feature: Create a New Classroom

  Background: Given I have the base URL and a valid provider token

  Scenario: Successfully create a classroom using the provider token
    Given I prepare a valid request body for creating a classroom
    When I send a POST request to the "/providers/programs" endpoint
    Then the response status code on Creating Classroom  should be 200
    And the response body should contain "success" as true
    And the response body should contain the key "classroomId"

  Scenario Outline: Create classroom with dynamic or optional values
    Given I prepare a request body with the following values:
      | name                      | <name>                      |
      | min_age                   | <min_age>                   |
      | max_age                   | <max_age>                   |
      | start_date                | <start_date>                |
      | license_capacity          | <license_capacity>          |
      | capacity                  | <capacity>                  |
      | enrollment_cutoff_window  | <enrollment_cutoff_window>  |
      | fulltime_tuition          | <fulltime_tuition>          |
      | fulltime_tuition_cadence  | <fulltime_tuition_cadence>  |
      | fulltime_tuition_status   | <fulltime_tuition_status>   |
      | mornings_tuition_status   | <mornings_tuition_status>   |
      | afternoons_tuition_status | <afternoons_tuition_status> |
      | mwf_tuition_status        | <mwf_tuition_status>        |
      | tuth_tuition_status       | <tuth_tuition_status>       |
      | dropin_tution_status      | <dropin_tution_status>      |
      | keywords                  | <keywords>                  |
    When I send a POST request to the "/providers/programs" endpoint
    Then the response status code on Creating Classroom  should be 200
    And the response body should contain "success" as true
    And the response body should contain the key "classroomId"

    Examples:
      | name | min_age | max_age | start_date | license_capacity | capacity | enrollment_cutoff_window | fulltime_tuition | fulltime_tuition_cadence | fulltime_tuition_status | mornings_tuition_status | afternoons_tuition_status | mwf_tuition_status | tuth_tuition_status | dropin_tution_status | keywords |
      |      |         |         |            |                   |          |                          |                  |                           |                         |                         |                           |                     |                     |                      |          |

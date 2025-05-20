Feature: Provider Capacity API
Background : Given I have a valid ProviderToken and baseurl


  Scenario: Verify the provider capacity API returns correct data for classroom ID 78
    When I send a GET request to the endpoint with path parameter 78
    Then the response status code should be 200
    And the classroom object should have the following fields:
      | id                     | 78          |
      | provider_id            | 12          |
      | name                   | TEST Class  |
      | min_age                | 1           |
      | max_age                | 102         |
      | license_capacity       | 100         |
      | capacity               | 205         |
      | enrollment_cutoff_range| 56          |
      | start_date             | 2025-05-13  |
    And the classroom should contain a schedule_capacity object
    And the full_time_enrollment should be 12
    And the dropin_capacity should contain today and tomorrow dates
    And the today dropin capacity should have:
      | date                    | 2025-05-20  |
      | classroom_id            | 78          |
      | classroom_name          | TEST Class  |
      | license_capacity        | 100         |
      | tuition                 | 23.00       |
      | morning_used_capacity   | 13          |
      | afternoon_used_capacity | 13          |
      | total_used_capacity     | 13          |
      | available_capacity      | 87          |
    And the tomorrow dropin capacity should have:
      | date                    | 2025-05-21  |
      | classroom_id            | 78          |
      | classroom_name          | TEST Class  |
      | license_capacity        | 100         |
      | tuition                 | 23.00       |
      | morning_used_capacity   | 12          |
      | afternoon_used_capacity | 12          |
      | total_used_capacity     | 12          |
      | available_capacity      | 88          |

  Scenario Outline: Verify the provider capacity API returns correct data for classroom ID <classroomId>
    When I send a GET request to the endpoint with path parameter <classroomId>
    Then the response status code should be 200
    And the classroom object in response should have "id" equal to <classroomId>

    Examples:
      | classroomId |
      | 78          |
      | 75          |

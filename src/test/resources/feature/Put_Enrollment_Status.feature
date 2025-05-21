Feature: Update Enrollment Status for a Child

  Background: Given I have a valid ProviderToken and baseurl
    And I have a valid child ID


  Scenario : Successfully update enrollment status to <status> for child ID
    Given I prepare a request body with enrollment status "<status>"
    When I send a PUT request to endpoint with child id
    Then the response status code should be 200
    And the response body should contain:
      |status  | message | Enrollment successfully updated |
      |Approved| program | 314                             |


  Scenario Outline: Successfully update enrollment status to <status> for child ID
    Given I prepare a request body with enrollment status "<status>"
    When I send a PUT request to endpoint with child id
    Then the response status code should be 200
    And the response body should contain:
      | message | Enrollment successfully updated |
      | program | 314                             |

    Examples:
      | status         |
      | Pending Review |
      | In Review      |
      | Approved       |
      | Rejected       |
      | Wait Listed    |
      | Terminated     |
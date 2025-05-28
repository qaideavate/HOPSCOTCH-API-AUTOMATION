Feature: Update Enrollment Status for a Child

  Background: Given I have a valid ProviderToken and baseurl

@runthis
 Scenario: Successfully update enrollment status for child ID
    When I send a PUT request to endpoint with "484" and status "approved"
    Then the update Enrollment status code  should be 201
  		


  Scenario Outline: Successfully update enrollment status to <status> for child ID
    When I send a PUT request to endpoint with "<childId>" and status "approved"
    Then the update Enrollment status code  should be 201
    And the response body should contain:
      | message | Enrollment successfully updated |
      | program | 265                             |

    Examples:
     | childId | status         |
     |2009 		 | Pending Review |
     |2009 		 | In Review      |
     |2009 		 | Approved       |
		 |2009 		 | Rejected       |
     |2009     | Wait Listed    |
     |2009     | Terminated     |
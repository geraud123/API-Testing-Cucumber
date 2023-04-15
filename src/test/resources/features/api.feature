Feature: Simple Books API Testing

  Scenario Outline: Returns the status of the API.
    Given The user open the "<URL>"
    Then API return the response with status code as "200"

    Examples:
      | URL                                |
      | https://simple-books-api.glitch.me |


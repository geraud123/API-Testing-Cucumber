Feature: Simple Books API Testing

  Scenario: Returns the status of the API
    Given The user open the URL with the path "/status"
    Then API return the response with status code as 200
    And The response contain the attribut "status" with the value "OK"

  Scenario: Returns a list of books
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contain 6 books

  Scenario: Returns a list of books with type non-fiction
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contain 2 books with a parameter "type" equals to "non-fiction"

  Scenario: Returns a list of books with limit 4
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contains 4 first books from the library
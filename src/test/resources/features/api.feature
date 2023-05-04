Feature: Simple Books API Testing

  Scenario: Returns the status of the API
    Given The user open the URL with the path "/status"
    Then API return the response with status code as 200
    And The response contains the attribute "status" with the value "OK"

  Scenario: Returns a list of books
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contains 6 books

  Scenario: Returns a list of books with type non-fiction
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contains 2 books with a parameter "type" equals to "non-fiction"

  Scenario: Returns a list of books with limit 4
    Given The user open the URL with the path "/books"
    Then API return the response with status code as 200
    And The response contains 4 first books from the library

  Scenario: Get a single book with a bookId=2
    Given The user open the URL with the path "/books/2"
    Then API return the response with status code as 200
    And The response contains 1 books with a parameter "name" equals to "Just as I Am"

  Scenario: API Authentication
    Given The user open the URL with the path "/api-clients/" and the new user
    Then API return the response with status code as 201
    And The response contains the accessToken

  Scenario: Submit an order
    Given The user open the URL with the path "/orders" with the properties
    Then API return the response with status code as 201
    And The response contains the parameter "created" equals to "true"

  Scenario: Get all orders
    Given The user open the URL with the path "/orders" with the authorization
    Then API return the response with status code as 200
    And The response contains a not-empty list

  Scenario: Get an order
    Given The user open the URL with the path "/orders/" with the id
    Then API return the response with status code as 200
    And The response contains a order with the same id

  Scenario: Update an order
    Given The user open the URL with the path "/orders/" with the id and the new name "myName"
    Then API return the response with status code as 204

  Scenario: Get the modified order
    Given The user open the URL with the path "/orders/" with the id
    Then API return the response with status code as 200
    And The response contains a order with the same id and the customerName equals "myName"

  Scenario: Delete an order
    Given The user open the URL with the path "/orders/" with the id for deleting
    Then API return the response with status code as 204

  Scenario: Get the deleted order
    Given The user open the URL with the path "/orders/" with the id
    Then API return the response with status code as 404
    And The response contains error message

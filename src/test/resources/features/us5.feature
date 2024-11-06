Feature: Retrieve To-Do List Item in Different Formats

    Background: Existing To-Do List Item
        Given the application is running on port "1234"
        And the following To-Do List Items exist
            | title    | description                  | doneStatus | id |
            | Get Milk | Buy 2% milk at the groceries | false      | 3  |

    @Test13
    Scenario: 13. Retrieve a To-Do List Item in JSON format
        When the user sets the Accept header to "application/json"
        And the user makes a "GET" request to the "/todos/3" endpoint
        Then the response should be in "application/json" format

    @Test14
    Scenario: 14. Retrieve a To-Do List Item in XML format
        When the user sets the Accept header to "application/xml"
        And the user makes a "GET" request to the "/todos/3" endpoint
        Then the response should be in "application/xml" format

    @Test15
    Scenario: 15. Fail to retrieve a To-Do List Item in HTML format
        When the user sets the Accept header to "text/html"
        And the user makes a "GET" request to the "/todos/3" endpoint
        Then the user should receive a 406 code error message
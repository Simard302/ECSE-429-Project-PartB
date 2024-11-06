Feature: Update a To-Do List Item

    Background: Existing To-Do List Item
        Given the application is running on port "4567"
        And the following To-Do List Items exist
            | title    | description                  | doneStatus | id |
            | Get Milk | Buy 2% milk at the groceries | false      | 3  |

    Scenario: 7. Update the doneStatus of a To-Do List Item
        When the user has a title: "Get Milk", description: "Buy 2% milk at the groceries", and doneStatus: "true"
        And the user makes a "PUT" request to the "/todos/3" endpoint
        Then the To-Do List Item with id "3" should have title "Get Milk"
        And the To-Do List Item with id "3" should have description "Buy 2% milk at the groceries"
        And the To-Do List Item with id "3" should have doneStatus "true"

    Scenario: 8. Update the description of a To-Do List Item
        When the user has a title: "Get Milk", description: "Buy 3% milk at the grocery store", and doneStatus: "false"
        And the user makes a "PUT" request to the "/todos/3" endpoint
        Then the To-Do List Item with id "3" should have title "Get Milk"
        And the To-Do List Item with id "3" should have description "Buy 3% milk at the grocery store"
        And the To-Do List Item with id "3" should have doneStatus "false"

    Scenario: 9. Fail to update a To-Do List Item with an empty title
        When the user has a title: "", description: "Buy 2% milk at the groceries", and doneStatus: "false"
        And the user makes a "PUT" request to the "/todos/3" endpoint
        Then the user should receive a 400 code error message
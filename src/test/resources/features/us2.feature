Feature: Retrieve To-Do List Items

    Background: Existing To-Do List Items with various doneStatus values
        Given the application is running on port "4567"
        And the following To-Do List Items exist
            | title      | description                            | doneStatus | id |
            | Get Milk   | Buy 2% milk at the groceries           | false      | 3  |
            | Get Eggs   | Buy a dozen eggs at the groceries      | false      | 4  |
            | Get Bread  | Buy a loaf of bread at the groceries   | true       | 5  |
            | Get Butter | Buy a stick of butter at the groceries | true       | 6  |

    Scenario: 4. Retrieve all To-Do List Items
        When the user makes a "GET" request to the "/todos" endpoint
        Then the response contains a To-Do List Item with the following ids
            | id |
            | 3  |
            | 4  |
            | 5  |
            | 6  |
        And the To-Do List Item with id "3" should have title "Get Milk"
        And the To-Do List Item with id "4" should have title "Get Eggs"
        And the To-Do List Item with id "5" should have title "Get Bread"
        And the To-Do List Item with id "6" should have title "Get Butter"

    Scenario: 5. Retrieve all To-Do List Items where doneStatus=false
        When the user makes a "GET" request to the "/todos?doneStatus=false" endpoint
        Then the response contains a To-Do List Item with the following ids
            | id |
            | 3  |
            | 4  |
        And the To-Do List Item with id "3" should have title "Get Milk"
        And the To-Do List Item with id "4" should have title "Get Eggs"

    Scenario: 6. Fail to retrieve To-Do List Items where doneStatus=maybe
        When the user makes a "GET" request to the "/todos?doneStatus=maybe" endpoint
        Then the user should receive an empty list of To-Do List Items
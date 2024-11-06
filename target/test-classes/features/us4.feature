Feature: Delete a To-Do List Item

    Background: Existing To-Do List Item
        Given the application is running on port "4567"
        And the following To-Do List Items exist
            | title      | description                            | doneStatus | id |
            | Get Milk   | Buy 2% milk at the groceries           | false      | 3  |
            | Get Eggs   | Buy a dozen eggs at the groceries      | false      | 4  |
            | Get Bread  | Buy a loaf of bread at the groceries   | true       | 5  |
            | Get Butter | Buy a stick of butter at the groceries | true       | 6  |

    Scenario: 10. Delete a To-Do List Item that is completed
        When the user makes a "DELETE" request to the "/todos/5" endpoint
        Then no To-Do List Item with id "5" should exist

    Scenario: 11. Delete a To-Do List Item that is incomplete
        When the user makes a "DELETE" request to the "/todos/3" endpoint
        Then no To-Do List Item with id "3" should exist

    Scenario: 12. Fail to delete a To-Do List Item that is not found
        When the user makes a "DELETE" request to the "/todos/7" endpoint
        Then the user should receive a 404 code error message
        And no To-Do List Item with id "7" should exist
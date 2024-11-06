Feature: Create a To-Do List Item

    Background: No To-Do List Items
        Given the application is running on port "1234"
        And no To-Do List Items exist

    @Test1
    Scenario: 1. Create a task with title, description, and doneStatus=false
        Given the user has a title: "Get Bread", description: "Buy a loaf of bread at the groceries", and doneStatus: "false"
        When the user makes a "POST" request to the "/todos" endpoint
        Then the response contains a To-Do List Item with the following ids
            | id |
            | 3  |
        And the To-Do List Item with id "3" should have title "Get Bread"
        And the To-Do List Item with id "3" should have description "Buy a loaf of bread at the groceries"
        And the To-Do List Item with id "3" should have doneStatus "false"

    @Test2
    Scenario: 2. Create a task with title, description, and doneStatus=true
        Given the user has a title: "Get Bread", description: "Buy a loaf of bread at the groceries", and doneStatus: "true"
        When the user makes a "POST" request to the "/todos" endpoint
        Then the response contains a To-Do List Item with the following ids
            | id |
            | 3  |
        And the To-Do List Item with id "3" should have title "Get Bread"
        And the To-Do List Item with id "3" should have description "Buy a loaf of bread at the groceries"
        And the To-Do List Item with id "3" should have doneStatus "true"

    @Test3
    Scenario: 3. Fail to create a task with no title, no description, and doneStatus=false
        Given the user has a title: "", description: "", and doneStatus: "true"
        When the user makes a "POST" request to the "/todos" endpoint
        Then the user should receive a 400 code error message
        And no To-Do List Items should exist
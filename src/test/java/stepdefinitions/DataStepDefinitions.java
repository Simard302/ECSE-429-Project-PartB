package stepdefinitions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dto.TodoRequestDTO;
import dto.TodoResponseDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static junit.framework.Assert.assertEquals;

public class DataStepDefinitions {

    private TodoRequestDTO requestDto;
    private String acceptHeader;
    private Response response;
    private boolean singleTodo;

    @Given("the user has a title: {string}, description: {string}, and doneStatus: {string}")
    public void userHasRequestData(String title, String description, String doneStatus) {
        assertEquals(true, doneStatus.equals("true") || doneStatus.equals("false"));
        this.requestDto = new TodoRequestDTO(title, doneStatus.equals("true"), description);
    }

    @When("the user sets the Accept header to {string}")
    public void userSetsAcceptHeader(String accept) {
        this.acceptHeader = accept;
    }

    @When(
            "the user makes a {string} request to the {string} endpoint")
    public void userMakesRequest(String method, String endpoint) {
        if (acceptHeader == null) {
            acceptHeader = "application/json";
        }
        switch (method) {
            case "GET" -> {
                this.response = given()
                        .accept(acceptHeader)
                        .get(endpoint);
                this.singleTodo = endpoint.contains("/todos/");
            }
            case "POST" -> {
                this.response = given()
                        .contentType(ContentType.JSON)
                        .body(this.requestDto)
                        .accept(acceptHeader)
                        .post(endpoint);
                this.singleTodo = true;
            }
            case "PUT" -> {
                this.response = given()
                        .contentType(ContentType.JSON)
                        .accept(acceptHeader)
                        .body(this.requestDto)
                        .put(endpoint);
                this.singleTodo = true;
            }
            case "DELETE" -> {
                this.response = given()
                        .accept(acceptHeader)
                        .delete(endpoint);
                this.singleTodo = true;
            }
        }
    }

    @Then("the response contains a To-Do List Item with the following ids")
    public void verifyResponseIds(DataTable dt) {
        List<String> ids = dt.cells().stream()
                .skip(1)
                .map(row -> row.get(0)) // Get first column value
                .collect(Collectors.toList());

        if (this.singleTodo) {
            TodoResponseDTO todoResponseDTO = response.as(TodoResponseDTO.class);
            assertEquals(1, ids.size());
            assertEquals(ids.get(0), todoResponseDTO.getId());
        } else {
            TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
            assertEquals(ids.size(), todoResponseDTOs.length);
            for (int i = 0; i < ids.size(); i++) {
                final int index = i;
                assertEquals(true, Arrays.stream(todoResponseDTOs).anyMatch(obj -> obj.getId().equals(ids.get(index))));
            }
        }
    }

    @Then("the user should receive an empty list of To-Do List Items")
    public void verifyEmptyList() {
        TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
        assertEquals(0, todoResponseDTOs.length);
    }

    @Then("the To-Do List Item with id {string} should have title {string}")
    public void verifyTitle(String id, String title) {
        if (this.singleTodo) {
            TodoResponseDTO todoResponseDTO = response.as(TodoResponseDTO.class);
            assertEquals(id, todoResponseDTO.getId());
            assertEquals(title, todoResponseDTO.getTitle());
        } else {
            TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
            assertEquals(true, Arrays.stream(todoResponseDTOs).anyMatch(obj -> obj.getId().equals(id) && obj.getTitle().equals(title)));
        }
    }

    @Then("the To-Do List Item with id {string} should have description {string}")
    public void verifyDescription(String id, String description) {
        if (this.singleTodo) {
            TodoResponseDTO todoResponseDTO = response.as(TodoResponseDTO.class);
            assertEquals(id, todoResponseDTO.getId());
            assertEquals(description, todoResponseDTO.getDescription());
        } else {
            TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
            assertEquals(true, Arrays.stream(todoResponseDTOs).anyMatch(obj -> obj.getId().equals(id) && obj.getDescription().equals(description)));
        }
    }

    @Then("the To-Do List Item with id {string} should have doneStatus {string}")
    public void verifyDoneStatus(String id, String doneStatus) {
        if (this.singleTodo) {
            TodoResponseDTO todoResponseDTO = response.as(TodoResponseDTO.class);
            assertEquals(id, todoResponseDTO.getId());
            assertEquals(doneStatus, todoResponseDTO.getDoneStatus());
        } else {
            TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
            assertEquals(true, Arrays.stream(todoResponseDTOs).anyMatch(obj -> obj.getId().equals(id) && obj.getDoneStatus().equals(doneStatus)));
        }
    }

    @Then("the user should receive a {int} code error message")
    public void verifyErrorCode(int code) {
        assertEquals(code, response.getStatusCode());
    }

    @Then("the response should be in {string} format")
    public void verifyResponseFormat(String format) {
        assertEquals(format, response.getContentType());
    }
}

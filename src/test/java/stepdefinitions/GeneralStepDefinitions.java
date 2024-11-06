package stepdefinitions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dto.TodoRequestDTO;
import dto.TodoResponseDTO;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static junit.framework.Assert.assertEquals;

public class GeneralStepDefinitions {

    public Process jarProcess;

    @Given("the application is running on port {string}")
    public void applicationIsRunning(String port) {
        // Start the JAR file in a separate process
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "runTodoManagerRestAPI-1.5.5.jar", "-port=" + port);
        processBuilder.redirectErrorStream(true);
        try {
            this.jarProcess = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jarProcess.getInputStream()));

            int lineCount = 0;
            while ((reader.readLine()) != null) {
                lineCount++;
                if (lineCount >= 16) {
                    RestAssured.baseURI = "http://localhost:" + port;
                    Thread.sleep(100); // Wait for the server to start
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.ensureNoTodos();
    }

    @Given("no To-Do List Items exist")
    public void ensureNoTodos() {
        Response response = given()
                .get("/todos");
        TodoResponseDTO[] todoResponseDTOs = response.jsonPath().getObject("todos", TodoResponseDTO[].class);
        for (TodoResponseDTO todoResponseDTO : todoResponseDTOs) {
            given()
                    .delete("/todos/" + todoResponseDTO.getId());
        }
    }

    @Before
    public void logScenarioName(Scenario scenario) {
        System.out.println("Running scenario: " + scenario.getName());
    }

    @After
    public void stopJar() {
        this.jarProcess.destroy();
    }

    @Given("the following To-Do List Items exist")
    public void createItem(DataTable dt) {
        List<Map<String, String>> todos = dt.asMaps(String.class, String.class);
        for (Map<String, String> todo : todos) {
            String title = todo.get("title");
            String description = todo.get("description");
            String doneStatus = todo.get("doneStatus");
            String id = todo.get("id");

            assertEquals(true, doneStatus.equals("true") || doneStatus.equals("false"));
            TodoRequestDTO todoRequestDTO = new TodoRequestDTO(title, doneStatus.equals("true"), description);
            TodoResponseDTO todoResponseDTO = given()
                    .contentType(ContentType.JSON)
                    .body(todoRequestDTO)
                    .post("/todos")
                    .as(TodoResponseDTO.class);
            assertEquals(id, todoResponseDTO.getId());
        }
    }

    @Then("no To-Do List Items should exist")
    public void verifyNoItemsExist() {
        TodoResponseDTO[] todoResponseDTOs = given()
                .contentType(ContentType.JSON)
                .get("/todos")
                .jsonPath()
                .getObject("todos", TodoResponseDTO[].class);
        assertEquals(0, todoResponseDTOs.length);
    }

    @Then("no To-Do List Item with id {string} should exist")
    public void verifyItemNotExists(String id) {
        Response response = given()
                .get("/todos/" + id);
        assertEquals(404, response.getStatusCode());
    }
}

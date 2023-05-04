package step_def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class StepsFeatures {

    Response response;
    JsonPath jsonPath;
    URL url;
    Faker faker = new Faker();
    ObjectMapper mapper = new ObjectMapper();
    int BookId = 1;
    String orderId;

    @Given("The user open the URL with the path {string}")
    public void theUserOpenTheURLWithThePath(String path) {
        RestAssured.basePath = path;
        response = RestAssured.given()
                .when()
                .get().prettyPeek()
                .then()
                .extract()
                .response();

        Assert.assertNotNull(response);
        String content = response.getBody().asString();
        jsonPath = new JsonPath(content);
        Assert.assertNotNull(jsonPath);
    }

    @Then("API return the response with status code as {int}")
    public void api_return_the_response_with_status_code_as(int expectedStatusCode) throws Exception {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @And("The response contains the attribute {string} with the value {string}")
    public void theResponseContainsTheAttributeWithTheValue(String attribut, String exceptedValue) {
        String attributeValue = jsonPath.get(attribut);

        Assert.assertNotNull(attributeValue);
        Assert.assertEquals(exceptedValue, attributeValue);
    }

    @And("The response contains {int} books")
    public void theResponseContainsBooks(int exceptedQuantity) {
        int numberOfElements = jsonPath.getList("$").size();

        Assert.assertEquals(exceptedQuantity, numberOfElements);
    }

    @And("The response contains {int} books with a parameter {string} equals to {string}")
    public void theResponseContainsBooksWithAParameterEqualsTo(int exceptedQuantity, String parameterName, String parameterValue) {
        int numberOfElements = 0;
        Object json = response.jsonPath().get("$");
        int size = 0;

        if (json instanceof List) {
            List<Map<String, Object>> jsonResponse = (List<Map<String, Object>>) json;
            numberOfElements = jsonPath.getList("findAll { it." + parameterName + " == '" + parameterValue + "' }").size();
        } else if (json instanceof Map) {
            Map<String, Object> jsonResponse = (Map<String, Object>) json;
            if (jsonResponse.get(parameterName).equals(parameterValue)) {
                numberOfElements = 1;
            }
        }

        Assert.assertEquals(exceptedQuantity, numberOfElements);
    }

    @And("The response contains {int} first books from the library")
    public void theResponseContainsFirstBooksFromTheLibrary(int limit) {
        int numberOfElements = jsonPath.getList("myArray[0.." + (limit - 1) + "]").size();

        Assert.assertEquals(limit, numberOfElements);
    }


    @Given("The user open the URL with the path {string} and the new user")
    public void theUserOpenTheURLWithThePathAndTheNewUser(String path) throws IOException {
        RestAssured.basePath = path;
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String body = "{\n" +
                "    \"clientName\": \"" + name + "\",\n" +
                "    \"clientEmail\": \"" + email + "\"\n" +
                "}";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post().prettyPeek()
                .then()
                .extract()
                .response();

        Assert.assertNotNull(response);
    }


    @And("The response contains the accessToken")
    public void theResponseContainsTheAccessToken() {
        Boolean containsAccessToken = false;
        if (response.getStatusCode() == 201) {
            String content = response.getBody().asString();
            try (FileWriter fileWriter = new FileWriter("response.json")) {
                fileWriter.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            containsAccessToken = true;
        }
        Assert.assertTrue(containsAccessToken);
    }

    @Given("The user open the URL with the path {string} with the properties")
    public void theUserOpenTheURLWithThePathWithTheProperties(String path) {
        RestAssured.basePath = path;
        String name = faker.name().firstName();
        String body = "{\"bookId\": " + BookId + ", \"customerName\": \"" + name + "\"}";

        try {
            // Read the file contents into a JsonNode object
            JsonNode rootNode = mapper.readTree(new File("response.json"));

            // Extract the value of the 'accessToken' attribute
            String accessToken = rootNode.get("accessToken").asText();
            response = RestAssured.given()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .post().prettyPeek()
                    .then()
                    .extract()
                    .response();

            Assert.assertNotNull(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Given("The user open the URL with the path {string} with the authorization")
    public void theUserOpenTheURLWithThePathWithTheAuthorization(String path) {
        RestAssured.basePath = path;

        try {
            // Read the file contents into a JsonNode object
            JsonNode rootNode = mapper.readTree(new File("response.json"));

            // Extract the value of the 'accessToken' attribute
            String accessToken = rootNode.get("accessToken").asText();
            response = RestAssured.given()
                    .header("Authorization", "Bearer " + accessToken)
                    .get().prettyPeek()
                    .then()
                    .extract()
                    .response();

            Assert.assertNotNull(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @And("The response contains a not-empty list")
    public void theResponseContainsANotEmptyList() {
        String content = response.getBody().asString();
        JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
        Assert.assertTrue(jsonArray.size() >= 1);
    }

    @Given("The user open the URL with the path {string} with the id")
    public void theUserOpenTheURLWithThePathWithTheId(String path) throws IOException {
        // Read the file contents into a JsonNode object
        JsonNode rootNode = mapper.readTree(new File("response.json"));

        // Extract the value of the 'accessToken' attribute
        orderId = rootNode.get("orderId").asText();
        String accessToken = rootNode.get("accessToken").asText();
        RestAssured.basePath = path+orderId;

        response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .get().prettyPeek()
                .then()
                .extract()
                .response();

        Assert.assertNotNull(response);
    }

    @And("The response contains a order with the same id")
    public void theResponseContainsAOrderWithTheSameId() {
        Assert.assertEquals(orderId, response.jsonPath().getString("id"));
    }

    @And("The response contains the parameter {string} equals to {string}")
    public void theResponseContainsTheParameterEqualsTo(String attribute, String value) {

        String val = response.jsonPath().getString(attribute);
        String orderId = response.jsonPath().getString("orderId");
        Assert.assertEquals(value, val);
        try {
            File jsonFile = new File("response.json");
            JsonNode rootNode = mapper.readTree(jsonFile);
            ((ObjectNode) rootNode).put("orderId", orderId);
            mapper.writeValue(jsonFile, rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Given("The user open the URL with the path {string} with the id and the new name {string}")
    public void theUserOpenTheURLWithThePathWithTheIdAndTheNewName(String path, String newName) throws IOException {
        JsonNode rootNode = mapper.readTree(new File("response.json"));

        // Extract the value of the 'accessToken' attribute
        orderId = rootNode.get("orderId").asText();
        String accessToken = rootNode.get("accessToken").asText();
        RestAssured.basePath = path+orderId;
        String body = "{\"customerName\": \"" + newName + "\"}";

        response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(body)
                .patch().prettyPeek()
                .then()
                .extract()
                .response();

        Assert.assertNotNull(response);
    }

    @And("The response contains a order with the same id and the customerName equals {string}")
    public void theResponseContainsAOrderWithTheSameIdAndTheCustomerNameEquals(String newName) {
        Assert.assertEquals(orderId, response.jsonPath().getString("id"));
        Assert.assertEquals(newName, response.jsonPath().getString("customerName"));
    }

    @Given("The user open the URL with the path {string} with the id for deleting")
    public void theUserOpenTheURLWithThePathWithTheIdForDeleting(String path) throws IOException {
        JsonNode rootNode = mapper.readTree(new File("response.json"));

        // Extract the value of the 'accessToken' attribute
        orderId = rootNode.get("orderId").asText();
        String accessToken = rootNode.get("accessToken").asText();
        RestAssured.basePath = path+orderId;

        response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .delete().prettyPeek()
                .then()
                .extract()
                .response();

        Assert.assertNotNull(response);
    }

    @And("The response contains error message")
    public void theResponseContainsErrorMessage() {
        String excepted = "No order with id "+orderId+".";
        Assert.assertEquals(excepted, response.jsonPath().getString("error"));
    }
}

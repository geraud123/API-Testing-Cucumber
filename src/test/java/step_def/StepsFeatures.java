package step_def;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.net.URL;


public class StepsFeatures {

    Response response;
    JsonPath jsonPath;
    URL url;

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

    @And("The response contain the attribute {string} with the value {string}")
    public void theResponseContainTheAttributeWithTheValue(String attribut, String exceptedValue) {
        String attributeValue = jsonPath.get(attribut);

        Assert.assertNotNull(attributeValue);
        Assert.assertEquals(exceptedValue, attributeValue);
    }

    @And("The response contain {int} books")
    public void theResponseContainBooks(int exceptedQuantity) {
        int numberOfElements = jsonPath.getList("$").size();

        Assert.assertEquals(exceptedQuantity, numberOfElements);
    }

    @And("The response contain {int} books with a parameter {string} equals to {string}")
    public void theResponseContainBooksWithAParameterEqualsTo(int exceptedQuantity, String parameterName, String parameterValue) {
        int numberOfElements = jsonPath.getList("findAll { it." + parameterName + " == '" + parameterValue + "' }").size();

        Assert.assertEquals(exceptedQuantity, numberOfElements);
    }

    @And("The response contains {int} first books from the library")
    public void theResponseContainsFirstBooksFromTheLibrary(int limit) {
        int numberOfElements = jsonPath.getList("myArray[0.." + (limit-1) + "]").size();

        Assert.assertEquals(limit, numberOfElements);
    }
}

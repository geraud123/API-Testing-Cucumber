package step_def;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.*;

public class StepsFeatures {

    Response response;

    @Given("The user open the {string}")
    public void the_user_open_the(String url) {
        response = given()
                .when()
                .get("url");
    }

    @Then("API return the response with status code as {int}")
    public void api_return_the_response_with_status_code_as(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("The response should match {string}")
    public void the_response_should_match(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}

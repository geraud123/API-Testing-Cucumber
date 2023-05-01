package utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Utils {

    public static String registerApiClient(){
        Faker faker = new Faker();
        RestAssured.basePath = "/api-clients/";
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();

        String body = "{\n" +
                "    \"clientName\": \""+name+"\",\n" +
                "    \"clientEmail\": \""+email+"\"\n" +
                "}";

        Response response = RestAssured.given().header("Content-Type", "application/json")
                .body(body)
                .post().prettyPeek();

        return response.jsonPath().getString("accessToken");

    }
}

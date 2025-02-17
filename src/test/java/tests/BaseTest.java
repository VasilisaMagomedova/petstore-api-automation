package tests;

import endpoints.Endpoints;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTest {

    public static RequestSpecification setupRequestSpecification() {
        return new RequestSpecBuilder()
            .setBaseUri(Endpoints.PETSTORE_BASE_URL)
            .setContentType("application/json")
            .build();
    }

    public static ResponseSpecification setupResponseSpecification200() {
        return new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("petSchema.json"))
            .build();
    }

    public static void installSpecification(RequestSpecification reqSpec, ResponseSpecification respSpec) {
        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = respSpec;
    }

}

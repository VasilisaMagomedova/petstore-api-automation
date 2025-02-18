package tests;

import endpoints.Endpoints;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTest {

    RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(Endpoints.PETSTORE_BASE_URL)
            .setContentType("application/json")
            .setAccept("application/json")
            .build();

    ResponseSpecification responseSpecification200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("petSchema.json"))
            .build();

    ResponseSpecification responseSpecification404 = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectBody(matchesJsonSchemaInClasspath("petSchema404.json"))
                .build();

}

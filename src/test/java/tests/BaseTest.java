package tests;

import endpoints.Endpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
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

    ResponseSpecification responseSpecificationForPetsList200 = new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath("petsListSchema.json"))
        .build();

    ResponseSpecification responseSpecification404 = new ResponseSpecBuilder()
        .expectStatusCode(404)
        .expectBody(matchesJsonSchemaInClasspath("petSchema404.json"))
        .build();

}

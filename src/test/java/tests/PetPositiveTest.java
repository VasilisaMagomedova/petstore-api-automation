package tests;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import payload.Pet;
import utils.TestValues;

import static endpoints.Endpoints.PET_POST_URL;
import static io.restassured.RestAssured.*;

public class PetPositiveTest extends BaseTest {

    @BeforeTest
    public Pet setupPetData() {
        Pet pet = new Pet();

        pet.setId(TestValues.testPetId);
        pet.setCategory(TestValues.testPetCategoryId, TestValues.testPetCategoryName);
        pet.setName(TestValues.testPetName);
        pet.setPhotoUrls(TestValues.testPetPhotoUrls);
        pet.setTags(TestValues.getPetTestTags());
        pet.setStatus(TestValues.testPetStatus);

        return pet;

    }

    @Test
    public void createPet() {

        installSpecification(RequestSpecification(), ResponseSpecification200());

        given()
                .body(setupPetData())
        .when()
                .post(PET_POST_URL)
        .then()
                .log().all();
    }

}

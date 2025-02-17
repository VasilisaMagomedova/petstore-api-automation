package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import payload.Pet;
import utils.TestValues;

import static endpoints.Endpoints.PET_GET_BY_ID_URL;
import static endpoints.Endpoints.PET_POST_URL;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PetPositiveTest extends BaseTest {

    @BeforeTest
    public static Pet setupPetData() {
        Pet pet = new Pet();

        pet.setId(TestValues.testPetId);
        pet.setCategory(TestValues.testPetCategoryId, TestValues.testPetCategoryName);
        pet.setName(TestValues.testPetName);
        pet.setPhotoUrls(TestValues.testPetPhotoUrls);
        pet.setTags(TestValues.getPetTestTags());
        pet.setStatus(TestValues.testPetStatus);

        return pet;

    }

    @Test(description = "Создание питомца")
    @Description("Проверка успешного создания нового питомца")
    public void createPetTest() {
        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        Response getPetResponse = getPetByIdStep(createdPetId);
        verifyPetDataStep(postPetResponse, getPetResponse);
    }

    @Step("Отправить POST /pet с валидными данными")
    public static Response addPetStep() {

        installRequestSpecification(RequestSpecification());
        installResponseSpecification(ResponseSpecification200());

        return given()
            .filter(new AllureRestAssured())
            .body(setupPetData())

        .when()
            .post(PET_POST_URL)

        .then()
            .extract().response();

    }

    @Step("Отправить GET /pet/petId с petId созданного питомца на предыдущем шаге")
    public static Response getPetByIdStep(int petId) {
        installResponseSpecification(ResponseSpecification200());

        return given()
            .pathParam("petId", petId)

        .when()
            .get(PET_GET_BY_ID_URL)

        .then()
            .extract().response();
    }

    @Step("Проверить совпадение данных питомца (id, name)")
    public static void verifyPetDataStep(Response postPetResponse, Response getPetResponse) {
        assertThat(postPetResponse.jsonPath().getInt("id"))
                .isEqualTo(getPetResponse.jsonPath().getInt("id"));

        assertThat(postPetResponse.jsonPath().getString("name"))
                .isEqualTo(getPetResponse.jsonPath().getString("name"));
    }

}

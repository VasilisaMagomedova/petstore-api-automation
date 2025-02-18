package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import payload.Pet;
import utils.TestValues;
import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.TestValues.*;

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
            .post(PET_CREATE_POST_URL)

        .then()
            .extract().response();

    }

    @Step("Запросить данные питомца через GET /pet/petID")
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


    @Test(description = "Обновление питомца")
    @Description("Проверка обновления данных питомца")
    public void updatePetTest() {
        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        editPetStep(createdPetId);
        Response getUpdatedPetResponse = getPetByIdStep(createdPetId);
        checkUpdatedPetDataStep(getUpdatedPetResponse);

    }

    @Step("Изменить данные питомца через PUT /pet")
    public static void editPetStep(int createdPetId) {

        JSONObject petDataUpdated = new JSONObject();
        petDataUpdated.put("id", createdPetId);
        petDataUpdated.put("name", testPetNameUpdated);
        petDataUpdated.put("photoUrls", testPetPhotoUrlsUpdated);
        petDataUpdated.put("status", testPetStatusUpdated);

        installRequestSpecification(RequestSpecification());
        installResponseSpecification(ResponseSpecification200());

        given()
            .filter(new AllureRestAssured())
            .body(petDataUpdated.toString())

        .when()
            .put(PET_UPDATE_PUT_URL);

    }

    @Step("Проверить изменение данных питомца")
    public static void checkUpdatedPetDataStep(Response getUpdatedPetResponse) {
        assertThat(getUpdatedPetResponse.jsonPath().getString("name")).isEqualTo(testPetNameUpdated);
        assertThat(getUpdatedPetResponse.jsonPath().getList("photoUrls")).isEqualTo(testPetPhotoUrlsUpdated);
        assertThat(getUpdatedPetResponse.jsonPath().getString("status")).isEqualTo(testPetStatusUpdated);
    }

}

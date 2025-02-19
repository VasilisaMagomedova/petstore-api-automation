package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import payload.Pet;
import utils.TestValues;
import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.TestValues.*;

public class PetPositiveTest extends BaseTest {

    @Test(description = "Создание питомца", timeOut = 10000)
    @Description("Проверка успешного создания нового питомца")
    public void createPetTest() {
        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        Response getPetResponse = getPetByIdStep(createdPetId);
        verifyPetDataStep(postPetResponse, getPetResponse);
    }

    @Step("Отправить POST /pet с валидными данными")
    public Response addPetStep() {

        Pet newPet = new Pet();
        newPet.setId(TestValues.testPetId);
        newPet.setCategory(TestValues.testPetCategoryId, TestValues.testPetCategoryName);
        newPet.setName(TestValues.testPetName);
        newPet.setPhotoUrls(TestValues.testPetPhotoUrls);
        newPet.setTags(TestValues.getPetTestTags());
        newPet.setStatus(TestValues.testPetStatus);

        return given()
            .filter(new AllureRestAssured())
            .spec(requestSpecification)
            .body(newPet)

        .when()
            .post(PET_CREATE_POST_URL)

        .then()
            .spec(responseSpecification200)
            .extract().response();

    }

    @Step("Запросить данные питомца через GET /pet/petID")
    public Response getPetByIdStep(int petId) {

        return given()
            .filter(new AllureRestAssured())
            .pathParam("petId", petId)

        .when()
            .get(PET_GET_BY_ID_URL)

        .then()
            .spec(responseSpecification200)
            .extract().response();
    }

    @Step("Проверить совпадение данных питомца (id, name)")
    public void verifyPetDataStep(Response postPetResponse, Response getPetResponse) {
        assertThat(postPetResponse.jsonPath().getInt("id"))
                .isEqualTo(getPetResponse.jsonPath().getInt("id"));

        assertThat(postPetResponse.jsonPath().getString("name"))
                .isEqualTo(getPetResponse.jsonPath().getString("name"));
    }


    @Test(description = "Обновление питомца", timeOut = 10000)
    @Description("Проверка обновления данных питомца")
    public void updatePetTest() {
        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        editPetStep(createdPetId);
        Response getUpdatedPetResponse = getPetByIdStep(createdPetId);
        checkUpdatedPetDataStep(getUpdatedPetResponse);
    }

    @Step("Изменить данные питомца через PUT /pet")
    public void editPetStep(int createdPetId) {

        JSONObject petDataUpdated = new JSONObject();
        petDataUpdated.put("id", createdPetId);
        petDataUpdated.put("name", testPetNameUpdated);
        petDataUpdated.put("photoUrls", testPetPhotoUrlsUpdated);
        petDataUpdated.put("status", testPetStatusUpdated);

        given()
            .filter(new AllureRestAssured())
            .spec(requestSpecification)
            .body(petDataUpdated.toString())

        .when()
            .put(PET_UPDATE_PUT_URL)

        .then()
            .spec(responseSpecification200);

    }

    @Step("Проверить изменение данных питомца")
    public void checkUpdatedPetDataStep(Response getUpdatedPetResponse) {
        assertThat(getUpdatedPetResponse.jsonPath().getString("name")).isEqualTo(testPetNameUpdated);
        assertThat(getUpdatedPetResponse.jsonPath().getList("photoUrls")).isEqualTo(testPetPhotoUrlsUpdated);
        assertThat(getUpdatedPetResponse.jsonPath().getString("status")).isEqualTo(testPetStatusUpdated);
    }

    @Test(description = "Удаление питомца", timeOut = 10000)
    @Description("Проверка удаления питомца")
    public void deletePetTest() {
        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        deletePetByIdStep(createdPetId);
        getPetByIdAndStatus404Step(createdPetId);
    }

    @Step("Удалить питомца через DELETE /pet/petID")
    public void deletePetByIdStep(int petId) {

        given()
            .filter(new AllureRestAssured())
            .pathParam("petId", petId)

        .when()
            .delete(PET_DELETE_URL)

        .then()
            .statusCode(200);

    }

    @Step("Проверить статус-код 404 при запросе данных питомца через GET /pet/petID")
    public void getPetByIdAndStatus404Step(int petId) {

        given()
            .filter(new AllureRestAssured())
            .pathParam("petId", petId)

        .when()
            .get(PET_GET_BY_ID_URL)

        .then()
            .spec(responseSpecification404);

    }


    @Test(description = "Получение списка доступных питомцев", timeOut = 10000)
    @Description("Проверка фильтрации списка питомцев по статусу available")
    public void getPetsByStatusAvailableTest() {
        String getPetsByStatusResponse = getPetsByStatusAvailableStep();
        verifyStatusAvailableFromPetsDataStep(getPetsByStatusResponse);
    }

    @Step("Запросить список доступных питомцев через GET /pet/findByStatus?status=available")
    public String getPetsByStatusAvailableStep() {

        return given()
            .filter(new AllureRestAssured())
            .queryParam("status", "available")

        .when()
            .get(PET_GET_BY_STATUS_URL)

        .then()
            .spec(responseSpecificationForPetsList200)
            .extract().asString();

    }

    @Step("Проверить в ответе наличие питомцев только со статусом available")
    public void verifyStatusAvailableFromPetsDataStep(String getPetsByStatusResponse) {

        JSONArray petsResponseJSONArray = new JSONArray(getPetsByStatusResponse);

        for(int i = 0; i < petsResponseJSONArray.length(); i++) {
            String petStatus = petsResponseJSONArray.getJSONObject(i).get("status").toString();
            assertThat(petStatus).isEqualTo("available");
        }

    }

}

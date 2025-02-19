package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import payload.Pet;
import utils.RetryAnalyzer;
import utils.TestValues;
import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.TestValues.*;

public class PetPositiveTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PetPositiveTest.class);

    @Test(description = "Создание питомца", timeOut = 10000, retryAnalyzer = RetryAnalyzer.class)
    @Description("Проверка успешного создания нового питомца")
    public void createPetTest() {

        logger.info("Тест 'Создание питомца' запущен");

        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        Response getPetResponse = getPetByIdStep(createdPetId);
        verifyPetDataStep(postPetResponse, getPetResponse);

        logger.info("Тест 'Создание питомца' завершен");

    }

    @Step("Отправить POST /pet с валидными данными")
    public Response addPetStep() {

        logger.info("Шаг теста 'Отправить POST /pet с валидными данными' запущен");

        Pet newPet = new Pet();
        newPet.setId(TestValues.testPetId);
        newPet.setCategory(TestValues.testPetCategoryId, TestValues.testPetCategoryName);
        newPet.setName(TestValues.testPetName);
        newPet.setPhotoUrls(TestValues.testPetPhotoUrls);
        newPet.setTags(TestValues.getPetTestTags());
        newPet.setStatus(TestValues.testPetStatus);

        try {
            return given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .body(newPet)

            .when()
                .post(PET_CREATE_POST_URL)

            .then()
                .spec(responseSpecification200)
                .extract().response();

        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса POST /pet: ", e);
            throw e;
        }

    }

    @Step("Запросить данные питомца через GET /pet/petID")
    public Response getPetByIdStep(int petId) {

        logger.info("Шаг теста 'Запросить данные питомца через GET /pet/petID' запущен");

        try {
            return given()
                .filter(new AllureRestAssured())
                .pathParam("petId", petId)

            .when()
                .get(PET_GET_BY_ID_URL)

            .then()
                .spec(responseSpecification200)
                .extract().response();

        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса GET /pet/petID: {}", petId, e);
            throw e;
        }

    }

    @Step("Проверить совпадение данных питомца (id, name)")
    public void verifyPetDataStep(Response postPetResponse, Response getPetResponse) {

        logger.info("Шаг теста 'Проверить совпадение данных питомца (id, name)' запущен");

        try {
            assertThat(postPetResponse.jsonPath().getInt("id"))
                    .isEqualTo(getPetResponse.jsonPath().getInt("id"));

            assertThat(postPetResponse.jsonPath().getString("name"))
                    .isEqualTo(getPetResponse.jsonPath().getString("name"));

        } catch (AssertionError e) {
            logger.error("Ошибка валидации данных питомца: {}", e.getMessage(), e);
            throw e;
        }

    }


    @Test(description = "Обновление питомца", timeOut = 10000, retryAnalyzer = RetryAnalyzer.class)
    @Description("Проверка обновления данных питомца")
    public void updatePetTest() {

        logger.info("Тест 'Обновление питомца' запущен");

        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        editPetStep(createdPetId);
        Response getUpdatedPetResponse = getPetByIdStep(createdPetId);
        checkUpdatedPetDataStep(getUpdatedPetResponse);

        logger.info("Тест 'Обновление питомца' завершен");

    }

    @Step("Изменить данные питомца через PUT /pet")
    public void editPetStep(int createdPetId) {

        logger.info("Шаг теста 'Изменить данные питомца через PUT /pet' запущен");

        JSONObject petDataUpdated = new JSONObject();
        petDataUpdated.put("id", createdPetId);
        petDataUpdated.put("name", testPetNameUpdated);
        petDataUpdated.put("photoUrls", testPetPhotoUrlsUpdated);
        petDataUpdated.put("status", testPetStatusUpdated);

        try {
            given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .body(petDataUpdated.toString())

            .when()
                .put(PET_UPDATE_PUT_URL)

            .then()
                .spec(responseSpecification200);

        } catch (Exception e) {
        logger.error("Ошибка при обработке запроса PUT /pet: ", e);
        throw e;
        }

    }

    @Step("Проверить изменение данных питомца")
    public void checkUpdatedPetDataStep(Response getUpdatedPetResponse) {

        logger.info("Шаг теста 'Проверить изменение данных питомца' запущен");

        try {
            assertThat(getUpdatedPetResponse.jsonPath().getString("name")).isEqualTo(testPetNameUpdated);
            assertThat(getUpdatedPetResponse.jsonPath().getList("photoUrls")).isEqualTo(testPetPhotoUrlsUpdated);
            assertThat(getUpdatedPetResponse.jsonPath().getString("status")).isEqualTo(testPetStatusUpdated);

        } catch (AssertionError e) {
            logger.error("Ошибка валидации данных питомца: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Test(description = "Удаление питомца", timeOut = 10000, retryAnalyzer = RetryAnalyzer.class)
    @Description("Проверка удаления питомца")
    public void deletePetTest() {

        logger.info("Тест 'Удаление питомца' запущен");

        Response postPetResponse = addPetStep();
        int createdPetId = postPetResponse.jsonPath().getInt("id");
        deletePetByIdStep(createdPetId);
        getPetByIdAndStatus404Step(createdPetId);

        logger.info("Тест 'Удаление питомца' завершен");

    }

    @Step("Удалить питомца через DELETE /pet/petID")
    public void deletePetByIdStep(int petId) {

        logger.info("Шаг теста 'Удалить питомца через DELETE /pet/petID' запущен");

        given()
            .filter(new AllureRestAssured())
            .pathParam("petId", petId)

        .when()
            .delete(PET_DELETE_URL)

        .then()
            .log().ifValidationFails()
            .statusCode(200);

    }

    @Step("Проверить статус-код 404 при запросе данных питомца через GET /pet/petID")
    public void getPetByIdAndStatus404Step(int petId) {

        logger.info("Шаг теста 'Проверить статус-код 404 при запросе данных питомца через GET /pet/petID' запущен");

        try {
            given()
                .filter(new AllureRestAssured())
                .pathParam("petId", petId)

            .when()
                .get(PET_GET_BY_ID_URL)

            .then()
                .spec(responseSpecification404);

        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса GET /pet/petID: ", e);
            throw e;
        }

    }


    @Test(description = "Получение списка доступных питомцев", timeOut = 10000, retryAnalyzer = RetryAnalyzer.class)
    @Description("Проверка фильтрации списка питомцев по статусу available")
    public void getPetsByStatusAvailableTest() {

        logger.info("Тест 'Получение списка доступных питомцев' запущен");

        String getPetsByStatusResponse = getPetsByStatusAvailableStep();
        verifyStatusAvailableFromPetsDataStep(getPetsByStatusResponse);

        logger.info("Тест 'Получение списка доступных питомцев' завершен");

    }

    @Step("Запросить список доступных питомцев через GET /pet/findByStatus?status=available")
    public String getPetsByStatusAvailableStep() {

        logger.info("Шаг теста 'Запросить список доступных питомцев через GET /pet/findByStatus?status=available' запущен");

        try {
            return given()
                .filter(new AllureRestAssured())
                .queryParam("status", "available")

            .when()
                .get(PET_GET_BY_STATUS_URL)

            .then()
                .spec(responseSpecificationForPetsList200)
                .extract().asString();

        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса GET /pet/findByStatus?status=available: ", e);
            throw e;
        }

    }

    @Step("Проверить в ответе наличие питомцев только со статусом available")
    public void verifyStatusAvailableFromPetsDataStep(String getPetsByStatusResponse) {

        logger.info("Проверить в ответе наличие питомцев только со статусом available");

        try {
            JSONArray petsResponseJSONArray = new JSONArray(getPetsByStatusResponse);

            for (int i = 0; i < petsResponseJSONArray.length(); i++) {
                String petStatus = petsResponseJSONArray.getJSONObject(i).get("status").toString();
                assertThat(petStatus).isEqualTo("available");
            }

        } catch(AssertionError e) {
            logger.error("Ошибка валидации статуса available у питомцев: {}", e.getMessage(), e);
            throw e;
        }

    }

}

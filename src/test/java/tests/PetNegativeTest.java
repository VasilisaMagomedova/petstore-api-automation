package tests;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import org.testng.annotations.Test;
import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;
import static utils.TestValues.*;

public class PetNegativeTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PetNegativeTest.class);

    @Test(description = "Создание питомца без данных", timeOut = 10000)
    @Description("Проверка валидации отсутствия body запроса у POST /pet")
    public void createPetWithoutDataTest() {

        logger.info("Тест 'Создание питомца без данных' запущен");

        Response response = given()
            .filter(new AllureRestAssured())
            .spec(requestSpecification)

        .when()
            .post(PET_CREATE_POST_URL);

        int statusCode = response.getStatusCode();

        if (statusCode == 405) {
            logger.info("Ожидаемый статус 405");
            logger.info("Тест 'Создание питомца без данных' завершен");
        } else {
            logger.error("Возникла ошибка в тесте 'Создание питомца без данных'");
        }

        response.then().statusCode(405);

    }

    @Test(description = "Поиск питомца без id", timeOut = 10000)
    @Description("Проверка валидации отсутствия id в GET /pet/petId")
    public void searchPetWithoutIdTest() {

        logger.info("Тест 'Поиск питомца без id' запущен");

        Response response = given()
            .filter(new AllureRestAssured())

        .when()
            .get(PETSTORE_BASE_URL + "/pet");

        int statusCode = response.getStatusCode();

        if (statusCode == 405) {
            logger.info("Ожидаемый статус 405");
            logger.info("Тест 'Поиск питомца без id' завершен");
        } else {
            logger.error("Возникла ошибка в тесте 'Поиск питомца без id'");
        }

        response.then().statusCode(405);

    }

    @Test(description = "Удаление несуществующего питомца", timeOut = 10000)
    @Description("Проверка невозможности удаления питомца в DELETE /pet/petId при отправке несуществующего petId")
    public void deleteAbsentPetTest() {

        logger.info("Тест 'Удаление несуществующего питомца' запущен");

        Response response = given()
            .filter(new AllureRestAssured())
            .pathParam("petId", testPetIdAbsent)

        .when()
            .delete(PET_DELETE_URL);

        int statusCode = response.getStatusCode();

        if (statusCode == 404) {
            logger.info("Ожидаемый статус 404: питомец не найден");
            logger.info("Тест 'Удаление несуществующего питомца' завершен");
        } else {
            logger.error("Возникла ошибка в тесте 'Удаление несуществующего питомца'");
        }

        response.then().statusCode(404);

    }

}

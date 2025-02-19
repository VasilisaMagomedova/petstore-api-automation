package tests;

import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;
import static utils.TestValues.*;

public class PetNegativeTest extends BaseTest {

    @Test(description = "Создание питомца без данных", timeOut = 10000)
    @Description("Проверка валидации отсутствия body запроса у POST /pet")
    public void createPetWithoutDataTest() {

        given()
            .filter(new AllureRestAssured())
            .spec(requestSpecification)

        .when()
            .post(PET_CREATE_POST_URL)

        .then()
            .statusCode(405);

    }

    @Test(description = "Поиск питомца без id", timeOut = 10000)
    @Description("Проверка валидации отсутствия id в GET /pet/petId")
    public void searchPetWithoutIdTest() {

        given()
            .filter(new AllureRestAssured())

        .when()
            .get(PETSTORE_BASE_URL + "/pet")

        .then()
            .statusCode(405);

    }

    @Test(description = "Удаление несуществующего питомца", timeOut = 10000)
    @Description("Проверка невозможности удаления питомца в DELETE /pet/petId при отправке несуществующего petId")
    public void deleteAbsentPetTest() {

        given()
            .filter(new AllureRestAssured())
            .pathParam("petId", testPetIdAbsent)

        .when()
            .delete(PET_DELETE_URL)

        .then()
            .statusCode(404);

    }

}

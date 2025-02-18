package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import org.testng.annotations.Test;

import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class PetNegativeTest extends BaseTest {

    @Test(description = "Создание питомца без данных")
    @Description("Проверка валидации отсутствия body у POST /pet")
    public void createPetWithoutDataTest() {
        createPetWithoutDataStep();
    }

    @Step("Отправить POST /pet без body")
    public void createPetWithoutDataStep() {

        given()
            .filter(new AllureRestAssured())
            .spec(requestSpecification)

        .when()
            .post(PET_CREATE_POST_URL)

        .then()
            .statusCode(405);

    }


    @Test(description = "Поиск питомца без id")
    @Description("Проверка валидации отсутствия id в GET /pet/petId")
    public void searchPetWithoutIdTest() {
        searchPetWithoutIdStep();
    }

    @Step("Отправить GET /pet/petId без petId")
    public void searchPetWithoutIdStep() {

        given()
            .filter(new AllureRestAssured())

        .when()
            .get(PETSTORE_BASE_URL + "/pet")

        .then()
                .statusCode(405);

    }

}

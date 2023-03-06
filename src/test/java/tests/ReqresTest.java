package tests;


import jdk.jfr.Description;
import models.createMethod.CreateBodyRequestModel;
import models.createMethod.CreateResponseModel;
import models.getMethod.GetResponseModel;
import models.getUserList.GetListResponseModel;
import models.updateMethod.UpdateBodyRequestModel;
import models.updateMethod.UpdateResponseModel;
import org.junit.jupiter.api.Test;


import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class ReqresTest {

    @Description("Создание пользователя")
    @Test
    public void createUserTest(){

        String id = "339";
        String job = "leader";
        String name = "morpheus";

        CreateBodyRequestModel bodyData = new CreateBodyRequestModel();
        bodyData.setId(id);
        bodyData.setJob(job);
        bodyData.setName(name);

        CreateResponseModel response =  step("Отправка запроса на создание пользователя", () -> given(requestSpec)
                .body(bodyData)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().as(CreateResponseModel.class));

        step("Проверка значения id полученного в ответе", () ->
                assertThat(response.getId()).isEqualTo(id));
        step("Проверка значения поля name полученного в ответе", () ->
                assertThat(response.getName()).isEqualTo(name));
        step("Проверка значения поля job полученного в ответе", () ->
                assertThat(response.getJob()).isEqualTo(job));
    }

    @Description("Получение данных по пользователю")
    @Test
    public void getUserDataTest() {

        GetResponseModel response =  step("Отправка запроса на получение данных о пользователе", () -> given(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(GetResponseModel.class));

        step("Проверка значения поля id полученного в ответе", () ->
                assertThat(response.getData()).hasFieldOrPropertyWithValue("id",2));
        step("Проверка значения поля first_name полученного в ответе", () ->
                assertThat(response.getData()).hasFieldOrPropertyWithValue("first_name","Janet"));
        step("Проверка значения поля last_name полученного в ответе", () ->
                assertThat(response.getData()).hasFieldOrPropertyWithValue("last_name","Weaver"));
        step("Проверка значения поля text полученного в ответе", () ->
                assertThat(response.getSupport()).hasFieldOrPropertyWithValue("text","To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Description("Получение данных по списку пользователей, проверка имени во втором объекте массива")
    @Test
    public void getUserListDataTest(){

        GetListResponseModel response = step("Отправка запроса на получение списка пользователей", () -> given(requestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(GetListResponseModel.class));

        step("Проверка значения поля first_name во втором объекте массива data", () ->
                assertThat(response.getData().get(1)).hasFieldOrPropertyWithValue("first_name", "Lindsay"));
    }

    @Description("Проверка статус кода, при поиске несуществующего пользователя")
    @Test
    public void userNotFoundTest(){

        step("Отправка запроса о несуществующем пользователе, получение статус кода 404 в ответе", () ->given(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(responseSpec)
                .statusCode(404));
    }

    @Description("Обновление данных по пользователю")
    @Test
    public void updateUserTest(){

        String job = "zion resident";
        String name = "morpheus";

        UpdateBodyRequestModel request = new UpdateBodyRequestModel();
        request.setJob(job);
        request.setName(name);

        UpdateResponseModel response = step("Отправка запроса на обновление данных о пользователе", () ->  given(requestSpec)

                .body(request)
                .when()
                .patch("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UpdateResponseModel.class));

        step("Проверка значения поля name в ответе", () ->
                assertThat(response.getName()).isEqualTo(name));
        step("Проверка значения поля Job в ответе", () ->
                assertThat(response.getJob()).isEqualTo(job));
    }
}

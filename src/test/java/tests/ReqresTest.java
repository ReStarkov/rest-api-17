package tests;

import jdk.jfr.Description;
import models.updateuser.UpdateBodyRequestModel;
import org.junit.jupiter.api.Test;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class ReqresTest {


    @Description("Получение данных по пользователю")
    @Test
    public void getUserDataUsingGroovyTest() {

        given(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Description("Получение данных по списку пользователей, проверка имени во втором объекте массива")
    @Test
    public void getUserListDataUsingGroovyTest() {

        given(requestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("data.findAll{it.id == 8}.first_name.flatten()", hasItem("Lindsay"));

    }

    @Description("Проверка статус кода, при поиске несуществующего пользователя")
    @Test
    public void userNotFoundTest() {

        step("Отправка запроса о несуществующем пользователе, получение статус кода 404 в ответе",
                () -> given(requestSpec)
                        .when()
                        .get("/users/23")
                        .then()
                        .spec(responseSpec)
                        .statusCode(404));
    }

    @Description("Обновление данных по пользователю")
    @Test
    public void updateUserUsingGroovyTest() {

        String job = "zion resident";
        String name = "morpheus";

        UpdateBodyRequestModel request = new UpdateBodyRequestModel();
        request.setJob(job);
        request.setName(name);

        given(requestSpec)

                .body(request)
                .when()
                .patch("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("name", is(name))
                .body("job", is(job));
    }

    @Test
    public void checkEmailUsingGroovy() {
        given(requestSpec)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("janet.weaver@reqres.in"));
    }
}

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresTest {

    @Description("Получение данных по пользователю")
    @Test
    public void getUserDataTest(){

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is ("Weaver"))
                .body("support.text", is ("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Description("Получение данных по списку пользователей, проверка имени во втором объекте массива")
    @Test
    public void getUserListDataTest(){

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                . body("data[1].first_name", equalTo("Lindsay"));

    }

    @Description("Проверка статус кода, при поиске несуществующего пользователя")
    @Test
    public void userNotFoundTest(){

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Description("Создание пользователя")
    @Test
    public void createUserTest(){

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\", \"id\": \"339\", \"createdAt\": \"2023-03-05T13:13:53.930Z\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", is("339"))
                .body("job", is("leader"))
                .body("name", is("morpheus"));
    }

    @Description("Обновление данных по пользователю")
    @Test
    public void updateUserTest(){

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("zion resident"))
                .body("name", is("morpheus"));
    }
}

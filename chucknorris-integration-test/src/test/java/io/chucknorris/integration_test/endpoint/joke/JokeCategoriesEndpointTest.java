package io.chucknorris.integration_test.endpoint.joke;

import io.chucknorris.integration_test.endpoint.AbstractEndpointTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

class JokeCategoriesEndpointTest extends AbstractEndpointTest {

    @DisplayName("Should return categories as json")
    @Test
    void shouldReturnCategoriesAsJson() {
        var specification = given()
                .log().all()
                .accept("application/json")
                .header("Origin", "http://localhost:3000");

        var response = specification.when().get("/jokes/categories");

        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Access-Control-Allow-Origin", Matchers.equalTo("*"))
                .header("Access-Control-Expose-Headers", Matchers.equalTo("Access-Control-Allow-Origin Access-Control-Allow-Credentials"))
                .header("Content-Type", Matchers.equalTo("application/json"))
                .body("$.", Matchers.isA(List.class));
    }

    @DisplayName("Should return categories as plain text")
    @Test
    void shouldReturnCategoriesAsPlainText() {
        // given:
        var specification = given()
                .log().all()
                .accept("text/plain")
                .header("Origin", "http://localhost:3000");

        // when:
        var response = specification.when().get("/jokes/categories");

        // then:
        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Access-Control-Allow-Origin", Matchers.equalTo("*"))
                .header("Access-Control-Expose-Headers", Matchers.equalTo("Access-Control-Allow-Origin Access-Control-Allow-Credentials"))
                .header("Content-Type", Matchers.equalTo("text/plain;charset=UTF-8"));

        // and:
        Assertions.assertEquals(
                String.join(
                        "\n",
                        "career",
                        "celebrity",
                        "dev",
                        "explicit",
                        "fashion",
                        "food",
                        "money",
                        "movie",
                        "religion",
                        "travel"),
                response.body().asString().trim());
    }
}

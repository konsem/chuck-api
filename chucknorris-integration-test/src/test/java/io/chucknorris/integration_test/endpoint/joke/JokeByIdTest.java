package io.chucknorris.integration_test.endpoint.joke;

import io.chucknorris.integration_test.endpoint.AbstractEndpointTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class JokeByIdTest extends AbstractEndpointTest {

    private final String jokeId = "c9h1endqtmm31b1auddvdg";

    @DisplayName("Should return joke by id as json")
    @Test
    void shouldReturnJokeAsJson() {
        // given:
        var specification = given()
                .log().all()
                .accept("application/json");

        // when:
        var response = specification.when().get("/jokes/" + jokeId);

        // then:
        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Content-Type", Matchers.equalTo("application/json"))
                .body("categories", Matchers.emptyIterable())
                .body("created_at", Matchers.comparesEqualTo("2024-06-19 09:28:25.390787"))
                .body("icon_url", Matchers.comparesEqualTo("https://api.chucknorris.io/img/avatar/chuck-norris.png"))
                .body("id", Matchers.comparesEqualTo(jokeId))
                .body("updated_at", Matchers.comparesEqualTo("2024-06-19 09:28:25.390787"))
                .body("url", Matchers.comparesEqualTo("https://api.chucknorris.io/jokes/c9h1endqtmm31b1auddvdg"))
                .body("value", Matchers.comparesEqualTo("Chuck Norris once lost the remote, but maintained control of the TV by yelling at it in between bites of his \"Filet of Child\" sandwich."));
    }

    @DisplayName("Should return joke by id as plain text")
    @Test
    void shouldReturnJokeAsPlainText() {
        // given:
        var specification = given()
                .log().all()
                .accept("text/plain");

        // when:
        var response = specification.when().get("/jokes/" + jokeId);

        // then:
        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Content-Type", Matchers.equalTo("text/plain;charset=UTF-8"));

        // and:
        Assertions.assertEquals(
                "Chuck Norris once lost the remote, but maintained control of the TV by yelling at it in between bites of his \"Filet of Child\" sandwich.",
                response.body().asString().trim());
    }
}

package io.chucknorris.integration_test.endpoint.joke;

import io.chucknorris.integration_test.endpoint.AbstractEndpointTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

class JokeRandomEndpointTest extends AbstractEndpointTest {

    @DisplayName("Should return joke as json")
    @Test
    void shouldReturnJokeAsJson() {
        // given:
        var specification = given()
                .log().all()
                .accept("application/json");

        // when:
        var response = specification.when().get("/jokes/random");

        // then:
        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Content-Type", Matchers.equalTo("application/json"))
                .body("categories", Matchers.isA(List.class))
                .body("created_at", Matchers.isA(String.class))
                .body("icon_url", Matchers.isA(String.class))
                .body("id", Matchers.isA(String.class))
                .body("updated_at", Matchers.isA(String.class))
                .body("url", Matchers.isA(String.class))
                .body("value", Matchers.isA(String.class));
    }

    @DisplayName("Should include CORS headers")
    @Test
    void shouldIncludeCORSHeaders() {
        // given:
        var specification = given()
                .log().all()
                .accept("application/json")
                .header("Origin", "http://localhost:3000");

        // when:
        var response = specification.when().get("/jokes/random");

        // then:
        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Access-Control-Allow-Origin", Matchers.equalTo("*"))
                .header("Access-Control-Expose-Headers", Matchers.equalTo("Access-Control-Allow-Origin Access-Control-Allow-Credentials"))
                .header("Content-Type", Matchers.equalTo("application/json"));
    }
}

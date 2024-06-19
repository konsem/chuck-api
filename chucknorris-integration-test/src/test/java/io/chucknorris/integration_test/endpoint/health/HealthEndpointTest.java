package io.chucknorris.integration_test.endpoint.health;

import io.chucknorris.integration_test.endpoint.AbstractEndpointTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class HealthEndpointTest extends AbstractEndpointTest {

    @DisplayName("Should return joke")
    @Test
    void shouldReturnJoke() {
        var specification = given()
                .log().all()
                .accept("application/json");

        var response = specification.when().get("/health");

        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Content-Type", Matchers.equalTo("application/json"))
                .body("status", Matchers.is("UP"))
                .body("start_time", Matchers.isA(String.class))
                .body("uptime", Matchers.isA(String.class));
    }
}

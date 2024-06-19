package io.chucknorris.integration_test.endpoint.home;

import io.chucknorris.integration_test.endpoint.AbstractEndpointTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class HomeEndpointTest extends AbstractEndpointTest {

    @DisplayName("Should return html page")
    @Test
    void shouldReturnHtmlPage() {
        var specification = given()
                .log().all()
                .accept("text/html");

        var response = specification.when().get("/");

        response.then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .header("Content-Type", Matchers.equalTo("text/html;charset=UTF-8"));
    }
}

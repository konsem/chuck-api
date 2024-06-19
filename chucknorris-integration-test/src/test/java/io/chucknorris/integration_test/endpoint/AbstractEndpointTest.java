package io.chucknorris.integration_test.endpoint;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.Optional;

public abstract class AbstractEndpointTest {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = Optional
                .ofNullable(System.getenv("TEST_SUBJECT_BASE_URI"))
                .orElse("http://localhost");

        RestAssured.port = Optional
                .ofNullable(System.getenv("TEST_SUBJECT_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);
    }
}

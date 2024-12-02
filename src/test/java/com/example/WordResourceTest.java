package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class WordResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
        .queryParam("length", 8)
        .when()
          .get("/api/word")
        .then()
          .statusCode(200);
    }
}
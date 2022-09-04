package de.hrw.distsys.project.mashup.controller;

import de.hrw.distsys.project.mashup.FunctionalTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

class WavesControllerTest extends FunctionalTest {

    @Test
    void basicPingTest() {
        RestAssured.given().when().get().then()
                .statusCode(200);
        RestAssured.given().when().get("/get_nodes/active").then()
                .statusCode(200);
        RestAssured.given().when().get("/get_nodes/main").then()
                .statusCode(200);
        RestAssured.given().when().get("/get_nodes/all").then()
                .statusCode(200);
    }

    @Test
    void verifyIfResponseTypeIsJSON() {
        RestAssured.given().when().get("/get_nodes/active").then()
                .contentType("application/json");
        RestAssured.given().when().get("/get_nodes/main")
                .then().contentType("application/json");
        RestAssured.given().when().get("/get_nodes/all")
                .then().contentType("application/json");
    }

    @Test
    void verifyIfBodyContainsNodes() {
        RestAssured.given().when().get("/get_nodes/main").then()
                .body(containsString("peers"))
                .body(containsString("address"))
                .body(containsString("declaredAddress"))
                .body(containsString("peerName"))
                .body(containsString("peerNonce"))
                .body(containsString("applicationName"))
                .body(containsString("applicationVersion"))
                .body(containsString("latitude"))
                .body(containsString("longitude"))
                .body(containsString("country_name"))
                .body(containsString("city"))
                .body(containsString("country_flag"));
        RestAssured.given().when().get("/get_nodes/active").then()
                .body(containsString("peers"))
                .body(containsString("address"))
                .body(containsString("declaredAddress"))
                .body(containsString("peerName"))
                .body(containsString("peerNonce"))
                .body(containsString("applicationName"))
                .body(containsString("applicationVersion"))
                .body(containsString("latitude"))
                .body(containsString("longitude"))
                .body(containsString("country_name"))
                .body(containsString("city"))
                .body(containsString("country_flag"));
        RestAssured.given().when().get("/get_nodes/all").then()
                .body(containsString("peers"))
                .body(containsString("address"))
                .body(containsString("lastSeen"))
                .body(containsString("latitude"))
                .body(containsString("longitude"))
                .body(containsString("country_name"))
                .body(containsString("city"))
                .body(containsString("country_flag"));
    }
}
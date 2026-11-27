package org.echangedetemps.listing;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Starts the whole app with a fresh test database (Dev Services),
 * loaded with the Flyway schema + demo data, then calls the real HTTP API.
 */
@QuarkusTest
class ListingResourceTest {

    @Test
    void listReturnsOnlyActiveListings() {
        given()
                .when().get("/api/listings")
                .then()
                .statusCode(200)
                // the demo data contains 3 active listings out of 5
                .body("size()", is(3))
                .body("status", everyItem(is("ACTIVE")))
                .body("title", hasItem("Cours de menuiserie"));
    }

    @Test
    void listNeverExposesPrivateAuthorData() {
        given()
                .when().get("/api/listings")
                .then()
                .statusCode(200)
                .body("[0].author.firstName", notNullValue())
                .body("[0].author.email", nullValue())
                .body("[0].author.passwordHash", nullValue());
    }

    @Test
    void getUnknownListingReturns404() {
        given()
                .when().get("/api/listings/999999")
                .then()
                .statusCode(404);
    }
}

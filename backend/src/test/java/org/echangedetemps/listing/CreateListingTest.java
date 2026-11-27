package org.echangedetemps.listing;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/** Tests for POST /api/listings */
@QuarkusTest
class CreateListingTest {

    @Test
    void createValidListingReturns201() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Aide pour monter une étagère",
                          "description": "J'ai une étagère en kit à monter, 1 h environ.",
                          "type": "REQUEST",
                          "durationMinutes": 60
                        }
                        """)
                .when().post("/api/listings")
                .then()
                .statusCode(201)
                .header("Location", containsString("/api/listings/"))
                .body("id", notNullValue())
                .body("title", is("Aide pour monter une étagère"))
                .body("type", is("REQUEST"))
                .body("status", is("PENDING"))
                // temporary author, see CurrentUser
                .body("author.firstName", is("Karima"))
                .body("createdAt", notNullValue());
    }

    @Test
    void clientCannotChooseStatusOrAuthor() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Cours de guitare",
                          "description": "Débutants bienvenus.",
                          "type": "OFFER",
                          "status": "ACTIVE",
                          "author": { "id": 1 }
                        }
                        """)
                .when().post("/api/listings")
                .then()
                .statusCode(201)
                .body("status", is("PENDING"))
                .body("author.firstName", is("Karima"));
    }

    @Test
    void missingTitleReturns400WithFrenchMessage() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        { "description": "Sans titre", "type": "OFFER" }
                        """)
                .when().post("/api/listings")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("Le titre est obligatoire"));
    }

    @Test
    void negativeDurationReturns400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        { "title": "Test", "description": "Test", "type": "OFFER", "durationMinutes": -30 }
                        """)
                .when().post("/api/listings")
                .then()
                .statusCode(400)
                .body("violations.message", hasItem("La durée doit être supérieure à 0"));
    }

    @Test
    void unknownTypeReturns400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        { "title": "Test", "description": "Test", "type": "PLOP" }
                        """)
                .when().post("/api/listings")
                .then()
                .statusCode(400);
    }
}

package org.echangedetemps.listing;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests for PUT and DELETE /api/listings/{id}.
 * The current user is always Karima (temporary author, see CurrentUser).
 */
@QuarkusTest
class UpdateDeleteListingTest {

    @Inject
    ListingRepository listingRepository;

    // ----- PUT -----

    @Test
    void updateOwnListingReturns200() {
        Long id = createListing("Titre avant");

        given()
                .contentType(ContentType.JSON)
                .body(validBody("Titre après"))
                .when().put("/api/listings/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id.intValue()))
                .body("title", is("Titre après"))
                .body("durationMinutes", is(45));
    }

    @Test
    void updateWithInvalidDataReturns400() {
        Long id = createListing("Annonce à corriger");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        { "title": "", "description": "Test", "type": "OFFER" }
                        """)
                .when().put("/api/listings/" + id)
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("violations.field", hasItem("title"))
                .body("violations.message", hasItem("Le titre est obligatoire"));
    }

    @Test
    void updateSomeoneElsesListingReturns403() {
        Long lucasListing = idOf("Aide pour un déménagement");

        given()
                .contentType(ContentType.JSON)
                .body(validBody("Piratage"))
                .when().put("/api/listings/" + lucasListing)
                .then()
                .statusCode(403)
                .body("message", is("Vous ne pouvez modifier que vos propres annonces"));
    }

    @Test
    void updateClosedListingReturns409() {
        Long closedListing = idOf("Arrosage des plantes pendant les vacances");

        given()
                .contentType(ContentType.JSON)
                .body(validBody("Nouveau titre"))
                .when().put("/api/listings/" + closedListing)
                .then()
                .statusCode(409);
    }

    @Test
    void updateUnknownListingReturns404InCommonFormat() {
        given()
                .contentType(ContentType.JSON)
                .body(validBody("Peu importe"))
                .when().put("/api/listings/999999")
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("message", is("Annonce introuvable"))
                .body("timestamp", notNullValue());
    }

    // ----- DELETE -----

    @Test
    void deleteOwnListingReturns204ThenIsGone() {
        Long id = createListing("Annonce à supprimer");

        given().when().delete("/api/listings/" + id)
                .then().statusCode(204);

        given().when().get("/api/listings/" + id)
                .then().statusCode(404);
    }

    @Test
    void deleteSomeoneElsesListingReturns403() {
        Long inesListing = idOf("Conversation en espagnol");

        given().when().delete("/api/listings/" + inesListing)
                .then()
                .statusCode(403)
                .body("message", is("Vous ne pouvez supprimer que vos propres annonces"));
    }

    @Test
    void deleteListingWithExchangeReturns409() {
        // Karima's listing, already answered by Lucas in the demo data
        Long id = idOf("Cours de menuiserie");

        given().when().delete("/api/listings/" + id)
                .then().statusCode(409);
    }

    // ----- Helpers -----

    private Long createListing(String title) {
        return given()
                .contentType(ContentType.JSON)
                .body(validBody(title))
                .when().post("/api/listings")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    private static String validBody(String title) {
        return """
                { "title": "%s", "description": "Description de test", "type": "OFFER", "durationMinutes": 45 }
                """.formatted(title);
    }

    /** Finds a demo listing id by its title (ids depend on insertion order, so we never hard-code them). */
    private Long idOf(String title) {
        return QuarkusTransaction.requiringNew()
                .call(() -> listingRepository.find("title", title).firstResult().getId());
    }
}

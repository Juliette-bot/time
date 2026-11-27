package org.echangedetemps.common.error;

import java.time.Instant;
import java.util.List;

/**
 * The single JSON format for every error returned by the API.
 * Messages are shown to users, so they are in French.
 *
 * Example:
 * { "status": 404, "message": "Annonce introuvable", "violations": [], "timestamp": "..." }
 */
public record ApiError(
        int status,
        String message,
        List<FieldViolation> violations,
        Instant timestamp
) {

    /** One invalid field, e.g. { "field": "title", "message": "Le titre est obligatoire" } */
    public record FieldViolation(String field, String message) {
    }

    public static ApiError of(int status, String message) {
        return new ApiError(status, message, List.of(), Instant.now());
    }

    public static ApiError withViolations(int status, String message, List<FieldViolation> violations) {
        return new ApiError(status, message, violations, Instant.now());
    }
}

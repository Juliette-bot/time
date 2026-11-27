package org.echangedetemps.common.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.List;

/**
 * Turns every exception into an ApiError, so the front-end always
 * receives the same JSON format.
 * Quarkus picks the most specific mapper for each exception type.
 */
public class ExceptionMappers {

    private static final Logger LOG = Logger.getLogger(ExceptionMappers.class);

    /** 400: invalid data (@NotBlank, @Positive... on a request DTO) */
    @ServerExceptionMapper
    public Response mapValidation(ConstraintViolationException exception) {
        List<ApiError.FieldViolation> violations = exception.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldViolation(fieldName(v), v.getMessage()))
                .toList();
        return json(ApiError.withViolations(400, "Les données envoyées sont invalides", violations));
    }

    /** 403, 404, 409...: thrown on purpose by the services (ForbiddenException, NotFoundException...) */
    @ServerExceptionMapper
    public Response mapWebApplication(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        return json(ApiError.of(status, frenchMessage(status, exception.getMessage())));
    }

    /** 400: malformed JSON, or a wrong value such as "type": "PLOP" */
    @ServerExceptionMapper
    public Response mapBadJson(JsonProcessingException exception) {
        return json(ApiError.of(400, "Le format des données envoyées est invalide"));
    }

    /** 500: anything unexpected. The real cause goes to the logs, never to the user. */
    @ServerExceptionMapper
    public Response mapUnexpected(Exception exception) {
        LOG.error("Unexpected error", exception);
        return json(ApiError.of(500, "Une erreur inattendue est survenue"));
    }

    // ----- Helpers -----

    private static Response json(ApiError error) {
        return Response.status(error.status())
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /** "create.request.title" -> "title" */
    private static String fieldName(ConstraintViolation<?> violation) {
        String name = null;
        for (Path.Node node : violation.getPropertyPath()) {
            name = node.getName();
        }
        return name;
    }

    /**
     * Keeps our own French messages ("Annonce introuvable").
     * Replaces the default English ones ("HTTP 404 Not Found"),
     * e.g. when someone calls a route that does not exist.
     */
    private static String frenchMessage(int status, String message) {
        if (message != null && !message.startsWith("HTTP ")) {
            return message;
        }
        return switch (status) {
            case 400 -> "Requête invalide";
            case 401 -> "Authentification requise";
            case 403 -> "Accès refusé";
            case 404 -> "Ressource introuvable";
            case 405 -> "Méthode non autorisée";
            case 409 -> "Action impossible dans l'état actuel";
            case 415 -> "Format de données non supporté";
            default -> "Une erreur est survenue";
        };
    }
}

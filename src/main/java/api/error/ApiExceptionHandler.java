package api.error;

import application.GetHero.HeroNotFoundException;
import application.GetHero.InvalidHeroIdException;
import application.InvalidHeroInputException;
import com.fasterxml.jackson.core.JsonProcessingException;
import domain.exception.DomainException;
import domain.exception.HeroAlreadyExistsException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(ApiExceptionHandler.class.getName());

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof HeroAlreadyExistsException) {
            return error(Response.Status.CONFLICT, "HERO_ALREADY_EXISTS", exception.getMessage());
        }
        if (exception instanceof HeroNotFoundException) {
            return error(Response.Status.NOT_FOUND, "HERO_NOT_FOUND", exception.getMessage());
        }
        if (exception instanceof DomainException
                || exception instanceof InvalidHeroInputException
                || exception instanceof InvalidHeroIdException) {
            return error(Response.Status.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage());
        }
        if (exception instanceof JsonProcessingException || exception instanceof BadRequestException) {
            return error(Response.Status.BAD_REQUEST, "INVALID_REQUEST", "Le corps JSON est invalide.");
        }
        if (exception instanceof WebApplicationException httpException) {
            Response response = httpException.getResponse();
            return Response.fromResponse(response)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(body(response.getStatus(), "HTTP_ERROR", response.getStatusInfo().getReasonPhrase()))
                    .build();
        }

        LOGGER.log(Level.SEVERE, "Erreur inattendue pendant le traitement de la requête.", exception);
        return error(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Une erreur interne est survenue.");
    }

    private Response error(Response.Status status, String code, String message) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(body(status.getStatusCode(), code, message))
                .build();
    }

    private ApiErrorResponse body(int status, String code, String message) {
        return new ApiErrorResponse(Instant.now(), status, code, message, uriInfo.getRequestUri().getRawPath());
    }
}

package api.error;

import api.security.AuthenticationRequiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import hero.application.InvalidHeroInputException;
import hero.application.getHero.HeroNotFoundException;
import hero.application.getHero.InvalidHeroIdException;
import hero.domain.exception.DomainException;
import hero.domain.exception.HeroAlreadyExistsException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lobby.application.ForbiddenActionException;
import lobby.application.LobbyNotFoundException;
import lobby.domain.exception.GameAlreadyStartedException;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;
import lobby.domain.exception.PlayerNotInLobbyException;
import player.application.PlayerNotFoundException;
import player.application.getPlayerProfile.ProfileAccessForbiddenException;
import player.application.login.InvalidCredentialsException;
import player.application.login.InvalidLoginInputException;
import player.domain.exception.EmailAlreadyTakenException;
import player.domain.exception.InvalidPlayerDataException;
import player.domain.exception.PasswordTooShortException;
import player.domain.exception.PseudonymAlreadyTakenException;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<Exception> {

    private static final Response.StatusType UNPROCESSABLE_ENTITY = new Response.StatusType() {
        @Override
        public int getStatusCode() {
            return 422;
        }

        @Override
        public Response.Status.Family getFamily() {
            return Response.Status.Family.CLIENT_ERROR;
        }

        @Override
        public String getReasonPhrase() {
            return "Unprocessable Entity";
        }
    };

    private static final Logger LOGGER = Logger.getLogger(ApiExceptionHandler.class.getName());

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof AuthenticationRequiredException
                || exception instanceof lobby.api.AuthenticationRequiredException) {
            return error(Response.Status.UNAUTHORIZED, "AUTHENTICATION_REQUIRED", exception.getMessage());
        }
        if (exception instanceof PlayerAlreadyInOpenLobbyException) {
            return error(Response.Status.CONFLICT, "PLAYER_ALREADY_IN_OPEN_LOBBY", exception.getMessage());
        }
        if (exception instanceof LobbyNotFoundException) {
            return error(Response.Status.NOT_FOUND, "LOBBY_NOT_FOUND", exception.getMessage());
        }
        if (exception instanceof PlayerNotInLobbyException) {
            return error(Response.Status.NOT_FOUND, "PLAYER_NOT_IN_LOBBY", exception.getMessage());
        }
        if (exception instanceof ForbiddenActionException) {
            return error(Response.Status.FORBIDDEN, "FORBIDDEN", exception.getMessage());
        }
        if (exception instanceof GameAlreadyStartedException) {
            return error(Response.Status.CONFLICT, "GAME_ALREADY_STARTED", exception.getMessage());
        }
        if (exception instanceof PseudonymAlreadyTakenException) {
            return error(Response.Status.CONFLICT, "PSEUDONYM_TAKEN", exception.getMessage());
        }
        if (exception instanceof EmailAlreadyTakenException) {
            return error(Response.Status.CONFLICT, "EMAIL_TAKEN", exception.getMessage());
        }
        if (exception instanceof ProfileAccessForbiddenException) {
            return error(Response.Status.FORBIDDEN, "FORBIDDEN", exception.getMessage());
        }
        if (exception instanceof PlayerNotFoundException) {
            return error(Response.Status.NOT_FOUND, "PLAYER_NOT_FOUND", exception.getMessage());
        }
        if (exception instanceof InvalidCredentialsException) {
            return error(Response.Status.UNAUTHORIZED, "INVALID_CREDENTIALS", exception.getMessage());
        }
        if (exception instanceof InvalidLoginInputException) {
            return error(Response.Status.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage());
        }
        if (exception instanceof PasswordTooShortException) {
            return error(UNPROCESSABLE_ENTITY, "PASSWORD_TOO_SHORT", exception.getMessage());
        }
        if (exception instanceof InvalidPlayerDataException) {
            return error(Response.Status.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage());
        }
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

    private Response error(Response.StatusType status, String code, String message) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(body(status.getStatusCode(), code, message))
                .build();
    }

    private ApiErrorResponse body(int status, String code, String message) {
        return new ApiErrorResponse(Instant.now(), status, code, message, uriInfo.getRequestUri().getRawPath());
    }
}

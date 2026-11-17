package api.lobby;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lobby.application.createLobby.CreateLobbyUseCase;

import java.security.Principal;
import java.util.UUID;

@Path("/lobbies")
@Produces(MediaType.APPLICATION_JSON)
public class LobbyResource {

    private final CreateLobbyUseCase createLobbyUseCase;

    @Inject
    public LobbyResource(CreateLobbyUseCase createLobbyUseCase) {
        this.createLobbyUseCase = createLobbyUseCase;
    }

    @POST
    public Response create(@Context SecurityContext securityContext) {
        UUID playerId = authenticatedPlayerId(securityContext);
        LobbyResponse response = LobbyResponse.from(createLobbyUseCase.execute(playerId));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    private UUID authenticatedPlayerId(SecurityContext securityContext) {
        Principal principal = securityContext == null ? null : securityContext.getUserPrincipal();
        if (principal == null || principal.getName() == null) {
            throw new AuthenticationRequiredException();
        }

        String name = principal.getName();
        try {
            UUID playerId = UUID.fromString(name);
            if (!playerId.toString().equalsIgnoreCase(name)) {
                throw new AuthenticationRequiredException();
            }
            return playerId;
        } catch (IllegalArgumentException exception) {
            throw new AuthenticationRequiredException();
        }
    }
}

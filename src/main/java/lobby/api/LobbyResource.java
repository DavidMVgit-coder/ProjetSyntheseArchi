package lobby.api;

import api.security.AuthenticatedPlayer;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lobby.application.createLobby.CreateLobbyUseCase;
import lobby.application.leaveLobby.LeaveLobbyUseCase;

import java.util.UUID;

@Path("/lobbies")
@Produces(MediaType.APPLICATION_JSON)
public class LobbyResource {

    private final CreateLobbyUseCase createLobbyUseCase;
    private final LeaveLobbyUseCase leaveLobbyUseCase;

    @Inject
    public LobbyResource(CreateLobbyUseCase createLobbyUseCase, LeaveLobbyUseCase leaveLobbyUseCase) {
        this.createLobbyUseCase = createLobbyUseCase;
        this.leaveLobbyUseCase = leaveLobbyUseCase;
    }

    @POST
    public Response create(@Context SecurityContext securityContext) {
        UUID playerId = AuthenticatedPlayer.idFrom(securityContext);
        LobbyResponse response = LobbyResponse.from(createLobbyUseCase.execute(playerId));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @DELETE
    @Path("/{lobbyId}/players/{playerId}")
    public Response leave(@PathParam("lobbyId") UUID lobbyId,
            @PathParam("playerId") UUID playerId,
            @Context SecurityContext securityContext) {
        UUID authenticatedPlayerId = AuthenticatedPlayer.idFrom(securityContext);
        leaveLobbyUseCase.execute(lobbyId, playerId, authenticatedPlayerId);
        return Response.noContent().build();
    }
}

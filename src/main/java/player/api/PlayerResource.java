package player.api;

import api.security.AuthenticatedPlayer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import player.application.createPlayer.CreatePlayerCommand;
import player.application.createPlayer.CreatePlayerUseCase;
import player.application.getPlayerProfile.GetPlayerProfileUseCase;

import java.util.UUID;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final GetPlayerProfileUseCase getPlayerProfileUseCase;

    @Inject
    public PlayerResource(CreatePlayerUseCase createPlayerUseCase, GetPlayerProfileUseCase getPlayerProfileUseCase) {
        this.createPlayerUseCase = createPlayerUseCase;
        this.getPlayerProfileUseCase = getPlayerProfileUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreatePlayerRequest request, @Context UriInfo uriInfo) {
        CreatePlayerCommand command = request == null
                ? null
                : new CreatePlayerCommand(request.pseudonym(), request.email(), request.password());
        PlayerResponse response = PlayerResponse.from(createPlayerUseCase.execute(command));
        return Response.created(uriInfo.getAbsolutePathBuilder().path("/{id}").build(response.id()))
                .entity(response)
                .build();
    }

    @GET
    @Path("/{playerId}")
    public PlayerResponse getProfile(@PathParam("playerId") String playerId,
            @Context SecurityContext securityContext) {
        UUID authenticatedPlayerId = AuthenticatedPlayer.idFrom(securityContext);
        return PlayerResponse.from(getPlayerProfileUseCase.execute(playerId, authenticatedPlayerId));
    }
}

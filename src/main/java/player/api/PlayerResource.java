package player.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import player.application.createPlayer.CreatePlayerCommand;
import player.application.createPlayer.CreatePlayerUseCase;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    private final CreatePlayerUseCase createPlayerUseCase;

    @Inject
    public PlayerResource(CreatePlayerUseCase createPlayerUseCase) {
        this.createPlayerUseCase = createPlayerUseCase;
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
}

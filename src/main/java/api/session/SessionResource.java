package api.session;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import player.application.login.LoginCommand;
import player.application.login.LoginUseCase;

/** POST /sessions : seule route de joueur accessible sans être connecté. */
@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {

    private final LoginUseCase loginUseCase;

    @Inject
    public SessionResource(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        LoginCommand command = request == null ? null : new LoginCommand(request.email(), request.password());
        SessionResponse response = SessionResponse.from(loginUseCase.execute(command));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}

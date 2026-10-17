package api.hero;

import application.CreateHero.CreateHeroUseCase;
import application.CreateHero.HeroOutput;
import application.GetHero.GetHeroUseCase;
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
import jakarta.ws.rs.core.UriInfo;

@Path("/heroes")
@Produces(MediaType.APPLICATION_JSON)
public class HeroResource {

    private final CreateHeroUseCase createHeroUseCase;
    private final GetHeroUseCase getHeroUseCase;
    private final CreateHeroRequestMapper requestMapper;
    private final HeroResponseMapper responseMapper;

    @Inject
    public HeroResource(CreateHeroUseCase createHeroUseCase, GetHeroUseCase getHeroUseCase) {
        this.createHeroUseCase = createHeroUseCase;
        this.getHeroUseCase = getHeroUseCase;
        this.requestMapper = new CreateHeroRequestMapper();
        this.responseMapper = new HeroResponseMapper();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateHeroRequest request, @Context UriInfo uriInfo) {
        HeroOutput output = createHeroUseCase.execute(requestMapper.toCommand(request));
        HeroResponse response = responseMapper.toResponse(output);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                .path("/{id}")
                .build(response.id()))
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    public HeroResponse getById(@PathParam("id") String id) {
        return responseMapper.toResponse(getHeroUseCase.execute(id));
    }
}

package api.hero;

import application.CreateHero.CreateHeroUseCase;
import application.CreateHero.HeroOutput;
import application.GetHero.GetHeroUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/heroes")
public class HeroResource {

    private final CreateHeroUseCase createHeroUseCase;
    private final GetHeroUseCase getHeroUseCase;
    private final CreateHeroRequestMapper requestMapper;
    private final HeroResponseMapper responseMapper;

    public HeroResource(CreateHeroUseCase createHeroUseCase, GetHeroUseCase getHeroUseCase) {
        this.createHeroUseCase = createHeroUseCase;
        this.getHeroUseCase = getHeroUseCase;
        this.requestMapper = new CreateHeroRequestMapper();
        this.responseMapper = new HeroResponseMapper();
    }

    @PostMapping
    public ResponseEntity<HeroResponse> create(@RequestBody(required = false) CreateHeroRequest request) {
        HeroOutput output = createHeroUseCase.execute(requestMapper.toCommand(request));
        HeroResponse response = responseMapper.toResponse(output);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);
    }

    @GetMapping("/{id}")
    public HeroResponse getById(@PathVariable String id) {
        return responseMapper.toResponse(getHeroUseCase.execute(id));
    }
}

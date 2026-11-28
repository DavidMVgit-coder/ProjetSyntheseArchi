package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import lobby.application.port.HeroSummary;
import lobby.infrastructure.InMemoryActiveGames;
import lobby.infrastructure.InMemoryHeroCatalog;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectHeroApiHttpTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final UUID ALICE = UUID.fromString("8feadfd3-8280-4d70-8770-fc28c6641c22");
    private static final UUID BOB = UUID.fromString("a8bf2f4e-7551-4fb4-bddf-0ee2d921d101");
    private static final UUID ALICE_HERO = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID BOB_HERO = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID HERO_IN_GAME = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private HttpServer server;
    private HttpClient client;
    private String baseUrl;

    @BeforeEach
    void startApi() {
        InMemoryHeroCatalog heroCatalog = new InMemoryHeroCatalog();
        heroCatalog.add(new HeroSummary(ALICE_HERO, "Thorin", ALICE));
        heroCatalog.add(new HeroSummary(BOB_HERO, "Eldrin", BOB));
        heroCatalog.add(new HeroSummary(HERO_IN_GAME, "Aria", ALICE));
        InMemoryActiveGames activeGames = new InMemoryActiveGames();
        activeGames.add(HERO_IN_GAME);

        ApiConfiguration configuration = new ApiConfiguration(heroCatalog, activeGames);
        configuration.register(new TestAuthenticationFilter());
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://127.0.0.1:0/"), configuration);
        int port = server.getListeners().iterator().next().getPort();
        baseUrl = "http://127.0.0.1:" + port;
        client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    @AfterEach
    void stopApi() {
        client.close();
        server.shutdownNow();
    }

    @Test
    void aMemberSelectsTheirHeroAndGetsTheUpdatedLobby() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{\"heroId\": \"" + ALICE_HERO + "\"}", "alice");

        assertEquals(200, response.statusCode(), response.body());
        JsonNode lobby = JSON.readTree(response.body());
        JsonNode player = lobby.path("players").get(0);
        assertEquals(ALICE.toString(), player.path("playerId").asText());
        assertEquals(ALICE_HERO.toString(), player.path("heroId").asText());
    }

    @Test
    void selectingForAnotherPlayerIsForbidden() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{\"heroId\": \"" + ALICE_HERO + "\"}", "bob");

        assertError(response, 403, "FORBIDDEN");
    }

    @Test
    void selectingTheHeroOfAnotherPlayerIsForbidden() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{\"heroId\": \"" + BOB_HERO + "\"}", "alice");

        assertError(response, 403, "FORBIDDEN");
    }

    @Test
    void anUnknownHeroGets404() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{\"heroId\": \"" + UUID.randomUUID() + "\"}", "alice");

        assertError(response, 404, "HERO_NOT_FOUND");
    }

    @Test
    void aHeroAlreadyInAGameGets409() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{\"heroId\": \"" + HERO_IN_GAME + "\"}", "alice");

        assertError(response, 409, "HERO_IN_GAME");
    }

    @Test
    void aMissingHeroIdGets400() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, ALICE, "{}", "alice");

        assertError(response, 400, "INVALID_REQUEST");
    }

    @Test
    void anUnknownLobbyGets404() throws Exception {
        HttpResponse<String> response = selectHero(UUID.randomUUID().toString(), ALICE,
                "{\"heroId\": \"" + ALICE_HERO + "\"}", "alice");

        assertError(response, 404, "LOBBY_NOT_FOUND");
    }

    @Test
    void aPlayerWhoIsNotAMemberGets404() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = selectHero(lobbyId, BOB, "{\"heroId\": \"" + BOB_HERO + "\"}", "bob");

        assertError(response, 404, "PLAYER_NOT_IN_LOBBY");
    }

    private String createLobby(String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(baseUrl + "/lobbies"))
                .POST(HttpRequest.BodyPublishers.noBody());
        HttpResponse<String> response = send(request, token);
        assertEquals(201, response.statusCode());
        return JSON.readTree(response.body()).path("id").asText();
    }

    private HttpResponse<String> selectHero(String lobbyId, UUID playerId, String body, String token)
            throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(
                        URI.create(baseUrl + "/lobbies/" + lobbyId + "/players/" + playerId + "/hero"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body));
        return send(request, token);
    }

    private HttpResponse<String> send(HttpRequest.Builder request, String token) throws Exception {
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private void assertError(HttpResponse<String> response, int status, String code) throws Exception {
        assertEquals(status, response.statusCode(), response.body());
        JsonNode error = JSON.readTree(response.body());
        assertEquals(code, error.path("code").asText());
    }

    @Priority(Priorities.AUTHENTICATION)
    private static final class TestAuthenticationFilter implements ContainerRequestFilter {

        private static final Map<String, String> IDENTITIES = Map.of(
                "Bearer alice", ALICE.toString(),
                "Bearer bob", BOB.toString());

        @Override
        public void filter(ContainerRequestContext requestContext) {
            String authorization = requestContext.getHeaderString("Authorization");
            String playerId = authorization == null ? null : IDENTITIES.get(authorization);
            if (playerId == null) {
                return;
            }
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> playerId;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "TEST";
                }
            });
        }
    }
}

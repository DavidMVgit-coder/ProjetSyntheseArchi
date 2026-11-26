package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
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

class LeaveLobbyApiHttpTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final UUID ALICE = UUID.fromString("8feadfd3-8280-4d70-8770-fc28c6641c22");
    private static final UUID BOB = UUID.fromString("a8bf2f4e-7551-4fb4-bddf-0ee2d921d101");

    private HttpServer server;
    private HttpClient client;
    private String baseUrl;

    @BeforeEach
    void startApi() {
        ApiConfiguration configuration = new ApiConfiguration();
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
    void aPlayerLeavesTheirLobbyAndCanThenCreateANewOne() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = leave(lobbyId, ALICE, "alice");

        assertEquals(204, response.statusCode());
        assertEquals(201, post("/lobbies", "alice").statusCode());
    }

    @Test
    void aPlayerCannotRemoveAnotherPlayer() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = leave(lobbyId, ALICE, "bob");

        assertError(response, 403, "FORBIDDEN");
    }

    @Test
    void aPlayerWhoIsNotAMemberGets404() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = leave(lobbyId, BOB, "bob");

        assertError(response, 404, "PLAYER_NOT_IN_LOBBY");
    }

    @Test
    void anUnknownLobbyGets404() throws Exception {
        HttpResponse<String> response = leave(UUID.randomUUID().toString(), ALICE, "alice");

        assertError(response, 404, "LOBBY_NOT_FOUND");
    }

    @Test
    void anUnauthenticatedPlayerGets401() throws Exception {
        String lobbyId = createLobby("alice");

        HttpResponse<String> response = leave(lobbyId, ALICE, null);

        assertEquals(401, response.statusCode());
    }

    private String createLobby(String token) throws Exception {
        HttpResponse<String> response = post("/lobbies", token);
        assertEquals(201, response.statusCode());
        return JSON.readTree(response.body()).path("id").asText();
    }

    private HttpResponse<String> leave(String lobbyId, UUID playerId, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(
                URI.create(baseUrl + "/lobbies/" + lobbyId + "/players/" + playerId)).DELETE();
        return send(request, token);
    }

    private HttpResponse<String> post(String path, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.noBody());
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

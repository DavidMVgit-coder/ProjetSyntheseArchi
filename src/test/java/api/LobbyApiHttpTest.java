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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LobbyApiHttpTest {

    private static final int EXPECTED_LOBBY_CAPACITY = 4;
    private static final int EXPECTED_INITIAL_MEMBER_COUNT = 1;
    private static final int EXPECTED_INITIAL_AVAILABLE_SLOTS = 3;
    private static final int HOST_MEMBER_INDEX = 0;
    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final URI SERVER_BIND_URI = URI.create("http://127.0.0.1:0/");
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final UUID ALICE = UUID.fromString("8feadfd3-8280-4d70-8770-fc28c6641c22");
    private static final UUID BOB = UUID.fromString("a8bf2f4e-7551-4fb4-bddf-0ee2d921d101");

    private HttpServer server;
    private HttpClient client;
    private URI lobbiesUri;

    @BeforeEach
    void startApi() throws Exception {
        ApiConfiguration configuration = new ApiConfiguration();
        configuration.register(new TestAuthenticationFilter());
        server = GrizzlyHttpServerFactory.createHttpServer(SERVER_BIND_URI, configuration);
        int port = server.getListeners().iterator().next().getPort();
        lobbiesUri = URI.create("http://127.0.0.1:" + port + "/lobbies");
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(CONNECTION_TIMEOUT)
                .build();
    }

    @AfterEach
    void stopApi() {
        if (client != null) {
            client.close();
        }
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    void rejectsUnauthenticatedRequestsWithoutCreatingALobby() throws Exception {
        assertError(post(null), HTTP_UNAUTHORIZED, "AUTHENTICATION_REQUIRED");

        assertEquals(HTTP_CREATED, post("alice").statusCode());
    }

    @Test
    void doesNotTreatClientSuppliedPlayerIdsAsAuthentication() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(lobbiesUri)
                .header("X-Player-Id", ALICE.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"playerId\":\"" + ALICE + "\"}")));

        assertError(response, HTTP_UNAUTHORIZED, "AUTHENTICATION_REQUIRED");
    }

    @Test
    void rejectsUnrecognizedCredentials() throws Exception {
        assertError(post("unknown"), HTTP_UNAUTHORIZED, "AUTHENTICATION_REQUIRED");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-identity", "abbreviated-identity", "blank-identity"})
    void rejectsUnusableAuthenticatedIdentities(String token) throws Exception {
        assertError(post(token), HTTP_UNAUTHORIZED, "AUTHENTICATION_REQUIRED");
    }

    @Test
    void createsAnOpenLobbyForTheAuthenticatedPlayer() throws Exception {
        HttpResponse<String> response = post("alice");

        assertEquals(HTTP_CREATED, response.statusCode(), response.body());
        assertJson(response);
        JsonNode lobby = JSON.readTree(response.body());
        assertEquals(lobby.path("id").asText(), UUID.fromString(lobby.path("id").asText()).toString());
        assertEquals(ALICE.toString(), lobby.path("hostId").asText());
        assertEquals(EXPECTED_INITIAL_MEMBER_COUNT, lobby.path("memberIds").size());
        assertEquals(ALICE.toString(), lobby.path("memberIds").get(HOST_MEMBER_INDEX).asText());
        assertEquals(EXPECTED_LOBBY_CAPACITY, lobby.path("capacity").asInt());
        assertEquals(EXPECTED_INITIAL_AVAILABLE_SLOTS, lobby.path("availableSlots").asInt());
        assertEquals("OPEN", lobby.path("status").asText());
    }

    @Test
    void refusesASecondOpenLobbyForTheSamePlayer() throws Exception {
        assertEquals(HTTP_CREATED, post("alice").statusCode());

        assertError(post("alice"), HTTP_CONFLICT, "PLAYER_ALREADY_IN_OPEN_LOBBY");
    }

    @Test
    void differentPlayersCanCreateTheirOwnLobbies() throws Exception {
        HttpResponse<String> aliceResponse = post("alice");
        HttpResponse<String> bobResponse = post("bob");

        assertEquals(HTTP_CREATED, aliceResponse.statusCode());
        assertEquals(HTTP_CREATED, bobResponse.statusCode());
        JsonNode aliceLobby = JSON.readTree(aliceResponse.body());
        JsonNode bobLobby = JSON.readTree(bobResponse.body());
        assertNotEquals(aliceLobby.path("id"), bobLobby.path("id"));
        assertEquals(ALICE.toString(), aliceLobby.path("hostId").asText());
        assertEquals(BOB.toString(), bobLobby.path("hostId").asText());
    }

    @Test
    void takesTheHostFromAuthenticationEvenIfTheBodyNamesAnotherPlayer() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(lobbiesUri)
                .header("Authorization", "Bearer alice")
                .header("X-Player-Id", BOB.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"hostId\":\"" + BOB + "\"}")));

        assertEquals(HTTP_CREATED, response.statusCode(), response.body());
        JsonNode lobby = JSON.readTree(response.body());
        assertEquals(ALICE.toString(), lobby.path("hostId").asText());
        assertEquals(ALICE.toString(), lobby.path("memberIds").get(HOST_MEMBER_INDEX).asText());
    }

    @Test
    void productionConfigurationRejectsRequestsUntilAuthenticationIsIntegrated() throws Exception {
        HttpServer productionServer = Main.startServer(SERVER_BIND_URI);
        try {
            int port = productionServer.getListeners().iterator().next().getPort();
            URI uri = URI.create("http://127.0.0.1:" + port + "/lobbies");

            HttpResponse<String> response = send(HttpRequest.newBuilder(uri)
                    .header("Authorization", "Bearer alice")
                    .POST(HttpRequest.BodyPublishers.noBody()));

            assertError(response, HTTP_UNAUTHORIZED, "AUTHENTICATION_REQUIRED");
        } finally {
            productionServer.shutdownNow();
        }
    }

    private HttpResponse<String> post(String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(lobbiesUri)
                .POST(HttpRequest.BodyPublishers.noBody());
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }
        return send(request);
    }

    private HttpResponse<String> send(HttpRequest.Builder request) throws Exception {
        return client.send(request.timeout(REQUEST_TIMEOUT).build(), HttpResponse.BodyHandlers.ofString());
    }

    private void assertError(HttpResponse<String> response, int status, String code) throws Exception {
        assertEquals(status, response.statusCode(), response.body());
        assertJson(response);
        JsonNode error = JSON.readTree(response.body());
        assertEquals(status, error.path("status").asInt());
        assertEquals(code, error.path("code").asText());
        assertEquals("/lobbies", error.path("path").asText());
        assertFalse(error.path("message").asText().isBlank());
        Instant.parse(error.path("timestamp").asText());
    }

    private void assertJson(HttpResponse<String> response) {
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/json"));
    }

    /** Simule uniquement dans les tests le contexte fourni par la future authentification. */
    @Priority(Priorities.AUTHENTICATION)
    private static final class TestAuthenticationFilter implements ContainerRequestFilter {

        private static final Map<String, String> IDENTITIES = Map.of(
                "Bearer alice", ALICE.toString(),
                "Bearer bob", BOB.toString(),
                "Bearer invalid-identity", "not-a-uuid",
                "Bearer abbreviated-identity", "1-1-1-1-1",
                "Bearer blank-identity", " ");

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

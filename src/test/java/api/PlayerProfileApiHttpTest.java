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
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PlayerProfileApiHttpTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final URI SERVER_BIND_URI = URI.create("http://127.0.0.1:0/");
    private static final ObjectMapper JSON = new ObjectMapper();

    private HttpServer server;
    private HttpClient client;
    private String baseUrl;

    @BeforeEach
    void startApi() {
        ApiConfiguration configuration = new ApiConfiguration();
        configuration.register(new TestAuthenticationFilter());
        server = GrizzlyHttpServerFactory.createHttpServer(SERVER_BIND_URI, configuration);
        int port = server.getListeners().iterator().next().getPort();
        baseUrl = "http://127.0.0.1:" + port;
        client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(TIMEOUT).build();
    }

    @AfterEach
    void stopApi() {
        client.close();
        server.shutdownNow();
    }

    @Test
    void aConnectedPlayerSeesTheirIdPseudonymAndEmail() throws Exception {
        String ariaId = createPlayer("Aria", "aria@mail.com");

        HttpResponse<String> response = getProfile(ariaId, ariaId);

        assertEquals(200, response.statusCode(), response.body());
        JsonNode profile = JSON.readTree(response.body());
        assertEquals(ariaId, profile.path("id").asText());
        assertEquals("Aria", profile.path("pseudonym").asText());
        assertEquals("aria@mail.com", profile.path("email").asText());
        assertEquals(3, profile.size());
    }

    @Test
    void neverReturnsThePassword() throws Exception {
        String ariaId = createPlayer("Aria", "aria@mail.com");

        HttpResponse<String> response = getProfile(ariaId, ariaId);

        assertEquals(200, response.statusCode(), response.body());
        assertFalse(response.body().contains("dragon-2026"));
        assertFalse(response.body().toLowerCase().contains("password"));
        assertFalse(response.body().contains("argon2"));
    }

    @Test
    void aPlayerCannotSeeAnotherPlayersProfile() throws Exception {
        String ariaId = createPlayer("Aria", "aria@mail.com");
        String bramId = createPlayer("Bram", "bram@mail.com");

        HttpResponse<String> response = getProfile(bramId, ariaId);

        assertError(response, 403, "FORBIDDEN");
        assertFalse(response.body().contains("bram@mail.com"));
    }

    @Test
    void anAuthenticatedPlayerWithoutAccountGets404() throws Exception {
        String unknownId = UUID.randomUUID().toString();

        HttpResponse<String> response = getProfile(unknownId, unknownId);

        assertError(response, 404, "PLAYER_NOT_FOUND");
    }

    @Test
    void anUnauthenticatedRequestGets401() throws Exception {
        String ariaId = createPlayer("Aria", "aria@mail.com");

        HttpResponse<String> response = getProfile(ariaId, null);

        assertError(response, 401, "AUTHENTICATION_REQUIRED");
    }

    @Test
    void productionConfigurationRejectsRequestsUntilAuthenticationIsIntegrated() throws Exception {
        HttpServer productionServer = Main.startServer(SERVER_BIND_URI);
        try {
            int port = productionServer.getListeners().iterator().next().getPort();
            String id = UUID.randomUUID().toString();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/players/" + id))
                    .timeout(TIMEOUT)
                    .header("Authorization", "Bearer " + id)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertError(response, 401, "AUTHENTICATION_REQUIRED");
        } finally {
            productionServer.shutdownNow();
        }
    }

    private String createPlayer(String pseudonym, String email) throws Exception {
        String body = "{\"pseudonym\":\"" + pseudonym + "\",\"email\":\"" + email + "\",\"password\":\"dragon-2026\"}";
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/players"))
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), response.body());
        return JSON.readTree(response.body()).path("id").asText();
    }

    private HttpResponse<String> getProfile(String playerId, String authenticatedPlayerId) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(baseUrl + "/players/" + playerId))
                .timeout(TIMEOUT)
                .GET();
        if (authenticatedPlayerId != null) {
            request.header("Authorization", "Bearer " + authenticatedPlayerId);
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private void assertError(HttpResponse<String> response, int status, String code) throws Exception {
        assertEquals(status, response.statusCode(), response.body());
        JsonNode error = JSON.readTree(response.body());
        assertEquals(status, error.path("status").asInt());
        assertEquals(code, error.path("code").asText());
        assertFalse(error.path("message").asText().isBlank());
    }

    @Priority(Priorities.AUTHENTICATION)
    private static final class TestAuthenticationFilter implements ContainerRequestFilter {

        private static final String BEARER = "Bearer ";

        @Override
        public void filter(ContainerRequestContext requestContext) {
            String authorization = requestContext.getHeaderString("Authorization");
            if (authorization == null || !authorization.startsWith(BEARER)) {
                return;
            }
            String playerId = authorization.substring(BEARER.length());
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

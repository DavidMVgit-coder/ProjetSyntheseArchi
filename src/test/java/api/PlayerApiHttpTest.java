package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerApiHttpTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final URI SERVER_BIND_URI = URI.create("http://127.0.0.1:0/");
    private static final ObjectMapper JSON = new ObjectMapper();

    private HttpServer server;
    private HttpClient client;
    private URI playersUri;

    @BeforeEach
    void startApi() {
        server = GrizzlyHttpServerFactory.createHttpServer(SERVER_BIND_URI, new ApiConfiguration());
        int port = server.getListeners().iterator().next().getPort();
        playersUri = URI.create("http://127.0.0.1:" + port + "/players");
        client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(TIMEOUT).build();
    }

    @AfterEach
    void stopApi() {
        client.close();
        server.shutdownNow();
    }

    @Test
    void createsAnAccountWithoutAuthentication() throws Exception {
        HttpResponse<String> response = post(body("Aria", "aria@mail.com", "dragon-2026"));

        assertEquals(201, response.statusCode(), response.body());
        JsonNode player = JSON.readTree(response.body());
        UUID id = UUID.fromString(player.path("id").asText());
        assertEquals("Aria", player.path("pseudonym").asText());
        assertEquals("aria@mail.com", player.path("email").asText());
        assertTrue(response.headers().firstValue("Location").orElse("").endsWith("/players/" + id));
    }

    @Test
    void neverReturnsThePassword() throws Exception {
        HttpResponse<String> response = post(body("Aria", "aria@mail.com", "dragon-2026"));

        assertFalse(response.body().contains("dragon-2026"));
        assertFalse(response.body().toLowerCase().contains("password"));
        assertFalse(response.body().contains("argon2"));
    }

    @Test
    void rejectsMissingOrMalformedFields() throws Exception {
        assertError(post("{\"email\":\"aria@mail.com\",\"password\":\"dragon-2026\"}"), 400, "INVALID_REQUEST");
        assertError(post("{\"pseudonym\":\"Aria\",\"password\":\"dragon-2026\"}"), 400, "INVALID_REQUEST");
        assertError(post("{\"pseudonym\":\"Aria\",\"email\":\"aria@mail.com\"}"), 400, "INVALID_REQUEST");
        assertError(post(body("A!", "aria@mail.com", "dragon-2026")), 400, "INVALID_REQUEST");
        assertError(post(body("Aria", "pas-un-courriel", "dragon-2026")), 400, "INVALID_REQUEST");
    }

    @Test
    void rejectsAPseudonymAlreadyTakenIgnoringCase() throws Exception {
        assertEquals(201, post(body("Aria", "aria@mail.com", "dragon-2026")).statusCode());

        assertError(post(body("aria", "autre@mail.com", "dragon-2026")), 409, "PSEUDONYM_TAKEN");
    }

    @Test
    void rejectsAnEmailAlreadyTakenIgnoringCase() throws Exception {
        assertEquals(201, post(body("Aria", "aria@mail.com", "dragon-2026")).statusCode());

        assertError(post(body("Bram", "ARIA@mail.com", "dragon-2026")), 409, "EMAIL_TAKEN");
    }

    @Test
    void rejectsAPasswordThatIsTooShort() throws Exception {
        assertError(post(body("Aria", "aria@mail.com", "court")), 422, "PASSWORD_TOO_SHORT");
    }

    private String body(String pseudonym, String email, String password) {
        return "{\"pseudonym\":\"" + pseudonym + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
    }

    private HttpResponse<String> post(String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(playersUri)
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void assertError(HttpResponse<String> response, int status, String code) throws Exception {
        assertEquals(status, response.statusCode(), response.body());
        JsonNode error = JSON.readTree(response.body());
        assertEquals(status, error.path("status").asInt());
        assertEquals(code, error.path("code").asText());
        assertEquals("/players", error.path("path").asText());
        assertFalse(error.path("message").asText().isBlank());
        assertFalse(response.body().contains("dragon-2026"));
    }
}

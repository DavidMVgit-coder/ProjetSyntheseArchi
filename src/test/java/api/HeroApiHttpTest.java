package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroApiHttpTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static HttpServer server;
    private static HttpClient client;
    private static URI baseUri;

    @BeforeAll
    static void startApi() throws Exception {
        server = ProjetSyntheseArchiApplication.startServer(URI.create("http://127.0.0.1:0/"));
        int port = server.getListeners().iterator().next().getPort();
        baseUri = URI.create("http://127.0.0.1:" + port + "/");
        // Le serveur Grizzly est configuré pour HTTP/1.1, sans négociation HTTP/2.
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @AfterAll
    static void stopApi() {
        if (client != null) {
            client.close();
        }
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    void createsHeroAndReadsItBackFromLocation() throws Exception {
        HttpResponse<String> created = post(validHero("Aria"));

        assertEquals(201, created.statusCode(), created.body());
        assertJson(created);
        JsonNode hero = JSON.readTree(created.body());
        String id = hero.path("id").asText();
        assertEquals(id, UUID.fromString(id).toString());
        assertEquals("Aria", hero.path("name").asText());
        assertEquals("ELF", hero.path("species").asText());
        assertEquals("RANGER", hero.path("heroClass").asText());
        assertEquals(1, hero.path("level").asInt());
        assertEquals(9, hero.size());
        assertTrue(hero.path("maxHitPoints").asInt() > 0);
        assertEquals(hero.path("maxHitPoints"), hero.path("currentHitPoints"));
        assertEquals(hero.path("maxMana"), hero.path("currentMana"));

        URI location = URI.create(created.headers().firstValue("Location").orElseThrow());
        assertEquals(baseUri.resolve("heroes/" + id), location);
        HttpResponse<String> fetched = send(HttpRequest.newBuilder(location).GET().build());
        assertEquals(200, fetched.statusCode(), fetched.body());
        assertJson(fetched);
        assertEquals(hero, JSON.readTree(fetched.body()));
    }

    @Test
    void rejectsDuplicateHeroNames() throws Exception {
        assertEquals(201, post(validHero("Luna")).statusCode());
        assertError(post(validHero("Luna")), 409, "HERO_ALREADY_EXISTS", "/heroes");
    }

    @Test
    void returnsNotFoundForAnUnknownHero() throws Exception {
        String path = "heroes/00000000-0000-0000-0000-000000000000";
        assertError(get(path), 404, "HERO_NOT_FOUND", "/" + path);
    }

    @Test
    void rejectsInvalidHeroIds() throws Exception {
        assertError(get("heroes/not-a-uuid"), 400, "INVALID_REQUEST", "/heroes/not-a-uuid");
    }

    @ParameterizedTest
    @MethodSource("invalidHeroFields")
    void rejectsInvalidHeroData(String field, Object value) throws Exception {
        ObjectNode request = (ObjectNode) JSON.readTree(validHero("Invalid hero"));
        request.set(field, JSON.valueToTree(value));

        assertError(post(JSON.writeValueAsString(request)), 400, "INVALID_REQUEST", "/heroes");
    }

    static Stream<Arguments> invalidHeroFields() {
        return Stream.of(
                Arguments.of("name", null),
                Arguments.of("name", ""),
                Arguments.of("species", "UNKNOWN"),
                Arguments.of("species", null),
                Arguments.of("heroClass", "UNKNOWN"),
                Arguments.of("strength", 2),
                Arguments.of("strength", 19));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "{\"name\":", "[]", "{\"strength\":\"abc\"}"})
    void rejectsMissingOrUnreadableRequestBodies(String body) throws Exception {
        assertError(post(body), 400, "INVALID_REQUEST", "/heroes");
    }

    @Test
    void preservesNotFoundForUnknownRoutes() throws Exception {
        assertError(get("unknown-route"), 404, "HTTP_ERROR", "/unknown-route");
    }

    @Test
    void preservesMethodNotAllowedAndTheAllowHeader() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(baseUri.resolve("heroes"))
                .PUT(HttpRequest.BodyPublishers.noBody()).build());

        assertError(response, 405, "HTTP_ERROR", "/heroes");
        assertTrue(response.headers().allValues("Allow").stream().anyMatch(value -> value.contains("POST")));
    }

    @Test
    void rejectsUnsupportedMediaTypes() throws Exception {
        HttpResponse<String> response = send(HttpRequest.newBuilder(baseUri.resolve("heroes"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(validHero("Mira"))).build());

        assertError(response, 415, "HTTP_ERROR", "/heroes");
    }

    private static HttpResponse<String> post(String body) throws Exception {
        return send(HttpRequest.newBuilder(baseUri.resolve("heroes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build());
    }

    private static HttpResponse<String> get(String path) throws Exception {
        return send(HttpRequest.newBuilder(baseUri.resolve(path)).GET().build());
    }

    private static HttpResponse<String> send(HttpRequest request) throws Exception {
        HttpRequest timedRequest = HttpRequest.newBuilder(request, (name, value) -> true)
                .timeout(Duration.ofSeconds(5)).build();
        return client.send(timedRequest, HttpResponse.BodyHandlers.ofString());
    }

    private static void assertError(HttpResponse<String> response, int status, String code, String path)
            throws Exception {
        assertEquals(status, response.statusCode(), response.body());
        assertJson(response);
        JsonNode error = JSON.readTree(response.body());
        assertEquals(status, error.path("status").asInt());
        assertEquals(code, error.path("code").asText());
        assertEquals(path, error.path("path").asText());
        assertFalse(error.path("message").asText().isBlank());
        Instant.parse(error.path("timestamp").asText());
    }

    private static void assertJson(HttpResponse<String> response) {
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/json"),
                () -> "Réponse attendue en JSON : " + response.headers().map() + " " + response.body());
    }

    private static String validHero(String name) {
        return """
                {
                  "name": "%s",
                  "species": "elf",
                  "heroClass": "ranger",
                  "strength": 10,
                  "dexterity": 15,
                  "constitution": 12,
                  "intelligence": 13,
                  "wisdom": 14,
                  "charisma": 11
                }
                """.formatted(name);
    }
}

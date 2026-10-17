package api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class ProjetSyntheseArchiApplication {

    public static HttpServer startServer(URI baseUri) throws IOException {
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, new ApiConfiguration(), false);
        server.start();
        return server;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        URI baseUri = URI.create("http://localhost:8080/");
        HttpServer server = startServer(baseUri);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow, "api-shutdown"));
        System.out.println("API des héros disponible sur " + baseUri + "heroes");

        try {
            new CountDownLatch(1).await();
        } finally {
            server.shutdownNow();
        }
    }
}


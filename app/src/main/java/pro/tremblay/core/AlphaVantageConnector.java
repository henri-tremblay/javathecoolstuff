package pro.tremblay.core;

import com.sun.net.httpserver.SimpleFileServer;
import jdk.incubator.concurrent.StructuredTaskScope;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.Future;

public class AlphaVantageConnector {

    private final HttpClient client = HttpClient.newHttpClient();

    private final String baseUrl;

    public AlphaVantageConnector(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Amount dailyPrice(String ticker) {

        HttpRequest get = HttpRequest
            .newBuilder(URI.create(baseUrl + "/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=demo"))
            .header("Accept", "application/json")
            .GET()
            .build();

        try(var scope = new StructuredTaskScope<String>()) {
            Future<String> future = scope.fork(() -> client.send(get, HttpResponse.BodyHandlers.ofString()).body());
            scope.join();
            System.out.println(future.resultNow());
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Amount.amnt(42); // fake value because I do not want to showcase parsing json
    }

    public static void main(String[] args) {
        launchServer();

        AlphaVantageConnector connector = new AlphaVantageConnector("http://localhost:8000");
        connector.dailyPrice("IBM");
    }

    private static void launchServer() {
        var server = SimpleFileServer.createFileServer(
            new InetSocketAddress(8000),
            Path.of(".").toAbsolutePath(),
            SimpleFileServer.OutputLevel.INFO);

        server.start();
    }
}

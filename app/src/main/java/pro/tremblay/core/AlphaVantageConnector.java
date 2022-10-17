package pro.tremblay.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AlphaVantageConnector {

    private final HttpClient client = HttpClient.newHttpClient();

    public Amount dailyPrice(String ticker) {

        HttpRequest get = HttpRequest
            .newBuilder(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=demo"))
            .header("Accept", "application/json")
            .GET()
            .build();

        CompletableFuture<HttpResponse<String>> response =
            client.sendAsync(get, HttpResponse.BodyHandlers.ofString());

        try {
            System.out.println(response.get().body());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Amount.amnt(42);
    }

    public static void main(String[] args) {
        AlphaVantageConnector connector = new AlphaVantageConnector();
        connector.dailyPrice("IBM");
    }
}

package pro.tremblay.core;

import java.util.List;

public class AlphaVantageConnector {

    private final String baseUrl;

//    private final HttpClient client = HttpClient.newHttpClient();

    public AlphaVantageConnector(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> dailyPrice(String... tickers) {
//        List<CompletableFuture<String>> results = Stream.of(tickers)
//            .map(ticker -> HttpRequest
//                .newBuilder(URI.create(baseUrl + "/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=demo"))
//                .header("Accept", "application/json")
//                .GET()
//                .build())
//            .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body))
//            .toList();
//
//        return results.stream()
//            .map(future -> {
//                try {
//                    return future.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    throw new RuntimeException(e);
//                }
//            })
//            .toList();
        return null;
    }

    public static void main(String[] args) {
        AlphaVantageConnector connector = new AlphaVantageConnector("https://www.alphavantage.co");
        System.out.println(connector.dailyPrice("IBM"));
    }
}









//     private static void launchServer() {
//        var server = SimpleFileServer.createFileServer(
//            new InetSocketAddress(8000),
//            Path.of(".").toAbsolutePath(),
//            SimpleFileServer.OutputLevel.INFO);
//
//        server.start();
//    }

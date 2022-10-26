/*
 * Copyright 2022-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

//         try (var scope = new StructuredTaskScope<String>()) {
//            List<Future<String>> futures = Stream.of(tickers)
//                .map(ticker -> scope.fork(() -> {
//                    HttpRequest get = HttpRequest
//                        .newBuilder(URI.create(baseUrl + "/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=demo"))
//                        .header("Accept", "application/json")
//                        .GET()
//                        .build();
//                    return client.send(get, HttpResponse.BodyHandlers.ofString()).body();
//                }))
//                .toList();
//            try {
//                scope.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            return futures.stream().map(Future::resultNow).toList();
//        }

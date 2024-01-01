/*
 * Copyright 2022-2024 the original author or authors.
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;

/**
 * Service returning security prices. This is actually a fake implementation using randomly generated prices.
 */
@ThreadSafe
public class RealPriceService implements PriceService {

//    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl;

    public RealPriceService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns the current price for a security. The price comes from an HTTP call in the form of {@code baseUrl/ticker}.
     * The returned text is the price.
     *
     * @param security security for which we want a price
     * @throws IllegalArgumentException if no price is found at this date
     * @return the price of the security at a given date
     */
    @Override
    @Nonnull
    public Amount getPrice(@Nonnull Security security) {
        Amount price = queryPrice(security.symbol());
        if(price == null) {
            throw new IllegalArgumentException("No price found for " + security.symbol());
        }
        return price;
    }

    private Amount queryPrice(@Nonnull String ticker) {
//        var request = HttpRequest.newBuilder(URI.create(baseUrl + "/" + ticker))
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//        HttpResponse<String> body;
//        try {
//            body = client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        } catch (InterruptedException e) {
//            return null;
//        }
//        if (body.statusCode() == 404) {
//            return null;
//        }
//        return Amount.amnt(body.body().trim());
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        var server = launchServer();
//
//        var connector = new RealPriceService("http://localhost:8000");
//        System.out.println(connector.queryPrice("IBM"));
//
//        server.stop(0);
//    }
//
//    private static HttpServer launchServer() {
//        var server = SimpleFileServer.createFileServer(
//            new InetSocketAddress(8000),
//            Path.of(".").toAbsolutePath(),
//            SimpleFileServer.OutputLevel.INFO);
//
//        server.start();
//
//        return server;
//    }
}

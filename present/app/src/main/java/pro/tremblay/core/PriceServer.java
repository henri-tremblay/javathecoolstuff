/*
 * Copyright 2022-2026 the original author or authors.
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import pro.tremblay.core.price.RandomPriceRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * Service returning security prices. This is actually a fake implementation using randomly generated prices.
 */
public class PriceServer implements AutoCloseable {

    private final HttpServer server;
    private final RandomPriceRepository priceRepository;

    static void main() throws Exception {
        try (PriceServer _ = new PriceServer(new RandomPriceRepository())) {
            IO.readln("Type any key to stop the server");
        }
    }

    public PriceServer(RandomPriceRepository priceRepository) throws IOException {
        this.priceRepository = priceRepository;
        server = launchServer();
    }

    private HttpServer launchServer() throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/price", exchange -> {
            String ticker = extractTicker(exchange);
            Amount price = priceRepository.getPrice(ticker);
            byte[] body = price.value().toPlainString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, body.length);
            try (exchange; OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });

        server.start();
        return server;
    }

    private String extractTicker(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    public void close() {
        server.stop(0);
    }
}

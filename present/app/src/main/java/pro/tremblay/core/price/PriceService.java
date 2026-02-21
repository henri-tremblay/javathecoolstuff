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
package pro.tremblay.core.price;

import org.jspecify.annotations.Nullable;
import pro.tremblay.core.Amount;
import pro.tremblay.core.security.Security;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PriceService implements AutoCloseable {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl;
    private final AuthenticationProvider auth;

    public PriceService(String baseUrl, AuthenticationProvider auth) {
        this.baseUrl = baseUrl;
        this.auth = auth;
    }

    /**
     * Returns the current price for a security. The price comes from an HTTP call in the form of {@code baseUrl/ticker}.
     * The returned text is the price.
     *
     * @param security security for which we want a price
     * @throws IllegalArgumentException if no price is found at this date
     * @return the price of the security at a given date
     */
    public Amount getPrice(Security security) {
        Amount price = queryPrice(security.symbol());
        if(price == null) {
            throw new IllegalArgumentException("No price found for " + security.symbol());
        }
        return price;
    }

    private @Nullable Amount queryPrice(String ticker) {
        var request = HttpRequest.newBuilder(URI.create(baseUrl + "/price/" + URLEncoder.encode(ticker, StandardCharsets.UTF_8)))
                .header("Accept", "text/plain")
                .header("Authorization", "Basic " + auth.password())
                .GET()
                .build();
        HttpResponse<String> body;
        try {
            body = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        if (body.statusCode() == 404) {
            return null;
        }
        return new Amount(body.body().trim());
    }

    @Override
    public void close() {
        client.close();
    }
}

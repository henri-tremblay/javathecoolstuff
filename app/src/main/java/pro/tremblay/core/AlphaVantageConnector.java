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

import com.sun.net.httpserver.HttpServer;
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

    public static void main(String[] args) throws Exception {
        var server = launchServer();

        var connector = new AlphaVantageConnector("http://localhost:8000");
        connector.dailyPrice("IBM");

        server.stop(0);
    }

    private static HttpServer launchServer() {
        var server = SimpleFileServer.createFileServer(
            new InetSocketAddress(8000),
            Path.of(".").toAbsolutePath(),
            SimpleFileServer.OutputLevel.INFO);

        server.start();

        return server;
    }
}

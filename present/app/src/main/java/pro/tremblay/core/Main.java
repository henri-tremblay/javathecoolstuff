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

import com.sun.net.httpserver.HttpServer;
import pro.tremblay.core.position.Position;
import pro.tremblay.core.position.PositionReader;
import pro.tremblay.core.price.PriceService;
import pro.tremblay.core.security.SecurityService;
import pro.tremblay.core.transaction.Transaction;
import pro.tremblay.core.transaction.TransactionReader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    private static final String TEMPLATE = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="refresh" content="5">
            <title>Return on investment</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">\
        </head>
        <body>
            <div class="alert alert-primary" role="alert">
                Valeur du portefeuille: %s<br>
                Valeur il y a un an: %s<br>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>Symbol</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                %s
                </tbody>
            </table>
        </body>
        </html>""";

    void main() throws IOException {
        FileProvider fileProvider = new FileProvider();

        SecurityService securityService = new SecurityService(fileProvider.securityFile());
        PositionReader positionReader = new PositionReader(securityService);
        TransactionReader transactionReader = new TransactionReader(securityService);

        Position current = positionReader.readFromFile(fileProvider.positionFile());
        List<Transaction> transactions = transactionReader.read(fileProvider.transactionFile());

        try (PriceService priceService = new PriceService("http://localhost:8000")) {
            HttpServer server = launchServer();
            server.createContext("/", exchange -> {
                Amount currentAmount = current.securityPositionValue(priceService);
                Position initial = current.copy();
                initial.revert(transactions);
                Amount initialAmount = initial.securityPositionValue(priceService);

                String positions = current.getSecurityPositions().stream()
                    .map(sp -> String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
                        sp.security().symbol(),
                        sp.quantity(),
                        priceService.getPrice(sp.security())))
                    .collect(java.util.stream.Collectors.joining("\n"));

                String result = TEMPLATE.formatted(initialAmount, currentAmount, positions);
                byte[] body = result.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, body.length);
                try (exchange; OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            });
            IO.readln("Type any key to stop the server");
            server.stop(0);
        }
    }

    private HttpServer launchServer() throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.start();
        return server;
    }

}

// mvn -pl app dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/mods
// jpackage --app-version 1.0 --name app --module-path app/target/classes:app/target/mods --module app/pro.tremblay.core.Main --java-options --enable-preview

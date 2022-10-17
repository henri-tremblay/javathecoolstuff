/*
 * Copyright 2022-2023 the original author or authors.
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

public class Main {

    private static final String TEMPLATE = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Return on investment</title>
        </head>
        <body>
        Your current return on investment as of %s is: %s
        </body>
        </html>
        """;

    public static void main(String[] args) throws IOException {
        Preferences preferences = new Preferences();
        preferences.put(ReportingService.LENGTH_OF_YEAR, "365");

        Clock clock = Clock.systemUTC();
        SecurityService securityService = new SecurityService(Paths.get("listing_status.csv"));
        PriceService priceService = new PriceService(securityService, clock);
        ReportingService reportingService = new ReportingService(preferences, clock, priceService);

        TransactionReader transactionReader = new TransactionReader(securityService);
        Collection<Transaction> transactions = transactionReader.read(Paths.get("transaction.csv"));

        Position current = Position.position().cash(Amount.amnt(10_000));

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);
        String result = TEMPLATE.formatted(roi, LocalDateTime.now(clock));
        Files.writeString(Paths.get("result.html"), result, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println(result);
    }

}

// jdeps app/target/app-1.0-SNAPSHOT.jar
// jlink --add-modules java.base,java.net.http --output myjre --no-header-files --no-man-pages --strip-java-debug-attributes --strip-debug --compress=2
// myjre/bin/java -cp app/target/app-1.0-SNAPSHOT.jar pro.tremblay.core.Main
// jpackage --name myapp --input app/target --main-jar app-1.0-SNAPSHOT.jar --main-class pro.tremblay.core.Main --runtime-image myjre
// ./myapp.app/Contents/MacOS/myapp

// jlink --add-modules java.base,java.net.http,jdk.random --output myjre --no-header-files --no-man-pages --strip-java-debug-attributes --strip-debug --compress=2

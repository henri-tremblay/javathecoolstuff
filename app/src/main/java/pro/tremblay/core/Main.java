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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

public class Main {

    private static final String TEMPLATE = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <title>Return on investment</title>\n" +
        "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">" +
        "</head>\n" +
        "<body>\n" +
        "    <div class=\"alert alert-primary\" role=\"alert\">\n" +
        "        Your current return on investment as of ${now} is: ${roi}\n" +
        "    </div>" +
        "</body>\n" +
        "</html>";

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
        String result = TEMPLATE
            .replace("${roi}", roi.toString())
            .replace("${now}", LocalDateTime.now(clock).toString());
        Files.write(Paths.get("result.html"), result.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println(result);
    }

}

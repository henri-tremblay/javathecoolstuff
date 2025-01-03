/*
 * Copyright 2022-2025 the original author or authors.
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
import java.util.List;

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
        "        Portfolio value at the beginning of the year: ${initialValue}<br>\n" +
        "        Portfolio value now: ${currentValue}\n" +
        "    </div>" +
        "</body>\n" +
        "</html>";

    public static void main(String[] args) throws IOException {
        SecurityService securityService = new SecurityService(Paths.get("securities.csv"));
        PositionReader positionReader = new PositionReader(securityService);
        PriceService priceService = new FakePriceService();
        TransactionReader transactionReader = new TransactionReader(securityService);

        Position current = positionReader.readFromFile(Paths.get("positions.csv"));
        List<Transaction> transactions = transactionReader.read(Paths.get("transactions.csv"));

        Amount currentAmount = current.securityPositionValue(priceService);
        Position initial = current.copy();
        initial.revert(transactions);
        Amount initialAmount = initial.securityPositionValue(priceService);

        String result = TEMPLATE
            .replace("${initialValue}", initialAmount.toString())
            .replace("${currentValue}", currentAmount.toString());
        Files.write(Paths.get("result.html"), result.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println(result);
    }

}

// jdeps app/target/app-1.0-SNAPSHOT.jar
// jlink --add-modules java.base,java.net.http --output myjre --no-header-files --no-man-pages --strip-java-debug-attributes --strip-debug --compress=2
// myjre/bin/java -cp app/target/app-1.0-SNAPSHOT.jar pro.tremblay.core.Main
// jpackage --name myapp --input app/target --main-jar app-1.0-SNAPSHOT.jar --main-class pro.tremblay.core.Main --runtime-image myjre
// ./myapp.app/Contents/MacOS/myapp

// jlink --add-modules java.base,java.net.http,jdk.random --output myjre --no-header-files --no-man-pages --strip-java-debug-attributes --strip-debug --compress=2

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

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PositionGenerator {

    public static void main(String[] args) throws Exception {
        SecurityService securityService = new SecurityService(Paths.get("listing_status.csv"));
        List<Security> securities = new ArrayList<>(securityService.allSecurities());
        // Let's add CASH
        securities.add(new Security("CASH", "Cash position", "FOREX", "FX", LocalDate.now()));

        Random random = new Random();
        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("positions.csv"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {

            for (int i = 0; i < 1_000_000; i++) {
                Security security = securities.get(random.nextInt(securities.size()));
                long quantity = 1 + random.nextInt(999);
                out.write(security.symbol());
                out.write(",");
                out.write("" + quantity);
                out.newLine();
            }
        }
    }

}

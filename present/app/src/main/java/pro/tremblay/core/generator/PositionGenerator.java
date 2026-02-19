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
package pro.tremblay.core.generator;

import pro.tremblay.core.FileProvider;
import pro.tremblay.core.security.Security;
import pro.tremblay.core.security.SecurityService;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class PositionGenerator {

    public static final int POSITION_COUNT = 50;

    static void main() throws Exception {
        FileProvider fileProvider = new FileProvider();
        SecurityService securityService = new SecurityService(fileProvider.securityFile());
        List<Security> securities = new ArrayList<>(securityService.allSecurities());

        RandomGenerator random = RandomGenerator.getDefault();
        try (BufferedWriter out = Files.newBufferedWriter(fileProvider.positionFile())) {
            for (int i = 0; i < POSITION_COUNT; i++) {
                // remove to prevent duplicates
                Security security = securities.remove(random.nextInt(securities.size()));
                long quantity = random.nextInt(1, 1000);
                out.write(security.symbol());
                out.write(",");
                out.write("" + quantity);
                out.newLine();
            }

            // Add CASH position at the end
            out.write("CASH,1000000");
            out.newLine();
        }
    }

}

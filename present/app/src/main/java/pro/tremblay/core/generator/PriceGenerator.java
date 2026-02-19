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
import pro.tremblay.core.position.PositionReader;
import pro.tremblay.core.price.RandomPriceRepository;
import pro.tremblay.core.security.Security;
import pro.tremblay.core.position.SecurityPosition;
import pro.tremblay.core.security.SecurityService;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.util.List;

public class PriceGenerator {

    static void main() throws Exception {
        FileProvider fileProvider = new FileProvider();
        RandomPriceRepository priceRepository = new RandomPriceRepository();

        PositionReader positionReader = new PositionReader(new SecurityService(fileProvider.securityFile()));
        List<String> securities = positionReader.readFromFile(fileProvider.positionFile()).getSecurityPositions().stream()
            .map(SecurityPosition::security)
            .map(Security::symbol)
            .filter(symbol -> !symbol.equals("CASH"))
            .toList();

        try (BufferedWriter out = Files.newBufferedWriter(fileProvider.priceFile())) {
            for(String security : securities) {
                out.write(security);
                out.write(",");
                for (int i = 0; i < 365; i++) {
                    out.write(priceRepository.getPrice(security).value().toPlainString());
                    out.write(",");
                }
                out.newLine();
            }
        }
    }

}

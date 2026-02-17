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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PositionReader {

    private final SecurityService securityService;

    public PositionReader(SecurityService securityService) {
        this.securityService = securityService;
    }

    public Position readFromFile(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Securities file doesn't exist");
        }
        Position position = Position.position();
        try (Stream<String> lines = Files.lines(file)) {
            lines.map(line -> line.split(","))
                .map(tokens -> new Object() {
                    final Security security = securityService.findForTicker(tokens[0]);
                    final Quantity quantity = Quantity.qty(Long.parseLong(tokens[1]));
                })
                .forEach(tuple -> position.addSecurityPosition(tuple.security, tuple.quantity));
        }
        return position;
    }

}

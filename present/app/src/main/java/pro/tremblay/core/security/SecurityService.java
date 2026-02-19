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
package pro.tremblay.core.security;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityService {

    private final Path file;
    private final Supplier<Map<String, Security>> allSecurities = StableValue.supplier(this::loadSecurities);

    public SecurityService(Path file) {
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Securities file doesn't exist");
        }
        this.file = file;
    }

    public Collection<Security> allSecurities() {
        return allSecurities.get().values();
    }

    private Map<String, Security> loadSecurities() {
        return readFile(file, line -> {
                        String[] fields = line.split(",");
                        return new Security(
                            fields[0],
                            fields[1],
                            fields[2],
                            fields[3],
                            LocalDate.parse(fields[4])
                        );
                    });
    }

    public Security findForTicker(String ticker) {
        return allSecurities.get().get(ticker);
    }

    private Map<String, Security> readFile(Path file, Function<String, Security> mapper) {
        try (Stream<String> lines = Files.lines(file)){
            return lines
                .skip(1) // skip the header
                .map(mapper)
                .collect(Collectors.toMap(Security::symbol, Function.identity()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

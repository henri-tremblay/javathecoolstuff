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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SecurityService {

    private final Path file;
    private final Object mutex = new Object();
    private volatile Collection<Security> allSecurities;

    public SecurityService(Path file) {
        if (!file.toFile().exists()) {
            throw new IllegalArgumentException("Securities file doesn't exist");
        }
        this.file = file;
    }

    public Collection<Security> allSecurities() {
        if (allSecurities == null) {
            synchronized (mutex) {
                if (allSecurities == null) {
                    allSecurities = readFile(file, line -> {
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
            }
        }
        return allSecurities;
    }

    public void clear() {
        synchronized (mutex) {
            allSecurities = null;
        }
    }

    public Security findForTicker(String ticker) {
        return allSecurities().stream()
            .filter(security -> security.symbol().equals(ticker)) // yeah, O(n). I know
            .findAny()
            .orElse(null);
    }

    private Collection<Security> readFile(Path file, Function<String, Security> mapper) {
        try {
            return Files.lines(file)
                .parallel()
                .skip(1)
                .map(mapper)
                .collect(Collectors.toList());
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

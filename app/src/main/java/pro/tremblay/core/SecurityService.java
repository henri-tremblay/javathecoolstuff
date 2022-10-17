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
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SecurityService {

    private final Path file;
    private final Object mutex = new Object();
    private volatile Map<String, Security> allSecurities;

    public SecurityService(Path file) {
        if (!file.toFile().exists()) {
            throw new IllegalArgumentException("Securities file doesn't exist");
        }
        this.file = file;
    }

    public Collection<Security> allSecurities() {
        loadSecurities();
        return allSecurities.values();
    }

    private void loadSecurities() {
        if (allSecurities == null) {
            synchronized (mutex) {
                if (allSecurities == null) {
                    Function<String, String[]> tokenizer = line -> line.split(",");
                    Function<String[], Map.Entry<String, Security>> mapper = fields -> {
                        try {
                            return Map.entry(fields[0], new Security(
                                fields[0],
                                fields[1],
                                fields[2],
                                fields[3],
                                LocalDate.parse(fields[4])
                            ));
                        } catch (RuntimeException e) {
                            throw new RuntimeException("Failed to parse line " + Arrays.toString(fields), e);
                        }
                    };
                    allSecurities = readFile(file, tokenizer.andThen(mapper));
                }
            }
        }
    }

    public void clear() {
        synchronized (mutex) {
            allSecurities = null;
        }
    }

    public Security findForTicker(String ticker) {
        loadSecurities();
        return allSecurities.get(ticker);
    }

    private Map<String, Security> readFile(Path file, Function<String, Map.Entry<String, Security>> mapper) {
        try {
            return Files.lines(file)
                .parallel()
                .skip(1)
                .map(mapper)
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

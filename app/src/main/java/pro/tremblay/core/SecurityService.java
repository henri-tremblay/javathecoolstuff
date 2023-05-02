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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SecurityService {

    private final Path file;
    private final Object mutex = new Object();
    private volatile Map<String, Security> allSecurities;

    public SecurityService(Path file) {
        if (!Files.exists(file)) {
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
    }

    public void clear() {
        synchronized (mutex) {
            allSecurities = null;
        }
    }

    public Security findForTicker(String ticker) {
        loadSecurities();
        Security[] pointer = new Security[1];
        allSecurities.forEach((k, v) -> {
            if (k.equals(ticker)) {
                pointer[0] = v;
            }
        });
        return pointer[0];
    }

    private Map<String, Security> readFile(Path file, Function<String, Security> mapper) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), Charset.forName("UTF-8")));
        }
        catch(FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        try {
            List<Security> list = new ArrayList<>();
            String s = in.readLine(); // skip first line
            while((s = in.readLine()) != null) {
                Security security = mapper.apply(s);
                map.put(security.symbol(), security);
            }
            return list.stream().collect(Collectors.toMap(Security::symbol, Function.identity()));
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
        finally {
            try {
                in.close();
            }
            catch(IOException e) {
                // ignore silently
            }
        }
    }
}

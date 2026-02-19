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
package pro.tremblay.core.price;

import net.jcip.annotations.ThreadSafe;
import pro.tremblay.core.Amount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@ThreadSafe
public class HistoricalPriceRepository {

    private final ConcurrentMap<String, List<Amount>> priceMap = new ConcurrentHashMap<>();

    public HistoricalPriceRepository(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Price file doesn't exist");
        }
        try (Stream<String> lines = Files.lines(file)) {
            lines
                .map(line -> line.split(","))
                .forEach(tuple -> {
                    String ticker = tuple[0];
                    List<Amount> prices = Stream.of(tuple)
                        .skip(1) // skip the ticker
                        .map(Amount::new)
                        .toList();
                    priceMap.put(ticker, prices);
                });
        }
    }

    public Amount getPrice(String ticker, int index) {
        List<Amount> prices = priceMap.get(ticker);
        if (prices == null) {
            throw new IllegalArgumentException("Can't find index " + index + " for ticker " + ticker);
        }
        return prices.get(index);
    }
}

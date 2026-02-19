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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;

/**
 * Service returning security prices. This is actually a fake implementation using randomly generated prices.
 */
@ThreadSafe
public class RandomPriceRepository {

    private final ConcurrentMap<String, Double> prices = new ConcurrentHashMap<>();
    private final RandomGenerator random = RandomGenerator.getDefault();

    public Amount getPrice(String ticker) {
        return new Amount(prices.compute(ticker, (k, v) -> {
            // a new random price if the ticket is unknown and a price near the last one if known
            double price = v == null ?
                random.nextDouble(1, 100) :
                random.nextGaussian(v, 2);

            if (price < 0.1) {
                price = 0.1;
            }
            return price;
        }));
    }

}

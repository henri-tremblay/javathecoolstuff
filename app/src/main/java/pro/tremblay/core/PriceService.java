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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static pro.tremblay.core.Amount.amnt;

/**
 * Service returning security prices. This is actually a fake implementation using randomly generated prices.
 */
@ThreadSafe
public class PriceService {

    private final Map<String, Amount> prices = new HashMap<>();

    public PriceService(@Nonnull SecurityService securityService, @Nonnull Clock clock) {
        // Randomly generated price since the beginning of the year
        Random random = new Random();
        LocalDate now = LocalDate.now(clock);
        for (Security security : securityService.allSecurities()) {
            LocalDate start = now.withDayOfYear(1);
            Amount price = amnt(100 + random.nextInt(200));
            while(!start.isAfter(now)) {
                Amount tick = amnt(random.nextGaussian());
                prices.put(getKey(security, start), price.add(tick));
                start = start.plusDays(1);
            }
        }
    }

    private static String getKey(Security security, LocalDate date) {
        return date + "#" + security;
    }

    /**
     * Returns the price at a given date for a security. The implementation generates random prices to
     * simulate a real price source. During your refactoring, please keep the current price generation concept.
     *
     * @param date date on which we want the price
     * @param security security for which we want a price
     * @throws IllegalArgumentException if no price is found at this date
     * @return the price of the security at a given date
     */
    @Nonnull
    public Amount getPrice(@Nonnull LocalDate date, @Nonnull Security security) {
        Amount price = prices.get(getKey(security, date));
        if(price == null) {
            throw new IllegalArgumentException("No price found at " + date + " for " + security.symbol());
        }
        return price;
    }
}

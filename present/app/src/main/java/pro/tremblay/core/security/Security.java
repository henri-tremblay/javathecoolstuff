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

import net.jcip.annotations.ThreadSafe;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representation of a security.
 */
@ThreadSafe
public record Security(String symbol, String name, String exchange, String assetType, LocalDate ipoDate) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Security security &&  symbol.equals(security.symbol) && exchange.equals(security.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, exchange);
    }

}

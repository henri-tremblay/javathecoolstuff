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

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Enumeration listing useful available security. In real-life it would be a full-fledged java object but to keep
 * things simple, it's just an enum.
 */
@ThreadSafe
public class Security {

    private final String symbol;
    private final String name;
    private final String exchange;
    private final String assetType;
    private final LocalDate ipoDate;

    public Security(String symbol, String name, String exchange, String assetType, LocalDate ipoDate) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.assetType = assetType;
        this.ipoDate = ipoDate;
    }

    public String symbol() {
        return symbol;
    }

    public String name() {
        return name;
    }

    public String exchange() {
        return exchange;
    }

    public String assetType() {
        return assetType;
    }

    public LocalDate ipoDate() {
        return ipoDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return symbol.equals(security.symbol) && exchange.equals(security.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, name);
    }

    @Override
    public String toString() {
        return "Security{" +
            "symbol='" + symbol + '\'' +
            ", name='" + name + '\'' +
            ", exchange='" + exchange + '\'' +
            ", assetType='" + assetType + '\'' +
            ", ipoDate=" + ipoDate +
            '}';
    }
}

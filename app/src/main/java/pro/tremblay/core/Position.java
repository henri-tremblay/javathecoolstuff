/*
 * Copyright 2022-2025 the original author or authors.
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

import javax.annotation.concurrent.NotThreadSafe;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pro.tremblay.core.SecurityPosition.securityPosition;

/**
 * All positions (cash and security) of a user. There is only one cash position since we are trading in only one
 * currency.
 */
@NotThreadSafe
public class Position {

    private Amount cash = Amount.zero();
    private final Map<Security, Quantity> securityPositions = new HashMap<>();

    public static Position position() {
        return new Position();
    }

    private Position() {}

    public Position addSecurityPosition(Security security, Quantity quantity) {
        securityPositions.compute(security, (sec, currentQuantity) -> {
            if(currentQuantity == null) {
                return quantity.isZero() ? null : quantity;
            }
            Quantity sum = quantity.add(currentQuantity);
            return sum.isZero() ? null : sum;
        });
        return this;
    }

    public Quantity getSecurityPosition(Security security) {
        return securityPositions.getOrDefault(security, Quantity.zero());
    }

    public void addCash(Amount cash) {
        this.cash = this.cash.add(cash);
    }

    public Amount getCash() {
        return cash;
    }

    public Position cash(Amount cash) {
        this.cash = cash;
        return this;
    }

    public Position addSecurityPositions(SecurityPosition... securityPositions) {
        for (SecurityPosition sp : securityPositions) {
            // No need to add flat positions
            if (!sp.isFlat()) {
                this.securityPositions.put(sp.getSecurity(), sp.getQuantity());
            }
        }
        return this;
    }

    public Position copy() {
        Position position = position()
                .cash(getCash());
        position.securityPositions.putAll(securityPositions);
        return position;
    }

    public Amount securityPositionValue(PriceService priceService) {
        return securityPositions
            .entrySet()
            .stream()
            .map(entry -> priceService.getPrice(entry.getKey()).multiply(entry.getValue()))
            .reduce(Amount.zero(), Amount::add);
    }

    public Collection<SecurityPosition> getSecurityPositions() {
        return securityPositions
            .entrySet()
            .stream()
            .map(entry -> securityPosition(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Position{" +
            "cash=" + cash +
            ", securityPositions=" + sortedSecurityPositions() +
            '}';
    }

    private List<Map.Entry<String, Quantity>> sortedSecurityPositions() {
        return securityPositions.entrySet().stream()
            .map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry.getKey().symbol(), entry.getValue()))
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toList());
    }

    public void revert(List<Transaction> transactions) {
        transactions.forEach(t -> t.revert(this));
    }
}

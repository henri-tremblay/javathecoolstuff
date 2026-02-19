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
package pro.tremblay.core.transaction;

import pro.tremblay.core.Amount;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.security.Security;
import pro.tremblay.core.security.SecurityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionReader {

    private final SecurityService securityService;

    public TransactionReader(SecurityService securityService) {
        this.securityService = securityService;
    }

    public List<Transaction> read(Path transactionFile) {
        try (Stream<String> lines = Files.lines(transactionFile)) {
            return lines
                .map(line -> line.split(",", 5))
                .map(tokens -> {
                    TransactionType type = TransactionType.valueOf(tokens[0]);
                    LocalDate date = LocalDate.parse(tokens[1]);
                    Amount cash = new Amount(tokens[2]);
                    Quantity quantity = tokens[3].isEmpty() ? null : new Quantity(tokens[3]);
                    Security security = tokens[4].isEmpty() ? null : securityService.findForTicker(tokens[4]);
                    return switch (type) {
                        case BUY -> Transaction.buy(date, cash, Objects.requireNonNull(security), Objects.requireNonNull(quantity));
                        case SELL -> Transaction.sell(date, cash, Objects.requireNonNull(security), Objects.requireNonNull(quantity));
                        case DEPOSIT -> Transaction.deposit(date, cash);
                        case WITHDRAWAL -> Transaction.withdrawal(date, cash);
                    };
                })
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

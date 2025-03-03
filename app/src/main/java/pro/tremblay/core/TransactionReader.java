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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
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
                .map(tokens -> Transaction.transaction()
                    .type(TransactionType.valueOf(tokens[0]))
                    .date(LocalDate.parse(tokens[1]))
                    .cash(Amount.amnt(tokens[2]))
                    .quantity(tokens[3].isEmpty() ? null : Quantity.qty(tokens[3]))
                    .security(tokens[4].isEmpty() ? null : securityService.findForTicker(tokens[4]))
                )
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

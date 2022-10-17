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

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class TransactionGenerator {

    public static void main(String[] args) throws Exception {
        RandomGenerator random = RandomGenerator.getDefault();
        SecurityService securityService = new SecurityService(Paths.get("listing_status.csv"));

        List<Security> securities = new ArrayList<>(securityService.allSecurities());
        LocalDate now = LocalDate.now();

        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("transaction.csv"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {

            TransactionType[] transactionTypes = TransactionType.values();
            for (int i = 0; i < 1_000_000; i++) {
                Transaction transaction = Transaction.transaction()
                    .type(transactionTypes[random.nextInt(transactionTypes.length)])
                    .date(now.minusDays(random.nextInt(365)))
                    .cash(Amount.amnt(random.nextInt(1_000,  10_000))); // some transactions will be before this year

                if (transaction.getType().hasQuantity()) {
                    transaction.quantity(Quantity.qty(random.nextInt(10, 10_000)));
                    transaction.security(securities.get(random.nextInt(securities.size())));
                }

                out.write(transaction.getType().name());
                out.write(",");
                out.write(transaction.getDate().toString());
                out.write(",");
                out.write(transaction.getCash().value().toPlainString());
                out.write(",");
                out.write(transaction.getQuantity() == null ? "" : transaction.getQuantity().value().toPlainString());
                out.write(",");
                out.write(transaction.getSecurity() == null ? "" : transaction.getSecurity().symbol());
                out.newLine();
            }
        }
    }
}

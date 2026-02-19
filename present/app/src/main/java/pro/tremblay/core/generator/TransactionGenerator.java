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
package pro.tremblay.core.generator;

import pro.tremblay.core.Amount;
import pro.tremblay.core.transaction.Buy;
import pro.tremblay.core.transaction.Deposit;
import pro.tremblay.core.FileProvider;
import pro.tremblay.core.price.HistoricalPriceRepository;
import pro.tremblay.core.position.Position;
import pro.tremblay.core.position.PositionReader;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.security.Security;
import pro.tremblay.core.security.SecurityService;
import pro.tremblay.core.transaction.Sell;
import pro.tremblay.core.transaction.Transaction;
import pro.tremblay.core.transaction.Withdrawal;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.random.RandomGenerator;

public class TransactionGenerator {

    private static final int TRANSACTION_COUNT = 1_000_000;

    static void main() throws Exception {
        RandomGenerator random = RandomGenerator.getDefault();
        FileProvider fileProvider = new FileProvider();

        SecurityService securityService = new SecurityService(fileProvider.securityFile());
        HistoricalPriceRepository priceRepository = new HistoricalPriceRepository(fileProvider.priceFile());
        PositionReader positionReader = new PositionReader(securityService);
        Position position = positionReader.readFromFile(fileProvider.positionFile());
        List<String> securities = position.getSecurityPositions().stream()
            .map(sp -> sp.security().symbol())
            .filter(symbol -> !symbol.equals("CASH"))
            .toList();

        LocalDate yearAgo = LocalDate.now().minusYears(1);
        @SuppressWarnings("unchecked")
        Class<Transaction>[] classes = (Class<Transaction>[]) Transaction.class.getPermittedSubclasses();

        try (BufferedWriter out = Files.newBufferedWriter(fileProvider.transactionFile())) {
            for (int i = 0; i < TRANSACTION_COUNT; i++) {
                int dateIndex = random.nextInt(365);

                Class<Transaction> clazz = classes[random.nextInt(classes.length)];
                LocalDate date = yearAgo.plusDays(dateIndex);

                Quantity quantity = new Quantity(random.nextInt(10, 10_000));
                String symbol = securities.get(random.nextInt(securities.size()));
                Amount price = priceRepository.getPrice(symbol, dateIndex);
                Amount cash = price.multiply(quantity);

                Transaction transaction = switch (clazz) {
                    case Class<Transaction> c when c.equals(Buy.class) -> new Buy(date, cash, securityService.findForTicker(symbol), quantity);
                    case Class<Transaction> c when c.equals(Sell.class) ->  new Sell(date, cash, securityService.findForTicker(symbol), quantity);
                    case Class<Transaction> c when c.equals(Deposit.class) -> new Deposit(date, new Amount(random.nextLong(1, 10) * 10_000L));
                    case Class<Transaction> c when c.equals(Withdrawal.class)  -> new Withdrawal(date, new Amount(random.nextLong(1, 10) * 10_000L));
                    default -> throw new IllegalStateException("Unexpected value: " + clazz);
                };

                out.write(transaction.type().toString());
                out.write(",");
                out.write(transaction.date().toString());
                out.write(",");
                out.write(transaction.cash().value().toPlainString());
                out.write(",");
                quantity = transaction.quantity();
                if (quantity != null) {
                    out.write(quantity.value().toPlainString());
                }
                out.write(",");
                Security security = transaction.security();
                if (security != null) {
                    out.write(security.symbol());
                }
                out.newLine();
            }
        }
    }
}



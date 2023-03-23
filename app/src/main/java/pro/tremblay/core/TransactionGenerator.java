package pro.tremblay.core;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionGenerator {

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        SecurityService securityService = new SecurityService(Paths.get("securities.csv"));

        List<Security> securities = new ArrayList<>(securityService.allSecurities());
        LocalDate now = LocalDate.now();
        // Let's add CASH
        securities.add(new Security("CASH", "Cash position", "FOREX", "FX", LocalDate.now()));

        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("transactions.csv"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            for (int i = 0; i < 1_000_000; i++) {
                Transaction transaction = Transaction.transaction()
                    .type(TransactionType.values()[random.nextInt(TransactionType.values().length)])
                    .date(now.minusDays(random.nextInt(365)))
                    .cash(Amount.amnt(1_000 + random.nextInt(9_000))); // some transactions will be before this year

                if (transaction.getType().hasQuantity()) {
                    transaction.quantity(Quantity.qty(10 + random.nextInt(9_990)));
                    transaction.security(securities.get(random.nextInt(securities.size())));
                }

                out.write(transaction.getType().name());
                Security security = securities.get(random.nextInt(securities.size()));
                long quantity = 1 + random.nextInt(999);
                out.write(security.symbol());
                out.write(",");
                out.write(transaction.getDate().toString());
                out.write(",");
                out.write(transaction.getCash().value().toPlainString());
                out.write(",");
                out.write(transaction.getQuantity() == null ? "" : transaction.getQuantity().value().toPlainString());
                out.write(",");
                out.write(transaction.getSecurity() == null ? "" : transaction.getSecurity().symbol());
                out.write("" + quantity);
                out.newLine();
            }
        }
    }
}



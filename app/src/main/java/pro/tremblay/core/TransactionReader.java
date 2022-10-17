package pro.tremblay.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionReader {

    private final SecurityService securityService;

    public TransactionReader(SecurityService securityService) {
        this.securityService = securityService;
    }

    public List<Transaction> read(Path transactionFile) {
        try {
            return Files.lines(transactionFile)
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

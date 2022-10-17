package pro.tremblay.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

public class Main {

    private static final String TEMPLATE = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <title>Return on investment</title>\n" +
        "</head>\n" +
        "<body>\n" +
        "Your current return on investment as of ${now} is: ${roi}\n" +
        "</body>\n" +
        "</html>";

    public static void main(String[] args) throws IOException {
        Preferences preferences = new Preferences();
        preferences.put(ReportingService.LENGTH_OF_YEAR, "365");

        Clock clock = Clock.systemUTC();
        SecurityService securityService = new SecurityService(Paths.get("listing_status.csv"));
        PriceService priceService = new PriceService(securityService, clock);
        ReportingService reportingService = new ReportingService(preferences, clock, priceService);

        TransactionReader transactionReader = new TransactionReader(securityService);
        Collection<Transaction> transactions = transactionReader.read(Paths.get("transaction.csv"));

        Position current = Position.position().cash(Amount.amnt(10_000));

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);
        String result = TEMPLATE
            .replace("${roi}", roi.toString())
            .replace("${now}", LocalDateTime.now(clock).toString());
        Files.write(Paths.get("result.html"), result.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}

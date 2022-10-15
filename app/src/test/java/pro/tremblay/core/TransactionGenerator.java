package pro.tremblay.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class TransactionGenerator {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("listing_status.csv");
        List<Security> securities = Files.lines(path)
            .map(s -> s.split(","))
            .map(tuple -> new Security(tuple[0], tuple[1], tuple[2], tuple[3], LocalDate.parse(tuple[4])))
            .toList();
        System.out.println(securities);
    }
}

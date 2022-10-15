package pro.tremblay.core;

import java.time.LocalDate;
import java.util.List;

public class SecuritiesForTest {

    public static final Security AAPL = new Security("AAPL", "Apple Inc", "NASDAQ",
        "Stock", LocalDate.of(1980, 12, 12));
    public static final Security GOOGL = new Security("GOOGL", "Alphabet Inc - Class A", "NASDAQ",
        "Stock", LocalDate.of(2004, 8, 19));
    public static final Security IBM = new Security("IBM", "International Business Machines Corp", "NYSE",
        "Stock" ,LocalDate.of(1962, 1, 2));
    public static final Security INTC = new Security("INTC", "Intel Corp", "NASDAQ",
        "Stock" ,LocalDate.of(1980, 3, 17));

    public static final List<Security> SECURITIES = List.of(AAPL, GOOGL, IBM, INTC);
}

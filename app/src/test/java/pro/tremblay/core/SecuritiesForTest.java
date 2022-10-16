/*
 * Copyright 2022-2022 the original author or authors.
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

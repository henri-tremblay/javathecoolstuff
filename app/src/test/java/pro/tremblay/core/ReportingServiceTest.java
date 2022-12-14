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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static pro.tremblay.core.Amount.amnt;
import static pro.tremblay.core.Assertions.assertThat;
import static pro.tremblay.core.Position.position;
import static pro.tremblay.core.Quantity.qty;
import static pro.tremblay.core.Transaction.transaction;

class ReportingServiceTest {

    private final Preferences preferences = mock(Preferences.class);
    private final PriceService priceService = mock(PriceService.class);

    private final Position current = position();

    private final LocalDate today = LocalDate.ofYearDay(2019, 200);
    private final Clock clock = Clock.fixed(today.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    private final LocalDate hundredDaysAgo = today.minusDays(100);

    private final ReportingService reportingService = new ReportingService(preferences, clock, priceService);

    @BeforeEach
    void setup() {
        expect(preferences.getInteger(ReportingService.LENGTH_OF_YEAR)).andStubReturn(360);
        replay(preferences);
    }

    @Test
    void calculateReturnOnInvestmentYTD_noTransactionAndPosition() {
        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, Collections.emptyList());
        assertThat(roi).isEqualTo("0.00");
    }

    @Test
    void calculateReturnOnInvestmentYTD_cashAdded() {
        current.cash(amnt(200));

        Collection<Transaction> transactions = Collections.singleton(
                transaction()
                        .cash(amnt(100))
                        .type(TransactionType.DEPOSIT)
                        .date(hundredDaysAgo));

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);
        assertThat(roi).isEqualTo("180.00"); // (200$ - 100$) / 100$ * 100% * 360 days / 200 days
    }

    @Test
    void calculateReturnOnInvestmentYTD_secBought() {
        current.addSecurityPosition(SecuritiesForTest.GOOGL, qty(50));

        Collection<Transaction> transactions = Collections.singleton(
            transaction()
                .security(SecuritiesForTest.GOOGL)
                .quantity(qty(50))
                .cash(amnt(100))
                .type(TransactionType.BUY)
                .date(hundredDaysAgo));

        expect(priceService.getPrice(today, SecuritiesForTest.GOOGL)).andStubReturn(amnt(2));
        replay(priceService);

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Cash value now: 0, Sec value now: 50 x 2 = 100, Total: 100
        // Cash value initial: 100, Sec value now: 0, Total: 100
        // (100$ - 100$) / 100$ * 100% * 360 days / 200 days
        assertThat(roi).isEqualTo("0.00");
    }

    @Test
    void testThreeArgsConstructor() {
        new ReportingService(preferences, clock, priceService);
    }

    @Test
    void calculateReturnOnInvestmentYTD_twoCashTransactions() {
        current.cash(amnt(200));

        Collection<Transaction> transactions = Arrays.asList(
            transaction()
                .cash(amnt(100))
                .type(TransactionType.DEPOSIT)
                .date(hundredDaysAgo),
            transaction()
                .cash(amnt(50))
                .type(TransactionType.DEPOSIT)
                .date(hundredDaysAgo.minusDays(1)));

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Current cash value = 200$, Current security value = 0$
        // Initial cash value = 50$, Initial cash value = 0$
        // Year length = 360
        // (200$ - 50$) / 50$ * 100% * 360 days / 200 days
        assertThat(roi).isEqualTo("540.00");
    }

    @Test
    void calculateReturnOnInvestmentYTD_twoCashTransactionsOnTheSameDay() {
        current.cash(amnt(200));

        Collection<Transaction> transactions = Arrays.asList(
            transaction()
                .cash(amnt(100))
                .type(TransactionType.DEPOSIT)
                .date(hundredDaysAgo),
            transaction()
                .cash(amnt(50))
                .type(TransactionType.DEPOSIT)
                .date(hundredDaysAgo));

        Percentage roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Current cash value = 200$, Current security value = 0$
        // Initial cash value = 50$, Initial cash value = 0$
        // Year length = 360
        // (200$ - 50$) / 50$ * 100% * 360 days / 200 days
        assertThat(roi).isEqualTo("540.00");
    }
}

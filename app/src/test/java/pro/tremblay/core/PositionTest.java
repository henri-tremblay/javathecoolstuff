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

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static pro.tremblay.core.Amount.amnt;
import static pro.tremblay.core.Assertions.assertThat;
import static pro.tremblay.core.Position.position;
import static pro.tremblay.core.Quantity.qty;

class PositionTest {

    private Position position = position()
        .cash(amnt(10))
        .addSecurityPosition(SecuritiesForTest.GOOGL, qty(22))
        .addSecurityPosition(SecuritiesForTest.AAPL, qty(11));

    @Test
    void copy() {
        Position actual = position.copy();
        assertThat(actual).isNotSameAs(position);
        assertThat(actual.getCash()).isEqualTo(position.getCash());
        assertThat(actual.getSecurityPositions())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(position.getSecurityPositions());
    }

    @Test
    void addCash() {
        position.addCash(amnt(200));
        assertThat(position.getCash()).isEqualTo("210.00");
    }

    @Test
    void addSecurity_existing() {
        position.addSecurityPosition(SecuritiesForTest.GOOGL, qty(30));
        Quantity securityPosition = position.getSecurityPosition(SecuritiesForTest.GOOGL);
        assertThat(securityPosition).isEqualTo("52");
    }

    @Test
    void addSecurity_new() {
        position.addSecurityPosition(SecuritiesForTest.IBM, qty(30));
        Quantity securityPosition = position.getSecurityPosition(SecuritiesForTest.IBM);
        assertThat(securityPosition).isEqualTo("30");
    }

    @Test
    void securityPositionValue() {
        LocalDate now = LocalDate.now();

        RealPriceService priceService = mock(RealPriceService.class);
        expect(priceService.getPrice(SecuritiesForTest.GOOGL)).andStubReturn(amnt(10));
        expect(priceService.getPrice(SecuritiesForTest.AAPL)).andStubReturn(amnt(5));
        replay(priceService);

        Amount result = position.securityPositionValue(priceService);
        assertThat(result).isEqualTo("275.00"); // 22 * 10 + 11 * 5
    }

    @Test
    void securityPositionValue_noPriceNeededForFlatPosition() {
        LocalDate now = LocalDate.now();

        position = position()
            .addSecurityPosition(SecuritiesForTest.AAPL, qty(0));

        RealPriceService priceService = mock(RealPriceService.class);
        replay(priceService);

        Amount result = position.securityPositionValue(priceService);
        assertThat(result).isEqualTo(Amount.zero());

        verify(priceService);
    }

    @Test
    void getSecurityPosition() {
        assertThat(position.getSecurityPosition(SecuritiesForTest.GOOGL)).isEqualTo("22");
        assertThat(position.getSecurityPosition(SecuritiesForTest.IBM).isZero()).isTrue();
    }

    @Test
    void testToString() {
        assertThat(position.toString()).isEqualTo("Position{cash=10.00$, securityPositions=[AAPL=11, GOOGL=22]}");
    }

    @Test
    void addSecurityPositions() {
        position.addSecurityPositions(
            SecurityPosition.securityPosition(SecuritiesForTest.IBM, qty(100)),
            SecurityPosition.securityPosition(SecuritiesForTest.INTC, qty(200)));
        assertThat(position.getSecurityPosition(SecuritiesForTest.IBM)).isEqualTo("100");
        assertThat(position.getSecurityPosition(SecuritiesForTest.INTC)).isEqualTo("200");
        assertThat(position.getSecurityPosition(SecuritiesForTest.GOOGL)).isEqualTo("22");
        assertThat(position.getSecurityPosition(SecuritiesForTest.AAPL)).isEqualTo("11");
    }
}

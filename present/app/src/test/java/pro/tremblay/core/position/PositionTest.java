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
package pro.tremblay.core.position;

import org.junit.jupiter.api.Test;
import pro.tremblay.core.Amount;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.SecuritiesForTest;
import pro.tremblay.core.price.PriceService;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static pro.tremblay.core.Assertions.assertThat;
import static pro.tremblay.core.position.Position.position;

class PositionTest {

    private Position position = position()
        .cash(new Amount(10))
        .addSecurityPosition(SecuritiesForTest.GOOGL, new Quantity(22))
        .addSecurityPosition(SecuritiesForTest.AAPL, new Quantity(11));

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
        position.addCash(new Amount(200));
        assertThat(position.getCash()).isEqualTo("210.00");
    }

    @Test
    void addSecurity_existing() {
        position.addSecurityPosition(SecuritiesForTest.GOOGL, new Quantity(30));
        Quantity securityPosition = position.getSecurityPosition(SecuritiesForTest.GOOGL);
        assertThat(securityPosition).isEqualTo("52");
    }

    @Test
    void addSecurity_new() {
        position.addSecurityPosition(SecuritiesForTest.IBM, new Quantity(30));
        Quantity securityPosition = position.getSecurityPosition(SecuritiesForTest.IBM);
        assertThat(securityPosition).isEqualTo("30");
    }

    @Test
    void securityPositionValue() {
        PriceService priceService = mock(PriceService.class);
        expect(priceService.getPrice(SecuritiesForTest.GOOGL)).andStubReturn(new Amount(10));
        expect(priceService.getPrice(SecuritiesForTest.AAPL)).andStubReturn(new Amount(5));
        replay(priceService);

        Amount result = position.securityPositionValue(priceService);
        assertThat(result).isEqualTo("275.00"); // 22 * 10 + 11 * 5
    }

    @Test
    void securityPositionValue_noPriceNeededForFlatPosition() {
        position = position()
            .addSecurityPosition(SecuritiesForTest.AAPL, new Quantity(0));

        PriceService priceService = mock(PriceService.class);
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
            new SecurityPosition(SecuritiesForTest.IBM, new Quantity(100)),
            new SecurityPosition(SecuritiesForTest.INTC, new Quantity(200)));
        assertThat(position.getSecurityPosition(SecuritiesForTest.IBM)).isEqualTo("100");
        assertThat(position.getSecurityPosition(SecuritiesForTest.INTC)).isEqualTo("200");
        assertThat(position.getSecurityPosition(SecuritiesForTest.GOOGL)).isEqualTo("22");
        assertThat(position.getSecurityPosition(SecuritiesForTest.AAPL)).isEqualTo("11");
    }
}

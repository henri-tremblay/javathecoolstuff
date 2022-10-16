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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

class PriceServiceTest {

    private Clock clock = Clock.systemDefaultZone();

    private SecurityService securityService = mock(SecurityService.class);
    private PriceService priceService;

    @BeforeEach
    public void before() {
        expect(securityService.allSecurities()).andStubReturn(SecuritiesForTest.SECURITIES);
        replay(securityService);
        priceService = new PriceService(securityService, clock);
    }

    @Test
    void getPrice() {
        LocalDate now = LocalDate.now(clock);
        LocalDate date = now.withDayOfYear(1);
        while(!date.isAfter(now)) {
            assertThat(priceService.getPrice(date, SecuritiesForTest.GOOGL)).isNotNull();
            date = date.plusDays(1);
        }
    }

    @Test
    void getPrice_noPrice() {
        LocalDate yearsAgo = LocalDate.of(1977, 1, 1);
        assertThatIllegalArgumentException()
            .isThrownBy(() -> priceService.getPrice(yearsAgo, SecuritiesForTest.IBM))
            .withMessage("No price found at 1977-01-01 for IBM");
    }
}

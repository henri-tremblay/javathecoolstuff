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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.tremblay.core.SecurityPosition.securityPosition;

class SecurityPositionTest {

    private SecurityPosition securityPosition = securityPosition(SecuritiesForTest.GOOGL, Quantity.ten());

    @Test
    void isFlat_notFlat() {
        assertThat(securityPosition.isFlat()).isFalse();
    }

    @Test
    void isFlat_flat() {
        securityPosition = securityPosition(SecuritiesForTest.GOOGL, Quantity.zero());
        assertThat(securityPosition.isFlat()).isTrue();
    }

    @Test
    void testToString() {
        assertThat(securityPosition.toString()).isEqualTo("SecurityPosition{security=Security{symbol='GOOGL', name='Alphabet Inc - Class A', exchange='NASDAQ', assetType='Stock', ipoDate=2004-08-19}, quantity=10}");
    }

    @Test
    void testHashCode() {
        assertThat(securityPosition.hashCode()).isNotEqualTo(0);
    }

    @Test
    void testEquals() {
        assertThat(securityPosition).isNotEqualTo(null);
        assertThat(securityPosition).isEqualTo(securityPosition);

        SecurityPosition newSecurityPosition = securityPosition(SecuritiesForTest.GOOGL, Quantity.ten());
        assertThat(securityPosition).isEqualTo(newSecurityPosition);
        newSecurityPosition = securityPosition(SecuritiesForTest.GOOGL, Quantity.zero());
        assertThat(securityPosition).isNotEqualTo(newSecurityPosition);
        newSecurityPosition = securityPosition(SecuritiesForTest.AAPL, Quantity.ten());
        assertThat(securityPosition).isNotEqualTo(newSecurityPosition);
    }
}

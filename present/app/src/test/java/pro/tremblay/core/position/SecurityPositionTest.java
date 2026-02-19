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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.SecuritiesForTest;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityPositionTest {

    private SecurityPosition securityPosition = new SecurityPosition(SecuritiesForTest.GOOGL, Quantity.ten());

    @Test
    void isFlat_notFlat() {
        assertThat(securityPosition.isFlat()).isFalse();
    }

    @Test
    void isFlat_flat() {
        securityPosition = new SecurityPosition(SecuritiesForTest.GOOGL, Quantity.zero());
        assertThat(securityPosition.isFlat()).isTrue();
    }

    @Test
    void equalsHashCode() {
        EqualsVerifier.simple().forClass(SecurityPosition.class).verify();
    }

}

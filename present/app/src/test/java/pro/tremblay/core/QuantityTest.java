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
package pro.tremblay.core;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static pro.tremblay.core.Assertions.assertThat;

class QuantityTest {

    @Test
    void quantityInteger() {
        assertThat(new Quantity(12L)).isEqualTo("12");
    }

    @Test
    void quantityBigDecimal() {
        assertThat(new Quantity(BigDecimal.valueOf(12))).isEqualTo("12");
    }

    @Test
    void quantityString() {
        assertThat(new Quantity("12")).isEqualTo("12");
    }

    @Test
    void testToString() {
        assertThat(new Quantity(12).toString()).isEqualTo("12");
    }

    @Test
    void equalsHashcode() {
        EqualsVerifier.forClass(Quantity.class)
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
}

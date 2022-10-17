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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static pro.tremblay.core.Assertions.assertThat;
import static pro.tremblay.core.Quantity.qty;

class QuantityTest {

    @Test
    void quantityInteger() {
        assertThat(qty(12L)).isEqualTo("12");
    }

    @Test
    void quantityBigDecimal() {
        assertThat(qty(BigDecimal.valueOf(12))).isEqualTo("12");
    }

    @Test
    void quantityString() {
        assertThat(qty("12")).isEqualTo("12");
    }

    @Test
    void testToString() {
        assertThat(qty(12).toString()).isEqualTo("12");
    }

    @Test
    void testEquals() {
        assertThat(qty("12").equals(qty("12"))).isTrue();
        assertThat(qty("12").equals(qty("13"))).isFalse();
    }

    @Test
    void testHashCode() {
        assertThat(qty("12.4").hashCode()).isEqualTo(qty("12.4").hashCode());
    }
}

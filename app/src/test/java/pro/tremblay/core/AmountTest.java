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

import java.math.BigDecimal;

import static pro.tremblay.core.Amount.amnt;
import static pro.tremblay.core.Assertions.assertThat;

class AmountTest {

    @Test
    void amountDouble() {
        assertThat(amnt(12.1)).isEqualTo("12.10");
    }

    @Test
    void amountInteger() {
        assertThat(amnt(12L)).isEqualTo("12.00");
    }

    @Test
    void amountBigDecimal() {
        assertThat(amnt(BigDecimal.valueOf(12))).isEqualTo("12.00");
    }

    @Test
    void testToString() {
        assertThat(amnt("1.2").toString()).isEqualTo("1.20$");
    }

    @Test
    void testEquals() {
        assertThat(amnt("12.4").equals(amnt("12.4"))).isTrue();
        assertThat(amnt("12.4").equals(amnt("12.5"))).isFalse();
        assertThat(amnt("12.4").equals(null)).isFalse();
    }

    @Test
    void testHashCode() {
        assertThat(amnt("12.4").hashCode()).isEqualTo(amnt("12.4").hashCode());
    }
}

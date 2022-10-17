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

import java.math.BigDecimal;

import static pro.tremblay.core.Assertions.assertThat;

class NumericTest {

    Amount a1 = n("1.2");
    Amount a2 = n("1.3");

    private Amount n(String s) {
        return new Amount(new BigDecimal(s));
    }

    @Test
    void isZero() {
        assertThat(n("0.0").isZero()).isTrue();
        assertThat(n("0.1").isZero()).isFalse();
    }

    @Test
    void toBigDecimal() {
        assertThat(a1).isEqualTo("1.20");
    }

    @Test
    void add() {
        assertThat(a1.add(a2)).isEqualTo("2.50");
    }

    @Test
    void subtract() {
        assertThat(a2.subtract(a1)).isEqualTo("0.10");
    }

    @Test
    void negate() {
        assertThat(a1.negate()).isEqualTo("-1.20");
    }

    @Test
    void scale() {
        assertThat(a1.scale(100, 200)).isEqualTo("2.40");
    }

    @Test
    void setScale() {
        assertThat(new Amount(new BigDecimal("1.234"))).isEqualTo("1.23");
        assertThat(new Amount(new BigDecimal("1.255"))).isEqualTo("1.26");
    }
}

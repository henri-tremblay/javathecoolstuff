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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static pro.tremblay.core.Assertions.assertThat;

class NumericTest {

    private static class ANumeric implements Numeric<ANumeric> {

        private final BigDecimal value;

        public ANumeric(BigDecimal value) {
            this.value = setScale(value);
        }

        @Override
        public BigDecimal value() {
            return value;
        }

        @Override
        public ANumeric fromValue(BigDecimal newValue) {
            return new ANumeric(newValue);
        }

        @Override
        public int precision() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ANumeric aNumeric = (ANumeric) o;
            return value.equals(aNumeric.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value.toPlainString();
        }
    }

    ANumeric a1 = n("1.2");
    ANumeric a2 = n("1.3");

    private ANumeric n(String s) {
        return new ANumeric(new BigDecimal(s));
    }

    @Test
    void isZero() {
        assertThat(n("0.0").isZero()).isTrue();
        assertThat(n("0.1").isZero()).isFalse();
    }

    @Test
    void toBigDecimal() {
        assertThat(a1).isEqualTo("1.2");
    }

    @Test
    void add() {
        assertThat(a1.add(a2)).isEqualTo("2.5");
    }

    @Test
    void subtract() {
        assertThat(a2.subtract(a1)).isEqualTo("0.1");
    }

    @Test
    void negate() {
        assertThat(a1.negate()).isEqualTo("-1.2");
    }

    @Test
    void scale() {
        assertThat(a1.scale(100, 200)).isEqualTo("2.4");
    }

    @Test
    void setScale() {
        assertThat(new ANumeric(new BigDecimal("1.234"))).isEqualTo("1.2");
        assertThat(new ANumeric(new BigDecimal("1.254"))).isEqualTo("1.3");
    }
}

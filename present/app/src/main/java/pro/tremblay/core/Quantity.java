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

import java.math.BigDecimal;
import java.util.Objects;

public record Quantity(BigDecimal value) implements Numeric<Quantity> {

    private static final Quantity ZERO = new Quantity(BigDecimal.ZERO);
    private static final Quantity TEN = new Quantity(BigDecimal.TEN);

    public static Quantity zero() {
        return ZERO;
    }

    public static Quantity ten() {
        return TEN;
    }

    public Quantity(BigDecimal value) {
        this.value = setScale(Objects.requireNonNull(value));
    }

    public Quantity(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Quantity(String value) {
        this(new BigDecimal(Objects.requireNonNull(value)));
    }

    @Override
    public Quantity fromValue(BigDecimal newValue) {
        return new Quantity(newValue);
    }

    public int precision() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

}

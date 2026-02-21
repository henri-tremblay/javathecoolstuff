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

public record Amount(BigDecimal value) implements Numeric<Amount> {

    private static final Amount ZERO = new Amount(BigDecimal.ZERO);

    public static Amount zero() {
        return ZERO;
    }

    public Amount(BigDecimal value) {
        this.value = setScale(Objects.requireNonNull(value));
    }

    public Amount(double value) {
        this(BigDecimal.valueOf(value));
    }

    public Amount(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Amount(String value) {
        BigDecimal convertedValue = new BigDecimal(Objects.requireNonNull(value));
        this(convertedValue);
    }

    @Override
    public Amount fromValue(BigDecimal newValue) {
        return new Amount(newValue);
    }

    @Override
    public int precision() {
        // To simplify, we consider everything is in the same currency so all amounts have a precision of 2
        return 2;
    }

    public Amount multiply(Quantity quantity) {
        return new Amount(value.multiply(quantity.value()));
    }

    @Override
    public String toString() {
        return value.toPlainString() + "$";
    }

}

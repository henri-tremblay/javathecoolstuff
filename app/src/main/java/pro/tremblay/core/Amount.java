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

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

public final class Amount implements Numeric<Amount> {

    private static final Amount ZERO = new Amount(BigDecimal.ZERO);

    private final BigDecimal value;

    public static Amount zero() {
        return ZERO;
    }

    public static Amount amnt(double value) {
        return new Amount(BigDecimal.valueOf(value));
    }

    public static Amount amnt(long value) {
        return new Amount(BigDecimal.valueOf(value));
    }

    public static Amount amnt(@Nonnull BigDecimal value) {
        return new Amount(Objects.requireNonNull(value));
    }

    public static Amount amnt(@Nonnull String value) {
        return new Amount(new BigDecimal(Objects.requireNonNull(value)));
    }

    private Amount(@Nonnull BigDecimal value) {
        this.value = setScale(value);
    }

    @Nonnull
    @Override
    public BigDecimal value() {
        return value;
    }

    @Override
    public @Nonnull Amount fromValue(@Nonnull BigDecimal newValue) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Amount)) {
            return false;
        }
        Amount a = (Amount) o;
        return value.equals(a.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

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
import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.Objects;

@ThreadSafe
public final class Quantity implements Numeric<Quantity> {

    private static final Quantity ZERO = new Quantity(BigDecimal.ZERO);
    private static final Quantity TEN = qty(10);

    private final BigDecimal value;

    public static Quantity zero() {
        return ZERO;
    }

    public static Quantity ten() {
        return TEN;
    }

    public static Quantity qty(long value) {
        return new Quantity(BigDecimal.valueOf(value));
    }

    public static Quantity qty(@Nonnull BigDecimal value) {
        return new Quantity(Objects.requireNonNull(value));
    }

    private Quantity(@Nonnull BigDecimal value) {
        this.value = setScale(value);
    }

    public static Quantity qty(@Nonnull String value) {
        return new Quantity(new BigDecimal(Objects.requireNonNull(value)));
    }

    @Nonnull
    @Override
    public Quantity fromValue(@Nonnull BigDecimal newValue) {
        return new Quantity(newValue);
    }

    public int precision() {
        return 0;
    }

    @Nonnull
    @Override
    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quantity)) {
            return false;
        }
        Quantity a = (Quantity) o;
        return value.equals(a.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

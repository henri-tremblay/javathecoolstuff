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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@ThreadSafe
public final class Percentage implements Numeric<Percentage> {

    private static final Percentage HUNDRED = pct("100");
    private static final Percentage ZERO = pct("0");

    private final BigDecimal value;

    public static Percentage hundred() {
        return HUNDRED;
    }

    public static Percentage zero() {
        return ZERO;
    }

    public static Percentage pct(long value) {
        return new Percentage(BigDecimal.valueOf(value));
    }

    public static Percentage pct(@Nonnull String value) {
        return new Percentage(new BigDecimal(Objects.requireNonNull(value)));
    }

    public static Percentage pct(@Nonnull BigDecimal value) {
        return new Percentage(Objects.requireNonNull(value));
    }

    private Percentage(@Nonnull BigDecimal value) {
        this.value = setScale(value);
    }

    @Nonnull
    @Override
    public Percentage fromValue(@Nonnull BigDecimal newValue) {
        return new Percentage(newValue);
    }

    @Override
    public int precision() {
        return 2;
    }

    @Override
    public String toString() {
        return value.toPlainString() + "%";
    }

    @Nonnull
    @Override
    public BigDecimal value() {
        return value;
    }

    /**
     * Scale this numerator to another denominator. E.g. if this is "3" on "4" ("from" param)
     * and we want to scale to "8" ("to" param), we expect 3 x 8 / 4 = 6 as a result.
     *
     * @param from denominator from which to start
     * @param to denominator to go to
     * @return the numerator scaling to another denominator
     */
    public Percentage scale(int from, int to) {
        return fromValue(value.multiply(BigDecimal.valueOf(to))
            .divide(BigDecimal.valueOf(from), 2, RoundingMode.HALF_UP));
    }
}

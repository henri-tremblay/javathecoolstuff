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

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public interface Numeric<T extends Numeric<T>> {

    @NonNull
    BigDecimal value();

    @NonNull
    T fromValue(BigDecimal newValue);

    int precision();

    default T add(T numeric) {
        return fromValue(value().add(numeric.value()));
    }

    default T subtract(T numeric) {
        return fromValue(value().subtract(numeric.value()));
    }

    default boolean isZero() {
        return value().signum() == 0;
    }

    default T negate() {
        return fromValue(value().negate());
    }

    /**
     * Scale this numerator to another denominator. E.g. if this is "3" on "4" ("from" param)
     * and we want to scale to "8" ("to" param), we expect 3 x 8 / 4 = 6 as a result.
     *
     * @param from denominator from which to start
     * @param to denominator to go to
     * @return the numerator scaling to another denominator
     */
    default T scale(int from, int to) {
        return fromValue(value().multiply(BigDecimal.valueOf(to))
            .divide(BigDecimal.valueOf(from), 2, RoundingMode.HALF_UP));
    }

    @NonNull
    default BigDecimal setScale(BigDecimal value) {
        return Objects.requireNonNull(value).setScale(precision(), RoundingMode.HALF_UP);
    }
}

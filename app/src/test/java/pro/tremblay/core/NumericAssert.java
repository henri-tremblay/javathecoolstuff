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

import org.assertj.core.api.BigDecimalAssert;

public class NumericAssert<T extends Numeric<T>> extends BigDecimalAssert {
    private final Numeric<T> actualNumeric;

    public NumericAssert(Numeric<T> actual) {
        super(actual.value());
        actualNumeric = actual;
    }

    public NumericAssert<T> isEqualTo(Numeric<T> expected) {
        objects.assertEqual(info, actualNumeric, expected);
        return this;
    }

    public NumericAssert<T> isNotEqualTo(Object other) {
        objects.assertNotEqual(info, actualNumeric, other);
        return this;
    }

}

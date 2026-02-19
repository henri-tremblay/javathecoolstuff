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
package pro.tremblay.core.price;

import org.junit.jupiter.api.Test;
import pro.tremblay.core.Amount;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class PriceRepositoryTest {

    private final RandomPriceRepository repository = new RandomPriceRepository();

    @Test
    void shouldStoreAndRetrievePrice() {
        Amount first = repository.getPrice("IBM");
        assertThat(first.value()).isBetween(BigDecimal.ZERO, BigDecimal.valueOf(100));
        // should be a price nearby but not identical (unless unlucky... so this is slightly flaky)
        Amount second = repository.getPrice("IBM");
        assertThat(second.value()).isBetween(BigDecimal.ZERO, BigDecimal.valueOf(100));
        assertThat(second.value()).isNotEqualTo(first.value());
        // should be totally different
        Amount third = repository.getPrice("APPL");
        assertThat(third.value()).isBetween(BigDecimal.ZERO, BigDecimal.valueOf(100));
        assertThat(third.value()).isNotEqualTo(second.value());
    }

}

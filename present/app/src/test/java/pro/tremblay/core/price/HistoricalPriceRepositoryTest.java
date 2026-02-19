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
import org.junit.jupiter.api.io.TempDir;
import pro.tremblay.core.Amount;

import java.nio.file.Files;
import java.nio.file.Path;

import static pro.tremblay.core.Assertions.assertThat;

class HistoricalPriceRepositoryTest {

    @Test
    void getPrice(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("prices.csv");
        Files.writeString(file, "IBM,2.3,2.4,2.5\nAPPL,12.34,45.67,78.90");

        HistoricalPriceRepository repository = new HistoricalPriceRepository(file);
        assertThat(repository.getPrice("IBM", 0)).isEqualTo(new Amount(2.3));
        assertThat(repository.getPrice("IBM", 1)).isEqualTo(new Amount(2.4));
        assertThat(repository.getPrice("IBM", 2)).isEqualTo(new Amount(2.5));

        assertThat(repository.getPrice("APPL", 0)).isEqualTo(new Amount(12.34));
        assertThat(repository.getPrice("APPL", 1)).isEqualTo(new Amount(45.67));
        assertThat(repository.getPrice("APPL", 2)).isEqualTo(new Amount(78.90));
    }
}

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

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FakePriceServiceTest {

    PriceService priceService = new FakePriceService();

    @Test
    void getPrice_alwaysTheSamePriceForASecurity() {
        Amount a1 = priceService.getPrice(SecuritiesForTest.GOOGL);
        Amount a2 = priceService.getPrice(SecuritiesForTest.GOOGL);
        assertSame(a1, a2);
    }

    @RetryingTest(2)
    void getPrice_randomPrices() {
        Amount a1 = priceService.getPrice(SecuritiesForTest.GOOGL);
        Amount a2 = priceService.getPrice(SecuritiesForTest.IBM);
        assertNotEquals(a1, a2);
    }


}

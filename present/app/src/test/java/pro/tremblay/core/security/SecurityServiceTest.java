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
package pro.tremblay.core.security;

import org.junit.jupiter.api.Test;
import pro.tremblay.core.FileProvider;
import pro.tremblay.core.SecuritiesForTest;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityServiceTest {

    private final FileProvider fileProvider = new FileProvider();
    private final SecurityService securityService = new SecurityService(fileProvider.shortSecurityFile());

    @Test
    void allSecurities() {
        assertThat(securityService.allSecurities()).containsExactlyInAnyOrderElementsOf(SecuritiesForTest.SECURITIES);
    }

    @Test
    void findForTicker() {
        assertThat(securityService.findForTicker("IBM")).isEqualTo(SecuritiesForTest.IBM);
    }
}

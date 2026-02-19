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

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "pro.tremblay.core")
class ArchitectureTest {

    @ArchTest
    static final ArchRule no_junit_assertions =
            noClasses()
                    .that().doNotBelongToAnyOf(ArchitectureTest.class) // ArchitectureTest uses Assertions right below, so we need to ignore it
                    .should().dependOnClassesThat()
                    .belongToAnyOf(org.junit.jupiter.api.Assertions.class)
                    .because("Use AssertJ assertions instead of JUnit assertions");
}

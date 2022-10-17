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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionGenerator {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("listing_status.csv");
        List<Security> securities = Files.lines(path)
            .map(s -> s.split(","))
            .map(tuple -> new Security(tuple[0], tuple[1], tuple[2], tuple[3], LocalDate.parse(tuple[4])))
            .collect(Collectors.toList());
        System.out.println(securities);
    }
}

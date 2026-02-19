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

import java.nio.file.Path;

public class FileProvider {

    private final Path data = Path.of("../../data");

    public Path securityFile() {
        return data.resolve("securities.csv");
    }

    public Path shortSecurityFile() {
        return data.resolve("securities_short.csv");
    }

    public Path positionFile() {
        return data.resolve("positions.csv");
    }

    public Path longPositionFile() {
        return data.resolve("positions_long.csv");
    }

    public Path transactionFile() {
        return data.resolve("transactions.csv");
    }

    public Path priceFile() {
        return data.resolve("prices.csv");
    }
}

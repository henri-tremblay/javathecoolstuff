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
package pro.tremblay.core.benchmark;

import module jmh.core;
import module app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(2)
@State(Scope.Benchmark)
public class PositionBenchmark {

    PriceService priceService = new PriceService("http://localhost:8000");
    Position current;

    @Setup(Level.Trial)
    public void setUp() throws IOException {
        SecurityService securityService = new SecurityService(Path.of("../data/securities.csv"));
        PositionReader positionReader = new PositionReader(securityService);
        current = positionReader.readFromFile(Paths.get("../data/positions_long.csv"));
    }

    @Benchmark
    public Amount evaluation() {
        return current.securityPositionValue(priceService);
    }

}

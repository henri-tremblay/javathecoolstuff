/*
 * Copyright 2022-2023 the original author or authors.
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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import pro.tremblay.core.Amount;
import pro.tremblay.core.FakePriceService;
import pro.tremblay.core.Position;
import pro.tremblay.core.PositionReader;
import pro.tremblay.core.PriceService;
import pro.tremblay.core.SecurityService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(2)
@State(Scope.Benchmark)
public class PositionBenchmark {

    PriceService priceService = new FakePriceService();
    Position current;

    @Setup(Level.Trial)
    public void setUp() throws IOException {
        SecurityService securityService = new SecurityService(Paths.get("listing_status.csv"));
        PositionReader positionReader = new PositionReader(securityService);
        current = positionReader.readFromFile(Paths.get("positions.csv"));
    }

    @Benchmark
    public Amount evaluation() {
        return current.securityPositionValue(priceService);
    }

}

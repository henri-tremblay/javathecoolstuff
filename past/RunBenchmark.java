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

import java.lang.System;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Script to run the benchmarks quickly. It compiles and run.
 */
public class RunBenchmark {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: RunBenchmarkSuite.java benchmark_name");
            System.out.println("Available benchmarks:");
            Files.list(Paths.get("benchmark/src/main/java/pro/tremblay/core/benchmark"))
                .map(path -> path.getFileName().toString().replace(".java", ""))
                .forEach(path -> System.out.println("- " + path));
            System.exit(1);
        }
        command("mvnd", "package", "-DskipTests", "-Denforcer.skip=true", "-Dmaven.javadoc.skip=true");
        command("java", "-jar", "benchmark/target/benchmarks.jar", "pro.tremblay.core.benchmark." + args[0]);
    }

    private static void command(String... args) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(args)
            .inheritIO();
        Process process = builder.start();

        process.waitFor();
    }
}

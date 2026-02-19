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

/**
 * Script to run the benchmarks quickly. It compiles and run.
 */
void main(String... args) throws Exception {
    if (args.length == 0) {
        IO.println("Usage: RunBenchmarkSuite.java benchmark_name");
        IO.println("Available benchmarks:");
        Files.list(Paths.get("benchmark/src/main/java/pro/tremblay/core/benchmark"))
            .forEach(path -> System.out.println("- " + path.getFileName()));
        System.exit(1);
    }
    Command.command("mvnd", "clean", "package", "-DskipTests", "-Denforcer.skip=true", "-Dmaven.javadoc.skip=true");
    IO.println("\n***** RECORDING *****\n");
    Command.command("java", "-XX:AOTCacheOutput=app.aot", "--enable-preview", "-jar", "benchmark/target/benchmarks.jar", args[0]);
    IO.println("\n***** NO AOT *****\n");
    Command.command("java", "--enable-preview", "-jar", "benchmark/target/benchmarks.jar", args[0]);
    IO.println("\n***** AOT *****\n");
    Command.command("java", "-XX:AOTCache=app.aot", "--enable-preview", "-jar", "benchmark/target/benchmarks.jar", args[0]);
}

private static void command(String... args) throws Exception {
    ProcessBuilder builder = new ProcessBuilder(args)
        .inheritIO();
    Process process = builder.start();

    process.waitFor();
}

//java -XX:AOTCacheOutput=app.aot Java11.java
//time java -XX:AOTCache=app.aot Java11.java
//time java Java11.java

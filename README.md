# Beyond Java 8: The Cool Stuff

Code I use during my conference "Beyond Java 8: The Cool Stuff".

Starting state is in the `master` branch.
Final state is in the `henri` branch.

## Usage

* Build: `./mvnw verify`
* Reporting (spotbugs and jacoco): `./mvnw verify site`
* Mutation testing: `./mvnw package org.pitest:pitest-maven:mutationCoverage -DwithHistory`

## Benchmark

To run: `./mvnw package -DskipTests && java -jar app/target/benchmarks.jar`
To run a specific benchmark `java -jar app/target/benchmarks.jar YourClass`

If you want to run it against multiple commits, you can do `java RunBenchmarkSuite.java commit1, commit2, ...`.

## Maintenance

* Upgrade license: `./mvnw validate license:format`
* Upgrade the Maven wrapper: `./mvnw wrapper:wrapper`
* Check plugins and dependencies to upgrade: `./mvnw versions:display-dependency-updates versions:display-plugin-updates`

#!/usr/bin/env bash

exec java --add-modules=jdk.incubator.concurrent --enable-preview --source 19 Main.java

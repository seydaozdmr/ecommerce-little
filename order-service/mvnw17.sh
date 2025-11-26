#!/bin/bash

# Set JAVA_HOME to Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Run Maven with the specified command
mvn "$@"

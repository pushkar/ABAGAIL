#!/usr/bin/env bash
#
# Script to simplify kicking off a Java program. It assumes that your main
# class is located in the 'exp' (experiments) package.
#

JAR=target/abagail-1.0-SNAPSHOT.jar

if [ "$1" == "" ]; then
    echo Usage: ./runexp.sh {exp.MyExperimentMainClass} [arg1 [arg2 [...] ] ]
    exit 1
fi

MAIN=exp.$1
shift

echo Invoking: $MAIN "$@"

java -cp $JAR $MAIN "$@"

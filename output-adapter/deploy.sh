#!/usr/bin/env bash

set -eu

SPATH=$(dirname $0)

VERSION=1.0.0
JAR_NAME=output-adapter_${VERSION}.jar

JOBMANAGER_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp ${SPATH}/target/scala-2.11/$JAR_NAME "$JOBMANAGER_CONTAINER":/$JAR_NAME
docker exec -t -i "$JOBMANAGER_CONTAINER" flink run /$JAR_NAME

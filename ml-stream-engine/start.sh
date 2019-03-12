#!/usr/bin/env bash

cd Desktop/programs/kafka/kafka_2.12-2.1.0/ && ./bin/zookeeper-server-start.sh config/zookeeper.properties
cd Desktop/programs/kafka/kafka_2.12-2.1.0/ && ./bin/kafka-server-start.sh config/server.properties

# input-adapter topics
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-event-in
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-event-out
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-config-in
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-config-out
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-model-in
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-model-out

# core topics
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic output-event-in
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic output-alarm-topic
Desktop/programs/kafka/kafka_2.12-2.1.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic output-other-topic

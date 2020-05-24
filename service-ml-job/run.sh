#!/usr/bin/env bash

set -eu

SPATH=$(dirname $0)

echo "Starting docker with TensorFlow Serving..."
#docker rm -f tensorflow_serving
#docker run -td -p 9000:9000 --name=tensorflow_serving tgowda/inception_serving_tika
#docker exec -ti tensorflow_serving /serving/server.sh
#echo -e "/Users/axreldable/Desktop/projects/msu/msu-diploma-thesis/service-ml-job/src/main/resources/retriever.jpg" | kafkacat -b localhost:9092 -P -t ml-stream-service-image-in

echo "Starting service-ml-job..."
java -cp ${SPATH}/target/scala-2.11/service-ml-job_1.0.0.jar ru.star.ServiceMlJob

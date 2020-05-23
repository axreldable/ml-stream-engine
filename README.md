
## Machine learning model serving system for event streams

The system allows you to serve machine learning models on event streams using different approaches.

### Architecture of the system
External Kafka to communicate with users.  
Internal Kafka - for the system's modules communication.

![Architecture](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/msu-ml-streaming-system.png)

### Architecture of the input-adapter
input-adapter allows you:
 - move events into right model
 - divide traffic between models (ex. A/B testing)
 - transform events before evaluation
 - update configuration online
 
![input-adapter](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/input-adapter.png)

### Architecture of the spark-ml-job
A separate spark-streaming job with one embedded spark-mlib model.

![spark-ml-job](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/spark-ml-job.png)

### Architecture of the pmml-ml-job
pmml-ml-job allows you:
- serve models in [PMML](http://dmg.org/pmml/v4-1/GeneralStructure.html) format
- add, update, delete models online

![spark-ml-job](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/pmml-ml-job.png)

### Architecture of the service-ml-job
pmml-ml-job allows you to serve models using [TensorFlow Serving](https://www.tensorflow.org/tfx/guide/serving)

![spark-ml-job](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/service-ml-job.png)

### Architecture of the output-adapter
output-adapter allows you:
 - move events into right result topic
 - transform events after evaluation
 - update configuration online
 
![input-adapter](https://github.com/axreldable/msu-diploma-thesis/blob/master/images/output-adapter.png)

#### Technologies

- [Apache Kafka](https://kafka.apache.org)
- [Apache Flink](https://flink.apache.org)
- [Apache Spark](https://spark.apache.org)
- Monitoring: [Prometheus](https://prometheus.io), [Grafana](https://grafana.com)

#### How to install
Requirements:
```
curl
git
docker
flink
```

#### How to run
```
./scripts/start.sh
```

#### How to stop
```
./scripts/stop.sh
```

#### How to monitor
Connect to Kafka on the localhost:9092 and use something like [Conductor](https://www.conduktor.io) to monitor messages.  
[Grafana UI](http://localhost:3000) with Kafka metrics  
[Prometheus UI](http://localhost:9090) with Kafka metrics  
[Flink cluster UI](http://localhost:8081) to monitor Flink jobs

## Kafka partitions and replica factor

In Kafka, topics are split into many `partitions`. Each partition can be written and read independently. So, how many partitions in your topic is affect to data throughput.

* The number of partitions should = expectation throughput / partition speed.
    * A single Kafka topic runs at about 10 MB/s
* The number of partitions should higher than or equal number of consumers. That all comsumers can be used to consume data from the topic's partitions.

Each partition should be replicated to avoid losting data and increasing data availability. Partitions are replicated through the topic's `replica factor`.

* Replica factor should be greater than the `min. insync. replicas` parameter.
* Replica factor should be smaller than the number of broker nodes in the cluster.


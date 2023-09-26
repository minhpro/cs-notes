## Decreasing replica factor

**Create a proposal `reassignment.json` file**

Create our `topics.json` file that contains topics need to reassign.

```json
{
  "version": 1,
  "topics": [
    { "topic": "my-topic-two"}
  ]
}
```

Generate the `reassignment.json` file

```
bin/kafka-reassign-partitions.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 \
--topics-to-move-json-file topics.json \
--broker-list 0,1,2,3,4 \
--generate
```

The current `reassignment.json` file

```
{"version":1,"partitions":[{"topic":"my-topic-two","partition":0,"replicas":[3,4,2,0],"log_dirs":["any","any","any","any"]},{"topic":"my-topic-two","partition":1,"replicas":[0,2,3,1],"log_dirs":["any","any","any","any"]},{"topic":"my-topic-two","partition":2,"replicas":[1,3,0,4],"log_dirs":["any","any","any","any"]}]}
```

Open `reassignment.json` file and remove some replicas (should not be leader replica)


```
{"version":1,"partitions":[{"topic":"my-topic-two","partition":0,"replicas":[1,0,2]},{"topic":"my-topic-two","partition":1,"replicas":[1,2,3]},{"topic":"my-topic-two","partition":2,"replicas":[2,3,4]}]}
```

After that, run reassign command

```
bin/kafka-reassign-partitions.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 \
--reassignment-json-file reassignment.json \
--execute
```

We can `--verify` the parition reassignment is done

```
bin/kafka-reassign-partitions.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 \
--reassignment-json-file reassignment.json \
--verify
```

Describe the topic

```
bin/kafka-topics.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic my-topic-two --describe
```

```
Topic: my-topic-two     TopicId: Gm-DqwrbT82PAEOE160Djw PartitionCount: 3       ReplicationFactor: 3    Configs: min.insync.replicas=2,segment.bytes=1073741824,retention.ms=7200000,message.format.version=3.0-IV1
Topic: my-topic-two     Partition: 0    Leader: 0       Replicas: 0,1,2 Isr: 0,1,2
Topic: my-topic-two     Partition: 1    Leader: 2       Replicas: 1,2,3 Isr: 1,2,3
Topic: my-topic-two     Partition: 2    Leader: 3       Replicas: 2,3,4 Isr: 2,3,4
```

## Add more partition to the topic

`./bin/kafka-topics.sh --create --zookeeper localhost:2181 --topic my-topic --replication-factor 3 --partitions 6`

## References

https://kafka.apache.org/documentation/#replication

https://strimzi.io/blog/2022/09/16/reassign-partitions/

https://www.allprogrammingtutorials.com/tutorials/adding-partitions-to-topic-in-apache-kafka.php
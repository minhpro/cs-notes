## Install local cluster

**Start kafka**

```sh
nohup ./bin/kafka-server-start.sh config/server.properties &
```

## Console commands

**Create topic**

```sh
bin/kafka-topics.sh --create --topic test-topic --replication-factor 5 --partitions 5 --bootstrap-server localhost:10091,localhost:10092,localhost:10093,localhost:10094,localhost:10095
```

**Describe topic**

```sh
bin/kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:10091,localhost:10092,localhost:10093,localhost:10094,localhost:10095
```

**Alter topic min.insync**

```sh
bin/kafka-configs.sh \
    --bootstrap-server localhost:10091,localhost:10092,localhost:10093,localhost:10094,localhost:10095 \
    --alter \
    --entity-type topics \
    --entity-name test-topic \
    --add-config min.insync.replicas=2
```




bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --group retailApp --topic email.delivery --partition 0 --offset 0 --property print.headers=true

1. Describe groups: topics, partitions, offsets

```
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group retailApp --describe
```

2. Reset offset

https://gist.github.com/marwei/cd40657c481f94ebe273ecc16601674b

bin/kafka-consumer-groups.sh  --command-config eventhub-pt.properties  --bootstrap-server pt-retailapp-eventhub.servicebus.windows.net:9093 --group retailApp --topic event.delivery --reset-offsets 4943422 --execute
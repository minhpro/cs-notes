## Console commands

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --group retailApp --topic email.delivery --partition 0 --offset 0 --property print.headers=true

1. Describe groups: topics, partitions, offsets

```
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group retailApp --describe
```

2. Reset offset

https://gist.github.com/marwei/cd40657c481f94ebe273ecc16601674b

bin/kafka-consumer-groups.sh  --command-config eventhub-pt.properties  --bootstrap-server pt-retailapp-eventhub.servicebus.windows.net:9093 --group retailApp --topic event.delivery --reset-offsets 4943422 --execute
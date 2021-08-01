## Kafka commands

List all topics

`bin/kafka-topics.sh --bootstrap-server localhost:9092 --list`

Describe a topic

`bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic synchronize.account`

Consume from a topic

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic synchronize.account`

List all consumer groups

`bin/kafka-consumer-groups.sh  --list --bootstrap-server localhost:9092`

Describe a group

`bin/kafka-consumer-groups.sh --describe --group spiral-service --bootstrap-server localhost:9092`

Reset offset of a group

`bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group spiral-service --topic synchronize.account --reset-offsets --to-earliest --execute`

## Notes

* Adding `spring.cloud.stream.bindings.accountInput.consumer.headerMode=embeddedHeaders
` to application.properties
> Update Spring Cloud Stream need to update kafka-client. In order to work with old kafka messages (default is embeddedHeaders, header included in message body)
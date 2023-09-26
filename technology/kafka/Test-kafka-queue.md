## Test kafka queue

0. Download và extract kafka

https://archive.apache.org/dist/kafka/2.8.0/kafka_2.13-2.8.0.tgz

```sh
cd kafka_2.13-2.8.0
```

1. Lấy thông tin partition và offset

**Local**

```
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group retailApp --describe
```

**Server SB**

```
bin/kafka-consumer-groups.sh --command-config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093 --group retailApp --describe
```


Example

```
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group retailApp --describe

GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                               HOST            CLIENT-ID
retailApp       email.delivery  0          1               1               0               consumer-retailApp-1-ec47edbf-f33d-404e-b54c-92b6fcc4ebbc /172.18.0.1     consumer-retailApp-1
retailApp       event.delivery  0          9               9               0               consumer-retailApp-2-41728a49-8502-4712-a6ef-c2a8d9090fd8 /172.18.0.1     consumer-retailApp-2
```

```
GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                                                                       HOST            CLIENT-ID
retailApp       email.delivery  0          128             128             0               dev-retailapp-eventhub.servicebus.windows.net:c:retailapp:I:consumer-retailApp-1-61f9cd205854401e933ed7b7b88981dc 0.0.0.0         consumer-retailApp-1
retailApp       event.delivery  0          4               4               0               dev-retailapp-eventhub.servicebus.windows.net:c:retailapp:I:consumer-retailApp-2-f034abebf87a4cec8f9a1ddda406ad7b 0.0.0.0         consumer-retailApp-2
```

2. Chờ message từ dead letter queue

**Local**

Event topic

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic event.delivery.DLT
```

Email topic

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic email.delivery.DLT
```

**Server SB**

Event topic

```
bin/kafka-console-consumer.sh --consumer.config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093  --topic event.delivery.DLT
```

Email topic

```
bin/kafka-console-consumer.sh --consumer.config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093  --topic email.delivery.DLT
```



3. Thực hiện chức năng có liên quan đến queue (gửi email, xử lý PF event)

Nếu cần test retry thì

* Test retry event thì sửa bảng camera_algorithms trường camera_key -> camera_keyy
* Test gửi email thì đổi password gửi email

4. Kiểm tra message được gửi vào queue  (email hay event topic)

**Local**

Event topic

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic event.delivery --partition {partition} --offset {current-offset}
```

Email topic

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic email.delivery --partition {partition} --offset {current-offset}
```

**Server SB**

Event topic

```
bin/kafka-console-consumer.sh --consumer.config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093  --topic event.delivery --partition {partition} --offset {current-offset}
```

Email topic

```
bin/kafka-console-consumer.sh --consumer.config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093  --topic email.delivery --partition {partition} --offset {current-offset}
```


Example

```
kafka_2.13-2.8.0 bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic event.delivery --partition 0 --offset 8
{"event_kind":1,"edgebox_event":{"event_key":null,"edgebox_key":null,"camera_key":5000002,"algorithm_key":1000015,"code":"202","message":"Analysis result","datetime":"2021-12-01 16:46:00"}}
```

```
kafka_2.13-2.8.0 bin/kafka-console-consumer.sh --consumer.config eventhub.properties --bootstrap-server dev-retailapp-eventhub.servicebus.windows.net:9093  --topic event.delivery --partition 0 --offset 3
{"event_kind":1,"edgebox_event":{"event_key":null,"edgebox_key":null,"camera_key":5000016,"algorithm_key":1000014,"code":"202","message":null,"datetime":"2021-12-01 16:14:00"}}
```

5. Kiểm tra retry

Check log (datadog) có `consumer` và `retries`, ví dụ

```
[OTP] consumer, email addresses: ***, retries: 4
```

hay 

```
[Event] consumer, eventKind: 1, eventCode: 202, retries: 4
```

Tiếp theo, kiểm tra dead letter queue có message mới (bước 2)

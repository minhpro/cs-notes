# Install zookeeper

## Step 1 — Creating a User for Zookeeper

`sudo adduser zookeeper`

Follow the prompts to set a password and create the kafka user.

Next, add the kafka user to the sudo group with the adduser command. You need these privileges to install Kafka’s dependencies:

`sudo adduser zookeeper sudo`

## Step 2: Creating a ZooKeeper Data Directory

`sudo mkdir -p /data/zookeeper`

Then, give the ZooKeeper user ownership to that directory:

`sudo chown -R zookeeper:zookeeper /data/zookeeper`

## Step 3 — Downloading and Extracting the Zookeeper Binaries

Create a directory in home called Downloads to store your downloads (usually it is created):

`mkdir Downloads`

Download the zookeeper binary:

`wget https://dlcdn.apache.org/zookeeper/zookeeper-3.7.1/apache-zookeeper-3.7.1-bin.tar.gz -P ~/Downloads`

Extract the archive you downloaded using the tar command:

`sudo tar -xvzf ~/Downloads/apache-zookeeper-3.7.1-bin.tar.gz -C /opt`

Give the zookeeper user ownership

`sudo chown -R zookeeper:zookeeper /opt/apache-zookeeper-3.7.1-bin`

Create a symbol link

`sudo ln -s /opt/apache-zookeeper-3.7.1-bin /opt/zookeeper`

## Step 3 — Configuring the Zookeeper Server

To start ZooKeeper you need a configuration file. Here is a sample, create it in conf/zoo.cfg:

`sudo vim /opt/zookeeper/zoo.cfg`

```
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5
dataDir=/data/zookeeper
clientPort=2181
```

## Step 4 — Creating Systemd Unit Files and Starting the Zookeeper Server

`sudo vim /etc/systemd/system/zookeeper.service`

```
[Unit]
Description=Zookeeper Daemon
Documentation=http://zookeeper.apache.org
Requires=network.target
After=network.target

[Service]    
Type=forking
WorkingDirectory=/opt/zookeeper
User=zookeeper
Group=zookeeper
ExecStart=/opt/zookeeper/bin/zkServer.sh start /opt/zookeeper/conf/zoo.cfg
ExecStop=/opt/zookeeper/bin/zkServer.sh stop /opt/zookeeper/conf/zoo.cfg
ExecReload=/opt/zookeeper/bin/zkServer.sh restart /opt/zookeeper/conf/zoo.cfg
TimeoutSec=30
Restart=on-failure

[Install]
WantedBy=default.target
```

```sh
sudo systemctl daemon-reload
sudo systemctl start zookeeper
sudo systemctl enable zookeeper
sudo systemctl status zookeeper
```

# Instal kafka 

## Step 1 — Creating a User for Kafka

`sudo adduser kafka`

Follow the prompts to set a password and create the kafka user.

Next, add the kafka user to the sudo group with the adduser command. You need these privileges to install Kafka’s dependencies:

`sudo adduser kafka sudo`

##  Step 2 — Downloading and Extracting the Kafka Binaries

Create a directory in home called Downloads to store your downloads (usually it is created):

`mkdir Downloads`

Download the kafka binary:

`wget https://archive.apache.org/dist/kafka/2.7.2/kafka_2.13-2.7.2.tgz -P ~/Downloads`

Extract the archive you downloaded using the tar command:

`sudo tar -xvzf ~/Downloads/kafka_2.13-2.7.2.tgz -C /opt`

Give the kafka user ownership

`sudo chown -R kafka:kafka /opt/kafka_2.13-2.7.2`

Create a symbol link

`sudo ln -s /opt/kafka_2.13-2.7.2 /opt/kafka`

## Step 3 - Configuring kafka server

`sudo vim /opt/kafka/config/server.properties`

Replace some contents:

```
delete.topic.enable=true
log.dirs=/opt/kafka/logs
zookeeper.connect=localhost:2181/kafka
```

## Step 4 — Creating Systemd Unit Files and Starting the Kafka Server

Create the systemd service file for kafka:

`sudo vim /etc/systemd/system/kafka.service`

Enter the following unit definition into the file:

```
[Unit]
Description=Kafka Daemon
Requires=zookeeper.service
After=zookeeper.service

[Service]
Type=simple
WorkingDirectory=/opt/kafka
User=kafka
Group=kafka
ExecStart=/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties
ExecStop=/opt/kafka/bin/kafka-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```

```sh
sudo systemctl daemon-reload
sudo systemctl start kafka
sudo systemctl enable kafka
sudo systemctl status kafka
```

## Step 5 - Test kafka installation

Create a topic

`/opt/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181/kafka --replication-factor 1 --partitions 1 --topic TutorialTopic`

Publish a message to the topic

`echo "Hello, World" | /opt/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic TutorialTopic > /dev/null`

Consumer messages from the topic

`/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic TutorialTopic --from-beginning`

## Step 6 - Hardening the Kafka Server

With your installation complete, you can remove the kafka user’s admin privileges

Remove the kafka user from the sudo group:

`sudo deluser kafka sudo`

# Add a new listener to the broker

https://www.confluent.io/blog/kafka-client-cannot-connect-to-broker-on-aws-on-docker-etc/

Add listeners to the server configuration file

`sudo vim /opt/kafka/config/server.properties`

```
listeners=PLAINTEXT://localhost:9092,OUTSIDE://10.22.30.11:19092,MINIKUBE://192.168.59.1:29092

advertised.listeners=PLAINTEXT://localhost:9092,OUTSIDE://10.22.30.11:19092,MINIKUBE://192.168.59.1:29092

listener.security.protocol.map=PLAINTEXT:PLAINTEXT,OUTSIDE:PLAINTEXT,MINIKUBE:PLAINTEXT
```

Test consume from outside (other machine)

`bin/kafka-console-consumer.sh --bootstrap-server 10.22.30.11:19092 --topic TutorialTopic --from-beginning`

```
Hello, World
```

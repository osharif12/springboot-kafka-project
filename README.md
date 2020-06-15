# springboot-kafka-project
A project which demonstrates the basics of Apache Kafka. Will demonstrate how to create a cluster of multiple Kafka brokers and Zookeeper nodes on AWS EC2 instances. Will also demonstrate how to use Spring Boot to create a custom Kafka producer to send records to Kafka cluster with specified topic. 

## Configure AWS EC2 Instances
You will create three EC2 instances on AWS. Eeach instance will host a Zookeeper node to manage the cluster and a Kafka broker to handle producers and consumers.

### Steps
1. Create an AWS account and launch three EC2 instances. The AMI for each instance should be Linux Ubuntu Server(16.04 LTS or above), instance type should be t2.medium, and security groups should be configured accordingly for each instance. See image below (note that category "source" should not be from anywhere for best practices and should be restricted to trusted machines) 

![alt text](https://github.com/osharif12/springboot-kafka-project/blob/master/src/main/resources/static/security-groups.png "")

2. Download the .pem file and launch the instance. Once launched you should be able to ssh into your instance using following commands: 

chmod 400 name-of-pem.pem  <br />
ssh -i "name-of-pem.pem" ubuntu@ec2-..-...-...-..us-east-2.compute.amazonaws.com

## Download proper packages and Kafka on each instance
1. sudo apt-get update
2. wget http://apache.mirrors.hoobly.com/kafka/2.5.0/kafka_2.12-2.5.0.tgz
3. tar xzf kafka_2.12-2.5.0.tgz
4. sudo apt install openjdk-8-jre-headless -y

## Configure and run Apache Zookeeper on each instance
1. Create a new directory using the following commands: <br />
   cd kafka_2.12-2.5.0/<br />
   mkdir -p data/zookeeper
2. Create a myid file using following commands. Note the id should be different for each instance (ex: 1 for instance 1, 2 for instance 2, etc) <br />
   touch data/zookeeper/myid <br />
   echo "1" >> data/zookeeper/myid
3. Update zookeeper.properties file in each instance accordingly:
   vim config/zookeeper.properties
   
   ### For Node 1
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper
   initLimit=5
   syncLimit=2
   tickTime=2000
   #list of servers
   server.1=0.0.0.0:2888:3888
   server.2=<Ip of second server>:2888:3888 # AWS IPv4 Public IP
   server.3=<ip of third server>:2888:3888 # AWS IPv4 Public IP
  
   ### For Node 2
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper
   initLimit=5
   syncLimit=2
   tickTime=2000
   #list of servers
   server.1=<ip of first server>:2888:3888 
   server.2=0.0.0.0:2888:3888
   server.3=<ip of third server>:2888:3888
   
   ### For Node 3
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper
   initLimit=5
   syncLimit=2
   tickTime=2000
   #list of servers
   server.1=<ip of first server>:2888:3888
   server.2=<ip of second server>:2888:3888
   server.3=0.0.0.0:2888:3888

4. While in the kafka folder, run zookeeper as a background process using the following command:
   nohup bin/zookeeper-server-start.sh config/zookeeper.properties &
   
5. You can check all processes or kill a process using following commands:
   ps aux
   kill -9 process-id

## Configure and run Apache Kafka brokers on each instance
1. Create a new directory using the following commands: <br />
   cd kafka_2.12-2.5.0/
   mkdir -p data/kafka

2. Update the server.properties file in each instance accordingly:
   vim config/server.properties
   
   broker.id=10 # should be unique for every instance, ex: 20 for instance 2, 30 for instance 3, etc.
   advertised.listeners=PLAINTEXT://ec2-.-..-...-....us-east-2.compute.amazonaws.com:9092
   log.dirs=/home/ubuntu/kafka_2.12-2.5.0/data/kafka
   zookeeper.connect=ip_of_instance_1:2181,ip_of_instance_2:2181,ip_of_instance_3:2181 # AWS IPv4 Public IP
   
3. While in the kafka folder, run kafka broker as a background process using the following command:
   nohup bin/kafka-server-start.sh config/server.properties &
   
## Create and manage Kafka topics
1. To create a kafka topic enter the following command. Note that any records sent to the topic will be partitioned into 3 pieces and each piece will be stored on all three brokers. 

   bin/kafka-topics.sh --create  --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3 --topic demo-topic

2. Command to list all topics: 
   bin/kafka-topics.sh --list  --bootstrap-server localhost:9092
   
3. Command to describe topic: 
   bin/kafka-topics.sh --describe  --bootstrap-server localhost:9092 --topic demo-topic

## Create custom producer class 
1. Refer to the code as a template. The custom producer class allows us to send records in more complex formats such as JSON rather than just simple String objects. 

2. Command for standard producer: 
   sudo bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test

## Run consumer to consume data sent by producer class
1. Enter the following command to start consumer. It will read in all records for given topic: 
   sudo bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
   
## Overview


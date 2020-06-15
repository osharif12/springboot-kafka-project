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
3. Update zookeeper.properties file in each instance accordingly: <br />
   vim config/zookeeper.properties
   
   ### For Node 1
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper <br />
   initLimit=5 <br />
   syncLimit=2 <br />
   tickTime=2000 <br />
   #list of servers <br />
   server.1=0.0.0.0:2888:3888 <br />
   server.2=<Ip of second server>:2888:3888 # AWS IPv4 Public IP <br />
   server.3=<ip of third server>:2888:3888 # AWS IPv4 Public IP <br />
  
   ### For Node 2
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper <br />
   initLimit=5 <br />
   syncLimit=2 <br />
   tickTime=2000 <br />
   #list of servers <br />
   server.1=<ip of first server>:2888:3888  <br />
   server.2=0.0.0.0:2888:3888 <br />
   server.3=<ip of third server>:2888:3888 <br />
   
   ### For Node 3
   dataDir=/home/ubuntu/kafka_2.12-2.5.0/data/zookeeper <br />
   initLimit=5 <br />
   syncLimit=2 <br />
   tickTime=2000 <br />
   #list of servers <br />
   server.1=<ip of first server>:2888:3888 <br />
   server.2=<ip of second server>:2888:3888 <br />
   server.3=0.0.0.0:2888:3888 <br />

4. While in the kafka folder, run zookeeper as a background process using the following command: <br />
   nohup bin/zookeeper-server-start.sh config/zookeeper.properties & <br />
   
5. You can check all processes or kill a process using following commands: <br />
   ps aux <br />
   kill -9 process-id <br />

## Configure and run Apache Kafka brokers on each instance
1. Create a new directory using the following commands: <br />
   cd kafka_2.12-2.5.0/  <br />
   mkdir -p data/kafka  <br />

2. Update the server.properties file in each instance accordingly: <br />
   vim config/server.properties <br />  
   
   broker.id=10 # should be unique for every instance, ex: 20 for instance 2, 30 for instance 3, etc. <br />
   advertised.listeners=PLAINTEXT://ec2-.-..-...-....us-east-2.compute.amazonaws.com:9092 <br />
   log.dirs=/home/ubuntu/kafka_2.12-2.5.0/data/kafka <br />
   zookeeper.connect=ip_of_instance_1:2181,ip_of_instance_2:2181,ip_of_instance_3:2181 # AWS IPv4 Public IP  <br />
   
3. While in the kafka folder, run kafka broker as a background process using the following command: <br />
   nohup bin/kafka-server-start.sh config/server.properties & <br /> 
   
## Create and manage Kafka topics
1. To create a kafka topic enter the following command. Note that any records sent to the topic will be partitioned into 3 pieces and each piece will be stored on all three brokers.  <br />

   bin/kafka-topics.sh --create  --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3 --topic demo-topic <br />

2. Command to list all topics:  <br />
   bin/kafka-topics.sh --list  --bootstrap-server localhost:9092 <br />
   
3. Command to describe topic:  <br />
   bin/kafka-topics.sh --describe  --bootstrap-server localhost:9092 --topic demo-topic <br />
   
   ![alt text](https://github.com/osharif12/springboot-kafka-project/blob/master/src/main/resources/static/topic-info.png "")

## Create custom producer class 
1. Refer to the code as a template. The custom producer class allows us to send records in more complex formats such as JSON rather than just simple String objects.  <br />

2. If using above template, after changing KafkaConfiguration to point to the proper kafka broker and port, run the project locally and send request. See below for example using Postman

![alt text](https://github.com/osharif12/springboot-kafka-project/blob/master/src/main/resources/static/postman.png "")
   
3. Command for standard producer:  <br />
   sudo bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test <br />

## Run consumer to consume data sent by producer class
1. Enter the following command to start consumer. It will read in all records for given topic:  <br />
   sudo bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning <br />
   
   ![alt text](https://github.com/osharif12/springboot-kafka-project/blob/master/src/main/resources/static/consumer.png "")
   
## Overview


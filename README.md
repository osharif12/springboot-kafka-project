# springboot-kafka-project
A project which demonstrates the basics of Apache Kafka. Will demonstrate how to create a cluster of multiple Kafka brokers and Zookeeper nodes on AWS EC2 instances. Will also demonstrate how to use Spring Boot to create a custom Kafka producer to send records to Kafka cluster with specified topic. 

## Configure AWS EC2 Instances
You will create three EC2 instances on AWS. Eeach instance will host a Zookeeper node to manage the cluster and a Kafka broker to handle producers and consumers.

### Steps
1. Create an AWS account and launch three EC2 instances. The AMI for each instance should be Linux Ubuntu Server(16.04 LTS or above), instance type should be t2.medium, and security groups should be configured accordingly for each instance. 
![alt text](https://github.com/osharif12/springboot-kafka-project/blob/master/src/main/resources/static/security-groups.png "")
2. 

## Download proper binaries on each instance

## Configure and run Apache Zookeeper nodes

## Configure and run Apache Kafka brokers

## Create custom producer class 

## Run consumer to consume data sent by producer class

## Overview

## Miscellaneous Commands

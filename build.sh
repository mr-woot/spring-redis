#!/usr/bin/env bash

while ! nc -z kafka_producer-server 8888 ; do
    echo "Waiting for upcoming kafka-producer server"
    sleep 2
done
java -jar /kafka_producer/lib/kafka_producer.jar
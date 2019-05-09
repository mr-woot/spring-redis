package com.paisabazaar.kafka_producer.service;

import org.springframework.stereotype.Service;

@Service
public interface KafkaService {
    void sendMessage(String topicName, String message);
}

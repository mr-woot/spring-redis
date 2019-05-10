package com.paisabazaar.kafka_producer.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public interface KafkaService {
    JSONObject sendMessage(String topicName, String message) throws InterruptedException, ExecutionException;
    String sendMessage(String topicName, String message, Integer key, Integer partition) throws InterruptedException, ExecutionException;
//    String sendMessage(String topicName, String message, int partition) throws InterruptedException, ExecutionException;
}

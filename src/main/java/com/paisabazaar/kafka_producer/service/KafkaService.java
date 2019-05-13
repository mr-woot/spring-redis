package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public interface KafkaService {
    JSONObject sendMessage(String topicName, Integer partition, String key, String message) throws InterruptedException, ExecutionException;

    JSONArray sendMessagesInBatch(String topic, Integer partition, String key, Map<String, Object>[] messages) throws ExecutionException, InterruptedException;
}

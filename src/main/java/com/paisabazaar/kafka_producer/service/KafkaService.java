package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface KafkaService {
    String sendMessage(String topicName, Integer partition, String key, JSONObject message) throws InterruptedException, ExecutionException;

    JSONArray sendMessagesInBatch(String topic, Integer partition, String key, Map<String, Object>[] messages, String metadata) throws ExecutionException, InterruptedException;
}

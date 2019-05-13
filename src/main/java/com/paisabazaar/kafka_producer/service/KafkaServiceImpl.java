package com.paisabazaar.kafka_producer.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public JSONObject sendMessage(String topicName, Integer partition, String key, String message) throws InterruptedException, ExecutionException {
        kafkaTemplate.send(topicName, partition, key, message);
        return null;
    }

    @Override
    public JSONArray sendMessagesInBatch(String topic, Integer partition, String key, Map<String, Object>[] messages) throws ExecutionException, InterruptedException {
        for (int i = 0; i < messages.length; i++) {
            this.sendMessage(topic, partition, key, messages[i].toString());
        }
        return null;
    }
}

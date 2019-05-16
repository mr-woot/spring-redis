package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String sendMessage(String topicName, Integer partition, String key, String message) throws InterruptedException, ExecutionException {
        kafkaTemplate.send(topicName, partition, key, message);
        return UUID.randomUUID().toString();
    }

    @Override
    public JSONArray sendMessagesInBatch(String topic, Integer partition, String key, Map<String, Object>[] messages, String metadata) throws ExecutionException, InterruptedException {
        JSONArray messagesArr = new JSONArray();
        for (int i = 0; i < messages.length; i++) {
            // ## Validate message with metadata
            messagesArr.put(this.sendMessage(topic, partition, key, messages[i].toString()));
        }
        return messagesArr;
    }
}

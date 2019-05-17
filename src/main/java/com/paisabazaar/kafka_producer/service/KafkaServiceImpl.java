package com.paisabazaar.kafka_producer.service;

import org.json.JSONArray;
import org.json.JSONObject;
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
    public String sendMessage(String topicName, Integer partition, String key, JSONObject message) throws InterruptedException, ExecutionException {
        String mId = UUID.randomUUID().toString();
        JSONObject obj = new JSONObject();
        obj.put("MessageID", mId);
        obj.put("Payload", message);
        kafkaTemplate.send(topicName, partition, key, String.valueOf(obj));
        return mId;
    }

    @Override
    public JSONArray sendMessagesInBatch(String topic, Integer partition, String key, Map<String, Object>[] messages, String metadata) throws ExecutionException, InterruptedException {
        JSONArray messagesArr = new JSONArray();
        for (int i = 0; i < messages.length; i++) {
            // ## Validate message with metadata
            messagesArr.put(this.sendMessage(topic, partition, key, new JSONObject(messages[i])));
        }
        return messagesArr;
    }
}

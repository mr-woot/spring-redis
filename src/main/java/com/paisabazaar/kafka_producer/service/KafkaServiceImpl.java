package com.paisabazaar.kafka_producer.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public JSONObject sendMessage(String topicName, String message) throws InterruptedException, ExecutionException {
        JSONObject response = new JSONObject();
        try {
            RecordMetadata future = kafkaTemplate.send(topicName, message).get().getRecordMetadata();
            response.put("topic", future.topic());
            response.put("partition", future.partition());
            response.put("offset", future.offset());
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
        return response;
    }

    @Override
    public String sendMessage(String topicName, String message, Integer key, Integer partition) throws InterruptedException, ExecutionException {
        return null;
    }

}

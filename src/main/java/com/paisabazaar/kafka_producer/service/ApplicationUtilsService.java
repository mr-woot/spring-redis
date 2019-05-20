package com.paisabazaar.kafka_producer.service;

public interface ApplicationUtilsService {
    void copyNonNullProperties(Object source, Object target);

    boolean validateMessage(String message, String metadata);
}

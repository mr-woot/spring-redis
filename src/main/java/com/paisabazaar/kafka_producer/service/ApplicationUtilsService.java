package com.paisabazaar.kafka_producer.service;

import org.springframework.stereotype.Service;

public interface ApplicationUtilsService {
    void copyNonNullProperties(Object source, Object target);
    boolean validateMessage(String message, String metadata);
}

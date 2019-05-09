package com.paisabazaar.kafka_producer.service;

import org.springframework.stereotype.Service;

@Service
public interface ApplicationUtilsService {
    void copyNonNullProperties(Object source, Object target);
}

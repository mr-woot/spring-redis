package com.paisabazaar.kafka_producer.controller;

import com.paisabazaar.kafka_producer.model.Producer;
import com.paisabazaar.kafka_producer.repository.ProducerRepository;
import com.paisabazaar.kafka_producer.service.ApplicationUtilsService;
import com.paisabazaar.kafka_producer.service.KafkaService;
import com.paisabazaar.kafka_producer.utils.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/PB_DATAPIPE_PRODUCER")
public class ProducerController {

    private final KafkaService kafkaService;

    private final ProducerRepository producerRepository;

    private final ApplicationUtilsService applicationUtilsService;

    public ProducerController(ProducerRepository producerRepository, ApplicationUtilsService applicationUtilsService, KafkaService kafkaService) {
        this.producerRepository = producerRepository;
        this.applicationUtilsService = applicationUtilsService;
        this.kafkaService = kafkaService;
    }

    @GetMapping("/producer")
    public ResponseEntity<HashMap<String, Object>> getProducers() {
        HashMap<String, Object> producersMap = new HashMap<>();
        producerRepository.findAll().forEach(producer -> {
            producersMap.put(producer.getId(), producer);
        });
        // ## Need to implement custom exception handler and custom response handler
        return new ResponseEntity<>(producersMap, HttpStatus.OK);
    }

    @PostMapping("/producer")
    public ResponseEntity<Producer> createProducer(@RequestBody Producer producer) {
        // Set uuid to id
        producer.setId(UUID.randomUUID().toString());
        // Set createdAt and updatedAt timestamps
        Date date = new Date();
        producer.setCreatedAt(date);
        producer.setUpdatedAt(date);
        // Save producer in redis
        producerRepository.save(producer);
        // ## Need to implement custom exception handler and custom response handler
        return new ResponseEntity<>(producer, HttpStatus.CREATED);
    }

    @PutMapping("/producer/{id}")
    public ResponseEntity<Producer> updateProducer(@PathVariable String id, @RequestBody Producer payload) {
        // Update producer
        Producer p = producerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
        applicationUtilsService.copyNonNullProperties(payload, p);
        producerRepository.save(p);
        // ## Need to implement custom exception handler and custom response handler
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @DeleteMapping("/producer/{id}")
    public void deleteProducer(@PathVariable String id) {
        // ## Need to implement custom exception handler and custom response handler
        producerRepository.deleteById(id);
    }

    @PostMapping("/produce_messages")
    public ResponseEntity<?> produceMessage(@Header(value = "x-producer-id") String id, @RequestBody Object message) {
        // ## Get Topic from producer id
        String topic = producerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id)).getTopic();
        // ## Produce to kafka
        kafkaService.sendMessage(topic, message.toString());
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }
}

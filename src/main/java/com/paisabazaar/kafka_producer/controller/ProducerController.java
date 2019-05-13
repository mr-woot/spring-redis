package com.paisabazaar.kafka_producer.controller;

import com.paisabazaar.kafka_producer.model.Producer;
import com.paisabazaar.kafka_producer.repository.ProducerRepository;
import com.paisabazaar.kafka_producer.service.ApplicationUtilsService;
import com.paisabazaar.kafka_producer.service.KafkaService;
import com.paisabazaar.kafka_producer.service.ResponseFormatter;
import com.paisabazaar.kafka_producer.utils.ResourceNotFoundException;
import com.paisabazaar.kafka_producer.utils.ResponseCode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/PB_DATAPIPE_PRODUCER")
public class ProducerController {

    private final ResponseFormatter responseFormatter;

    private final KafkaService kafkaService;

    private final ProducerRepository producerRepository;

    private final ApplicationUtilsService applicationUtilsService;

    public ProducerController(ProducerRepository producerRepository, ApplicationUtilsService applicationUtilsService, KafkaService kafkaService, ResponseFormatter responseFormatter) {
        this.producerRepository = producerRepository;
        this.applicationUtilsService = applicationUtilsService;
        this.kafkaService = kafkaService;
        this.responseFormatter = responseFormatter;
    }

    @GetMapping(value = "/producer", produces = "application/json")
    public ResponseEntity<?> getProducers() {
        HashMap<String, Object> producersMap = new HashMap<>();
        producerRepository.findAll().forEach(producer -> {
            producersMap.put(producer.getId(), producer);
        });
        // ## Need to implement custom exception handler and custom response handler
        JSONObject data = new JSONObject();
        data.put("producer", producersMap);
        JSONObject response = responseFormatter.buildResponse(
                "success", HttpStatus.OK.value(),
                data.getJSONObject("producer"),
                "Fetched successfully");
        return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
    }

    @PostMapping(value = "/producer", produces = "application/json")
    public ResponseEntity<?> createProducer(@RequestBody Producer producer) {
        // Set uuid to id
        producer.setId(UUID.randomUUID().toString());
        // Set createdAt and updatedAt timestamps
        Date date = new Date();
        producer.setCreatedAt(date);
        producer.setUpdatedAt(date);
        // Save producer in redis
        producerRepository.save(producer);
        // ## Need to implement custom exception handler and custom response handler
        JSONObject data = new JSONObject();
        data.put("producer", producer);
        JSONObject response = responseFormatter.buildResponse(
                "success", HttpStatus.OK.value(),
                data,
                "Producer created");
        return new ResponseEntity<>(response.toMap(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/producer/{id}", produces = "application/json")
    public ResponseEntity<?> updateProducer(@PathVariable String id, @RequestBody Producer payload) {
        // Update producer
        if (!producerRepository.existsById(id)) {
            JSONObject response = responseFormatter.buildErrorResponse(
                    "error",
                    ResponseCode.PRODUCER_NOT_RETRIEVED.getCode(),
                    new JSONObject().put("message", ResponseCode.PRODUCER_NOT_RETRIEVED.getMessage())
            );
            return new ResponseEntity<>(response.toMap(), HttpStatus.NOT_FOUND);
        }
        Producer p = producerRepository.findById(id).get();
        applicationUtilsService.copyNonNullProperties(payload, p);
        p.setUpdatedAt(new Date());
        producerRepository.save(p);
        // ## Need to implement custom exception handler and custom response handler
        JSONObject data = new JSONObject();
        data.put("producer", p);
        JSONObject response = responseFormatter.buildResponse(
                "success", HttpStatus.OK.value(),
                data,
                "Producer updated");
        return new ResponseEntity<>(response.toMap(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/producer/{id}", produces = "application/json")
    public ResponseEntity<?> deleteProducer(@PathVariable String id) {
        // ## Need to implement custom exception handler and custom response handler
        if (producerRepository.existsById(id)) {
            producerRepository.deleteById(id);
            JSONObject response = responseFormatter.buildResponse(
                    "success",
                    HttpStatus.OK.value(),
                    null,
                    "Producer Deleted");
            return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
        } else {
            JSONObject response = responseFormatter.buildErrorResponse(
                    "error",
                    ResponseCode.PRODUCER_NOT_RETRIEVED.getCode(),
                    new JSONObject().put("message", ResponseCode.PRODUCER_NOT_RETRIEVED.getMessage())
            );
            return new ResponseEntity<>(response.toMap(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/produce_messages", produces = "application/json")
    public ResponseEntity<?> produceMessages(@RequestHeader(value = "x-producer-id") String id, @RequestBody String message) throws ExecutionException, InterruptedException {
        // ## Get Topic from producer id
        String topic = producerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id)).getTopic();
        // ## Produce to kafka
        JSONObject x = kafkaService.sendMessage(topic, message);
        // Response build
        JSONObject response = responseFormatter.buildResponse(
                "success", HttpStatus.OK.value(),
                x,
                "Message produced");
        return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
    }
}

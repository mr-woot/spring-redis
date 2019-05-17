package com.paisabazaar.kafka_producer.controller;

import com.paisabazaar.kafka_producer.model.Producer;
import com.paisabazaar.kafka_producer.repository.ProducerRepository;
import com.paisabazaar.kafka_producer.service.ApplicationUtilsService;
import com.paisabazaar.kafka_producer.service.KafkaService;
import com.paisabazaar.kafka_producer.service.ResponseFormatter;
import com.paisabazaar.kafka_producer.utils.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/PB_DATAPIPE_PRODUCER")
public class ProducerController {

    private static final Logger LOGGER = LogManager.getLogger(ProducerController.class);

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
        for (Producer p : producerRepository.findAll()) {
            if (p != null) {
                producersMap.put(p.getId(), p);
            }
        }
        // ## Need to implement custom exception handler and custom response handler
        JSONObject data = new JSONObject();
        data.put("producer", producersMap);
        JSONObject response = responseFormatter.buildResponse(
                "success", HttpStatus.OK.value(),
                data.getJSONObject("producer"),
                "Fetched successfully");
        LOGGER.info("Producers fetched successfully");
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
        LOGGER.info("Producer created with id: " + producer.getId());
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
            LOGGER.error("Producer not found with id: " + id);
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
        LOGGER.info("Producer updated with id: " + p.getId());
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
                    (JSONObject) null,
                    "Producer Deleted");
            LOGGER.info("Producer deleted with id: " + id);
            return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
        } else {
            JSONObject response = responseFormatter.buildErrorResponse(
                    "error",
                    ResponseCode.PRODUCER_NOT_RETRIEVED.getCode(),
                    new JSONObject().put("message", ResponseCode.PRODUCER_NOT_RETRIEVED.getMessage())
            );
            LOGGER.info("Producer not found with id: " + id);
            return new ResponseEntity<>(response.toMap(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/produce_messages", produces = "application/json")
    public ResponseEntity<?> produceMessages(@RequestHeader(value = "x-producer-id", required = false) String id,
                                             @RequestParam(value = "producer_id", required = false) String producer_id,
                                             @RequestParam(value = "key", required = false) String key,
                                             @RequestParam(value = "partition", required = false) Integer partition,
                                             @RequestBody Map<String, Object>[] messages) throws ExecutionException, InterruptedException {
        String ID = null;
        if (id == null && producer_id == null) {
            JSONObject response = responseFormatter.buildErrorResponse(
                    "error",
                    ResponseCode.PRODUCER_ID_IS_REQUIRED.getCode(),
                    new JSONObject().put("message", ResponseCode.PRODUCER_ID_IS_REQUIRED.getMessage())
            );
            return new ResponseEntity<>(response.toMap(), HttpStatus.NOT_FOUND);
        } else if (id != null) {
            ID = id;
        } else {
            ID = producer_id;
        }
        // Get Topic from producer id
        if (!producerRepository.existsById(ID)) {
            JSONObject response = responseFormatter.buildErrorResponse(
                    "error",
                    ResponseCode.PRODUCER_NOT_RETRIEVED.getCode(),
                    new JSONObject().put("message", ResponseCode.PRODUCER_NOT_RETRIEVED.getMessage())
            );
            return new ResponseEntity<>(response.toMap(), HttpStatus.NOT_FOUND);
        } else {
            Producer producer = producerRepository.findById(ID).get();
            String metadata = producer.getMetadata();
            String topic = producer.getTopic();
            // Produce to kafka
            JSONArray data;
            if (partition != null) {
                data = kafkaService.sendMessagesInBatch(topic, partition, null, messages, metadata);
            } else if (key != null) {
                data = kafkaService.sendMessagesInBatch(topic, null, key, messages, metadata);
            } else {
                data = kafkaService.sendMessagesInBatch(topic, null, null, messages, metadata);
            }
            // Response build
            JSONObject response = responseFormatter.buildResponse(
                    "success", HttpStatus.OK.value(),
                    data,
                    messages.length > 1 ? "Messages produced" : "Message produced");
            LOGGER.info(response.put("producerId", ID).toString(4));
            return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
        }
    }
}

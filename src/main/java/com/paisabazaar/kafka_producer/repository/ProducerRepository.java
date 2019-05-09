package com.paisabazaar.kafka_producer.repository;

import com.paisabazaar.kafka_producer.model.Producer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends CrudRepository<Producer, String> {
}

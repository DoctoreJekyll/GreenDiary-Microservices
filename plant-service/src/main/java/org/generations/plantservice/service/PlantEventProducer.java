package org.generations.plantservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlantEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "plant-events";

    public PlantEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPlantCreatedEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println(">>> Evento enviado a Kafka: " + message);
    }
}

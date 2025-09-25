package org.generations.plantservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlantEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "plant-events";

    public PlantEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendPlantCreatedEvent(Object eventPayload) {
        try {
            String json = objectMapper.writeValueAsString(eventPayload);
            kafkaTemplate.send(TOPIC, json);
            System.out.println(">>> Evento enviado a Kafka -> topic=" + TOPIC + " payload=" + json);
        } catch (Exception e) {
            // Manejo sencillo por ahora
            System.err.println("Error serializando/enviando evento a Kafka: " + e.getMessage());
        }
    }
}

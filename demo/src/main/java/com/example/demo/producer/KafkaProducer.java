package com.example.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "my-topic"; // Update with your Kafka topic name

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate; // Use String as the value type

    public void sendMessage(String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
}




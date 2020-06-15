package com.spring.kafka.springbootkafkaproject.config;

import com.spring.kafka.springbootkafkaproject.model.Company;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for configuring kafka template which will be used to send records to a particular topic. We are
 * specifying the host and port of one of the Kafka brokers that we will send the record to
 */
@Configuration
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, Company> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ec2-18-217-167-202.us-east-2.compute.amazonaws.com:9092"); // broker port
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // What kind of key are we going to put
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // what kind of value are we going to put

        return new DefaultKafkaProducerFactory(config);
    }

    @Bean
    public KafkaTemplate<String, Company> kafkaTemplate(){

        return new KafkaTemplate<>(producerFactory());
    }
}

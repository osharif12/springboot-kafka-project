package com.spring.kafka.springbootkafkaproject.resource;

import com.spring.kafka.springbootkafkaproject.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A post request that will have a JSON in the body represening the company information and kafka topic where the
 * record will be sent
 */
@RestController
@RequestMapping("kafka")
public class CompanyResource {

    @Autowired
    private KafkaTemplate<String, Company> kafkaTemplate;

    @PostMapping(path = "/publish/kafka-record", consumes = "application/json", produces = "application/json")
    public String sendRecordPost(@RequestBody Company company){
        kafkaTemplate.send(company.getKafkaTopic(), company);

        return "Successfully added record to topic titled " + company.getKafkaTopic();
    }

}

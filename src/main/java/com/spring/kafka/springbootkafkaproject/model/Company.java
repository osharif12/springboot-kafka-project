package com.spring.kafka.springbootkafkaproject.model;

/**
 * A DTO with basic information on a company. Will be used to send records in JSON format to kafka brokers
 */
public class Company {

    private String name;
    private String ceo;
    private String founded;
    private long annualRevenue;
    private String headquarters;
    private String kafkaTopic;

    public Company(){}

    public Company(String name, String ceo, String founded, long annualRevenue, String headquarters) {
        this.name = name;
        this.ceo = ceo;
        this.founded = founded;
        this.annualRevenue = annualRevenue;
        this.headquarters = headquarters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public long getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(long annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }
}

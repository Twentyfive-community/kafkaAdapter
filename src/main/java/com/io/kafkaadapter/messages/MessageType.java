package com.io.kafkaadapter.messages;

public class MessageType {
    public final static String FATAL_ERROR = "Fatal error.";
    public final static String ZOOKEPER_START_SUCCESS = "Zookeeper service started successfully!";
    public final static String ZOOKEPER_START_ERROR = "A problem occurred while starting Zookeeper.";
    public final static String KAFKA_START_SUCCESS = "Kafka service started successfully!";
    public final static String KAFKA_START_ERROR = "A problem occurred while starting Kafka.";
    public final static String KAFKA_START_ERROR_ZKP_OFF = KAFKA_START_ERROR + " (Zookeper is not running)";
    public final static String KAFKA_TOPIC_ADD_SUCCESS = "Topic added successfully!";
    public final static String KAFKA_TOPIC_ADD_FAILURE = "A problem occurred while adding the topic.";
}

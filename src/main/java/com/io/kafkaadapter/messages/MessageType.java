package com.io.kafkaadapter.messages;

public class MessageType {
    public final static String FATAL_ERROR = "Fatal error.";
    public final static String ZOOKEPER_START_SUCCESS = "Zookeeper service started successfully!";
    public final static String ZOOKEPER_START_ERROR = "A problem occurred while starting Zookeeper.";
    public final static String KAFKA_START_SUCCESS = "Kafka service started successfully!";
    public final static String KAFKA_START_ERROR = "A problem occurred while starting Kafka.";
    public final static String KAFKA_START_ERROR_ZKP_OFF = KAFKA_START_ERROR + " Zookeeper is not running.";
    public final static String KAFKA_TOPIC_ADD_SUCCESS = "Topic added successfully!";
    public final static String KAFKA_TOPIC_ADD_FAILURE = "A problem occurred while adding the topic.";
    public final static String TEMP_BASH_FILE_ERROR = "Error while creating file.";
    public final static String STARTING_SERVICE_ERROR = "Starting failed.";

    public static final String ZOOKEEPER_STOP_SUCCESS = "Zookeeper stopped successfully!";
    public static final String ZOOKEEPER_NOT_RUNNING = "Zookeeper is not running.";
    public static final String KAKFA_STOP_SUCCESS = "Kafka stopped successfully!";
    public static final String KAFKA_NOT_RUNNING = "Kafka is not running.";
    public static final String KAFKA_TOPIC_REMOVE_FAILURE = "An error occurred while removing the topic";
    public static final String KAFKA_TOPIC_REMOVE_SUCCESS = "Topic deleted successfully!";
}

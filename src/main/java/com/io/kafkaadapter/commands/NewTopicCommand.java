package com.io.kafkaadapter.commands;

import com.io.kafkaadapter.utils.KafkaData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class NewTopicCommand {

    private Topic topic;

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer = "localhost:9092";

    public NewTopicCommand(Topic topic) {
        this.topic = topic;
    }

    public String generateCommand(){
        String path = KafkaData.KAFKA_DIRECTORY_PATH+"bin/";
        String commandTemplate = String.format("%skafka-topics.sh --bootstrap-server %s " +
                        "--topic %s ", path, bootstrapServer, topic.getName());
        StringBuilder commandBuilder = new StringBuilder(commandTemplate);
        Integer partitionsNumber = topic.getPartitionsNumber();
        if (partitionsNumber != null)
            commandBuilder.append(String.format("--create --partitions %s ", partitionsNumber.toString()));
        Integer replicationFactor = topic.getReplicationFactor();
        if (replicationFactor != null)
            commandBuilder.append(String.format("--replication-factor %s", replicationFactor.toString()));
        return commandBuilder.toString();
    }
}

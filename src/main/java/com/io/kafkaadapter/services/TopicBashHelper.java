package com.io.kafkaadapter.services;

import com.io.kafkaadapter.commands.Topic;
import com.io.kafkaadapter.commands.NewTopicCommand;
import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.utils.KafkaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TopicBashHelper implements BashHelper {
    @Autowired
    private KafkaBashHelper kafkaBashHelper;
    private final String serviceName = this.getClass().getSimpleName();
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer = "localhost:9092";

    public String addTopic(Topic topic) throws IOException {
        NewTopicCommand command = new NewTopicCommand(topic);
        return (kafkaBashHelper.isKafkaOn()) ?
                executeNewTopicCommand(command.generateCommand()+" >> " +
                        outuputFileUtils.outputFileName(serviceName) + " 2> "+
                        outuputFileUtils.errorFileName(serviceName))
                : "can't add topic: kafka is off";
    }

    private String executeNewTopicCommand(String command) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(command);
        try {executeCommands(commands, serviceName, 3L, null);}
        catch (IOException e){return MessageType.TEMP_BASH_FILE_ERROR;}
        catch (InterruptedException e) {return MessageType.FATAL_ERROR;}
        File topicErrorFile = new File(outuputFileUtils.errorFileName(serviceName));
        if (!(topicErrorFile.length() == 0)) {return MessageType.KAFKA_TOPIC_ADD_FAILURE;}
        return MessageType.KAFKA_TOPIC_ADD_SUCCESS;
    }

    public ArrayList<String> getAllTopics(){
        List<String> commands = new ArrayList<>();
        String outputFilePath = outuputFileUtils.outputFileName(serviceName);
        String command = KafkaData.KAFKA_DIRECTORY_PATH+"bin/kafka-topics.sh --list --bootstrap-server " + bootstrapServer + " > " + outputFilePath;
        commands.add(command);
        try {executeCommands(commands, serviceName, 0L, null);}
        catch (IOException | InterruptedException e) {throw new RuntimeException(e);}

        ArrayList<String> topics = new ArrayList<>();
        try {
            File outputFile = new File(outputFilePath);
            Scanner myReader = new Scanner(outputFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                topics.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return topics;
    }

    public String removeTopic(String name) {
        List<String> commands = new ArrayList<>();
        String command = KafkaData.KAFKA_DIRECTORY_PATH+"bin/kafka-topics.sh --bootstrap-server " + bootstrapServer +
                " --delete --topic " + name + " 2> " + outuputFileUtils.errorFileName(serviceName);
        commands.add(command);
        try {executeCommands(commands, serviceName, 0L, null);}
        catch (IOException | InterruptedException e) {throw new RuntimeException(e);}
        File topicErrorFile = new File(outuputFileUtils.errorFileName(serviceName));
        return ((topicErrorFile.length() == 0)) ? (name + ": " + MessageType.KAFKA_TOPIC_REMOVE_SUCCESS) :
                (name + ": " + MessageType.KAFKA_TOPIC_REMOVE_FAILURE);
    }
}

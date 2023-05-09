package com.io.kafkaadapter.controller;

import com.io.kafkaadapter.commands.Topic;
import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.services.KafkaBashHelper;
import com.io.kafkaadapter.services.TopicBashHelper;
import com.io.kafkaadapter.services.ZookeeperBashHelper;
import com.io.kafkaadapter.utils.KafkaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class KafkaController {

    private boolean isZookeperOn = false;
    private boolean isKafkaOn = false;

    @Autowired
    private KafkaBashHelper kafkaBashHelper;
    @Autowired
    private ZookeeperBashHelper zookeeperBashHelper;
    @Autowired
    private TopicBashHelper topicBashHelper;



    @GetMapping("/start-zookeeper")
    public String startZookeeper() throws IOException {
        //return turnZookeperOn();
        return zookeeperBashHelper.turnOn();
    }

    @GetMapping("/stop-zookeeper")
    public String stopZookeeper() throws IOException, InterruptedException {
        //return turnZookeperOn();
        return zookeeperBashHelper.turnOff();
    }

    @GetMapping("/start-kafka")
    public String startKafka() throws IOException {
        /*if (isZookeperOn) return executeKafkaCommands();
        else return MessageType.KAFKA_START_ERROR_ZKP_OFF;*/
        return kafkaBashHelper.turnOn();
    }

    @GetMapping("/stop-kafka")
    public String stopKafka() {
        return kafkaBashHelper.turnOff();
    }

    @PostMapping("/new-topic")
    public String addTopic(@RequestBody Topic topic) throws IOException {
        /*TopicCommand command = new TopicCommand(topic);
        return (isKafkaOn) ? executeNewTopicCommand(command.generateCommand()) : "can't add topic: kafka is off";*/
        return topicBashHelper.addTopic(topic);
    }

    @GetMapping("/all-topics")
    public ArrayList<String> getTopics(){
        return topicBashHelper.getAllTopics();
    }

    @DeleteMapping("/delete-topic/{name}")
    public String deleteTopic(@PathVariable("name") String name){
        return topicBashHelper.removeTopic(name);
    }



    private String executeKafkaCommands() throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(KafkaData.KAFKA_DIRECTORY_PATH +"bin/kafka-server-start.sh " + KafkaData.KAFKA_DIRECTORY_PATH+"config/server.properties");
        File tempScript = createTempScript(commands);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            if (process.waitFor(45, TimeUnit.SECONDS))
                return MessageType.KAFKA_START_ERROR;
        }
        catch (InterruptedException exception){return MessageType.FATAL_ERROR;}
        finally {tempScript.delete();}
        isKafkaOn=true;
        return MessageType.KAFKA_START_SUCCESS;
    }

    private String turnZookeperOn() throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(KafkaData.KAFKA_DIRECTORY_PATH+"bin/zookeeper-server-start.sh " + KafkaData.KAFKA_DIRECTORY_PATH+"config/zookeeper.properties");
        File tempScript = createTempScript(commands);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            if (process.waitFor(5, TimeUnit.SECONDS))
                return MessageType.ZOOKEPER_START_ERROR;
        }
        catch (InterruptedException exception){return MessageType.FATAL_ERROR;}
        finally {tempScript.delete();}
        isZookeperOn=true;
        return MessageType.ZOOKEPER_START_SUCCESS;
    }

    private String executeNewTopicCommand(String command) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(command);

        File tempScript = createTempScript(commands);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        }
        catch (InterruptedException exception){return MessageType.FATAL_ERROR;}
        finally {tempScript.delete();}
        return MessageType.KAFKA_TOPIC_ADD_SUCCESS;
    }

    private void turnZookeperOff(){
        isZookeperOn=false;
    }
    private void turnKafkaOff(){
        //kill kafka pid
        isKafkaOn=false; }

    public File createTempScript(List<String> commands) throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);
        printWriter.println("#!/bin/bash");
        commands.forEach((c) -> printWriter.println(c));
        printWriter.close();

        return tempScript;
    }

}

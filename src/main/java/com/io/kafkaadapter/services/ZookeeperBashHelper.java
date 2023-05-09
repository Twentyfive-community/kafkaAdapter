package com.io.kafkaadapter.services;

import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.utils.KafkaData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Service
public class ZookeeperBashHelper implements BashHelper {

    private boolean isZookeperOn = false;
    private final String serviceName = this.getClass().getSimpleName();
    @Autowired
    private ProcessHelper processHelper;


    public String turnOn() throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(KafkaData.KAFKA_DIRECTORY_PATH+"bin/zookeeper-server-start.sh " + KafkaData.KAFKA_DIRECTORY_PATH+"config/zookeeper.properties > " + outuputFileUtils.outputFileName(serviceName));
        try {executeCommands(commands, serviceName, 3L, processHelper);}
        catch (IOException e){return MessageType.TEMP_BASH_FILE_ERROR;}
        catch (InterruptedException e) {return MessageType.FATAL_ERROR;}
        catch (Exception e){if (e.getMessage().equalsIgnoreCase(MessageType.STARTING_SERVICE_ERROR))
            return MessageType.ZOOKEPER_START_ERROR;}
        isZookeperOn=true;
        return MessageType.ZOOKEPER_START_SUCCESS;
    }

    public String turnOff() throws IOException, InterruptedException {
        if (isZookeperOn){
            processHelper.getProcesses().get(serviceName).destroy();
            //closeZK();
            isZookeperOn=false;
            return MessageType.ZOOKEEPER_STOP_SUCCESS;
        }
        return MessageType.ZOOKEEPER_NOT_RUNNING;
    }


    /*private void closeZK() throws IOException, InterruptedException {

        File tempScript = createTempScript();

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } finally {
            tempScript.delete();
        }
    }

    public File createTempScript() throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("kill -9 " + processHelper.processes.get(serviceName).pid());

        printWriter.close();

        return tempScript;
    }*/
}

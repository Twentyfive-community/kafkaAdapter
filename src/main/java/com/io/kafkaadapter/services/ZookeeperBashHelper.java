package com.io.kafkaadapter.services;

import com.io.kafkaadapter.messages.MessageResponse;
import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.utils.KafkaData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class ZookeeperBashHelper implements BashHelper {

    private boolean isZookeperOn = false;
    private final String serviceName = this.getClass().getSimpleName();
    @Autowired
    private ProcessHelper processHelper;


    public MessageResponse turnOn() throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(KafkaData.KAFKA_DIRECTORY_PATH+"bin/zookeeper-server-start.sh " + KafkaData.KAFKA_DIRECTORY_PATH+"config/zookeeper.properties > " + outuputFileUtils.outputFileName(serviceName));
        try {executeCommands(commands, serviceName, 3L, processHelper);}
        catch (IOException e){return new MessageResponse(MessageType.TEMP_BASH_FILE_ERROR);}
        catch (InterruptedException e) {return new MessageResponse(MessageType.FATAL_ERROR);}
        catch (Exception e) {if (e.getMessage().equalsIgnoreCase(MessageType.STARTING_SERVICE_ERROR))
            return new MessageResponse(MessageType.ZOOKEPER_START_ERROR);}
        isZookeperOn=true;
        return new MessageResponse(MessageType.ZOOKEEPER_START_SUCCESS);
    }

    public MessageResponse turnOff() throws IOException, InterruptedException {
        if (isZookeperOn){
            List<String> commands = new ArrayList<>();
            commands.add("echo 1234 | sudo -S " + KafkaData.KAFKA_DIRECTORY_PATH + "bin/zookeeper-server-stop.sh > " + outuputFileUtils.outputFileName(serviceName));
            //processHelper.getProcesses().get(serviceName).destroy();
            //closeZK();
            File zookeeperErrorFile = new File(outuputFileUtils.errorFileName(serviceName));
            if ((zookeeperErrorFile.length() == 0)){
                isZookeperOn=false;
                return new MessageResponse(MessageType.ZOOKEEPER_STOP_SUCCESS);
            }
            else return new MessageResponse(MessageType.ZOOKEEPER_STOP_FAILURE);
        }
        return new MessageResponse(MessageType.ZOOKEEPER_NOT_RUNNING);
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

package com.io.kafkaadapter.services;

import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.utils.KafkaData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class KafkaBashHelper implements BashHelper{

    @Autowired
    private ZookeeperBashHelper zookeeperBashHelper;
    private boolean isKafkaOn = false;
    private final String serviceName = this.getClass().getSimpleName();
    @Autowired
    ProcessHelper processHelper;

    public String turnOn() throws IOException {
        if (zookeeperBashHelper.isZookeperOn()){
            List<String> commands = new ArrayList<>();
            commands.add(KafkaData.KAFKA_DIRECTORY_PATH +"bin/kafka-server-start.sh " + KafkaData.KAFKA_DIRECTORY_PATH+"config/server.properties");
            try {executeCommands(commands, serviceName, 5L, processHelper);}
            catch (IOException e){return MessageType.TEMP_BASH_FILE_ERROR;}
            catch (InterruptedException e) {return MessageType.FATAL_ERROR;}
            catch (Exception e){if (e.getMessage().equalsIgnoreCase(MessageType.STARTING_SERVICE_ERROR))
                return MessageType.KAFKA_START_ERROR;}
            isKafkaOn=true;
            return MessageType.KAFKA_START_SUCCESS;
        }
        else return MessageType.KAFKA_START_ERROR_ZKP_OFF;
    }


    public String turnOff(){
        if (isKafkaOn){
            processHelper.getProcesses().get(serviceName).destroy();
            isKafkaOn=false;
            return MessageType.KAKFA_STOP_SUCCESS;
        }
        return MessageType.KAFKA_NOT_RUNNING;
    }
}

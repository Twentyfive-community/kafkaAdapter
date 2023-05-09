package com.io.kafkaadapter;

import com.io.kafkaadapter.utils.OutuputFileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class KafkaAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaAdapterApplication.class, args);
    }

}

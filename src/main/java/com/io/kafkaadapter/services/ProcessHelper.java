package com.io.kafkaadapter.services;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Data
public class ProcessHelper {

    HashMap<String, Process> processes = new HashMap<>();

}

package com.io.kafkaadapter.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    private String name;
    private Integer partitionsNumber;
    private Integer replicationFactor;
}

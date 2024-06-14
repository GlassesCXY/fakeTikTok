package com.qiu.apifinal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Video {
    private Integer vid;
    private String name;
    private String description;
    private Integer uid;
}
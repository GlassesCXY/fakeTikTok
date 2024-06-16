package com.qiu.apifinal.entity.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private String videoId;
    private String commentContent;
}

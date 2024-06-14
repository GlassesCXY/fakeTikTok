package com.qiu.apifinal.entity.dto;

import lombok.Data;

@Data
public class SendCodeBody {
    String  username;
    String email;


    public boolean isComplete() {
        return username != null && !username.isEmpty() &&
                email != null && !email.isEmpty();
    }


}

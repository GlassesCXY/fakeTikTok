package com.qiu.apifinal.entity.dto;

import lombok.Data;

@Data
public class RegisterBody {
    String username;
    String password;
    String email;
    String code;


    public boolean isComplete() {
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty() &&
                email != null && !email.isEmpty() &&
                code != null && !code.isEmpty();
    }
}

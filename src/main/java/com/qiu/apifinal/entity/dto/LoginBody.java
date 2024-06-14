package com.qiu.apifinal.entity.dto;

import lombok.Data;

@Data
public class LoginBody {
    String username;
    String password;

    public boolean isComplete() {
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty();
    }
}

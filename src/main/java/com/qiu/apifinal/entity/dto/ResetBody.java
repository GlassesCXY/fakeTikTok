package com.qiu.apifinal.entity.dto;

import lombok.Data;

@Data
public class ResetBody {
    String username;
    String password;
    String email;
    String code;

    public ResetBody(String username, String password,String email ,String code) {
        this.username = username;
        this.password = password;
        this.code = code;
        this.email=email;
    }

    public boolean isComplete() {
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty() &&
                email != null && !email.isEmpty() &&
                code != null && !code.isEmpty();
    }

}

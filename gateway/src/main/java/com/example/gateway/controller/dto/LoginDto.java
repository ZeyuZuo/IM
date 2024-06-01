package com.example.gateway.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String token;
    private String ip;
    public LoginDto(String token, String ip) {
        this.token = token;
        this.ip = ip;
    }
}

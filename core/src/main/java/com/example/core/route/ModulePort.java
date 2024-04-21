package com.example.core.route;


import lombok.Getter;

@Getter
public enum ModulePort {
    USER(8080),
    MSG(8081),
    CHAT(8082),
    GATEWAY(12345),
    NETTY(12346);

    private final Integer type;

    ModulePort(Integer type){
        this.type = type;
    }
}

package com.example.core.element;


import lombok.Getter;

@Getter
public enum ChatActionEnum {
    CONNECT(0),
    CHAT(1),
    GROUPCHAT(2),
    KEEPALIVE(3);

    public final Integer type;

    ChatActionEnum(Integer type){
        this.type = type;
    }
}

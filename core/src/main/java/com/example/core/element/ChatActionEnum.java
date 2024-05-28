package com.example.core.element;


import lombok.Getter;

@Getter
public enum ChatActionEnum {
    CONNECT(0),
    CHAT(1),
    GROUPCHAT(2),
    SENDFILE(3),
    KEEPALIVE(4);

    public final Integer type;

    ChatActionEnum(Integer type){
        this.type = type;
    }
}

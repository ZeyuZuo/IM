package com.example.chat.netty.element;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class Content implements Serializable {

    private Integer action;
    private ChatMsg chatMsg;
    private String token;

    public Content(){
        this.action = -1;
        this.chatMsg = null;
        this.token = null;
    }
}

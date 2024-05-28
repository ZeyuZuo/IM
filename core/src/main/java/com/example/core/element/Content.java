package com.example.core.element;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class Content implements Serializable {

    private Integer action;
    private ChatMsg chatMsg;

    public Content(){
        this.action = -1;
        this.chatMsg = null;
    }

    public String toString(){
        return "{" +
                "\"action\":" + action +
                ", \"chatMsg\":" + chatMsg +
                "}";
    }
}

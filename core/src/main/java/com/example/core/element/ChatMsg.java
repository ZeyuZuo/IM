package com.example.core.element;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class ChatMsg implements Cloneable{
    private String sender;
    private String receiver;
    private String msg;

    public String toString(){
        return "{" +
                "\"sender\":\"" + sender + "\"" +
                ", \"receiver\":\"" + receiver + "\"" +
                ", \"msg\":\"" + msg + "\"" +
                "}";
    }

    @Override
    public ChatMsg clone() throws CloneNotSupportedException {
        return (ChatMsg) super.clone();
    }
}

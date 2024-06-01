package com.example.core.element;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMsg implements Cloneable{
    private String sender;
    private String receiver;
    private String msg;
    private String group;
    private LocalDateTime time;

    public ChatMsg(){
        this.sender = null;
        this.receiver = null;
        this.msg = null;
        this.group = null;
        this.time = null;
    }

    public ChatMsg(String sender, String receiver, String msg){
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.group = null;
        this.time = LocalDateTime.now();
    }

    public ChatMsg(String sender, String group, String msg, boolean isGroup){
        this.sender = sender;
        this.receiver = null;
        this.msg = msg;
        this.group = group;
        this.time = LocalDateTime.now();
    }

    public String toString(){
        return "{" +
                "\"sender\":\"" + sender + "\"" +
                ", \"receiver\":\"" + receiver + "\"" +
                ", \"msg\":\"" + msg + "\"" +
                ", \"group\":\"" + group + "\"" +
                ", \"time\":\"" + time + "\""+
                "}";
    }

    @Override
    public ChatMsg clone() throws CloneNotSupportedException {
        return (ChatMsg) super.clone();
    }
}

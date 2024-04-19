package com.example.msg.dao.bo;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Msg {
    private String sender;
    private String receiver;
    private String msg;
    private String group;
    private boolean isSent;
}

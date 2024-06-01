package com.example.msg.controller.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MsgVo {
    private Integer action;
    private String sender;
    private String receiver;
    private String msg;
    public MsgVo(Integer action, String sender, String receiver, String msg){
        this.action = action;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }
}

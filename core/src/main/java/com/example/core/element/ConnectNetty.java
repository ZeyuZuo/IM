package com.example.core.element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConnectNetty {
    private Integer action;
    private String sender;
    private String receiver;
    private String msg;

    public ConnectNetty(Integer action, String sender, String receiver, String msg){
        this.action = action;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }

    public ConnectNetty(){
        this.action = -1;
        this.sender = null;
        this.receiver = null;
        this.msg = null;
    }
}

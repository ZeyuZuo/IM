package com.example.chat.netty.element;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class ChatMsg {
    private String sender;
    private String receiver;
    private String msg;
    private String group;
}

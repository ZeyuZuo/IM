package com.example.chat.netty.element;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class Content implements Serializable {

    private Integer action;
    private ChatMsg chatMsg;
}

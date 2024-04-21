package com.example.chat.controller;

import com.example.chat.netty.NettyServer;
import com.example.chat.netty.element.Content;
import com.example.chat.netty.handler.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/chat", produces = "application/json;charset=UTF-8")
public class ChatController {

//    @Autowired
//    private NettyServer server;

//    @PostMapping("/sendMsg")
//    public void sendMsg(@RequestBody Content content){
//        server.sendMsg(content);
//    }
}

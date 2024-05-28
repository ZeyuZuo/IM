//package com.example.chat.controller;
//
//import com.example.chat.netty.handler.ChatHandler;
//import com.example.chat.service.ChatService;
//import com.example.core.element.Content;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping(value = "/chat", produces = "application/json;charset=UTF-8")
//public class ChatController {
//
//    @Autowired
//    private ChatService chatService;
//
//    @PostMapping("/sendMsg")
//    public void sendMsg(@RequestBody Content content){
//        chatService.send(content);
//    }
//}

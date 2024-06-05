package com.example.msg.controller;

import com.example.core.dto.ReturnObject;
import com.example.core.element.ChatActionEnum;
import com.example.core.element.ChatMsg;
import com.example.core.element.Content;
import com.example.msg.controller.vo.MsgVo;
import com.example.msg.controller.vo.TranslateVo;
import com.example.msg.dao.bo.Msg;
import com.example.msg.service.MsgService;
import com.example.msg.translate.TransApi;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;

import java.util.Objects;

@RestController
@RequestMapping(value = "/msg", produces = "application/json;charset=UTF-8")
public class MsgController {
    private final Logger logger = LoggerFactory.getLogger(MsgController.class);

    private static final String APP_ID = "20240415002024472";
    private static final String SECURITY_KEY = "3FR6je0HK4CsuDXAHjkl";
    private MsgService msgService;

    @Autowired
    public MsgController(MsgService msgService) {
        this.msgService = msgService;
    }

    @PostMapping("/translate")
    public ReturnObject translate(@RequestBody TranslateVo text){
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        logger.debug("translate text: {}", text.getText());
        String result = api.getTransResult(text.getText(), text.getSourceLanguage(), text.getTargetLanguage());
        logger.debug("translate result: {}", result);
        Object data = JSONObject.parse(result);
        return new ReturnObject(data);
    }

    @PostMapping("/unSent/{user}")
    public ReturnObject getUnsent(@PathVariable String user){
        logger.debug("{} get unsent msg", user);
        return new ReturnObject(msgService.getUnsent(user));
    }

    @PostMapping("/sendMsg")
    public ReturnObject sendMsg(@RequestBody MsgVo msgVo){
        Integer action = msgVo.getAction();

        if(Objects.equals(action, ChatActionEnum.CHAT.getType())){
            logger.debug("send msg: {}", msgVo);
            ChatMsg chatMsg = new ChatMsg(msgVo.getSender(), msgVo.getReceiver(), msgVo.getMsg());
            msgService.send2person(chatMsg);
        }else if(Objects.equals(action, ChatActionEnum.GROUPCHAT.getType())){
            logger.debug("send group msg: {}", msgVo);
            ChatMsg chatMsg = new ChatMsg(msgVo.getSender(), msgVo.getReceiver(), msgVo.getMsg(), true);
            msgService.send2group(chatMsg);
        }else{
            logger.debug("action error: {}", action);
            return new ReturnObject(-1,"action error",null);
        }

        return new ReturnObject(null);
    }
}

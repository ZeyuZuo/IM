package com.example.msg.controller;

import com.example.core.dto.ReturnObject;
import com.example.core.element.Content;
import com.example.msg.controller.vo.TranslateVo;
import com.example.msg.dao.bo.Msg;
import com.example.msg.service.RocketMQService;
import com.example.msg.translate.TransApi;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/msg", produces = "application/json;charset=UTF-8")
public class MsgController {
    private static final String APP_ID = "20240415002024472";
    private static final String SECURITY_KEY = "3FR6je0HK4CsuDXAHjkl";
    private RocketMQService rocketMQService;

    @Autowired
    public MsgController(RocketMQService rocketMQService){
        this.rocketMQService = rocketMQService;
    }

    @PostMapping("/translate")
    public ReturnObject translate(@RequestBody TranslateVo text){
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String result = api.getTransResult(text.getText(), text.getSourceLanguage(), text.getTargetLanguage());

        Object data = JSONObject.parse(result);
        return new ReturnObject(data);
    }

    @PostMapping("/unSent/{user}")
    public ReturnObject getUnsent(@PathVariable String user){
        return  new ReturnObject(null);
    }

    @PostMapping("/sendMsg")
    public ReturnObject sendMsg(@RequestBody Content content){

        return new ReturnObject(null);
    }
}

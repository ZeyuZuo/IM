package com.example.gateway.controller;

import com.example.core.auth.TokenUtils;
import com.example.core.dto.ReturnObject;
import com.example.gateway.controller.vo.LoginData;
import com.example.gateway.mapper.jpa.LogInMapper;
import com.example.gateway.mapper.po.LogInPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class LogInController {

    private LogInMapper logInMapper;

    @Autowired
    public LogInController(LogInMapper logInMapper){
        this.logInMapper = logInMapper;
    }

    @PostMapping("/login")
    public ReturnObject login(@RequestBody LoginData loginData){
        LogInPo po = logInMapper.findByUser(loginData.getUser());
        if(po!=null && Objects.equals(po.getPwd(),loginData.getPwd())){
            return new ReturnObject(TokenUtils.generateToken(po.getUser(),po.getPwd()));
        }
        else{
            return new ReturnObject(1,"登陆失败",null);
        }
    }
}

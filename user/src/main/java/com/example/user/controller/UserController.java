package com.example.user.controller;

import com.example.core.dto.ReturnObject;
import com.example.user.controller.vo.LoginDataVo;
import com.example.user.mapper.jpa.LogInMapper;
import com.example.user.mapper.po.LogInPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    private LogInMapper logInMapper;

    @Autowired
    public UserController(LogInMapper logInMapper){
        this.logInMapper = logInMapper;
    }

    @PostMapping("/login")
    public ReturnObject login(@RequestBody LoginDataVo data){

        LogInPo po = logInMapper.findByUser(data.getUser());
        if(po!=null && Objects.equals(po.getPwd(),data.getPwd())){
            return new ReturnObject();
        }
        else{
            return new ReturnObject(1,"登陆失败",null);
        }
    }
}

package com.example.user.controller;

import com.example.core.auth.TokenUtils;
import com.example.core.dto.ReturnObject;
import com.example.user.controller.vo.LoginDataVo;
import com.example.user.mapper.jpa.LogInMapper;
import com.example.user.mapper.po.LogInPo;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    private UserService userService;
    private LogInMapper logInMapper;

    @Autowired
    public UserController(LogInMapper logInMapper, UserService userService){
        this.logInMapper = logInMapper;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ReturnObject login(@RequestBody LoginDataVo data){
        LogInPo po = logInMapper.findByUser(data.getUser());
        if(po!=null && Objects.equals(po.getPwd(),data.getPwd())){
            String ip = userService.selectNetty();
            String token = TokenUtils.generateToken(po.getUser(),po.getPwd());
            String json = "{\"token\":\""+token+"\",\"ip\":\""+ip+"\"}";
            if(ip==null){
                return new ReturnObject(1,"服务器错误",null);
            }
            return new ReturnObject(0,"登陆成功",json);
        }
        else{
            return new ReturnObject(1,"登陆失败",null);
        }
    }
}

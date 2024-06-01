package com.example.gateway.controller;

import com.example.core.auth.TokenUtils;
import com.example.core.dto.ReturnObject;
import com.example.core.utils.JsonUtils;
import com.example.gateway.controller.dto.LoginDto;
import com.example.gateway.controller.vo.LoginData;
import com.example.gateway.mapper.jpa.LogInMapper;
import com.example.gateway.mapper.po.LogInPo;
import com.example.gateway.service.NettySelectService;
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
    private NettySelectService nettySelectService;

    @Autowired
    public LogInController(LogInMapper logInMapper, NettySelectService nettySelectService){
        this.logInMapper = logInMapper;
        this.nettySelectService = nettySelectService;
    }

    @PostMapping("/login")
    public ReturnObject login(@RequestBody LoginData loginData){
        LogInPo po = logInMapper.findByUser(loginData.getUser());
        if(po!=null && Objects.equals(po.getPwd(),loginData.getPwd())){
            String token = TokenUtils.generateToken(po.getUser(),po.getPwd());
            String ip = nettySelectService.selectNetty();
            if(ip==null) {
                return new ReturnObject(1,"登陆失败",null);
            }
            String json = JsonUtils.obj2Json(new LoginDto(token,ip));
            return new ReturnObject(0,"登陆成功",json);
        }
        else{
            return new ReturnObject(1,"登陆失败",null);
        }
    }
}

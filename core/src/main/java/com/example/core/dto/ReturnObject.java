package com.example.core.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnObject {
    private Integer status;
    private String msg;
    private Object data;

    public ReturnObject(){
        this.data = null;
        this.status = 0;
        this.msg = "ok";
    }
    public ReturnObject(Object data){
        this.data = data;
        this.status = 0;
        this.msg = "ok";
    }
    public ReturnObject(Integer status, String msg, Object data){
        this.data = data;
        this.msg = msg;
        this.status = status;
    }
}
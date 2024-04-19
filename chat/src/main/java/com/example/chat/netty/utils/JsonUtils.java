package com.example.chat.netty.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static String obj2Json(Object data){
        try{
            return MAPPER.writeValueAsString(data);
        }catch (Exception e){
            System.out.println("object to json error!!!");
            return null;
        }
    }
    public static <T>T json2Obj(String json, Class<T> type){
        try{
            return MAPPER.readValue(json,type);
        }catch(Exception e){
            System.out.println("json to object error!!!");
            return null;
        }
    }
}

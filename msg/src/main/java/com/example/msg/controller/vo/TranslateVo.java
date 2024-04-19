package com.example.msg.controller.vo;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslateVo {
    private String text;
    private String sourceLanguage;
    private String targetLanguage;
}

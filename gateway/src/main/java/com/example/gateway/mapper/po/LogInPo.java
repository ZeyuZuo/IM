package com.example.gateway.mapper.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name = "login")
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogInPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String user;
    private String pwd;
}

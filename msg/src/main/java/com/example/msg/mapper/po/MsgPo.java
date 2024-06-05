package com.example.msg.mapper.po;

import com.example.msg.dao.bo.Msg;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "message")
@ToString(callSuper = true, doNotUseGetters = true)
public class MsgPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sender;
    private String receiver;
    private String msg;
    private String chatgroup;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp time;
    private boolean isSent;

    public MsgPo(Msg msg) {
        this.sender = msg.getSender();
        this.receiver = msg.getReceiver();
        this.msg = msg.getMsg();
        this.chatgroup = msg.getGroup();
        this.time = Timestamp.valueOf(msg.getTime());
        this.isSent = msg.isSent();
    }

    public MsgPo() {
        this.sender = null;
        this.receiver = null;
        this.msg = null;
        this.chatgroup = null;
        this.time = null;
        this.isSent = false;
    }

}

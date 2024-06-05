package com.example.msg.dao.bo;


import com.example.core.element.ChatMsg;
import com.example.msg.mapper.po.MsgPo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Msg {
    private String sender;
    private String receiver;
    private String msg;
    private String group;
    private LocalDateTime time;
    private boolean isSent;

    public Msg(ChatMsg chatMsg) {
        this.sender = chatMsg.getSender();
        this.receiver = chatMsg.getReceiver();
        this.msg = chatMsg.getMsg();
        this.group = chatMsg.getGroup();
        this.time = chatMsg.getTime();
        this.isSent = false;
    }

    public Msg(){
        this.sender = null;
        this.receiver = null;
        this.msg = null;
        this.group = null;
        this.time = null;
        this.isSent = false;
    }

    public Msg(MsgPo po) {
        this.sender = po.getSender();
        this.receiver = po.getReceiver();
        this.msg = po.getMsg();
        this.group = po.getChatgroup();
        this.time = po.getTime().toLocalDateTime();
        this.isSent = po.isSent();
    }

    public static List<Msg> toBoList(List<MsgPo> poList) {
        List<Msg> boList = new ArrayList<>();
        for(MsgPo po : poList) {
            Msg bo = new Msg(po);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    public String toString(){
        return "Msg{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", msg='" + msg + '\'' +
                ", group='" + group + '\'' +
                ", time=" + time +
                ", isSent=" + isSent +
                '}';
    }
}
